package com.example.choosechef;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Clase desarrollada por EVA
 * para gestionar la actividad modificar perfil de usuario
 * Modificada por ELENA para utilizar el token y para usar la clase User como obtención de datos y para modificar y enviar al servidor
 */

public class Activity_mod_perfil extends AppCompatActivity {
    private final String TAG = Activity_mod_perfil.class.getSimpleName();
    // Variables para los campos de entrada
    private EditText mNameInput;
    private EditText mAdressInput;
    private EditText mPhoneInput;
    private EditText mPassInput;
    private EditText mNewPassInput;
    private EditText mNewPassConfInput;
    //private Switch mChangePass; (todavia no implementado)

    // Variables para conecatr con la API
    FastMethods mfastMethods;
    Retrofit retro;
    private User user; // ELENA
    //ProfileResponse profileResponse;
    String token;
    /*
    //variables para recibir usuario y contraseña de otra clase
    String usuario;
    String pass;
    */
    // Borrado por ELENA para usar el token

    /**
     * Método onCreate para la configuración incial de la actividad
     * @param savedInstanceState estado de la instancia guardada, un objeto Bundle que contiene el estado previamente guardado de la actividad
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mod_perfil); // Establece el diseño de la actividad.

        //Inicialización de variables
        mNameInput = findViewById(R.id.edt_nombre_mod_perfil);
        mAdressInput = findViewById(R.id.edt_direccion_mod_perfil);
        mPhoneInput = findViewById(R.id.edt_telefono_mod_perfil);
        mPassInput = findViewById(R.id.edt_contraseña_anterior_mod_perfil);
        mNewPassInput = findViewById(R.id.edt_contraseña_nueva_mod_perfil);
        mNewPassConfInput = findViewById(R.id.edt_contraseña2_nueva_mod_perfil);
        //mChangePass = findViewById(R.id.switch_cambio_contraseña); ( todavia no implementado)

        retro=FastClient.getClient();
        mfastMethods = retro.create(FastMethods.class);
        //profileResponse = new ProfileResponse();
        user = new User();  // ELENA

        // Obtener el token de SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MiPreferencia", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        /*
        //!!!!BORRAR
        //Recibir usuario y contraseña de la actividad anterior ( contenido)
        usuario = getIntent().getStringExtra("usuario");
        pass = getIntent().getStringExtra("pass");

        //Guardar usuario y contraseña del usuario en clase ProfileResponse para su futura utilización
        ProfileResponse.setUser(usuario);
        ProfileResponse.setPassword(pass);
        */

