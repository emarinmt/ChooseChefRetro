package com.example.choosechef;

import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;

import androidx.appcompat.app.AppCompatActivity;

public class Activity_reservar extends AppCompatActivity {
    CalendarView calendario;
    //private final String TAG = Activity_reservar.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establece el diseño de la actividad.
        setContentView(R.layout.activity_reservar);

        calendario = findViewById(R.id.calendarView);

        calendario.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String fecha = dayOfMonth + "/" + month + "/" + year;
            Utils.showToast(Activity_reservar.this, fecha);
        });
    }
    public void reservar (View view) {
        //llamar al método para crear reserva en base de datos
        //pasaremos el usuario que esta logado, el usuario del chef y la fecha seleccionada
        //volvemos a pantalla anterior
        Utils.gotoActivity(Activity_reservar.this, Activity_chef_ampliado.class);

    }
}
