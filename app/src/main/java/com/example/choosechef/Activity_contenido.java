package com.example.choosechef;

import android.annotation.SuppressLint;
import android.app.Activity;
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
 * Clase contenido
 * Gestionar la actividad contenido
 * Muestra una lista de todos los chefs de la app
 * Después de filtrar la búsqueda muestra los chefs que cumplen las condiciones de búsqueda
 * Botón lupa permite ir a la pantalla de búsqueda donde seleccionar los criterios para filtrar la búsqueda de chefs
 * Botón modificación de perfil permite ir a la pantalla de modificación de perfil estandard para todos los usuarios
 * Botón ajustes, permite ir a diferentes pantallas en función del tipo de usuario
 *        usuario administrador, va a la pantalla administrador, para gestionar todos los usuarios de la app
 *        usuaio cliente, va a la pantalla usuario, para gestionar sus reservas e introducir reseñas
 *        usuario chef, va a la pantalla chef, para gestionar su perfil de chef y modificar el servicio que ofrece
 */
public class Activity_contenido extends AppCompatActivity {
    private boolean contentSuccessful = false; // Variable para rastrear el estado de la muestra del listado
    private final String TAG = Activity_contenido.class.getSimpleName();
    private static final int REQUEST_CODE = 1;

    // Variables para mostrar los chefs
    RecyclerView recyclerView;
    Adapter_chef_contenido adapter;
    List<User> userList = new ArrayList<>(); // Lista para almacenar los chefs

    // Variables para conectar con la API
    FastMethods mfastMethods;
    Retrofit retro;
    private User user;
    private User user_logeado;
    String token;
    //Variables para los filtros de búsqueda y la gestión de ir a diferentes pantallas en función del usuario
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
        setContentView(R.layout.activity_contenido);

        retro=FastClient.getClient();
        mfastMethods = retro.create(FastMethods.class);

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.rv_chefs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter_chef_contenido(this, userList);
        recyclerView.setAdapter(adapter);

