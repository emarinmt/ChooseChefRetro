package com.example.choosechef;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Clase reserva
 * Muestra un calendario donde el usuario escoge una fecha para reservar
 * Llama al servidor para crear la reserva, el servidor comprueba si ese dia esta disponible y si es así crea la reserva
 */
public class Activity_reservar extends AppCompatActivity {
    private final String TAG = Activity_reservar.class.getSimpleName();
    private boolean contentSuccessful = false; // Variable para rastrear el estado de la muestra del usuario
    private boolean modifySuccessful = false; // Variable para rastrear el estado de la modificación
    //Variable para el calendario
    CalendarView calendario;
    //Variables para conectar con la API
    FastMethods mfastMethods;
    Retrofit retro;
    //Variable para recibir información de la pantalla anterior
    public Intent intent;

    private User user_logeado;
    private String user_chef;
    String usuario_cliente;
    String token;
    Reserva reserva;
    private String fechaStr;

    /**
     * Método onCreate para la configuración incial de la actividad
     * @param savedInstanceState estado de la instancia guardada, un objeto Bundle que contiene el estado previamente guardado de la actividad
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establece el diseño de la actividad.
        setContentView(R.layout.activity_reservar);
        contentSuccessful = false;
        //Inicializa variable
        calendario = findViewById(R.id.calendarView);

        retro=FastClient.getClient();
        mfastMethods = retro.create(FastMethods.class);
        reserva = new Reserva();

        // Obtener el Intent que inició esta actividad
        intent = getIntent();
        //recoge el usuario chef de la actividad anterior
        obtenerIntentChef(intent);

        // Obtener el token de SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MiPreferencia", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        //Recoge el usuario cliente logeado
        recuperarDatosCliente();

        //Recoge la fecha introducida por el usuario para la reserva
        calendario.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            //Se define el formato de dos digitos para el mes ( se suma uno,porque el calendario empieza en el 0 )
            @SuppressLint("DefaultLocale") String monthStr = String.format("%02d", month +1);
            //Se construye la fecha con el formato deseado para el servidor
            fechaStr = year + "-" + monthStr +"-"+ dayOfMonth;
            //mostramos la fecha escogida
            Utils.showToast(Activity_reservar.this, fechaStr);
        });

    }

    /**
     * Método para obtener el usuario chef
     * @param intent string usuario chef
     */
    public void obtenerIntentChef(Intent intent){
        // Verificar si el Intent contiene un extra con clave "user"
        if (intent != null && intent.hasExtra("string")) {
            contentSuccessful = true;
            // Extraer el objeto User del Intent
            user_chef = (String) intent.getSerializableExtra("string");

            // Usar el objeto User en esta actividad
            if (user_chef != null) {
                reserva.setUsuario_chef(user_chef);
            }
        }
        contentSuccessful = false;
    }

    /**
     * Método para obtener el usuario cliente
     */
    public void recuperarDatosCliente(){
        // Compruebe el estado de la conexión de red
        if (!Utils.isNetworkAvailable(this)) {
            Utils.showToast(Activity_reservar.this, "No hay conexión a Internet");
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
                        usuario_cliente = user_logeado.getUsuario();
                        reserva.setUsuario_cliente(usuario_cliente);
                    } else {
                        // Obtención de datos incorrecta, muestra un mensaje de error
                        Utils.showToast(Activity_reservar.this, "Obtención de datos incorrecta");
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
                Utils.showToast(Activity_reservar.this, "Error en la llamada: " + t.getMessage());
            }
        });
    }

    /**
     * Método para gestionar la creación de la reserva
     * @param view La vista (Button) a la que se hizo clic.
     */
    public void reservar (View view) {
        //guarda los datos en el objeto reserva
        reserva.setUsuario_chef(user_chef);
        reserva.setUsuario_cliente(usuario_cliente);
        reserva.setFecha(fechaStr);
        reserva.setComentario(" ");
        reserva.setValoracion(0.0F);

        // call HTTP client para modificar los datos de usuario
        Call<String> call = mfastMethods.crear_reserva(reserva);
        call.enqueue(new Callback<String>() { // Ejecutar la llamada de manera asíncrona
            /**
             * Método invocado cuando se recibe una respuesta de la solicitud HTTP
             * @param call llamada que generó la respuesta
             * @param response la respuesta recibida del servidor
             */
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    modifySuccessful = true;
                    // String responseBody = response.body();
                    Utils.showToast(Activity_reservar.this, "Reserva correcta!");
                    //volvemos a pantalla anterior
                    Utils.gotoActivity(Activity_reservar.this, Activity_chef_ampliado.class);
                } else {
                    Utils.showToast(Activity_reservar.this, "Error al crear la reserva");
                }
            }

            /**
             * Método invocado cuando ocurre un error durante la ejecución de la llamada HTTP
             * @param call la llamada que generó el error
             * @param t la excepción que ocurrió
             */
            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                // Error en la llamada, muestra el mensaje de error y registra la excepción
                t.printStackTrace();
                Log.e(TAG, "Error en la llamada:" + t.getMessage());
                Utils.showToast(Activity_reservar.this, "Error en la llamada: " + t.getMessage());
            }
        });
    }
    /**
     * Método para hacer logout
     * Redirige al usuario a la pantalla de inicio
     * @param view La vista (Button) a la que se hizo clic.
     */
    public void logout(View view){
        Utils.gotoActivity(Activity_reservar.this, MainActivity_inicio.class);
    }
    /**
     * Método para test
     * @return devuelve un booleano en función de si ha ido bien la muestra de contenido.
     */
    public boolean isContentSuccessful() {
        return contentSuccessful;
    }
    /**
     * Método para test
     * @return devuelve un booleano en función de si ha ido bien la creación de la reserva.
     */
    public boolean isModifySuccessful() {
        return modifySuccessful;
    }

}
