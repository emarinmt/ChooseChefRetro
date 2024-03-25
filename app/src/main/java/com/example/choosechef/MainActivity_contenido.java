package com.example.choosechef;


import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Clase desarrollada por ELENA (falta desarrollar la parte gràfica)
 * para gestionar la actividad contenido
 */

public class MainActivity_contenido extends AppCompatActivity {
    //Añadido por EVA para recibir el usuario en esta actividad y enviarlo a la siguiente( modificaicón perfil)
    String usuario;
    String pass;
    //Para mostrar los chefs
    RecyclerView recyclerView;
    Adapter adapter;
    ArrayList<String> items;


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

        //Aquí habrá que llamar a base de datos y recuperar lista de chefs
        items = new ArrayList<>();
        items.add("Primer chef");
        items.add("Segundo chef");
        items.add("Tercer chef");
        items.add("Cuarto chef");


        recyclerView = findViewById(R.id.rv_chefs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this,items);
        recyclerView.setAdapter(adapter);

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
    public void search(View view) {
        //cambiar cuando cree la siguiente pantalla
        // Redirige al usuario a la pantalla de
        Utils.gotoActivityMessage(MainActivity_contenido.this, MainActivity_mod_perfil.class, "usuario",usuario , "pass", pass, false);


    }
    public void gorro (View view) {
        //cambiar cuando cree la siguiente pantalla
        // Redirige al usuario a la pantalla de
        Utils.gotoActivityMessage(MainActivity_contenido.this, MainActivity_mod_perfil.class, "usuario",usuario , "pass", pass, false);


    }
}

