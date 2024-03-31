package com.example.choosechef;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Clase desarrollada por EVA
 * Actividad principal de inicio de la aplicación
 * Contiene dos botones los cuales llevan a la actividad de inicio de sesion o registro de usuario
 */

public class MainActivity_inicio extends AppCompatActivity {
    /**
     * método onCreate para la configuración incial de la actividad
     * @param savedInstanceState estado de la instancia guardada, un objeto Bundle que contiene el estado previamente guardado de la actividad
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_inicio);

        FastClient.initialize(getApplicationContext());

        // Configuración del botón de entrada
        ImageButton btnEntrar = findViewById(R.id.ibtn_entrar);
        btnEntrar.setOnClickListener(view -> {
            // Al hacer clic en el botón de entrada, se inicia la actividad de inicio de sesión
            Utils.gotoActivity(MainActivity_inicio.this, Activity_login.class);
        });

        // Configuración del botón de registro
        ImageButton btnRegistro = findViewById(R.id.ibtn_registro);
        btnRegistro.setOnClickListener(view -> {
            // Al hacer clic en el botón de registro, se inicia la actividad de registro
            Utils.gotoActivity(MainActivity_inicio.this, Activity_registro.class);
        });
    }
}