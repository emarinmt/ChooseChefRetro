package com.example.choosechef;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Clase para gestionar las opciones de menu del chef
 */
public class Activity_chef_menu extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establece el diseño de la actividad.
        setContentView(R.layout.activity_chef_menu);

    }
    /**
     * Método para ir a la pantalla de gestión del perfil del chef
     * @param view La vista (Button) a la que se hizo clic.
     */
    public void gestion_perfil_chef(View view) {
        Utils.gotoActivity(Activity_chef_menu.this, Activity_chef_servicio.class);
    }

    /**
     * Método para ir a la pantalla de gestión de reservas
     * @param view La vista (Button) a la que se hizo clic.
     */
    public void gestion_reservas(View view) {
        Utils.gotoActivity(Activity_chef_menu.this, Activity_chef_lista_reservas.class);
    }
    /**
     * Método para hacer logout
     * Redirige al usuario a la pantalla de inicio
     * @param view La vista (Button) a la que se hizo clic.
     */
    public void logout(View view){
        Utils.gotoActivity(Activity_chef_menu.this, MainActivity_inicio.class);
    }
}
