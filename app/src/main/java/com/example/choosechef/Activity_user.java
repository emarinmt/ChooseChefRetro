package com.example.choosechef;

import android.annotation.SuppressLint;
import android.content.Context;
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
 * Clase usuario
 * Gestiona las opciones del usuario
 * Muestra una lista de las reseñas del usuario
 */

public class Activity_user extends AppCompatActivity {
    private boolean contentSuccessful = false; // Variable para rastrear el estado de la muestra del listado
    private final String TAG = Activity_user.class.getSimpleName();

    // Variables para mostrar las reservas
    RecyclerView recyclerView;
    Adapter_reserva adapter;
    List<Reserva> reservasList = new ArrayList<>(); // Lista para almacenar las reservas

    // Variables para conectar con la API
    FastMethods mfastMethods;
    Retrofit retro;
    String token;
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
        // Obtener el token de SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MiPreferencia", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.rv_reservas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter_reserva(this, reservasList);
        recyclerView.setAdapter(adapter);

        // Llamar al método recuperarDatos
        recuperarDatos();
    }

    /**
     * Método para recuperar datos del servidor
     * LLama al servidor y recupera la lista de reserva del usuario logeado
     */
    public void recuperarDatos(){
        Context context = this;
        // Compruebe el estado de la conexión de red
        if (!Utils.isNetworkAvailable(this)) {
            Utils.showToastSecond(Activity_user.this, context,"No hay conexión a Internet");
            contentSuccessful = false;
            return;
        }
        // Call HTTP client para recuperar la información del usuario
        Call<List<Reserva>> call = mfastMethods.recuperar_reservas(token);
        call.enqueue(new Callback<List<Reserva>>() { // Ejecutar la llamada de manera asíncrona
            /**
             * Método invocado cuando se recibe una respuesta de la solicitud HTTP
             * @param call llamada que generó la respuesta
             * @param response la respuesta recibida del servidor
             */
            @SuppressLint("NotifyDataSetChanged")
            public void onResponse(@NonNull Call<List<Reserva>> call, @NonNull Response<List<Reserva>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    contentSuccessful = true;
                    reservasList.clear(); // Limpiar la lista actual
                    reservasList.addAll(response.body()); // Agregar todos los usuarios recuperados

                    // Notificar al adaptador que los datos han cambiado
                    adapter.notifyDataSetChanged();
                } else {
                    Utils.showToastSecond(Activity_user.this, context,"No se encontraron reservas");
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
                contentSuccessful = false;
                t.printStackTrace();
                Log.e(TAG, "Error en la llamada:" + t.getMessage());
                Utils.showToastSecond(Activity_user.this, context,"Error en la llamada: " + t.getMessage());
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
    /**
     * Método para test
     * @return devuelve un boleano en función de si ha ido bien la muestra de reservas
     */
    public boolean isContentSuccessful() {
        return contentSuccessful;
    }
}
