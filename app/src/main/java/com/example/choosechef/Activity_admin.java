package com.example.choosechef;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Activity_admin extends AppCompatActivity {
    /*
    pantalla opciones administrador. tengo que mirar si poner un recycler view y allí recuperamos todos los usuarios, similar al contenido pero más simple
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establece el diseño de la actividad.
        setContentView(R.layout.activity_admin);
    }
}

