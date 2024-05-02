package com.example.choosechef;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Clase para gestionar las opciones de menu del chef
 * Ofrece dos posibilidades, gestionar su perfil o ver sus reservas
 */
public class Activity_menu_chef extends AppCompatActivity {

    /**
     * Método onCreate para la configuración incial de la actividad
     * @param savedInstanceState estado de la instancia guardada, un objeto Bundle que contiene el estado previamente guardado de la actividad
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establece el diseño de la actividad.
        setContentView(R.layout.activity_menu_chef);
    }

    /**
     * Método para ir a la pantalla de gestión del perfil del chef
     * @param view La vista (Button) a la que se hizo clic.
     */
    public void gestion_perfil_chef(View view) {
        Utils.gotoActivity(Activity_menu_chef.this, Activity_chef.class);
    }

    /**
     * Método para ir a la pantalla de gestión de reservas
     * @param view La vista (Button) a la que se hizo clic.
     */
    public void gestion_reservas(View view) {
        Utils.gotoActivity(Activity_menu_chef.this, Activity_reservas_chef.class);
    }
    /**
     * Método para hacer logout
     * Redirige al usuario a la pantalla de inicio
     * @param view La vista (Button) a la que se hizo clic.
     */
    public void logout(View view){
        Utils.gotoActivity(Activity_menu_chef.this, MainActivity_inicio.class);
    }
}