        //llamar al método recuperarDatos
        recuperarDatos();

    }

    /**
     * Método recuperarDatos
     * Hace la primera consulta al servidor, para recuperar los datos del usuario y mostrarlos en pantalla
     */
    public void recuperarDatos(){
        //Compruebe el estado de la conexión de red.
        if (!Utils.isNetworkAvailable(this)) {
            Utils.showToast(Activity_mod_perfil.this, "No hay conexión a Internet");
            return;
        }
        //MODIDFICAR POR MÉTODO MOSTRAR POR TOKEN CUANDO ESTÉ, QUITAR USUARIO
        //MODIFICAR PARA OBTENER USER
        //call HTTP client para recuperar la información del usuario
        Call<List<String>> call = mfastMethods.recuperar_info("Bearer " + token);
        //ProfileResponse.getUser() Borrado por ELENA para usar el token
        call.enqueue(new Callback<List<String>>() { //Ejecutar la llamada de manera asíncrona
            /**
             *Método invocado cuando se recibe una respuesta de la solicitud HTTP
             * @param call llamada que generó la respuesta
             * @param response la respuesta recibida del servidor
             */
            public void onResponse(@NonNull Call<List<String>> call, @NonNull Response<List<String>> response) {
                if (response.isSuccessful()) {
                    //Recibe los datos del usuario en una lista
                    List obtenerDatos = response.body();
                    if (obtenerDatos != null) {
                        //Mostrar datos en pantalla
                        mNameInput.setText(obtenerDatos.get(0).toString());
                        mAdressInput.setText(obtenerDatos.get(2).toString());
                        mPhoneInput.setText(obtenerDatos.get(1).toString());
                        /* Si usamos el Profile Response
                         *  mNameInput.setText(usuario.getNombre());
                         * mAdressInput.setText(usuario.getUbicacion());
                         * mPhoneInput.setText(usuario.getTelefono());
                         */
                    } else {
                        // Obtención de datos incorrecta, muestra un mensaje de error
                        Utils.showToast(Activity_mod_perfil.this, "Obtención de datos incorrecta");
                    }
                }
            }
            /**
             *Método invocado cuando ocurre un error durante la ejecución de la llamada HTTP
             * @param call la llamada que generó el error
             * @param t la excepción que ocurrió
             */
            @Override
            public void onFailure(@NonNull Call<List<String>> call, @NonNull Throwable t) {
                // Error en la llamada, muestra el mensaje de error y registra la excepción
                t.printStackTrace();
                Log.e(TAG, "Error en la llamada:" + t.getMessage());
                Utils.showToast(Activity_mod_perfil.this, "Error en la llamada: " + t.getMessage());
            }
        });
    }


    /**
     * Método para manejar el clic del botón de confirmar cambios de modificación del perfil.
     * Guarda los cambios y dirige al usuario a la pantalla de contenido si es exitoso,
     * muestra un mensaje de error si falla.
     * @param view La vista (Button) que se hizo clic.
     */
    public void confirmaCambios(View view) {
        // Oculta el teclado cuando el usuario toca el botón
        Utils.hideKeyboard(this, view);

        //Recupera los datos introducidos por el usuario
        String queryNameString = mNameInput.getText().toString();
        String queryAdressString = mAdressInput.getText().toString();
        String queryPhoneString = mPhoneInput.getText().toString();
        String queryPassString = mPassInput.getText().toString();
        String queryNewPassString = mNewPassInput.getText().toString();
        String queryNewPassConfString = mNewPassConfInput.getText().toString();

        // MODIFICADO POR ELENA
        // Actualizamos los datos del usuario con los nuevos valores
        user.setNombre(queryNameString);
        user.setUbicacion(queryAdressString);
        user.setTelefono(queryPhoneString);

        /*
        //construir el objeto ModificarUsuarioRequest con los datos ingresados
        ModificarUsuarioRequest request = new ModificarUsuarioRequest();
        request.setNombre(queryNameString);
        request.setUbicacion(queryAdressString);
        request.setTelefono(queryPhoneString);

        //el id, lo obtenemos de la respuesta, ya que no se puede modificar
        //request.setId(request.getId());

        //estos campos no se piden al usuario y no se pueden modificar todavía, pero el método los necesita aunque esten vacíos
        request.setDescripcion(" ");
        request.setEmail(" ");
        request.setTipo(" ");
        */
        //Comprobación de contraseña para poder modificarla
        if(queryPassString.equals(user.getPassword()) ){
            if (queryNewPassString.equals(queryNewPassConfString)){
                user.setPassword(queryNewPassString);
            }else{
                user.setPassword(user.getPassword());
                Utils.showToast(Activity_mod_perfil.this,"La contraseña nueva no coincide");
            }
        }else{
            user.setPassword(user.getPassword());
            Utils.showToast(Activity_mod_perfil.this,"La contraseña anterior no es correcta");
        }

        //Compruebe el estado de la conexión de red.
        if (!Utils.isNetworkAvailable(this)) {
            Utils.showToast(Activity_mod_perfil.this, "No hay conexión a Internet");
            return;
        }

        // Si hay conexión hace la llamada para modificar los datos

        // call HTTP client para modificar los datos de usuario
        Call<String> call = mfastMethods.modificarUsuario("Bearer " + token, user);
        call.enqueue(new Callback<String>() { // Ejecutar la llamada de manera asíncrona
            /**
             *Método invocado cuando se recibe una respuesta de la solicitud HTTP
             * @param call llamada que generó la respuesta
             * @param response la respuesta recibida del servidor
             */
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    //String responseBody = response.body();
                    Utils.showToast(Activity_mod_perfil.this, "Modificación correcta!");
                    Utils.gotoActivity(Activity_mod_perfil.this, Activity_contenido.class);
                } else {
                    Utils.showToast(Activity_mod_perfil.this, "Error al modificar usuario");
                }
            }

            /**
             *Método invocado cuando ocurre un error durante la ejecución de la llamada HTTP
             * @param call la llamada que generó el error
             * @param t la excepción que ocurrió
             */
            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                // Error en la llamada, muestra el mensaje de error y registra la excepción
                t.printStackTrace();
                Log.e(TAG, "Error en la llamada:" + t.getMessage());
                Utils.showToast(Activity_mod_perfil.this, "Error en la llamada: " + t.getMessage());
            }
        });
    }
    //Preparado para futura utilización, de momento no se comprueba que venga texto porque el método acepta el valor vacío
    // Comprueba si el campo dirección tiene texto
    /*if ((networkInfo != null) && (queryAdressString.length() != 0)) {
    }
    // Comprueba si el campo teléfono tiene texto
    if ((networkInfo != null) && (queryPhoneString.length() != 0)) {
    }
    // Comprueba si el check está activo
   // if ((networkInfo != null) && (mChangePass.isChecked())) {
    //}*/

}