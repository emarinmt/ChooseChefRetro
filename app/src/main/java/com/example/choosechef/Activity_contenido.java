package com.example.choosechef;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
 * Clase desarrollada por ELENA
 * para gestionar la actividad contenido
 */

public class Activity_contenido extends AppCompatActivity {
    private boolean contentSuccessful = false; // Variable para rastrear el estado de la muestra del listado
    private final String TAG = Activity_contenido.class.getSimpleName();
    private static final int REQUEST_CODE = 1;

    // Variables para mostrar los chefs
    RecyclerView recyclerView;
    Adapter_chef adapter;
    List<User> userList = new ArrayList<>(); // Lista para almacenar los chefs

    // Variables para conectar con la API
    FastMethods mfastMethods;
    Retrofit retro;
    private User user; // ELENA
    private User user_logeado; // ELENA
    String token;
    String mTipoLogeado;
    String prov_seleccionada;
    String comida_seleccionada;
    String servicio_seleccionada;
    /**
     * Método onCreate para la configuración incial de la actividad
     * @param savedInstanceState estado de la instancia guardada, un objeto Bundle que contiene el estado previamente guardado de la actividad
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establece el diseño de la actividad.
        setContentView(R.layout.activity_content);

        retro=FastClient.getClient();
        mfastMethods = retro.create(FastMethods.class);

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.rv_chefs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter_chef(this, userList);
        recyclerView.setAdapter(adapter);

        // Obtener el token de SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MiPreferencia", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        // Llamar al método recuperarDatos
        recuperarChefs();
    }

    /**
     * Método para recuperar la lista de chefs del servidor
     */
    public void recuperarChefs(){
        userList.clear(); // Limpiar la lista actual
        // Obtener el contexto de la actividad (this)
        Context context = this;
        // Compruebe el estado de la conexión de red
        if (!Utils.isNetworkAvailable(this)) {
            Utils.showToastSecond(Activity_contenido.this, context,"No hay conexión a Internet");
            contentSuccessful = false;
            return;
        }
        // Call HTTP client para recuperar la información del usuario
        Call<List<User>> call = mfastMethods.recuperar_chefs();  //CAMBIAR
        call.enqueue(new Callback<List<User>>() { // Ejecutar la llamada de manera asíncrona
            /**
             * Método invocado cuando se recibe una respuesta de la solicitud HTTP
             * @param call llamada que generó la respuesta
             * @param response la respuesta recibida del servidor
             */
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    contentSuccessful = true;
                    userList.clear(); // Limpiar la lista actual
                    userList.addAll(response.body()); // Agregar todos los usuarios recuperados

                    // Notificar al adaptador que los datos han cambiado
                    adapter.notifyDataSetChanged();
                } else {
                    Utils.showToastSecond(Activity_contenido.this, context,"No se encontraron chefs");
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
                Utils.showToastSecond(Activity_contenido.this, context,"Error en la llamada: " + t.getMessage());
            }
        });
    }

    /**
     * Método recuperarDatos
     * Para recuperar los datos del usuario logeado
     */
    public void recuperarDatos(){
        // Compruebe el estado de la conexión de red
        if (!Utils.isNetworkAvailable(this)) {
            Utils.showToast(Activity_contenido.this, "No hay conexión a Internet");
            return;
        }
        // Call HTTP client para recuperar la información del usuario
        Call<User> call = mfastMethods.recuperar_info(token);
        call.enqueue(new Callback<User>() { // Ejecutar la llamada de manera asíncrona
            /**
             * Método invocado cuando se recibe una respuesta de la solicitud HTTP
             * @param call llamada que generó la respuesta
             * @param response la respuesta recibida del servidor
             */
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    user_logeado = response.body(); // Recibe los datos del usuario
                    if (user_logeado != null) {
                        mTipoLogeado = user_logeado.getTipo();
                    } else {
                        // Obtención de datos incorrecta, muestra un mensaje de error
                        Utils.showToast(Activity_contenido.this, "Obtención de datos incorrecta");
                    }
                }
            }
            /**
             *Método invocado cuando ocurre un error durante la ejecución de la llamada HTTP
             * @param call la llamada que generó el error
             * @param t la excepción que ocurrió
             */
            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                // Error en la llamada, muestra el mensaje de error y registra la excepción
                t.printStackTrace();
                Log.e(TAG, "Error en la llamada:" + t.getMessage());
                Utils.showToast(Activity_contenido.this, "Error en la llamada: " + t.getMessage());
            }
        });
    }


    /**
     * Método para manejar el clic del botón de modificación de perfil.
     * Dirige al usuario a la pantalla de modificación
     * @param view La vista (Button) que se hizo clic.
     */
    public void changeProfile (View view) {
        // Redirige al usuario a la pantalla de modificación de perfil
        Utils.gotoActivity(Activity_contenido.this, Activity_mod_perfil.class);
    }
    public void search(View view) {
        // Redirige al usuario a la pantalla de busqueda
        Utils.gotoActivityWithResult(Activity_contenido.this, Activity_busqueda.class, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    // Obtener datos devueltos por ActivityB
                    prov_seleccionada = data.getStringExtra("provincia");
                    comida_seleccionada = data.getStringExtra("comida");
                    servicio_seleccionada = data.getStringExtra("servicio");

                    // Filtrar userList localmente con los filtros seleccionados
                    List<User> filteredList = filterUsers(userList, prov_seleccionada, comida_seleccionada, servicio_seleccionada);

                    // Actualizar userList con la lista filtrada
                    userList.clear();
                    userList.addAll(filteredList);

                    // Mostrar la lista filtrada en el RecyclerView
                    adapter.notifyDataSetChanged();

                }
            }
        }
    }

    private List<User> filterUsers(List<User> userList, String provincia, String comida, String servicio) {
        List<User> filteredList = new ArrayList<>();
        for (User user : userList) {
            // Aplicar el filtro basado en los criterios seleccionados
            if (matchesFilter(user, provincia, comida, servicio)) {
                filteredList.add(user);
            }
        }
        return filteredList;
    }
    private boolean matchesFilter(User user, String provincia, String comida, String servicio) {
        // Verificar si el usuario cumple con los criterios de filtro
        boolean matchesProvincia = provincia == null || provincia.isEmpty() || user.getUbicacion().equalsIgnoreCase(provincia);
        boolean matchesComida = comida == null || comida.isEmpty() || user.getComida().equalsIgnoreCase(comida);
        boolean matchesServicio = servicio == null || servicio.isEmpty() || user.getServicio().equalsIgnoreCase(servicio);
        return matchesProvincia && matchesComida && matchesServicio;
    }
    public void ajustes (View view) {
       // Utils.gotoActivity(Activity_contenido.this, Activity_admin.class);
        //COMENTO PORQUE ME DA PROBLEMAS CON EL USUARIO ADMIN, SOLO QUIERO COMPROBAR QUE ESA CLASE FUNCIONA
        // Obtenemos tipo de usuario del usuario logeado
        recuperarDatos();
        if (mTipoLogeado != null && !mTipoLogeado.isEmpty()) {
            // Redirige segun el usuario a la pantalla de configuracion de chef, admin o usuario
            if (mTipoLogeado.equalsIgnoreCase("chef")){
                Utils.gotoActivity(Activity_contenido.this, Activity_chef.class);
            } else if (mTipoLogeado.equalsIgnoreCase("client")) {
                Utils.gotoActivity(Activity_contenido.this, Activity_user.class);
            } else if (mTipoLogeado.equalsIgnoreCase("admin")) {   //DE MOMENTO SOLO MUESTSRA CHEFS
                Utils.gotoActivity(Activity_contenido.this, Activity_admin.class);
            } else {
                Utils.showToast(Activity_contenido.this, "Tipo de usuario incorrecto");
            }
        } else {
            Utils.showToast(Activity_contenido.this, "Error obteniendo el tipo de usuario");
        }

    }
    public void logout(View view){
        Utils.gotoActivity(Activity_contenido.this, MainActivity_inicio.class);
    }

    public boolean isContentSuccessful() {
        return contentSuccessful;
    }

    public List<User> getUserList() {
        return userList;
    }

}

