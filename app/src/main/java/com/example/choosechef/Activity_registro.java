package com.example.choosechef;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
 * Clase desarrollada por ELENA
 * Representa la actividad de registro de la aplicación
 * Se llega a través del boton "Registro" de la actividad principal inicio
 */
public class Activity_registro extends AppCompatActivity {
    private final String TAG = Activity_mod_perfil.class.getSimpleName();
    // Variables para los campos de entrada
    private EditText mUserInput;
    private EditText mPassInput;
    private EditText mConfirmPassInput;
    private Switch mChef;

    // Variables para conecatr con la API
    FastMethods mfastMethods;
    Retrofit retro;
    /**
     * Método onCreate para la configuración incial de la actividad
     * @param savedInstanceState estado de la instancia guardada, un objeto Bundle que contiene el estado previamente guardado de la actividad
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro); // Establece el diseño de la actividad.

        // Inicialización de variables
        mUserInput = findViewById(R.id.edt_usuario_registro);
        mPassInput = findViewById(R.id.edt_contraseña_registro);
        mConfirmPassInput = findViewById(R.id.edt_contraseña2_registro);
        mChef = findViewById(R.id.switch_chef_registro);

        retro=FastClient.getClient();
        mfastMethods = retro.create(FastMethods.class);
    }

    /**
     * Método para manejar el clic del botón de registro.
     * Hace el registro y dirige al usuario a la pantalla login,
     * muestra un mensaje de error si falla.
     * @param view La vista (Button) a la que se hizo clic.
     */

    public void doRegister(View view) {
        // Obtención de los datos de entrada
        String queryUserString = mUserInput.getText().toString();
        String queryPasswordString = mPassInput.getText().toString();
        String queryConfirmPassString = mConfirmPassInput.getText().toString();
        String queryTipo = mChef.isChecked() ? "chef" : "client";

        //Comprueba el estado de la conexión de red.
        //if (!Utils.isNetworkAvailable(this)) {
          //  Utils.showToast(Activity_registro.this, "No hay conexión a Internet");
           // return;
       // }
        //Comprueba el estado de la conexión de red.
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        } else {
            Utils.showToast(Activity_registro.this,"No hay conexión");
        }

        // Comprueba si los campos estan vacios
        if (!validateFields(queryUserString,queryPasswordString, queryConfirmPassString)) {
            return;
        }

        //construir el objeto ModificarUsuarioRequest con los datos ingresados
        ModificarUsuarioRequest request = new ModificarUsuarioRequest();
        request.setUsuario(queryUserString);
        request.setPassword(queryPasswordString);
        request.setTipo(queryTipo);
        request.setNombre("");
        request.setUbicacion("");
        request.setTelefono("");
        //estos campos no se piden al usuario y no se pueden modificar todavía, pero el método los necesita aunque esten vacíos
        request.setDescripcion("");
        request.setEmail("");

        if (networkInfo != null) {
            // Envia los datos del usuario al servidor
            // crearUsuario(queryUserString, queryPasswordString, queryTipo);
            crearUsuario(request);
        }


    }

    /**
     * Método para validar los campos
     * @param username nombre de usuario introducido
     * @param password contraseña introducida
     * @param confirmPassword confirmación de contraseña introducida
     * @return booleano true si los campos estan rellenados o false si alguno está vacio
     */
    private boolean validateFields(String username, String password, String confirmPassword) {
        if (TextUtils.isEmpty(username)) {  // Comprueba si el campo de ususario está vacio
            mUserInput.setError("¡Debe ingresar un nombre de usuario!");
            return false;
        }

        if (TextUtils.isEmpty(password)) { // Comprueba si el campo contraseña está vacio
            mPassInput.setError("¡Debe ingresar una contraseña!");
            return false;
        }

        if (TextUtils.isEmpty(confirmPassword)) { // Comprueba si el campo contraseña está vacio
            mConfirmPassInput.setError("¡Debe confirmar la contraseña!");
            return false;
        }

        if (!password.equals(confirmPassword)) { // Comprobueba si las contraseñas coinciden
            Utils.showToast(this, "Las contraseñas no coinciden");
            return false;
        }

        return true;
    }

    /**
     * Método para realizar el registro
     * //@param username nombre de usuario introducido
     * //@param password contraseña introducida
     * //@param userType respuesta al check (chef o cliente)
     */
    private void crearUsuario(ModificarUsuarioRequest request) {
        //String username, String password, String userType
        Call<String> call = mfastMethods.crearUsuario(request);
        //password,userType
        call.enqueue(new Callback<String>() { // Ejecutar la llamada de manera asíncrona
            /**
             *Método invocado cuando se recibe una respuesta de la solicitud HTTP
             * @param call llamada que generó la respuesta
             * @param response la respuesta recibida del servidor
             */
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    Utils.showToast(Activity_registro.this, "Registro exitoso");
                    Utils.gotoActivity(Activity_registro.this, Activity_login.class);
                } else {
                    Utils.showToast(Activity_registro.this, "El usuario ya existe");
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
                Utils.showToast(Activity_registro.this, "Error en la llamada: " + t.getMessage());
            }
        });
    }

}

