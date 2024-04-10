package com.example.choosechef;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Clase desarrollada por EVA
 * para gestionar la actividad modificar perfil de usuario
 * Modificada por ELENA para utilizar el token y
 * la clase User como obtención de datos para modificar y enviar al servidor
 */

public class Activity_mod_perfil extends AppCompatActivity {
// FUNCIONANDO Y REVISADA CON COMENTARIOS
    private boolean modifySuccessful = false; // Variable para rastrear el estado de la modificación
    private final String TAG = Activity_mod_perfil.class.getSimpleName();
    // Variables para los campos de entrada
    private EditText mNameInput;
    private EditText mAdressInput;
    private EditText mPhoneInput;
    private EditText mPassInput;
    private EditText mNewPassInput;
    private EditText mNewPassConfInput;
    private Switch mChangePass;

    // Variables para conectar con la API
    FastMethods mfastMethods;
    Retrofit retro;
    private User user; // ELENA
    //ProfileResponse profileResponse; // Borrado por ELENA para usar el token
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

        // Inicialización de variables
        mNameInput = findViewById(R.id.edt_nombre_mod_perfil);
        mAdressInput = findViewById(R.id.edt_direccion_mod_perfil);
        mPhoneInput = findViewById(R.id.edt_telefono_mod_perfil);
        mPassInput = findViewById(R.id.edt_contraseña_anterior_mod_perfil);
        mNewPassInput = findViewById(R.id.edt_contraseña_nueva_mod_perfil);
        mNewPassConfInput = findViewById(R.id.edt_contraseña2_nueva_mod_perfil);
        mChangePass = findViewById(R.id.switch_cambio_contraseña);

        retro=FastClient.getClient();
        mfastMethods = retro.create(FastMethods.class);
        //profileResponse = new ProfileResponse(); // Borrado por ELENA para usar User
        user = new User();  // ELENA

