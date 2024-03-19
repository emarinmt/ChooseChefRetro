package com.example.choosechef;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

// Representa la actividad principal.
public class MainActivity_inicio extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_inicio);

        // Configuración del botón de entrada
        ImageButton btnEntrar = findViewById(R.id.ibtn_entrar);
        btnEntrar.setOnClickListener(view -> {
            // Al hacer clic en el botón de entrada, se inicia la actividad de inicio de sesión
            Utils.gotoActivity(MainActivity_inicio.this, MainActivity_login.class);
        });

        // Configuración del botón de registro
        ImageButton btnRegistro = findViewById(R.id.ibtn_registro);
        btnRegistro.setOnClickListener(view -> {
            // Al hacer clic en el botón de registro, se inicia la actividad de registro
            Utils.gotoActivity(MainActivity_inicio.this, MainActivity_registro.class);
        });
    }
}