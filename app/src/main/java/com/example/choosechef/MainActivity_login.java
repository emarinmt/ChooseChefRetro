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
    private TextView mResultPassText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        //Inicializamos variables
        mUserInput = (EditText)findViewById(R.id.edt_usuario_login);
        mPassInput = (EditText)findViewById(R.id.edt_contraseña_login);
        mResultText = (TextView)findViewById(R.id.txt_login_result);
        mResultPassText = (TextView)findViewById(R.id.txt_pass_result);

        //Para volver a conectarse al loader, si el loader ya existe (girar la pantalla)
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
        }


        // Si la red está disponible, conectada, y el campo de búsqueda
        // no está vacío, inicia una UserLoader AsyncTask.

        if (networkInfo != null && queryUserString.length() != 0) {
            //Pasamos esta variable en el objeto bundle que creamos
            // y si la contraseña no está vacía la añadimos tambien
            // procedemos a llamar al restartLoader() para iniciar una UserLoader AsyncTask.
            Bundle queryBundle = new Bundle();
            queryBundle.putString("queryUserString", queryUserString);

            if (queryPasswordString.length() != 0) {
                queryBundle.putString("queryPasswordString", queryPasswordString);
                LoaderManager.getInstance(this).restartLoader(0, queryBundle, this);
                mResultText.setText("");
                mResultPassText.setText("loading..");
            }
        }
        // Otherwise update the TextView to tell the user there is no
        // connection, or no search term.
        else {
            if ((queryUserString.length() == 0) || (queryPasswordString.length() == 0)) {
                mResultText.setText("");
                mResultPassText.setText("no_search_term");//PASAR A STRING
            } else {
                mResultText.setText("");
                mResultPassText.setText("no_network"); //PASAR A STRING
            }
        }

    }


    // Mètodo requerido por la interfaz
    //Es llamado cuando se instancia el loader, ya que devuelve una instancia de la clase UserLoader.
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        String queryUserString = "";
        String queryPasswordString = "";

        if (args != null) {
            queryUserString = args.getString("queryUserString");
            queryPasswordString = args.getString("queryPasswordString");
        }
        return new UserLoader(this, queryUserString, queryPasswordString);
    }

    // Mètodo requerido por la interfaz
    // Es llamado cuando la tarea finaliza, es donde implementamos el codigo para actualizar la interfaz con los resultados.
    // Para implementar este códifo es necesario conocer la documentación de la API,
    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        try {
            // Convert the response into a JSON object
            JSONObject jsonObject = new JSONObject(data);
            // Get the JSONArray of character.
            JSONArray itemsArray = jsonObject.getJSONArray("nombre");

            //Initialize the variables used for the parsing loop
            int i = 0;
            String userName = null;
            String password = null;

            //Iterate through the itemsArray array
            while (i < itemsArray.length() &&
                    (userName == null && password == null)) {
                // Get the current item information.
                JSONObject userItem = itemsArray.getJSONObject(i);

                // Intenta obtener el nombre
                try {
                    userName = userItem.getString("nombre");
                    password = userItem.getString("password");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Move to the next item.
                i++;
            }

            //Si se encuentra una respuesta que coincida, actualiza la interfaz de usuario con esa respuesta
            if (userName != null && password != null) {
                mResultText.setText(userName);
                mResultPassText.setText(password);
            } else {
                mResultText.setText("no encontrado");
                mResultPassText.setText("");
            }

        } catch (JSONException e) {
            e.printStackTrace();
            // If onPostExecute does not receive a proper JSON string,
            // update the UI to show failed results.
            mResultText.setText("no encontrado");
            mResultPassText.setText("");
            e.printStackTrace();
        }
    }

    // Mètodo requerido por la interfaz
    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}