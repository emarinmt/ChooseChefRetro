package com.example.choosechef;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Clase modificación de perfil
 * Gestiona la actividad modificar perfil de usuario
 * Modificada para utilizar el token y
 * la clase User como obtención de datos para modificar y enviar al servidor
 */

public class Activity_mod_perfil extends AppCompatActivity {
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
    String token;

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
        mPassInput = findViewById(R.id.edt_contrasena_anterior_mod_perfil);
        mNewPassInput = findViewById(R.id.edt_contrasena_nueva_mod_perfil);
        mNewPassConfInput = findViewById(R.id.edt_contrasena2_nueva_mod_perfil);
        mChangePass = findViewById(R.id.switch_cambio_contrasena);

        retro=FastClient.getClient();
        mfastMethods = retro.create(FastMethods.class);
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
        Context context = this; // Obtener el contexto de la actividad (this)
        // Compruebe el estado de la conexión de red
        if (!Utils.isNetworkAvailable(this)) {
            Utils.showToast(Activity_mod_perfil.this, "No hay conexión a Internet");
            return;
        }
        // Call HTTP client para recuperar la información del usuario
        Call<User> call = mfastMethods.recuperar_info(token);
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
                        Utils.showToastSecond(Activity_mod_perfil.this, context,"Obtención de datos incorrecta");
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
                Utils.showToastSecond(Activity_mod_perfil.this, context,"Error en la llamada: " + t.getMessage());
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

        // Condicional según la opción del switch de cambio de contrasena
        if (queryOptionPass) {
            // Comprueba si los campos de entrada son correctos
            if (!validateFields(queryPassString, queryNewPassString, queryNewPassConfString)) {
                return;
            }
            user.setPassword(queryNewPassString); // Cambio de contrasena
        }

        // Actualizamos los datos del usuario con los nuevos valores
        user.setNombre(queryNameString);
        user.setUbicacion(queryAdressString);
        user.setTelefono(queryPhoneString);

        // Llamamos al método que ejecuta la llamada al servidor enviando los datos
        modificacion(user);
    }

    /**
     * Método para validar los campos de entrada
     * @param oldPass contrasena actual introducido
     * @param newPass contrasena nuevaintroducida
     * @param confirmPassword confirmación de contrasena introducida
     * @return true si los campos estan rellenados y las contraseñas coinciden o false en caso contrario
     */
    private boolean validateFields(String oldPass, String newPass, String confirmPassword) {
        if (TextUtils.isEmpty(oldPass)) { // Comprueba si el campo contrasena actual está vacio
            mPassInput.setError("¡Debe ingresar una contrasena!");
            return false;
        }
        if (!oldPass.equals(user.getPassword())) { // Comprueba si la contrasena actual ingresada no coincide con la del usuario
            mPassInput.setError("¡La contrasena actual es incorrecta!");
            return false;
        }
        if (TextUtils.isEmpty(newPass)) { // Comprueba si el campo contrasena nueva está vacio
            mNewPassInput.setError("¡Debe ingresar una contrasena nueva!");
            return false;
        }
        if (TextUtils.isEmpty(confirmPassword)) { // Comprueba si el campo contrasena nueva confirmación está vacio
            mNewPassConfInput.setError("¡Debe confirmar la contrasena!");
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
        Context context = this; // Obtener el contexto de la actividad (this)
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
                    Utils.showToastSecond(Activity_mod_perfil.this,context, "Modificación correcta!");
                    Utils.gotoActivity(Activity_mod_perfil.this, Activity_contenido.class);
                } else {
                    Utils.showToastSecond(Activity_mod_perfil.this, context,"Error al modificar usuario");
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
                Utils.showToastSecond(Activity_mod_perfil.this, context,"Error en la llamada: " + t.getMessage());
            }
        });
    }

    /**
     * Método para test
     * @return devuelve un booleano en función de si ha ido bien la modificación
     */
    public boolean isModifySuccessful() {
        return modifySuccessful;
    }
    /**
     * Método para hacer logout
     * Redirige al usuario a la pantalla de inicio
     * @param view La vista (Button) a la que se hizo clic.
     */
    public void logout(View view){
        Utils.gotoActivity(Activity_mod_perfil.this, MainActivity_inicio.class);
    }
}