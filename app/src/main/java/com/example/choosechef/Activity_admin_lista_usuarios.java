package com.example.choosechef;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Clase administrador.
 * Gestiona las opciones del usuario administrador
 * Muestra una lista de todos los usuarios de la app
 */

public class Activity_admin_lista_usuarios extends AppCompatActivity {
    private boolean contentSuccessful = false; // Variable para rastrear el estado de la muestra del listado
    private final String TAG = Activity_admin_lista_usuarios.class.getSimpleName();

    // Variables para mostrar los chefs
    RecyclerView recyclerView;
    Adapter_user adapter;
    List<User> userList = new ArrayList<>(); // Lista para almacenar los usuarios
    List<User> originalUserList = new ArrayList<>(); // Lista original para almacenar los usuarios

    // Variables para conectar con la API
    FastMethods mfastMethods;
    Retrofit retro;
    //variable para filtrar por usuario
    private EditText nombre_usuario_filtro;

    /**
     * Método onCreate para la configuración incial de la actividad
     * @param savedInstanceState estado de la instancia guardada, un objeto Bundle que contiene el estado previamente guardado de la actividad
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establece el diseño de la actividad.
        setContentView(R.layout.activity_admin_lista_usuarios);

        //Inicializar variables
        nombre_usuario_filtro = findViewById(R.id.edt_buscar_usuario);

        retro=FastClient.getClient();
        mfastMethods = retro.create(FastMethods.class);

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.rv_users);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter_user(this, userList);
        recyclerView.setAdapter(adapter);

        // Llamar al método recuperarDatos
        recuperarDatos();
    }

    /**
     * Método para recuperar la lista de usuarios del servidor
     */
    public void recuperarDatos(){
        userList.clear(); // Limpiar la lista actual
        originalUserList.clear(); //Limpiar la lista original
        Context context = this; // Obtener el contexto de la actividad (this)
        // Compruebe el estado de la conexión de red
        if (!Utils.isNetworkAvailable(this)) {
            Utils.showToastSecond(Activity_admin_lista_usuarios.this, context, "No hay conexión a Internet");
            contentSuccessful = false;
            return;
        }
        // Call HTTP client para recuperar la información del usuario
        Call<List<User>> call = mfastMethods.recuperar_todos_usuarios();
        call.enqueue(new Callback<List<User>>() { // Ejecutar la llamada de manera asíncrona
            /**
             * Método invocado cuando se recibe una respuesta de la solicitud HTTP
             * @param call llamada que generó la respuesta
             * @param response la respuesta recibida del servidor
             */
            @SuppressLint("NotifyDataSetChanged")
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    contentSuccessful = true;
                    userList.clear(); // Limpiar la lista actual
                    userList.addAll(response.body()); // Agregar todos los usuarios recuperados
                    //Hacer una copia de la lista original sin filtrar
                    originalUserList.clear();
                    originalUserList.addAll(userList);

                    // Notificar al adaptador que los datos han cambiado
                    adapter.notifyDataSetChanged();
                } else {
                    Utils.showToastSecond(Activity_admin_lista_usuarios.this, context,"No se encontraron usuarios");
                }
            }
            /**
             *Método invocado cuando ocurre un error durante la ejecución de la llamada HTTP
             * @param call la llamada que generó el error
             * @param t la excepción que ocurrió
             */
            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                // Error en la llamada, muestra el mensaje de error y registra la excepción
                contentSuccessful = false;
                t.printStackTrace();
                Log.e(TAG, "Error en la llamada:" + t.getMessage());
                Utils.showToastSecond(Activity_admin_lista_usuarios.this, context,"Error en la llamada: " + t.getMessage());
            }
        });
    }

    /**
     * Método para hacer logout
     * Redirige al usuario a la pantalla de inicio
     * @param view La vista (Button) a la que se hizo clic.
     */
    public void logout(View view){
        Utils.gotoActivity(Activity_admin_lista_usuarios.this, MainActivity_inicio.class);
    }

    /**
     * Método para test
     * @return devuelve un boleano en función de si ha ido bien la muestra de usuarios
     */
    public boolean isContentSuccessful() {
        return contentSuccessful;
    }

    /**
     * Método para filtrar la lista de usuarios por el nombre de usuario
     * @param view La vista (Button) a la que se hizo clic.
     */
    public void buscar_usuario(View view){
        String usuario = nombre_usuario_filtro.getText().toString();
        if(usuario.isEmpty()){
            Utils.showToast(this, "Introduza un usuario para buscarlo");
        }else{
            buscar(usuario);
        }
    }

    /**
     * Método para filtrar la lista de usuarios localmente por el nombre de usuario.
     * @param username a filtrar
     */
    public void buscar(String username) {
        String searchText = username; // Texto de búsqueda (nombre de usuario a filtrar)

        // Filtrar userList localmente con el texto de búsqueda
        List<User> filteredList = filterUsers(originalUserList, searchText);

        // Actualizar userList con la lista filtrada
        userList.clear();
        userList.addAll(filteredList);

        // Actualizar el adaptador en el hilo principal utilizando runOnUiThread()
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado
            }
        });

        // Actualizar el estado de contentSuccessful basado en si se encontraron usuarios después del filtro
        contentSuccessful = !userList.isEmpty(); // Si la lista filtrada no está vacía, entonces el contenido fue exitoso
    }
    /**
     * Método para filtrar la lista de usuarios localmente por el nombre de usuario.
     * @param userList Lista actual de usuarios
     * @param username Nombre de usuario a buscar para el filtro
     */
    public List<User> filterUsers(List<User> userList, String username) {
        List<User> filteredList = new ArrayList<>();

        // Convertir la palabra clave a minúsculas para hacer la búsqueda insensible a mayúsculas/minúsculas
        String lowercaseKeyword = username.toLowerCase();

        for (User user : userList) {
            // Obtener el nombre de usuario en minúsculas para hacer la comparación insensible a mayúsculas/minúsculas
            String lowercaseUsername = user.getUsuario().toLowerCase();

            // Verificar si el nombre de usuario contiene la palabra clave (ignorando mayúsculas/minúsculas)
            if (lowercaseUsername.contains(lowercaseKeyword)) {
                filteredList.add(user);
            }
        }

        return filteredList;
    }


}

