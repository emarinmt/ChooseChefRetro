package com.example.choosechef;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Clase desarrollada por ELENA
 * para mostrar toda la información de un chef
 */
public class Activity_chef_ampliado extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_ampliado);

        // Obtener el Intent que inició esta actividad
        Intent intent = getIntent();

        // Verificar si el Intent contiene un extra con clave "user"
        if (intent != null && intent.hasExtra("user")) {
            // Extraer el objeto User del Intent
            User user = (User) intent.getSerializableExtra("user");

            // Usar el objeto User en esta actividad
            if (user != null) {
                // Por ejemplo, mostrar el nombre del chef en un TextView
                //TextView textViewNombre = findViewById(R.id.textViewNombre);
                //textViewNombre.setText(user.getNombre());
            }
        }
    }
}
