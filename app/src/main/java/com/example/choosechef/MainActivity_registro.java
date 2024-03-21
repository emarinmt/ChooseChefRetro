package com.example.choosechef;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Clase desarrollada por EVA
 * Para hacer el registro del usuario, todavía no está implementada, solo esta hecha la parte gràfica
 * A ella se llega a través del boton "Registro" de la actividad principal inicio
 */
// Representa la actividad de registro de la aplicación.
public class MainActivity_registro extends AppCompatActivity {
    /**
     * método onCreate para la configuración incial de la actividad
     * @param savedInstanceState estado de la instancia guardada, un objeto Bundle que contiene el estado previamente guardado de la actividad
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establece el diseño de la actividad.
        setContentView(R.layout.activity_main_registro);
    }
}