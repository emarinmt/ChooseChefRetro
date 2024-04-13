package com.example.choosechef;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Actividad ara gestionar la información del usuario ampliada,
 * a esta pantalla acedera el administrador para ver, modificar o borrar toda la info de los usuarios
 */
public class Activity_user_ampliado extends AppCompatActivity {
    //FUNCIONA TODO MENOS VER Y MODIFICAR LA VALORACION DEL USUARIO. YA LO REVISARÉ
    //REVISAR. EN LA MUESTRA USUARIOS. MUESTRA VACIOS ?
    private boolean contentSuccessful = false; // Variable para rastrear el estado de la muestra del usuario
    private boolean modifySuccessful = false; // Variable para rastrear el estado de la modificación
    private boolean deleteSuccessful = false; // Variable para rastrear el estado del borrado
    private final String TAG = Activity_user_ampliado.class.getSimpleName();
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
    public Intent intent2;
    FastMethods mfastMethods;
    Retrofit retro;
    private User user;
    String usuario_mod;
    private boolean camposHabilitados;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_ampliado);
        contentSuccessful = false;

        // Inicialización de variables
        usuario = findViewById(R.id.edt_usuario);
        nombreUsuario = findViewById(R.id.edt_nombre_usuario);
        passwordUsuario = findViewById(R.id.edt_contraseña_usuario);
        descripUsuario = findViewById(R.id.edt_descripcion_usuario);
        provinciaUsuario = findViewById(R.id.edt_provincia_usuario);
        mailUsuario = findViewById(R.id.edt_mail_usuario);
        telefonoUsuario = findViewById(R.id.edt_telefono_usuario);
        tipoUsuario = findViewById(R.id.edt_tipo_usuario);
        tipoComidaUsuario = findViewById(R.id.edt_comida_usuario);
        tipoServicioUsuario = findViewById(R.id.edt_servicio_usuario);
        valoracionUsuario = findViewById(R.id.edt_valoracion_usuario);

        retro=FastClient.getClient();
        mfastMethods = retro.create(FastMethods.class);
        user = new User();

        // Obtener el Intent que inició esta actividad
        intent2 = getIntent();
        //recoge el usuario de la actividad anterior y muestra su información
        obtenerIntent(intent2);
        //booleano para el boton editar
        camposHabilitados = false;

        habilitarCampos(false);

    }

    /**
     * Método para recibir el objeto usuario de la clase anterior
     * Y mostrar esos datos por pantalla
     * @param intent el intento que inicia esta actividad
     */
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
                // valoracionUsuario.setText(user.getValoracion());

                //guardar el nombre de usuario para la llamada al método de modicicación
                usuario_mod = user.getUsuario();
            }
        }
        contentSuccessful = false;
    }

    /**
     * Método para habilitar o desahabilitar los campos
     * @param activar booleano true, habilita campos, false, los deshabilita
     */
    public void habilitarCampos(Boolean activar){
        usuario.setEnabled(activar);
        nombreUsuario.setEnabled(activar);
        passwordUsuario.setEnabled(activar);
        descripUsuario.setEnabled(activar);
        provinciaUsuario.setEnabled(activar);
        mailUsuario.setEnabled(activar);
        telefonoUsuario.setEnabled(activar);
        tipoUsuario.setEnabled(activar);
        tipoComidaUsuario.setEnabled(activar);
        tipoServicioUsuario.setEnabled(activar);
        valoracionUsuario.setEnabled(activar);
    }

    /**
     * Método para gestionar el click del botón confirmar
     * Habilita los campos para poder editarlos, recoge los datos modificados
     * Y llama al método para modificar esos datos en el servidor
     * @param view visor para detectar el click del boton
     */
    public void editarUsuario(View view){
        if(!camposHabilitados){
            //Activar los campos edit text para poder editarlos
            habilitarCampos(true);
            camposHabilitados = true;
        }else {
            //Recupera los datos introducidor por el usuario
            String usuarioInput = usuario.getText().toString();
            String nombreInput = nombreUsuario.getText().toString();
            String passInput = passwordUsuario.getText().toString();
            String descripInput = descripUsuario.getText().toString();
            String provinciaInput = provinciaUsuario.getText().toString();
            String mailInput = mailUsuario.getText().toString();
            String telefonoInput = telefonoUsuario.getText().toString();
            String tipoInput = tipoUsuario.getText().toString();
            String comidaInput = tipoComidaUsuario.getText().toString();
            String servicioInput = tipoServicioUsuario.getText().toString();
            String valoracionInput = valoracionUsuario.getText().toString();

            // Comprueba el estado de la conexión de red
            if (!Utils.isNetworkAvailable(this)) {
                Utils.showToast(Activity_user_ampliado.this, "No hay conexión a Internet");
                return;
            }
            //Actualizar los datos del usuario con los nuevos valores
            user.setUsuario(usuarioInput);
            user.setNombre(nombreInput);
            user.setPassword(passInput);
            user.setDescripcion(descripInput);
            user.setUbicacion(provinciaInput);
            user.setEmail(mailInput);
            user.setTelefono(telefonoInput);
            user.setTipo(tipoInput);
            user.setComida(comidaInput);
            user.setServicio(servicioInput);
            // user.setValoracion(Float.parseFloat(valoracionInput));

            //llamar al método que ejecuta la llamada al servidor enviando los datos
            modificarDatos(user);
        }
    }

    public void modificarDatos(User user){
        // call HTTP client para modificar los datos de usuario
        Call<String> call = mfastMethods.modificarUsuario_admin(usuario_mod, user);
        call.enqueue(new Callback<String>() { // Ejecutar la llamada de manera asíncrona
            /**
             * Método invocado cuando se recibe una respuesta de la solicitud HTTP
             * @param call llamada que generó la respuesta
             * @param response la respuesta recibida del servidor
             */
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    modifySuccessful = true;
                    // String responseBody = response.body();
                    Utils.showToast(Activity_user_ampliado.this, "Modificación correcta!");
                    Utils.gotoActivity(Activity_user_ampliado.this, Activity_admin.class);
                } else {
                    Utils.showToast(Activity_user_ampliado.this, "Error al modificar usuario");
                }
            }

            /**
             * Método invocado cuando ocurre un error durante la ejecución de la llamada HTTP
             * @param call la llamada que generó el error
             * @param t la excepción que ocurrió
             */
            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                // Error en la llamada, muestra el mensaje de error y registra la excepción
                t.printStackTrace();
                Log.e(TAG, "Error en la llamada:" + t.getMessage());
                Utils.showToast(Activity_user_ampliado.this, "Error en la llamada: " + t.getMessage());
            }
        });
    }

    public void borrarUsuario(View view){

        if (usuario_mod != null && !usuario_mod.isEmpty()) {

            // call HTTP client para borrar al usuario
            Call<String> call = mfastMethods.borrar_usuario(usuario_mod);
            call.enqueue(new Callback<String>() { // Ejecutar la llamada de manera asíncrona
                /**
                 * Método invocado cuando se recibe una respuesta de la solicitud HTTP
                 *
                 * @param call     llamada que generó la respuesta
                 * @param response la respuesta recibida del servidor
                 */
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        deleteSuccessful = true;
                        // String responseBody = response.body();
                        Utils.showToast(Activity_user_ampliado.this, "Eliminación correcta!");
                        Utils.gotoActivity(Activity_user_ampliado.this, Activity_admin.class);
                    } else {
                        Utils.showToast(Activity_user_ampliado.this, "Error al eliminar usuario");
                    }
                }

                /**
                 * Método invocado cuando ocurre un error durante la ejecución de la llamada HTTP
                 *
                 * @param call la llamada que generó el error
                 * @param t    la excepción que ocurrió
                 */
                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    // Error en la llamada, muestra el mensaje de error y registra la excepción
                    t.printStackTrace();
                    Log.e(TAG, "Error en la llamada:" + t.getMessage());
                    Utils.showToast(Activity_user_ampliado.this, "Error en la llamada: " + t.getMessage());
                }
            });
        } else {
            Utils.showToast(Activity_user_ampliado.this, "Error el usuario esta vacío" + usuario_mod);
        }

    }

    /**
     * Método para hacer logout
     * @param view visor del botón asociado a logout
     */
    public void logout(View view){
        Utils.gotoActivity(Activity_user_ampliado.this, MainActivity_inicio.class);
    }


    public boolean isContentSuccessful() {
        return contentSuccessful;
    }
}