        // Obtener el token de SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MiPreferencia", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        // Llamar al método recuperarChefs
        recuperarChefs();
    }

    /**
     * Método para recuperar la lista de chefs del servidor
     */
    public void recuperarChefs(){
        userList.clear(); // Limpiar la lista actual
        Context context = this; // Obtener el contexto de la actividad (this)
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
            @SuppressLint("NotifyDataSetChanged")
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
     * Método para manejar el clic del botón de modificación de perfil.
     * Dirige al usuario a la pantalla de modificación de perfil
     * @param view La vista (Button) a la que se hizo clic.
     */
    public void changeProfile (View view) {
        // Redirige al usuario a la pantalla de modificación de perfil
        Utils.gotoActivity(Activity_contenido.this, Activity_mod_perfil.class);
    }
    /**
     * Método para manejar el clic del botón lupa.
     * Dirige al usuario a la pantalla de búsqueda
     * @param view La vista (Button) a la que se hizo clic.
     */
    public void search(View view) {
        // Redirige al usuario a la pantalla de busqueda
        Intent intent = new Intent(Activity_contenido.this, Activity_busqueda.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    /**
     *Método llamado cuando se recibe un resultado de una actividad lanzada mediante startActityForResult()
     * @param requestCode El código de solicitud originalmente proporcionado a startActivityForResult(),
     *  *                     que permite identificar de quién proviene este resultado.
     * @param resultCode El código de resultado devuelto por la actividad secundaria a través de su setResult().
     * @param data Un Intent, que puede devolver datos de resultado al llamador
     *  *             (varios datos pueden adjuntarse a "extras" del Intent).
     */
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                // Obtener datos devueltos por Activity_busqueda
                prov_seleccionada = data.getStringExtra("provincia");
                comida_seleccionada = data.getStringExtra("comida");
                servicio_seleccionada = data.getStringExtra("servicio");

                // Usar los datos recibidos para actualizar la interfaz de usuario
                Log.d(TAG, "Provincia seleccionada: " + prov_seleccionada);
                Log.d(TAG, "Comida seleccionada: " + comida_seleccionada);
                Log.d(TAG, "Servicio seleccionado: " + servicio_seleccionada);

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

    /**
     *Método para filtrar la lista de usuarios segun los criterios de búsqueda de provincia, comida y servicio
     * @param userList la lista de usuarios a filtrar
     * @param provincia la provincia seleccionada como criterio de búsqueda
     * @param comida la comida seleccionada como criterio de búsqueda
     * @param servicio el servicio seleccionado como criterio de búsqueda
     * @return devuelve una lista de usuarios filtrada según los criterios especificados
     */
    public List<User> filterUsers(List<User> userList, String provincia, String comida, String servicio) {
        List<User> filteredList = new ArrayList<>();
        for (User user : userList) {
            // Aplicar el filtro basado en los criterios seleccionados
            if (matchesFilter(user, provincia, comida, servicio)) {
                filteredList.add(user);
            }
        }
        return filteredList;
    }

    /**
     * Método para verificar si el usuario cumple los criterios de filtro
     * @param user usuario a verificar
     * @param provincia criterio de búsqueda provincia a verificar
     * @param comida criterio de búsqueda comida a verificar
     * @param servicio criterio de búsqueda servicio a verificar
     * @return devuelve un booleano en función de si están verificados o no dichos campos
     */
    private boolean matchesFilter(User user, String provincia, String comida, String servicio) {
        // Verificar si el usuario cumple con los criterios de filtro
        boolean matchesProvincia = provincia == null || provincia.isEmpty() || user.getUbicacion().equalsIgnoreCase(provincia);
        boolean matchesComida = comida == null || comida.isEmpty() || user.getComida().equalsIgnoreCase(comida);
        boolean matchesServicio = servicio == null || servicio.isEmpty() || user.getServicio().equalsIgnoreCase(servicio);
        return matchesProvincia && matchesComida && matchesServicio;
    }

    /**
     * Método para gestionar el botón ajustes
     * Redirige al usuario según su tipo a la pantalla diseñada para gestionar sus opciones específicas.
     * @param view La vista (Button) a la que se hizo clic.
     */
    public void ajustes (View view) {
        // Obtenemos tipo de usuario del usuario logeado
        recuperarDatos(new Runnable() {
            @Override
            public void run() {
                if (mTipoLogeado != null && !mTipoLogeado.isEmpty()) {
                    if (mTipoLogeado.equalsIgnoreCase("chef")) {
                        Utils.gotoActivity(Activity_contenido.this, Activity_chef_menu.class);
                    } else if (mTipoLogeado.equalsIgnoreCase("client")) {
                        Utils.gotoActivity(Activity_contenido.this, Activity_client_lista_reservas.class);
                    } else if (mTipoLogeado.equalsIgnoreCase("admin")) {
                        Utils.gotoActivity(Activity_contenido.this, Activity_admin_menu.class);
                    } else {
                        Utils.showToast(Activity_contenido.this, "Tipo de usuario incorrecto");
                    }
                } else {
                    Utils.showToast(Activity_contenido.this, "Error obteniendo el tipo de usuario");
                }
            }
        });

    }

    /**
     * Método recuperarDatos
     * Recuperar los datos del usuario logeado
     */
    public void recuperarDatos(final Runnable callback){
        Context context = this; // Obtener el contexto de la actividad (this)
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
                        callback.run(); // Ejecuta el callback una vez que se haya recibido la respuesta
                    } else {
                        // Obtención de datos incorrecta, muestra un mensaje de error
                        Utils.showToastSecond(Activity_contenido.this,context, "Obtención de datos incorrecta");
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
                Utils.showToastSecond(Activity_contenido.this, context,"Error en la llamada: " + t.getMessage());
            }
        });
    }

    /**
     * Método para hacer logout
     * Redirige al usuario a la pantalla de inicio
     * @param view La vista (Button) a la que se hizo clic.
     */
    public void logout(View view){
        Utils.gotoActivity(Activity_contenido.this, MainActivity_inicio.class);
    }

    /**
     * Método para test
     * @return devuelve un booleano en función de si ha ido bien la muestra de contenido.
     */
    public boolean isContentSuccessful() {
        return contentSuccessful;
    }

    /**
     * Método para recuperar la lista de usuarios
     * @return devuelve la lista de usuarios
     */
    public List<User> getUserList() {
        return userList;
    }

}

