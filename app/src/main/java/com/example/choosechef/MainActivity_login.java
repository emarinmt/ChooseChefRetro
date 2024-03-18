package com.example.choosechef;

import android.content.Intent;
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
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Consulta la API en busca de usuarios basados en los datos introducidos.
 * Utiliza un AsyncTaskLoader para ejecutar la tarea de búsqueda en segundo plano.
 */
public class MainActivity_login extends AppCompatActivity {
    private final String TAG = MainActivity_login.class.getSimpleName();

    // Variables para el campo de entrada de búsqueda y TextViews de resultados
    private EditText mUserInput;
    private EditText mPassInput;
    FastMethods mfastMethods;
    Retrofit retro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        //Inicializamos variables
        mUserInput = findViewById(R.id.edt_usuario_login);
        mPassInput = findViewById(R.id.edt_contra_login);

        retro=FastClient.getClient();
        mfastMethods = retro.create(FastMethods.class);

    }

    /**
     * Respuesta al clic del boton login (El botón de login tiene el atriubuto onclick doLogin)
     * Hace el login y va a la pantalla de contenido; avisa al usuario si se produce un error
     * @param view The view (Button) that was clicked.
     */
    public void doLogin(View view) {
        //Cogemos la referencia al texto escrito en el edittext y la asignamos a una variable de tipo string
        //Obtener la cadena de búsqueda del campo de entrada.
        String queryUserString = mUserInput.getText().toString();
        String queryPasswordString = mPassInput.getText().toString();

        //hides the keyboard when the user taps the button
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
           displayToast("No connection");
        }

        //Comprueba si el campo de ususario está vacio
        if (queryUserString.length() == 0) {
            mUserInput.setError("No User Provided!");
        }

        //Comprueba si el campo contraseña está vacio
        if (queryPasswordString.length() == 0) {
            mPassInput.setError("No Pass Provided!");
        }

        // Si la red está disponible, conectada, y los campo de búsqueda
        // no estan vacíos
        if ((networkInfo != null) && (queryUserString.length() != 0) && (queryPasswordString.length() != 0)) {
            //call HTTP client's login method
            Call<Boolean> call = mfastMethods.login(queryUserString,queryPasswordString);

            // Ejecutar la llamada de manera asíncrona
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                    if (response.isSuccessful()) {
                        Boolean loginSuccess = response.body();
                        if (loginSuccess != null && loginSuccess) {
                            // Inicio de sesión exitoso
                            // Realizar acciones necesarias después del inicio de sesión exitoso
                            Intent intent = new Intent(MainActivity_login.this, MainActivity_contenido.class);
                            startActivity(intent);
                        } else {
                            // Inicio de sesión fallido
                            // Realizar acciones necesarias después del inicio de sesión fallido
                            displayToast("login_erroneo");
                        }
                    } else {
                        // La llamada no fue exitosa
                        // Manejar el error según sea necesario
                        displayToast("conexion erronea");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                    // Error en la llamada
                    // Manejar el error según sea necesario
                    t.printStackTrace();
                    Log.e(TAG, "Response: " + t.getMessage());
                }
            });

        }
    }

    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }
}