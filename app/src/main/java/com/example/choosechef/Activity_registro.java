package com.example.choosechef;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
 * Clase desarrollada por Elena
 * Para hacer el registro del usuario
 * A ella se llega a través del boton "Registro" de la actividad principal inicio
 */
// Representa la actividad de registro de la aplicación.
public class Activity_registro extends AppCompatActivity {
    private final String TAG = Activity_mod_perfil.class.getSimpleName();
    // Variables para los campos de entrada
    private EditText mUserInput;
    private EditText mPassInput;
    private EditText mConfirmPassInput;
    FastMethods mfastMethods;
    Retrofit retro;
    /**
     * método onCreate para la configuración incial de la actividad
     * @param savedInstanceState estado de la instancia guardada, un objeto Bundle que contiene el estado previamente guardado de la actividad
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establece el diseño de la actividad.
        setContentView(R.layout.activity_registro);
        //Inicialización de variables
        mUserInput = findViewById(R.id.edt_usuario_registro);
        mPassInput = findViewById(R.id.edt_contraseña_registro);
        mConfirmPassInput = findViewById(R.id.edt_contraseña2_registro);

        retro=FastClient.getClient();
        mfastMethods = retro.create(FastMethods.class);
    }
    public void doRegister(View view) {
        String queryUserString = mUserInput.getText().toString();
        String queryPasswordString = mPassInput.getText().toString();
        String queryConfirmPassString = mConfirmPassInput.getText().toString();

        //Construir el objeto ModificarUsuarioRequest con los datos ingresados
        RegistroUsuarioRequest request = new RegistroUsuarioRequest();
        request.setUsuario(queryUserString);
        //Comprobación de contraseña
        if (queryPasswordString.equals(queryConfirmPassString)){
            request.setPassword(queryConfirmPassString);
        }else{
            Utils.showToast(Activity_registro.this,"Las contraseñas no coinciden");
        }

        //el id, lo obtenemos de la respuesta, ya que no se puede modificar
        request.setId(request.getId());
        //PENDIENTE
        request.setTipo(" ");

        //Compruebe el estado de la conexión de red.
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        } else {
            Utils.showToast(Activity_registro.this,"No hay conexión");
        }

        // Comprueba si el campo de ususario está vacio
        if (queryUserString.length() == 0) {
            mUserInput.setError("¡No se ha proporcionado un usuario!");
        }

        // Comprueba si el campo contraseña está vacio
        if (queryConfirmPassString.length() == 0) {
            mConfirmPassInput.setError("¡No se ha proporcionado una contraseña!");
        }

        if ((networkInfo != null) && (queryUserString.length() != 0) && (queryConfirmPassString.length() != 0)) {
            Call<String> call = mfastMethods.registrarUsuario(request);
            // Ejecutar la llamada de manera asíncrona
            call.enqueue(new Callback<String>() {
                /**
                 *Método invocado cuando se recibe una respuesta de la solicitud HTTP
                 * @param call llamada que generó la respuesta
                 * @param response la respuesta recibida del servidor
                 */
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        //String responseBody = response.body();
                        Utils.showToast(Activity_registro.this, "Registro correcto!");
                        Utils.gotoActivity(Activity_registro.this, Activity_login.class);
                    } else {
                        Utils.showToast(Activity_registro.this, "Error al registrar usuario");
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
                    Utils.showToast(Activity_registro.this, "Error en la llamada: " + t.getMessage());
                }
            });
        } else {
            Utils.showToast(Activity_registro.this, "No hay conexión a internet");
        }
    }
}