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
 * Clase desarrollada por EVA
 * Gestiona las opciones del usuario administrador
 * Muestra una lista de los usuarios de la app
 */

public class Activity_admin extends AppCompatActivity {

    private final String TAG = Activity_admin.class.getSimpleName();

    // Variables para mostrar los chefs
    RecyclerView recyclerView;
    Adapter_user adapter;
    List<User> userList = new ArrayList<>(); // Lista para almacenar los usuarios


    // Variables para conectar con la API
    FastMethods mfastMethods;
    Retrofit retro;
    private User user; // ELENA
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establece el diseño de la actividad.
        setContentView(R.layout.activity_admin);

        retro=FastClient.getClient();
        mfastMethods = retro.create(FastMethods.class);

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.rv_users);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter_user(this, userList);
        //adapter = new Adapter(this,items);
        recyclerView.setAdapter(adapter);

        // Llamar al método recuperarDatos
        recuperarDatos();
    }
    public void recuperarDatos(){
        // Compruebe el estado de la conexión de red
        if (!Utils.isNetworkAvailable(this)) {
            Utils.showToast(Activity_admin.this, "No hay conexión a Internet");
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
                    Utils.showToast(Activity_admin.this, "No se encontraron usuarios");
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
                Utils.showToast(Activity_admin.this, "Error en la llamada: " + t.getMessage());
            }
        });
    }
    public void editarUsuario(View view){
        //ira a la siguiente pantalla donde podrá modificar toda la info del usuario o borrarlo
        Utils.gotoActivity(Activity_admin.this, Activity_user_ampliado.class);

    }
    public void logout(View view){
        Utils.gotoActivity(Activity_admin.this, MainActivity_inicio.class);
    }
}

