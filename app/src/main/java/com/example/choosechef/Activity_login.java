package com.example.choosechef;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Clase desarrollada por ELENA
 * para gestionar la actividad para manejar el proceso de inicio de sesión de usuario.
 */

public class Activity_login extends AppCompatActivity {
    private final String TAG = Activity_login.class.getSimpleName();

    // Variables para los campos de entrada de usuario y contraseña.
    private EditText mUserInput;
    private EditText mPassInput;
    FastMethods mfastMethods;
    Retrofit retro;
    ProfileResponse ProfileResponse;

    // PARA TOKEN
    // private SharedPreferences sharedPreferences;
    /**
     * método onCreate para la configuración incial de la actividad
     * @param savedInstanceState estado de la instancia guardada, un objeto Bundle que contiene el estado previamente guardado de la actividad
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Inicialización de variables
        mUserInput = findViewById(R.id.edt_usuario_login);
        mPassInput = findViewById(R.id.edt_contra_login);
        ProfileResponse = new ProfileResponse(); // Inicializamos profileResponse

        retro=FastClient.getClient();
        mfastMethods = retro.create(FastMethods.class);

        /*
        PARA TOKEN

        //Inicialización de SharePreferences para recoger el token del servidor
        sharedPreferences = getSharedPreferences("MiPreferencia", Context.MODE_PRIVATE);
         */
    }

    /**
     * Método para manejar el clic del botón de inicio de sesión.
     * Hace el login y dirige al usuario a la pantalla de contenido si es exitoso,
     * muestra un mensaje de error si falla.
     * @param view La vista (Button) que se hizo clic.
     */
    public void doLogin(View view) {
        String queryUserString = mUserInput.getText().toString();
        String queryPasswordString = mPassInput.getText().toString();

        // Oculta el teclado cuando el usuario toca el botón
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null ) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

        //Compruebe el estado de la conexión de red.
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        } else {
            Utils.showToast(Activity_login.this,"No hay conexión");
        }

        // Comprueba si el campo de ususario está vacio
        if (queryUserString.length() == 0) {
            mUserInput.setError("¡No se ha proporcionado un usuario!");
        }

        // Comprueba si el campo contraseña está vacio
        if (queryPasswordString.length() == 0) {
            mPassInput.setError("¡No se ha proporcionado una contraseña!");
        }

        if ((networkInfo != null) && (queryUserString.length() != 0) && (queryPasswordString.length() != 0)) {
            //call HTTP client's login method
            Call<Boolean> call = mfastMethods.login(queryUserString,queryPasswordString); // PARA TOKEN CANVIAR BOOLEAN POR TokenResponse

            // Ejecutar la llamada de manera asíncrona
            call.enqueue(new Callback<Boolean>() { // PARA TOKEN CANVIAR BOOLEAN POR TokenResponse
                /**
                 *Método invocado cuando se recibe una respuesta de la solicitud HTTP
                 * @param call llamada que generó la respuesta
                 * @param response la respuesta recibida del servidor
                 */
                @Override
                // PARA TOKEN CANVIAR LOS DOS BOOLEAN POR TokenResponse
                public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                    if (response.isSuccessful()) { // PARA TOKEN AÑADIR && response.body() != null)

                        /*
                            PARA TOKEN

                            TokenResponse tokenResponse = response.body();
                            String token = tokenResponse.getToken();

                            // Guardar el token de acceso en SharedPreferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("token", token);
                            editor.apply();

                            // Inicio de sesión exitoso, redirige al usuario a la pantalla de contenido
                           //Utils.gotoActivity(MainActivity_login.this, MainActivity_contenido.class);

                    } else {
                            // Inicio de sesión fallido, muestra un mensaje de error
                            Utils.showToast(MainActivity_login.this, "Inicio de sesión incorrecto");
                        }
                         */

                        //BORRAR PARA TOKEN
                        Boolean loginSuccess = response.body();
                        if (loginSuccess != null && loginSuccess) {
                            //guardamos el usuario para utilizarlo después en otra consulta
                            ProfileResponse.setUser(queryUserString);
                            // Inicio de sesión exitoso, redirige al usuario a la pantalla de contenido
                           //Utils.gotoActivity(MainActivity_login.this, MainActivity_contenido.class);

                            //Modificado por EVA para enviar el usuario y contraseña a la siguiente actividad (contenido)
                            Utils.gotoActivityMessage(Activity_login.this, Activity_contenido.class, "usuario",queryUserString , "pass", queryPasswordString, true);

                        } else {
                            // Inicio de sesión fallido, muestra un mensaje de error
                            Utils.showToast(Activity_login.this, "Inicio de sesión incorrecto");
                        }

                        //
                    } else {
                        // La llamada no fue exitosa, muestra un mensaje de error
                        Utils.showToast(Activity_login.this, "Error de conexión");
                    }
                }
                /**
                 *Método invocado cuando ocurre un error durante la ejecución de la llamada HTTP
                 * @param call la llamada que generó el error
                 * @param t la excepción que ocurrió
                 */
                @Override
                public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                    // Error en la llamada, muestra el mensaje de error y registra la excepción
                    t.printStackTrace();
                    Log.e(TAG, "Error en la llamada:" + t.getMessage());
                }
            });

        }
    }

}