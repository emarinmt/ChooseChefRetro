package com.example.choosechef;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity_inicio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_inicio);

        ImageButton btnEntrar = findViewById(R.id.ibtn_entrar);
        btnEntrar.setOnClickListener(view -> {

            Intent intent = new Intent(MainActivity_inicio.this, MainActivity_login.class);
            startActivity(intent);
        });

        ImageButton btnRegistro = findViewById(R.id.ibtn_registro);
        btnRegistro.setOnClickListener(view -> {

            Intent intent = new Intent(MainActivity_inicio.this, MainActivity_registro.class);
            startActivity(intent);
        });
    }
}