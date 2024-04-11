package com.example.choosechef;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Clase desarrollada por ELENA
 * para mostrar toda la información de un chef
 */
public class Activity_chef_ampliado extends AppCompatActivity {
    private TextView nombreChefamp;
    private TextView tipoChefamp;
    private TextView provChefamp;
    private TextView descChefamp;
    private TextView telefonoChefamp;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_ampliado);

        // Inicialización de variables
        nombreChefamp = findViewById(R.id.nombre_chef_ampliado);
        tipoChefamp = findViewById(R.id.tipo_comida_ampliado);
        provChefamp = findViewById(R.id.provincia_ampliado);
        descChefamp = findViewById(R.id.descripcion_ampliado);

        telefonoChefamp = findViewById(R.id.edt_telefono_chef);

        // Obtener el Intent que inició esta actividad
        Intent intent = getIntent();

        // Verificar si el Intent contiene un extra con clave "user"
        if (intent != null && intent.hasExtra("user")) {
            // Extraer el objeto User del Intent
            User user = (User) intent.getSerializableExtra("user");

            // Usar el objeto User en esta actividad
            if (user != null) {
                nombreChefamp.setText(user.getNombre());
                tipoChefamp.setText(user.getComida());
                provChefamp.setText(user.getUbicacion());
                descChefamp.setText(user.getDescripcion());
                telefonoChefamp.setText(user.getTelefono());

                telefonoChefamp.setVisibility(View.INVISIBLE);
            }
        }
    }
    public void reservar(View view){
        //va a la siguiente pantalla donde vemos calendarios y crearemos la reserva
        Utils.gotoActivity(Activity_chef_ampliado.this, Activity_reservar.class);

    }
    public void contactar(View view){
        //muestra el telefono del chef
        //recuperar telefono del chef------------FALTA
        telefonoChefamp.setVisibility(View.VISIBLE);
    }
    public void logout(View view){
        Utils.gotoActivity(Activity_chef_ampliado.this, MainActivity_inicio.class);
    }
}
