package com.example.choosechef;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Activity_admin_menu extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establece el diseño de la actividad.
        setContentView(R.layout.activity_admin_menu);

    }
    /**
     * Método para ir a la pantalla de gestión de usuarios
     * @param view La vista (Button) a la que se hizo clic.
     */
    public void gestion_usuarios(View view) {
        Utils.gotoActivity(Activity_admin_menu.this, Activity_admin_lista_usuarios.class);
    }

    /**
     * Método para ir a la pantalla de gestión de reseñas
     * @param view La vista (Button) a la que se hizo clic.
     */
    public void gestion_reseñas(View view) {
        Utils.gotoActivity(Activity_admin_menu.this, Activity_admin_lista_reservas.class);
    }
    /**
     * Método para hacer logout
     * Redirige al usuario a la pantalla de inicio
     * @param view La vista (Button) a la que se hizo clic.
     */
    public void logout(View view){
        Utils.gotoActivity(Activity_admin_menu.this, MainActivity_inicio.class);
    }
}
