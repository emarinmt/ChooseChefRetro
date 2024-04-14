package com.example.choosechef;

import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Clase reserva
 * Muestra un calendario donde el usuario escoge una fecha para reservar
 * Llama al servidor para crear la reserva, el servidor comprueba si ese dia esta disponible y si es así crea la reserva
 */
public class Activity_reservar extends AppCompatActivity {
    //Variable para el calendario
    CalendarView calendario;
    //private final String TAG = Activity_reservar.class.getSimpleName();

    /**
     * Método onCreate para la configuración incial de la actividad
     * @param savedInstanceState estado de la instancia guardada, un objeto Bundle que contiene el estado previamente guardado de la actividad
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establece el diseño de la actividad.
        setContentView(R.layout.activity_reservar);
        //Inicializa variable
        calendario = findViewById(R.id.calendarView);

        calendario.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String fecha = dayOfMonth + "/" + (month+1) + "/" + year;
            Utils.showToast(Activity_reservar.this, fecha);
        });
    }

    /**
     * Método para gestionar la creación de la reserva
     * @param view La vista (Button) a la que se hizo clic.
     */
    public void reservar (View view) {
        //llamar al método para crear reserva en base de datos
        //pasaremos el usuario que esta logado, el usuario del chef y la fecha seleccionada
        //volvemos a pantalla anterior
        Utils.gotoActivity(Activity_reservar.this, Activity_chef_ampliado.class);

    }
    /**
     * Método para hacer logout
     * Redirige al usuario a la pantalla de inicio
     * @param view La vista (Button) a la que se hizo clic.
     */
    public void logout(View view){
        Utils.gotoActivity(Activity_reservar.this, MainActivity_inicio.class);
    }
}
