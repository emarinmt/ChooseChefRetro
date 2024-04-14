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
 * Clase usuario
 * Gestiona las opciones del usuario
 * Muestra una lista de las reseñas del usuario
 */

public class Activity_user extends AppCompatActivity {
    private final String TAG = Activity_user.class.getSimpleName();

    // Variables para mostrar las reservas
    RecyclerView recyclerView;
    Adapter_reserva adapter;
    List<Reserva> reservasList = new ArrayList<>(); // Lista para almacenar las reservas

    // Variables para conectar con la API
    FastMethods mfastMethods;
    Retrofit retro;
    /**
     * Método onCreate para la configuración incial de la actividad
     * @param savedInstanceState estado de la instancia guardada, un objeto Bundle que contiene el estado previamente guardado de la actividad
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establece el diseño de la actividad.
        setContentView(R.layout.activity_user);

        retro=FastClient.getClient();
        mfastMethods = retro.create(FastMethods.class);

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.rv_reservas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter_reserva(this, reservasList);
        //adapter = new Adapter(this,items);
        recyclerView.setAdapter(adapter);

        // Llamar al método recuperarDatos
        recuperarDatos();
    }

    /**
     * Método para recuperar datos del servidor
     * LLama al servidor y recupera la lista de reserva del usuario logeado
     */
    public void recuperarDatos(){
        // Compruebe el estado de la conexión de red
        if (!Utils.isNetworkAvailable(this)) {
            Utils.showToast(Activity_user.this, "No hay conexión a Internet");
            return;
        }
        // Call HTTP client para recuperar la información del usuario
        Call<List<Reserva>> call = mfastMethods.recuperar_reservas(); //CAMBIAR
        call.enqueue(new Callback<List<Reserva>>() { // Ejecutar la llamada de manera asíncrona
            /**
             * Método invocado cuando se recibe una respuesta de la solicitud HTTP
             * @param call llamada que generó la respuesta
             * @param response la respuesta recibida del servidor
             */
            public void onResponse(@NonNull Call<List<Reserva>> call, @NonNull Response<List<Reserva>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    reservasList.clear(); // Limpiar la lista actual
                    reservasList.addAll(response.body()); // Agregar todos los usuarios recuperados

                    // Notificar al adaptador que los datos han cambiado
                    adapter.notifyDataSetChanged();
                } else {
                    Utils.showToast(Activity_user.this, "No se encontraron reservas");
                }
            }
            /**
             *Método invocado cuando ocurre un error durante la ejecución de la llamada HTTP
             * @param call la llamada que generó el error
             * @param t la excepción que ocurrió
             */
            @Override
            public void onFailure(@NonNull Call<List<Reserva>> call, @NonNull Throwable t) {
                // Error en la llamada, muestra el mensaje de error y registra la excepción
                t.printStackTrace();
                Log.e(TAG, "Error en la llamada:" + t.getMessage());
                Utils.showToast(Activity_user.this, "Error en la llamada: " + t.getMessage());
            }
        });
    }
    /**
     * Método para hacer logout
     * Redirige al usuario a la pantalla de inicio
     * @param view La vista (Button) a la que se hizo clic.
     */
    public void logout(View view){
        Utils.gotoActivity(Activity_user.this, MainActivity_inicio.class);
    }
}