        // Obtener el token de SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MiPreferencia", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        // Habilitem els camps de contrasenya si el check està clicat
        mChangePass.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mPassInput.setEnabled(isChecked);
            mNewPassInput.setEnabled(isChecked);
            mNewPassConfInput.setEnabled(isChecked);
        });
        // Per defecte mostrem els camps de contrasenyes inhabilitats fins que es cliqui el switch
        mPassInput.setEnabled(false);
        mNewPassInput.setEnabled(false);
        mNewPassConfInput.setEnabled(false);

        // Llamar al método recuperarDatos
        recuperarDatos();
    }

    /**
     * Método recuperarDatos
     * Hace la primera consulta al servidor, para recuperar los datos del usuario y mostrarlos en pantalla
     */
    public void recuperarDatos(){
        // Compruebe el estado de la conexión de red
        if (!Utils.isNetworkAvailable(this)) {
            Utils.showToast(Activity_mod_perfil.this, "No hay conexión a Internet");
            return;
        }
        // Call HTTP client para recuperar la información del usuario
        Call<User> call = mfastMethods.recuperar_info(token);
        //ProfileResponse.getUser() Borrado por ELENA para usar el token
        call.enqueue(new Callback<User>() { // Ejecutar la llamada de manera asíncrona
            /**
             * Método invocado cuando se recibe una respuesta de la solicitud HTTP
             * @param call llamada que generó la respuesta
             * @param response la respuesta recibida del servidor
             */
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    user = response.body(); // Recibe los datos del usuario
                    if (user != null) {

                        // Mostrar datos en pantalla

                        mNameInput.setText(user.getNombre());
                        mAdressInput.setText(user.getUbicacion());
                        mPhoneInput.setText(user.getTelefono());

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
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
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

        // Recupera los datos introducidos por el usuario
        String queryNameString = mNameInput.getText().toString();
        String queryAdressString = mAdressInput.getText().toString();
        String queryPhoneString = mPhoneInput.getText().toString();
        String queryPassString = mPassInput.getText().toString();
        String queryNewPassString = mNewPassInput.getText().toString();
        String queryNewPassConfString = mNewPassConfInput.getText().toString();
        boolean queryOptionPass = mChangePass.isChecked();

        // Comprueba el estado de la conexión de red
        if (!Utils.isNetworkAvailable(this)) {
            Utils.showToast(Activity_mod_perfil.this, "No hay conexión a Internet");
            return;
        }

        // Condicional según la opción del switch de cambio de contraseña
        if (queryOptionPass) {
            // Comprueba si los campos de entrada son correctos
            if (!validateFields(queryPassString, queryNewPassString, queryNewPassConfString)) {
                return;
            }
            user.setPassword(queryNewPassString); // Cambio de contraseña
        }  else {
            // Se mantiene la contraseña enviada por el servidor
            //user.setPassword(user.getPassword());
        }

        //EL FALLO ESTÁ CUANDO EN LA BD EL CAMPO ES NULL
        // Actualizamos los datos del usuario con los nuevos valores
        user.setNombre(queryNameString);
        user.setUbicacion(queryAdressString);
        user.setTelefono(queryPhoneString);
        //user.setComida(user.getComida());
        //user.setServicio(user.getServicio());
        //user.setValoracion(user.getValoracion());

        // Llamamos al método que ejecuta la llamada al servidor enviando los datos
        modificacion(user);
    }

    /**
     * Método para validar los campos de entrada
     * @param oldPass contraseña actual introducido
     * @param newPass contraseña nuevaintroducida
     * @param confirmPassword confirmación de contraseña introducida
     * @return true si los campos estan rellenados y las contraseñas coinciden o false en caso contrario
     */
    private boolean validateFields(String oldPass, String newPass, String confirmPassword) {
        if (TextUtils.isEmpty(oldPass)) { // Comprueba si el campo contraseña actual está vacio
            mPassInput.setError("¡Debe ingresar una contraseña!");
            return false;
        }
        if (TextUtils.isEmpty(newPass)) { // Comprueba si el campo contraseña nueva está vacio
            mNewPassInput.setError("¡Debe ingresar una contraseña nueva!");
            return false;
        }
        if (TextUtils.isEmpty(confirmPassword)) { // Comprueba si el campo contraseña nueva confirmación está vacio
            mNewPassConfInput.setError("¡Debe confirmar la contraseña!");
            return false;
        }
        if (!newPass.equals(confirmPassword)) { // Comprobueba si las contraseñas coinciden
            Utils.showToast(this, "Las contraseñas no coinciden");
            return false;
        }
        return true;

        /*
        * CREO QUE NO ES NECESARIO
        * Preparado para futura utilización, de momento no se comprueba que venga texto
        * Comprueba si el campo dirección tiene texto
        * if ((networkInfo != null) && (queryAdressString.length() != 0)) {
        * }
        * Comprueba si el campo teléfono tiene texto
        * if ((networkInfo != null) && (queryPhoneString.length() != 0)) {
        * }
        * Comprueba si el check está activo
        * if ((networkInfo != null) && (mChangePass.isChecked())) {
        *}
        */
    }

    /**
     * Método para realizar el registro
     * @param user objeto con los datos del nuevo usuario
     */
    public void modificacion(User user) {
        // call HTTP client para modificar los datos de usuario
        Call<String> call = mfastMethods.modificarUsuario(token, user);
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
                    Utils.showToast(Activity_mod_perfil.this, "Modificación correcta!");
                    Utils.gotoActivity(Activity_mod_perfil.this, Activity_contenido.class);
                } else {
                    Utils.showToast(Activity_mod_perfil.this, "Error al modificar usuario");
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
                Utils.showToast(Activity_mod_perfil.this, "Error en la llamada: " + t.getMessage());
            }
        });
    }
    public boolean isModifySuccessful() {
        return modifySuccessful;
    }
}