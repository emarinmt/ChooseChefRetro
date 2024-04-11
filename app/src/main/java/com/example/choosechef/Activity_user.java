package com.example.choosechef;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Actividad desarrollada por
 * para gestionar la pantalla de las opciones del usuario
 */

public class Activity_user extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establece el diseño de la actividad.
        setContentView(R.layout.activity_user);
    }

    public void logout(View view){
        Utils.gotoActivity(Activity_user.this, MainActivity_inicio.class);
    }
}
