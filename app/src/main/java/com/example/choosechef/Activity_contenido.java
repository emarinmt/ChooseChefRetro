package com.example.choosechef;


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
    private final String TAG = Activity_contenido.class.getSimpleName();

    // Añadido por EVA para recibir el usuario en esta actividad y enviarlo a la siguiente( modificaicón perfil)
    //String usuario;
    //String pass;
    // Borrado por ELENA para usar token

    // Variables para mostrar los chefs
    RecyclerView recyclerView;
    Adapter_chef adapter;
    List<User> userList = new ArrayList<>(); // Lista para almacenar los chefs

    //ArrayList<String> items;

    // Variables para conectar con la API
    FastMethods mfastMethods;
    Retrofit retro;
    private User user; // ELENA

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
        //adapter = new Adapter(this,items);
        recyclerView.setAdapter(adapter);

        // Llamar al método recuperarDatos
        recuperarDatos();

    }

    /**
     * Método para recuperar la lista de chefs del servidor
     */
    public void recuperarDatos(){
        // Compruebe el estado de la conexión de red
        if (!Utils.isNetworkAvailable(this)) {
            Utils.showToast(Activity_contenido.this, "No hay conexión a Internet");
            return;
        }
        // Call HTTP client para recuperar la información del usuario
        Call<List<User>> call = mfastMethods.recuperar_chefs(); //CAMBIAR
        call.enqueue(new Callback<List<User>>() { // Ejecutar la llamada de manera asíncrona
            /**
             * Método invocado cuando se recibe una respuesta de la solicitud HTTP
             * @param call llamada que generó la respuesta
             * @param response la respuesta recibida del servidor
             */
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userList.clear(); // Limpiar la lista actual
                    userList.addAll(response.body()); // Agregar todos los usuarios recuperados

                    // Notificar al adaptador que los datos han cambiado
                    adapter.notifyDataSetChanged();
                } else {
                    Utils.showToast(Activity_contenido.this, "No se encontraron chefs");
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
                t.printStackTrace();
                Log.e(TAG, "Error en la llamada:" + t.getMessage());
                Utils.showToast(Activity_contenido.this, "Error en la llamada: " + t.getMessage());
            }
        });
    }


    /**
     * Método para manejar el clic del botón de modificación de perfil.
     * Dirige al usuario a la pantalla de modificacoón
     * @param view La vista (Button) que se hizo clic.
     */
    public void changeProfile (View view) {
        // Redirige al usuario a la pantalla de modificación de perfil
        //Utils.gotoActivity(MainActivity_contenido.this, MainActivity_mod_perfil.class);
        //Cambiado por EVA para poder recibir el usuario y la contraseña de la actividad anterior(login) y enviarlo a la siguiente actividad (modificación perfil)
        //Utils.gotoActivityMessage(Activity_contenido.this, Activity_mod_perfil.class, "usuario",usuario , "pass", pass, false);
        // Modificado por ELENA para usar el token
        // Redirige al usuario a la pantalla de modificación de perfil
        Utils.gotoActivity(Activity_contenido.this, Activity_mod_perfil.class);
    }
    public void search(View view) {
        // Redirige al usuario a la pantalla de busqueda
        Utils.gotoActivity(Activity_contenido.this, Activity_busqueda.class);


    }
    //DE MOMENTO SOLO MUESTSRA CHEFS
    public void ajustes (View view) {
        //cambiar cuando cree la siguiente pantalla ( falta añadir si es usuario admin, chef o usuario normal, de momento pongo ir a la clase chef para probarla.
        // Redirige segun el usuario a la pantalla de configuracion de chef, admin o usuario
        //Utils.gotoActivity(Activity_contenido.this, Activity_chef.class);
        Utils.gotoActivity(Activity_contenido.this, Activity_admin.class);
       // Utils.gotoActivity(Activity_contenido.this, Activity_user.class);

    }
    public void logout(View view){
        Utils.gotoActivity(Activity_contenido.this, MainActivity_inicio.class);
    }
}

