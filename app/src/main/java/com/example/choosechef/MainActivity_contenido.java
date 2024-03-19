package com.example.choosechef;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

// Representa la actividad que muestra el contenido principal (perfiles de chefs)
public class MainActivity_contenido extends AppCompatActivity {
    // Método llamado cuando se crea la actividad.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establece el diseño de la actividad.
        setContentView(R.layout.activity_content);
    }

    /**
     * Método para manejar el clic del botón de modificación de perfil.
     * Dirige al usuario a la pantalla de modificacoón
     * @param view La vista (Button) que se hizo clic.
     */
    public void changeProfile (View view) {
        // Redirige al usuario a la pantalla de modificación de perfil
        Utils.gotoActivity(MainActivity_contenido.this, MainActivity_mod_perfil.class);
    }
}

