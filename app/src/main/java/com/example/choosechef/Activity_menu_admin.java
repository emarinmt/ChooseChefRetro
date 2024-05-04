package com.example.choosechef;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Clase para gestionar el menú del administrador
 * Ofrece dos posibilidades, gestionar usuarios o reservas
 */
public class Activity_menu_admin extends AppCompatActivity {

    /**
     * Método onCreate para la configuración incial de la actividad
     * @param savedInstanceState estado de la instancia guardada, un objeto Bundle que contiene el estado previamente guardado de la actividad
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establece el diseño de la actividad.
        setContentView(R.layout.activity_menu_admin);
    }

    /**
     * Método para ir a la pantalla de gestión de usuarios
     * @param view La vista (Button) a la que se hizo clic.
     */
    public void gestion_usuarios(View view) {
        Utils.gotoActivity(Activity_menu_admin.this, Activity_admin.class);
    }

    /**
     * Método para ir a la pantalla de gestión de reseñas
     * @param view La vista (Button) a la que se hizo clic.
     */
    public void gestion_reseñas(View view) {
        Utils.gotoActivity(Activity_menu_admin.this, Activity_reservas_admin.class);
    }

    /**
     * Método para hacer logout
     * Redirige al usuario a la pantalla de inicio
     * @param view La vista (Button) a la que se hizo clic.
     */
    public void logout(View view){
        Utils.gotoActivity(Activity_menu_admin.this, MainActivity_inicio.class);
    }

    /**
     * Método para retroceder de pantalla
     * Redirige al usuario a la pantalla anterior
     * @param view La vista (Button) a la que se hizo clic.
     */
    public void atras(View view){
        Utils.gotoActivity(Activity_menu_admin.this, Activity_contenido.class);
    }
}
