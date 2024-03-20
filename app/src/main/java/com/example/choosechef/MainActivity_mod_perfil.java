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
import android.widget.Switch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

// Representa la actividad para modificar el perfil del usuario.
public class MainActivity_mod_perfil extends AppCompatActivity {
    // Variables para los campos de entrada
    private EditText mNameInput;
    private EditText mAdressInput;
    private EditText mPhoneInput;
    private EditText mPassInput;
    private EditText mNewPassInput;
    private EditText mNewPassConfInput;
    private Switch mChangePass;
    private String username;
    FastMethods mfastMethods;
    Retrofit retro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establece el diseño de la actividad.
        setContentView(R.layout.activity_main_mod_perfil);
        //Inicialización de variables
        mNameInput = findViewById(R.id.edt_nombre_mod_perfil);
        mAdressInput = findViewById(R.id.edt_direccion_mod_perfil);
        mPhoneInput = findViewById(R.id.edt_telefono_mod_perfil);
        mPassInput = findViewById(R.id.edt_contraseña_anterior_mod_perfil);
        mNewPassInput = findViewById(R.id.edt_contraseña_nueva_mod_perfil);
        mNewPassConfInput = findViewById(R.id.edt_contraseña2_nueva_mod_perfil);
        mChangePass = findViewById(R.id.switch_cambio_contraseña);

        retro=FastClient.getClient();
        mfastMethods = retro.create(FastMethods.class);
    }

    /**
     * Método para manejar el clic del botón de confirmar cambios de modificación del perfil.
     * Guarda los cambios y dirige al usuario a la pantalla de contenido si es exitoso,
     * muestra un mensaje de error si falla.
     * @param view La vista (Button) que se hizo clic.
     */
    public void confirmaCambios(View view) {
        String queryNameString = mNameInput.getText().toString();
        String queryAdressString = mAdressInput.getText().toString();
        String queryPhoneString = mPhoneInput.getText().toString();
        String querynewPassString = mNewPassConfInput.getText().toString();
        int option = 0;

        //Compruebe el estado de la conexión de red.
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        } else {
            Utils.showToast(MainActivity_mod_perfil.this,"No hay conexión");
        }

        // Comprueba si el campo nombre tiene texto
        if ((networkInfo != null) && (queryNameString.length() != 0)) {
            // call HTTP client's modify method
            // Ejecutar la llamada de manera asíncrona
            //Onresponse + onfailure
            //Utils.gotoActivity(MainActivity_mod_perfil.this, MainActivity_contenido.class);
            //Call<ProfileResponse> call = mfastMethods.login(queryPhoneString);

        }

        // Comprueba si el campo dirección tiene texto
        if ((networkInfo != null) && (queryAdressString.length() != 0)) {

        }

        // Comprueba si el campo teléfono tiene texto
        if ((networkInfo != null) && (queryPhoneString.length() != 0)) {

        }

        // Comprueba si el check está activo
        if ((networkInfo != null) && (mChangePass.isChecked())) {

        }
    }
}