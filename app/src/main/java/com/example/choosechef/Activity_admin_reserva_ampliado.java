package com.example.choosechef;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Clase reserva ampliado
 * Muestra todas las reservas del usuario logeado
 * Si la fecha de la reserva es igual o anterior a la fecha actual deja introducir una reseña ( valoración y comentario)
 */
public class Activity_admin_reserva_ampliado extends AppCompatActivity {
    private boolean contentSuccessful = false; // Variable para rastrear el estado de la muestra de la reserva
    private boolean modifySuccessful = false; // Variable para rastrear el estado de la modificacion de la reserva
    private boolean deleteSuccessful = false; // Variable para rastrear el estado del borrado
    private final String TAG = Activity_admin_reserva_ampliado.class.getSimpleName();
    //Variables para mostrar la información de la reserva
    private TextView nombre_chef;
    private TextView nombre_usuario;
    private TextView fecha_reserva;
    private EditText comentario;
    RatingBar valoracion;

    //Variable para recibir información de la reserva de la pantalla anterior
    public Intent intent;

    //Variables para conectar con la API
    FastMethods mfastMethods;
    Retrofit retro;
    public Reserva reserva = new Reserva();

    /**
     * Método onCreate para la configuración incial de la actividad
     * @param savedInstanceState estado de la instancia guardada, un objeto Bundle que contiene el estado previamente guardado de la actividad
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Establece el diseño de la actividad
        setContentView(R.layout.activity_admin_reserva_ampliado);
        contentSuccessful = false;
        modifySuccessful = false;

        retro=FastClient.getClient();
        mfastMethods = retro.create(FastMethods.class);
        // Reserva reserva = new Reserva();

        //Inicialización de variables
        nombre_usuario = findViewById(R.id.nombre_usuario_reserva);
        nombre_chef = findViewById(R.id.nombre_chef_reserva);
        fecha_reserva = findViewById(R.id.fecha_reserva);
        comentario = findViewById(R.id.comentario);
        valoracion = findViewById(R.id.rating_valoracion_reserva);

        //Por defecto los campos para añadir una reseña estan deshabilitados
        valoracion.setEnabled(false);
        comentario.setEnabled(false);

        // Obtener el Intent que inició esta actividad
        intent = getIntent();

        //Recuperar datos de la reserva de la pantalla anterior
        obtenerIntent(intent);

        valoracion.setEnabled(true);
        comentario.setEnabled(true);
       // fechaPosterior();
    }

    /**
     * Método para comprobar si la fecha es anterior o igual a hoy permite introducir la reseña ( valoraicón y comentario)
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void fechaPosterior(){
        //Si la fecha de la reserva es hoy o anterior se habilitan estos campos para escribir la reseña
        if(Utils.esFechaHoyAnterior(reserva.getFecha())){
            valoracion.setEnabled(true);
            comentario.setEnabled(true);
            Utils.showToast(Activity_admin_reserva_ampliado.this, "TRUE Introduce tu reseña!"+ reserva.getFecha());
        }else{
            Utils.showToast(Activity_admin_reserva_ampliado.this, "FALSE No puedes introducir la reseña todavia"+reserva.getFecha());
        }
    }

    /**
     * Método para obtener la información de la reserva de la pantalla anterior
     * @param intent contiene la información de la reserva de la pantalla anterior
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void obtenerIntent(Intent intent){
        // Verificar si el Intent contiene un extra con clave "user"
        if (intent != null && intent.hasExtra("reserva")) {
            contentSuccessful = true;
            // Extraer el objeto Reserva del Intent
            reserva = (Reserva) intent.getSerializableExtra("reserva");

            // Usar el objeto User en esta actividad
            if (reserva != null) {
                nombre_usuario.setText(reserva.getUsuario_cliente());
                nombre_chef.setText(reserva.getUsuario_chef());
                fecha_reserva.setText(reserva.getFecha());
                comentario.setText(reserva.getComentario());
                valoracion.setRating(reserva.getValoracion());
            }
            contentSuccessful = false;
        }
    }

    /**
     * Método para modificar la reserva en el servidor ( añadir valoración y comentario)
     * @param view La vista (Button) a la que se hizo clic.
     */
    public void editarReserva(View view){
        Context context = this; // Obtener el contexto de la actividad (this)
        //Recogemos los datos añadidos
        float valoracionInput = valoracion.getRating();
        String comentarioInput = comentario.getText().toString();

        // Comprueba el estado de la conexión de red
        if (!Utils.isNetworkAvailable(this)) {
            Utils.showToast(Activity_admin_reserva_ampliado.this, "No hay conexión a Internet");
            return;
        }
        //Actualiza los datos de la reserva con los nuevos datos
        reserva.setValoracion(valoracionInput);
        reserva.setComentario(comentarioInput);

        // call HTTP client para modificar los datos de usuario
        Call<String> call = mfastMethods.modificar_reserva(reserva.getId(),reserva);
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
                    Utils.showToastSecond(Activity_admin_reserva_ampliado.this, context,"Modificación correcta!");
                    Utils.gotoActivity(Activity_admin_reserva_ampliado.this, Activity_admin_lista_reservas.class);
                } else {
                    Utils.showToastSecond(Activity_admin_reserva_ampliado.this, context,"Error al modificar la reseña");
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
                Utils.showToastSecond(Activity_admin_reserva_ampliado.this, context,"Error en la llamada: " + t.getMessage());
            }
        });
    }

    /**
     * Método para borrar la reserva en el servidor
     * @param view La vista (Button) a la que se hizo clic.
     */
    public void borrarReserva(View view) {
        Context context = this; // Obtener el contexto de la actividad (this)
        if (reserva.getId() != 0) {
            // call HTTP client para borrar al usuario
            Call<String> call = mfastMethods.borrar_reserva(reserva.getId());

            call.enqueue(new Callback<String>() { // Ejecutar la llamada de manera asíncrona
                /**
                 * Método invocado cuando se recibe una respuesta de la solicitud HTTP
                 * @param call     llamada que generó la respuesta
                 * @param response la respuesta recibida del servidor
                 */
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        deleteSuccessful = true;
                        Log.d(TAG, "Eliminación exitosa");
                        Utils.showToastSecond(Activity_admin_reserva_ampliado.this, context, "Eliminación correcta!");
                        Utils.gotoActivity(Activity_admin_reserva_ampliado.this, Activity_admin_lista_reservas.class);
                    } else {
                        Utils.showToastSecond(Activity_admin_reserva_ampliado.this, context, "Error al eliminar la reseña");
                    }
                }
                /**
                 * Método invocado cuando ocurre un error durante la ejecución de la llamada HTTP
                 * @param call la llamada que generó el error
                 * @param t    la excepción que ocurrió
                 */
                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    // Error en la llamada, muestra el mensaje de error y registra la excepción
                    t.printStackTrace();
                    Log.e(TAG, "Error en la llamada:" + t.getMessage());
                    Utils.showToastSecond(Activity_admin_reserva_ampliado.this, context, "Error en la llamada: " + t.getMessage());
                }
            });
        } else {
            Utils.showToastSecond(Activity_admin_reserva_ampliado.this, context,"Error la reserva esta vacía");
        }
    }

    /**
     * Método para hacer logout
     * Redirige al usuario a la pantalla de inicio
     * @param view La vista (Button) a la que se hizo clic.
     */
    public void logout(View view){
        Utils.gotoActivity(Activity_admin_reserva_ampliado.this, MainActivity_inicio.class);
    }

    /**
     * Método para test
     * @return devuelve un booleano en funcion de si ha ido bien al muestra de contenido
     */
    public boolean isContentSuccessful() {
        return contentSuccessful;
    }
    /**
     * Método para test
     * @return devuelve un boleano en función de si ha ido bien la modificación de reservas
     */
    public boolean isModifySuccessful() {
        return modifySuccessful;
    }
    /**
     * Método para test
     * @return devuelve un booleano en funcion de si ha ido bien la eliminación de usuario
     */
    public boolean isDeleteSuccessful() {
        return deleteSuccessful;
    }
    /**
     * Método para test
     * @return devuelve el contenido del comentario de una reserva
     */
    public String getComentReserva() {
        return comentario.getText().toString(); // Obtener el texto del Edittext comentario
    }
}
