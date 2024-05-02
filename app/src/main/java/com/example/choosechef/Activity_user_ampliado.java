package com.example.choosechef;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Clase usuario ampliado
 * Actividad ara gestionar la información del usuario ampliada,
 * a esta pantalla acedera el administrador para ver, modificar o borrar toda la info de los usuarios
 */
public class Activity_user_ampliado extends AppCompatActivity {
    private boolean contentSuccessful = false; // Variable para rastrear el estado de la muestra del usuario
    private boolean modifySuccessful = false; // Variable para rastrear el estado de la modificación
    private boolean deleteSuccessful = false; // Variable para rastrear el estado del borrado
    private final String TAG = Activity_user_ampliado.class.getSimpleName();
    //Variables para mostrar la información de los usuarios
    private EditText usuario;
    private EditText nombreUsuario;
    private EditText passwordUsuario;
    private EditText descripUsuario;
    private EditText provinciaUsuario;
    private EditText mailUsuario;
    private EditText telefonoUsuario;
    private EditText tipoUsuario;
    private EditText tipoComidaUsuario;
    private EditText tipoServicioUsuario;
    private EditText valoracionUsuario;
    //Variable para recibir información de la pantalla anterior
    public Intent intent;
    // Variables para conectar con la API
    FastMethods mfastMethods;
    Retrofit retro;
    private User user;
    //Variables para llamadas a métodos
    String usuario_mod;
    private boolean camposHabilitados;

    /**
     * Método onCreate para la configuración incial de la actividad
     * @param savedInstanceState estado de la instancia guardada, un objeto Bundle que contiene el estado previamente guardado de la actividad
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Establece el diseño de la actividad
        setContentView(R.layout.activity_user_ampliado);
        contentSuccessful = false;

        // Inicialización de variables
        usuario = findViewById(R.id.edt_usuario);
        nombreUsuario = findViewById(R.id.edt_nombre_usuario);
        passwordUsuario = findViewById(R.id.edt_contrasena_usuario);
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
        intent = getIntent();
        //recoge el usuario de la actividad anterior y muestra su información
        obtenerIntent(intent);
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
            // Extraer el objeto User del Intent
            User user = (User) intent.getSerializableExtra("user");

            // Usar el objeto User en esta actividad
            if (user != null) {
                contentSuccessful = true;
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
                float valoracion = user.getValoracion();
                valoracionUsuario.setText(String.valueOf(valoracion));
                //guardar el nombre de usuario para la llamada al método de modicicación
                usuario_mod = user.getUsuario();
            }
        } else {
            // Si no se recibió información del usuario, la carga de contenido falló
            contentSuccessful = false;
        }
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
     * @param view La vista (Button) a la que se hizo clic.
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
            user.setValoracion(Float.parseFloat(valoracionInput));

            //llamar al método que ejecuta la llamada al servidor enviando los datos
            modificarDatos(user);
        }
    }

    /**
     * Método para modificar los datos del usuario
     * Llama al método para modificar los datos en el servidor
     * @param user usuario a modificar
     */
    public void modificarDatos(User user){
        Context context = this; // Obtener el contexto de la actividad (this)
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
                    Utils.showToastSecond(Activity_user_ampliado.this,context, "Modificación correcta!");
                    Utils.gotoActivity(Activity_user_ampliado.this, Activity_admin.class);
                } else {
                    Utils.showToastSecond(Activity_user_ampliado.this,context, "Error al modificar usuario");
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
                Utils.showToastSecond(Activity_user_ampliado.this, context,"Error en la llamada: " + t.getMessage());
            }
        });
    }

    /**
     * Método para borrar al usuario
     * @param view La vista (Button) a la que se hizo clic.
     */
    public void borrarUsuario(View view){
        Context context = this; // Obtener el contexto de la actividad (this)
        if (usuario_mod != null && !usuario_mod.isEmpty()) {
            // call HTTP client para borrar al usuario
            Call<String> call = mfastMethods.borrar_usuario(usuario_mod);
            call.enqueue(new Callback<String>() { // Ejecutar la llamada de manera asíncrona
                /**
                 * Método invocado cuando se recibe una respuesta de la solicitud HTTP
                 * @param call     llamada que generó la respuesta
                 * @param response la respuesta recibida del servidor
                 */
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        deleteSuccessful = true;
                        Log.d(TAG, "Eliminación exitosa");
                        // String responseBody = response.body();
                        Utils.showToastSecond(Activity_user_ampliado.this, context,"Eliminación correcta!");
                        Utils.gotoActivity(Activity_user_ampliado.this, Activity_admin.class);
                    } else {
                        Log.d(TAG, "Eliminación no exitosa");
                        Utils.showToastSecond(Activity_user_ampliado.this, context,"Error al eliminar usuario");
                    }
                }
                /**
                 * Método invocado cuando ocurre un error durante la ejecución de la llamada HTTP
                 * @param call la llamada que generó el error
                 * @param t    la excepción que ocurrió
                 */
                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    // Error en la llamada, muestra el mensaje de error y registra la excepción
                    t.printStackTrace();
                    Log.e(TAG, "Error en la llamada:" + t.getMessage());
                    Utils.showToastSecond(Activity_user_ampliado.this, context,"Error en la llamada: " + t.getMessage());
                }
            });
        } else {
            Utils.showToastSecond(Activity_user_ampliado.this, context,"Error el usuario esta vacío" + usuario_mod);
        }

    }

    /**
     * Método para hacer logout
     * Redirige al usuario a la pantalla de inicio
     * @param view La vista (Button) a la que se hizo clic.
     */
    public void logout(View view){
        Utils.gotoActivity(Activity_user_ampliado.this, MainActivity_inicio.class);
    }

    /**
     * Método para test
     * @return devuelve un booleano en funcion de si ha ido bien la muestra de contenido
     */
    public boolean isContentSuccessful() {
        return contentSuccessful;
    }
    /**
     * Método para test
     * @return devuelve un booleano en funcion de si ha ido bien la eliminación de usuario
     */
    public boolean isDeleteSuccessful() {
        return deleteSuccessful;
    }
    /**
     * Método para test
     * @return devuelve un booleano en funcion de si ha ido bien la modificación de usuario
     */
    public boolean isModifySuccessful() {
        return modifySuccessful;
    }

    /**
     * Método par test
     * @return devuelve el valor de la descripción de un usuario
     */
    public String getDescUsuario() {
        return descripUsuario.getText().toString(); // Obtener el texto del EditText Descripcion
    }


}
