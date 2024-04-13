package com.example.choosechef;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Actividad ara gestionar la información del usuario ampliada,
 * a esta pantalla acedera el administrador para ver, modificar o borrar toda la info de los usuarios
 */
public class Activity_user_ampliado extends AppCompatActivity {
    private boolean contentSuccessful = false; // Variable para rastrear el estado de la muestra del usuario
    private TextView usuario;
    private TextView nombreUsuario;
    private TextView passwordUsuario;
    private TextView descripUsuario;
    private TextView provinciaUsuario;
    private TextView mailUsuario;
    private TextView telefonoUsuario;
    private TextView tipoUsuario;
    private TextView tipoComidaUsuario;
    private TextView tipoServicioUsuario;
    private TextView valoracionUsuario;
    public Intent intent;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_ampliado);
        contentSuccessful = false;
        // Inicialización de variables
        usuario = findViewById(R.id.usuario);
        nombreUsuario = findViewById(R.id.nombre_usuario);
        passwordUsuario = findViewById(R.id.contraseña_usuario);
        descripUsuario = findViewById(R.id.descripcion_usuario);
        provinciaUsuario = findViewById(R.id.provincia_usuario);
        mailUsuario = findViewById(R.id.mail_usuario);
        telefonoUsuario = findViewById(R.id.telefono_usuario);
        tipoUsuario = findViewById(R.id.tipo_usuario);
        tipoComidaUsuario = findViewById(R.id.comida_usuario);
        tipoServicioUsuario = findViewById(R.id.servicio_usuario);
        valoracionUsuario = findViewById(R.id.valoracion_usuario);

        // Obtener el Intent que inició esta actividad
        intent = getIntent();
        obtenerIntent(intent);

    }
    //llamar al método para recuperar info usuario y mostrarla. PREGUNTA. EL MÉTODO QUE TENEMOS MUESTRA POR TOKEN,NECESITAMOS OTRO?
    public void editarUsuario(View view){
        //Activar los campos edit text para poder editarlos
        //llamar al método para editar el usuario

    }
    public void borrarUsuario(View view){
        //llamar método borrar usuario

    }
    public void logout(View view){
        Utils.gotoActivity(Activity_user_ampliado.this, MainActivity_inicio.class);
    }
    public void obtenerIntent(Intent intent){
        // Verificar si el Intent contiene un extra con clave "user"
        if (intent != null && intent.hasExtra("user")) {
            contentSuccessful = true;
            // Extraer el objeto User del Intent
            User user = (User) intent.getSerializableExtra("user");

            // Usar el objeto User en esta actividad
            if (user != null) {
                usuario.setText(user.getUsuario());
                nombreUsuario.setText(user.getNombre());
                passwordUsuario.setText(user.getPassword());
                descripUsuario.setText(user.getDescripcion());
                provinciaUsuario.setText(user.getUbicacion());
                mailUsuario.setText(user.getEmail());
                telefonoUsuario.setText(user.getTelefono());
                tipoUsuario.setText(user.getTipo());
                tipoComidaUsuario.setText(user.getComida());
                tipoServicioUsuario.setText(user.getServicio());
                valoracionUsuario.setText((int) user.getValoracion());
            }
        }
        contentSuccessful = false;
    }

    public boolean isContentSuccessful() {
        return contentSuccessful;
    }
}
