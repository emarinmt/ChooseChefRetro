package com.example.choosechef;

import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Activity_reserva_ampliado  extends AppCompatActivity {
    TextView nombre_chef, fecha_reserva, comentario;
    RatingBar valoracion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva_ampliado);

        nombre_chef = findViewById(R.id.nombre_chef_reserva);
        fecha_reserva = findViewById(R.id.fecha_reserva);
        comentario = findViewById(R.id.comentario);
        valoracion = findViewById(R.id.rating_valoracion_reserva);


    }


    public void confirmarComentario(View view){
        //llamar al método para guardar valoración y comentario en tabla reseñas

    }

    public void logout(View view){
        Utils.gotoActivity(Activity_reserva_ampliado.this, MainActivity_inicio.class);
    }
}
