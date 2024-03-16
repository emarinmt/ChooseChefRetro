package com.example.choosechef;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * Consulta la API en busca de usuarios basados en los datos introducidos.
 * Utiliza un AsyncTaskLoader para ejecutar la tarea de búsqueda en segundo plano.
 */
public class MainActivity_login extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<String>{

    // La conexión entre el AsyncTaskLoader y la actividad que lo llama se implementa con la interfaz
    // Hacemos que la actividad implemente esta interfaz para manejar los resultados del método loadInBackground()

    // Variables para el campo de entrada de búsqueda y TextViews de resultados (PROVISIONAL)
    private EditText mUserInput;
    private EditText mPassInput;
    private TextView mResultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        //Inicializamos variables
        mUserInput = (EditText)findViewById(R.id.edt_usuario_login);
        mPassInput = (EditText)findViewById(R.id.edt_contraseña_login);
        mResultText = (TextView)findViewById(R.id.txt_login_result);

        //Para volver a conectarse al loader, si el loader ya existe
        if(LoaderManager.getInstance(this).getLoader(0)!=null){
            LoaderManager.getInstance(this).initLoader(0, null, this);
        }
    }

    /**
     * Respuesta al clic del boton login (El botón de login tiene el atriubuto onclick searchUsers)
     * Hace el login y va a la pantalla de contenido; avisa al usuario si se produce un error
     * @param view The view (Button) that was clicked.
     */
    public void searchUsers(View view) {
    }


    // Mètodo requerido por la interfaz
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        return null;
    }

    // Mètodo requerido por la interfaz
    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {

    }

    // Mètodo requerido por la interfaz
    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}