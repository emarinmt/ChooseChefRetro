package com.example.choosechef;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
 * Clase chef ampliado.
 * Muestra la información de un chef ( Nombre, ubicación, comida, servicio y descripción)
 * Botón reservar lleva a otra pantalla donde seleccionar la fecha de la reserva
 * Botón contacto muestra el teléfono del chef
 */
public class Activity_chef_ampliado extends AppCompatActivity {
    private boolean contentSuccessful = false; // Variable para rastrear el estado de la muestra del chef
    private final String TAG = Activity_chef_ampliado.class.getSimpleName();
    //Variables para el texto mostrado en pantalla
    private TextView nombreChefamp;
    private TextView tipoChefamp;
    private TextView provChefamp;
    private TextView servChefamp;
    private TextView descChefamp;
    private TextView telefonoChefamp;
    public Intent intent;
    private String user_chef;
    // Variables para mostrar las reservas
    RecyclerView recyclerView;
    Adapter_reseña_chef_amp adapter;
    List<Reserva> reservasList = new ArrayList<>(); // Lista para almacenar las reservas
    // Variables para conectar con la API
    FastMethods mfastMethods;
    Retrofit retro;

    /**
     * Método onCreate para la configuración incial de la actividad
     * @param savedInstanceState estado de la instancia guardada, un objeto Bundle que contiene el estado previamente guardado de la actividad
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establece el diseño de la actividad.
        setContentView(R.layout.activity_chef_ampliado);
        contentSuccessful = false;

        // Inicialización de variables
        nombreChefamp = findViewById(R.id.nombre_chef_ampliado);
        tipoChefamp = findViewById(R.id.tipo_comida_ampliado);
        provChefamp = findViewById(R.id.provincia_ampliado);
        servChefamp = findViewById(R.id.servicio_ampliado);
        descChefamp = findViewById(R.id.descripcion_ampliado);
        telefonoChefamp = findViewById(R.id.edt_telefono_chef);
        ImageView imagen = findViewById(R.id.foto_chef_ampliado);

        // Obtener el Intent que inició esta actividad
        intent = getIntent();
        //Muestra la información obtenida por pantalla
        obtenerIntent(intent);

        //conectar con api
        retro=FastClient.getClient();
        mfastMethods = retro.create(FastMethods.class);
        // Configurar RecyclerView
        recyclerView = findViewById(R.id.rv_reseñas_chef_amp);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter_reseña_chef_amp(this, reservasList);
        recyclerView.setAdapter(adapter);
        // Llamar al método recuperarDatos
        recuperarDatos();
    }

    /**
     * Método para ir a la clase reserva donde se escoge la fecha de reserva
     * @param view La vista (Button) a la que se hizo clic.
     */
    public void reservar(View view){
        //va a la pantalla reservar donde vemos calendarios y crearemos la reserva ( envia el usuario chef de esta actividad)
        Utils.gotoActivityWithString(Activity_chef_ampliado.this, Activity_reservar.class, user_chef);
    }

    /**
     * Método para mostrar el teléfono del chef
     * @param view La vista (Button) a la que se hizo clic.
     */
    public void contactar(View view){
        //muestra el telefono del chef
        telefonoChefamp.setVisibility(View.VISIBLE);
    }

    /**
     * Método para hacer logout
     * Redirige al usuario a la pantalla de inicio
     * @param view La vista (Button) a la que se hizo clic.
     */
    public void logout(View view){
        Utils.gotoActivity(Activity_chef_ampliado.this, MainActivity_inicio.class);
    }

    /**
     * Método para recibir la información del usuario de la pantalla anterior
     * @param intent contiene la información del usuario de la pantalla anterior
     */
    public void obtenerIntent(Intent intent){
        // Verificar si el Intent contiene un extra con clave "user"
        if (intent != null && intent.hasExtra("user")) {
            // Extraer el objeto User del Intent
            User user = (User) intent.getSerializableExtra("user");
            // Usar el objeto User en esta actividad
            if (user != null) {
                contentSuccessful = true;
                Log.d("Activity_chef_ampliado", "contentSuccessful: " + contentSuccessful);
                //Mostrar información en pantalla
                nombreChefamp.setText(user.getNombre());
                tipoChefamp.setText(user.getComida());
                provChefamp.setText(user.getUbicacion());
                servChefamp.setText(user.getServicio());
                descChefamp.setText(user.getDescripcion());
                telefonoChefamp.setText(user.getTelefono());
                //Ocultar el teléfono
                telefonoChefamp.setVisibility(View.INVISIBLE);
                //guardar el usuario del chef para enviarlo a la siguiente pantalla
                user_chef = user.getUsuario();
            }
        } else {
            // Si no se recibió información del usuario, la carga de contenido falló
            contentSuccessful = false;
        }
    }

    /**
     * Método para test
     * @return devuelve un booleano en función de si ha ido bien la muestra de datos
     */
    public boolean isContentSuccessful() {
        return contentSuccessful;
    }

    /**
     * Método para recuperar datos del servidor
     * LLama al servidor y recupera la lista de reserva del usuario logeado
     */
    public void recuperarDatos(){
        Context context = this; // Obtener el contexto de la actividad (this)
        // Compruebe el estado de la conexión de red
        if (!Utils.isNetworkAvailable(this)) {
            Utils.showToastSecond(Activity_chef_ampliado.this, context,"No hay conexión a Internet");
            contentSuccessful = false;
            return;
        }
        //NECESITAMOS METODO NUEVO QUE RECUPERE TODAS LAS RESERVAS DEL CHEF-----------------------------------------------------------
        // Call HTTP client para recuperar la información del usuario
        Call<List<Reserva>> call = mfastMethods.recuperar_reseñas_chef(user_chef);
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
                    Utils.showToastSecond(Activity_chef_ampliado.this, context,"No se encontraron reservas");
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
                Utils.showToastSecond(Activity_chef_ampliado.this, context,"Error en la llamada: " + t.getMessage());
            }
        });
    }
}
