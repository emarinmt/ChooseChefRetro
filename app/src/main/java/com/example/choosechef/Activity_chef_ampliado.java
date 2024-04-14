package com.example.choosechef;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Clase chef ampliado.
 * Muestra la información de un chef ( Nombre, ubicación, comida, servicio y descripción)
 * Botón reservar lleva a otra pantalla donde seleccionar la fecha de la reserva
 * Botón contacto muestra el teléfono del chef
 */
public class Activity_chef_ampliado extends AppCompatActivity {
    private boolean contentSuccessful = false; // Variable para rastrear el estado de la muestra del chef
    //Variables para el texto mostrado en pantalla
    private TextView nombreChefamp;
    private TextView tipoChefamp;
    private TextView provChefamp;
    private TextView servChefamp;
    private TextView descChefamp;
    private TextView telefonoChefamp;
    public Intent intent;

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

        // Obtener el Intent que inició esta actividad
        intent = getIntent();
        //Muestra la información obtenida por pantalla
        obtenerIntent(intent);
    }

    /**
     * Método para ir a la clase reserva donde se escoge la fecha de reserva
     * @param view La vista (Button) a la que se hizo clic.
     */
    public void reservar(View view){
        //va a la pantalla reservar donde vemos calendarios y crearemos la reserva
        Utils.gotoActivity(Activity_chef_ampliado.this, Activity_reservar.class);
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
            contentSuccessful = true;
            // Extraer el objeto User del Intent
            User user = (User) intent.getSerializableExtra("user");

            // Usar el objeto User en esta actividad
            if (user != null) {
                //Mostrar información en pantalla
                nombreChefamp.setText(user.getNombre());
                tipoChefamp.setText(user.getComida());
                provChefamp.setText(user.getUbicacion());
                servChefamp.setText(user.getServicio());
                descChefamp.setText(user.getDescripcion());
                telefonoChefamp.setText(user.getTelefono());
                //Ocultar el teléfono
                telefonoChefamp.setVisibility(View.INVISIBLE);
            }
        }
        contentSuccessful = false;
    }

    /**
     * Método para test
     * @return devuelve un booleano en función de si ha ido bien la muestra de datos
     */
    public boolean isContentSuccessful() {
        return contentSuccessful;
    }
}
