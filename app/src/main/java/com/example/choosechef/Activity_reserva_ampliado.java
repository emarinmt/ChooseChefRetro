package com.example.choosechef;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import java.time.LocalDate;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Activity_reserva_ampliado  extends AppCompatActivity {
    private boolean contentSuccessful = false; // Variable para rastrear el estado de la muestra de la reserva
    private boolean modifySuccessful = false; // Variable para rastrear el estado de la modificacion de la reserva
    private final String TAG = Activity_reserva_ampliado.class.getSimpleName();
    private TextView nombre_chef;
    private TextView fecha_reserva;
    private EditText comentario;
    RatingBar valoracion;
    public Intent intent3;
    FastMethods mfastMethods;
    Retrofit retro;
    public Reserva reserva;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva_ampliado);
        contentSuccessful = false;
        modifySuccessful = false;

        retro=FastClient.getClient();
        mfastMethods = retro.create(FastMethods.class);
        reserva = new Reserva();

        //Inicialización de variables
        nombre_chef = findViewById(R.id.nombre_chef_reserva);
        fecha_reserva = findViewById(R.id.fecha_reserva);
        comentario = findViewById(R.id.comentario);
        valoracion = findViewById(R.id.rating_valoracion_reserva);

        // Obtener el Intent que inició esta actividad
        intent3 = getIntent();

        //Recuperar datos de la reserva de la pantalla anterior
        obtenerIntent(intent3);

        //Por defecto los campos para añadir una reseña estan deshabilitados
        valoracion.setEnabled(false);
        comentario.setEnabled(false);

        //Si la fecha de la reserva es hoy o posterior se habilitan estos campos para escribir la reseña
        if(reserva.esFechaHoyPosterior()){
            valoracion.setEnabled(true);
            comentario.setEnabled(true);
        }
    }


    public void obtenerIntent(Intent intent){
        // Verificar si el Intent contiene un extra con clave "user"
        if (intent != null && intent.hasExtra("reserva")) {
            contentSuccessful = true;
            // Extraer el objeto Reserva del Intent
            Reserva reserva = (Reserva) intent.getSerializableExtra("reserva");

            // Usar el objeto User en esta actividad
            if (reserva != null) {
                nombre_chef.setText(reserva.getUsuario_chef());
                fecha_reserva.setText(reserva.getFecha().toString());
                comentario.setText(reserva.getComentario());
                valoracion.setRating(reserva.getValoracion());
            }
        }
        contentSuccessful = false;
    }
    public void confirmarComentario(View view){
        //Recogemos los datos añadidos
        Float valoracionInput = valoracion.getRating();
        String comentarioInput = comentario.getText().toString();

        // Comprueba el estado de la conexión de red
        if (!Utils.isNetworkAvailable(this)) {
            Utils.showToast(Activity_reserva_ampliado.this, "No hay conexión a Internet");
            return;
        }
        //Actualiza los datos de la reserva con los nuevos datos
        reserva.setValoracion(valoracionInput);
        reserva.setComentario(comentarioInput);

        // call HTTP client para modificar los datos de usuario
        Call<String> call = mfastMethods.modificar_reserva(reserva);
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
                    Utils.showToast(Activity_reserva_ampliado.this, "Modificación correcta!");
                    Utils.gotoActivity(Activity_reserva_ampliado.this, Activity_user.class);
                } else {
                    Utils.showToast(Activity_reserva_ampliado.this, "Error al modificar la reseña");
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
                Utils.showToast(Activity_reserva_ampliado.this, "Error en la llamada: " + t.getMessage());
            }
        });
    }

    public void logout(View view){
        Utils.gotoActivity(Activity_reserva_ampliado.this, MainActivity_inicio.class);
    }
    public boolean isContentSuccessful() {
        return contentSuccessful;
    }

}
