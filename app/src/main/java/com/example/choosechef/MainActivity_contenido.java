package com.example.choosechef;


import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
/**
 * Clase desarrollada por ELENA (falta desarrollar la parte gràfica)
 * para gestionar la actividad contenido
 */

public class MainActivity_contenido extends AppCompatActivity {
    //Añadido por EVA para recibir el usuario en esta actividad y enviarlo a la siguiente( modificaicón perfil)
    String usuario;
    String pass;

    /**
     * método onCreate para la configuración incial de la actividad
     * @param savedInstanceState estado de la instancia guardada, un objeto Bundle que contiene el estado previamente guardado de la actividad
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establece el diseño de la actividad.
        setContentView(R.layout.activity_content);
        //Añadido por EVA para recibir el usuario y contraseña de la actividad anterior (login)
        usuario = getIntent().getStringExtra("usuario");
        pass = getIntent().getStringExtra("pass");
    }

    /**
     * Método para manejar el clic del botón de modificación de perfil.
     * Dirige al usuario a la pantalla de modificacoón
     * @param view La vista (Button) que se hizo clic.
     */
    public void changeProfile (View view) {
        // Redirige al usuario a la pantalla de modificación de perfil
        //Utils.gotoActivity(MainActivity_contenido.this, MainActivity_mod_perfil.class);
        //Cambiado por EVA para poder recibir el usuario y la contraseña de la actividad anterior(login) y enviarlo a la siguiente actividad (modificación perfil)
        Utils.gotoActivityMessage(MainActivity_contenido.this, MainActivity_mod_perfil.class, "usuario",usuario , "pass", pass, false);


    }
}

