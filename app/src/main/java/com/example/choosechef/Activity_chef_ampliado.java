package com.example.choosechef;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Activity_chef_ampliado extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_ampliado);
    }
    public void reservar(View view){
        //mirare la posibilidad de añadir el calendario en el hueco que queda en esta pantalla, cuando se clique al botón aparecerá el calendario.
        //buscaré info bien, si es muy dificil lo mandaré a otra pantalla donde aparecerá el calendario.

    }
    public void contactar(View view){
        //irá a otra pantalla donde aparecera el chat o el telefono/mail..
        //Utils.gotoActivity(Activity_chef_ampliado.this, Activity_chat.class);

    }
}
