package com.example.choosechef;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
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
 * Clase login
 * Maneja el proceso de inicio de sesión de los usuarios.
 */
public class Activity_login extends AppCompatActivity {
    private boolean loginSuccessful = false; // Variable para rastrear el estado del login
    private final String TAG = Activity_login.class.getSimpleName();

    // Variables para los campos de entrada de usuario y contrasena
    private EditText mUserInput;
    private EditText mPassInput;
    FastMethods mfastMethods;
    // Variables para conectar con la API
    Retrofit retro;
    private SharedPreferences sharedPreferences; // Para almacenar el token
    /**
     * Método onCreate para la configuración incial de la actividad
     * @param savedInstanceState estado de la instancia guardada, un objeto Bundle que contiene el estado previamente guardado de la actividad
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Establece el diseño de la actividad

        // Inicialización de variables
        mUserInput = findViewById(R.id.edt_usuario_login);
        mPassInput = findViewById(R.id.edt_contra_login);

        retro=FastClient.getClient();
        mfastMethods = retro.create(FastMethods.class);

        // Inicialización de SharePreferences para recoger el token del servidor
        sharedPreferences = getSharedPreferences("MiPreferencia", Context.MODE_PRIVATE);
    }

    /**
     * Método para manejar el clic del botón de inicio de sesión.
     * Hace el login y dirige al usuario a la pantalla de contenido si es exitoso,
     * muestra un mensaje de error si falla.
     * @param view La vista (Button) que se hizo clic.
     */
    public void doLogin(View view) {
        // Obtención de los datos de entrada
        String queryUserString = mUserInput.getText().toString();
        String queryPasswordString = mPassInput.getText().toString();

        // Oculta el teclado cuando el usuario toca el botón
        Utils.hideKeyboard(this, view);

        // Compruebe el estado de la conexión de red
        if (!Utils.isNetworkAvailable(this)) {
            Utils.showToast(Activity_login.this, "No hay conexión a Internet");
            return;
        }

        // Comprueba si los campos estan vacios
        if (!validateFields(queryUserString,queryPasswordString)) {
            return;
        }

        // Llamamos al método que ejecuta la llamada al servidor enviando los datos
        loginUsuario(queryUserString,queryPasswordString);
    }
    /**
     * Método para validar los campos
     * @param username nombre de usuario introducido
     * @param password contrasena introducida
     * @return booleano true si los campos estan rellenados o false si alguno está vacio
     */
    private boolean validateFields(String username, String password) {
        if (TextUtils.isEmpty(username)) {  // Comprueba si el campo de ususario está vacio
            mUserInput.setError("¡Debe ingresar un nombre de usuario!");
            return false;
        }
        if (TextUtils.isEmpty(password)) { // Comprueba si el campo contrasena está vacio
            mPassInput.setError("¡Debe ingresar una contrasena!");
            return false;
        }
        return true;
    }

    /**
     * Método para realizar el login
     * @param username nombre de usuario introducido
     * @param password contrasena introducida
     */
    private void loginUsuario(String username, String password) {
        Context context = this; // Obtener el contexto de la actividad (this)
        Call<TokenResponse> call = mfastMethods.login(username,password);
        call.enqueue(new Callback<TokenResponse>() { // Ejecutar la llamada de manera asíncrona
            /**
             * Método invocado cuando se recibe una respuesta de la solicitud HTTP
             * @param call llamada que generó la respuesta
             * @param response la respuesta recibida del servidor
             */
            @Override
            public void onResponse(@NonNull Call<TokenResponse> call, @NonNull Response<TokenResponse> response) {
                if ((response.isSuccessful()) && (response.body() != null)) {
                    TokenResponse tokenResponse = response.body();
                    // Ahora Verificamos si el inicio de sesión fue exitoso basado en la respuesta del servidor
                    if (tokenResponse.getToken() != null && !tokenResponse.getToken().isEmpty()) {
                        String token = tokenResponse.getToken();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("token", token);
                        editor.apply();
                        // Inicio de sesión exitoso, redirige al usuario a la pantalla de contenido
                        loginSuccessful = true;
                        Utils.gotoActivity(Activity_login.this, Activity_contenido.class);

                    } else {
                        // Inicio de sesión fallido, muestra un mensaje de error
                        Utils.showToastSecond(Activity_login.this, context,"Inicio de sesión incorrecto");
                    }
                } else {
                    // Inicio de sesión fallido, muestra un mensaje de error
                    Utils.showToastSecond(Activity_login.this,context, "Inicio de sesión incorrecto");
                }
            }

            /**
             * Método invocado cuando ocurre un error durante la ejecución de la llamada HTTP
             * @param call la llamada que generó el error
             * @param t la excepción que ocurrió
             */
            @Override
            public void onFailure(@NonNull Call<TokenResponse> call, @NonNull Throwable t) {
                // Error en la llamada, muestra el mensaje de error y registra la excepción
                t.printStackTrace();
                Log.e(TAG, "Error en la llamada:" + t.getMessage());
                Utils.showToastSecond(Activity_login.this, context,"Error en la llamada: " + t.getMessage());
            }
        });

    }

    /**
     * Método para test
     * @return devuelve un booleano en funcion de si ha ido bien el login
     */
    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }
}