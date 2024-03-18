package com.example.choosechef;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.Loader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Consulta la API en busca de usuarios basados en los datos introducidos.
 * Utiliza un AsyncTaskLoader para ejecutar la tarea de búsqueda en segundo plano.
 */
public class MainActivity_login extends AppCompatActivity {
    private String TAG = MainActivity_login.class.getSimpleName();

    // Variables para el campo de entrada de búsqueda y TextViews de resultados
    private EditText mUserInput;
    private EditText mPassInput;
    private TextView mResultText; //*BORRAR

    fastMethods mfastMethods;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        //Inicializamos variables
        mUserInput = (EditText)findViewById(R.id.edt_usuario_login);
        mPassInput = (EditText)findViewById(R.id.edt_contraseña_login);
        mResultText = (TextView)findViewById(R.id.txt_login_result); //*BORRAR
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
           // Utils.showToast(this,"No connection");
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
            mfastMethods.login(queryUserString,queryPasswordString).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                    //Response was successfull
                    Log.i(TAG, "Response: " + response.body());
                    Intent intent = new Intent(MainActivity_login.this, MainActivity_contenido.class);
                    startActivity(intent);
                }
                @Override
                public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    //Response failed
                    Log.e(TAG, "Response: " + t.getMessage());
                }
            });
        }
    }
}