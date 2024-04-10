package com.example.choosechef;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Actividad desarrollada por
 * Para gestionar la información del usuario ampliada, a esta pantalla acedera el administrador para ver, modificar o borrar toda la info de los usuarios
 */
public class Activity_user_ampliado extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_ampliado);


    }
    //llamar al método para recuperar info usuario y mostrarla. PREGUNTA. EL MÉTODO QUE TENEMOS MUESTRA POR TOKEN,NECESITAMOS OTRO?
    public void editarUsuario(View view){
        //Activar los campos edit text para poder editarlos
        //llamar al método para editar el usuario
        //PREGUNTA. EL MÉTODO QUE TENEMOS EDITA AL USUARIO POR TOKEN, NECESITAMOS OTRO MÉTODO?

    }
    public void borrarUsuario(View view){
        //llamar método borrar usuario

    }
}
