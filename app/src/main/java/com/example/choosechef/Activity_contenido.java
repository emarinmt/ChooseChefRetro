package com.example.choosechef;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Clase desarrollada por ELENA
 * para gestionar la actividad contenido
 */

public class Activity_contenido extends AppCompatActivity {
    private final String TAG = Activity_contenido.class.getSimpleName();
    private static final int REQUEST_CODE = 1;
    // Añadido por EVA para recibir el usuario en esta actividad y enviarlo a la siguiente( modificaicón perfil)
    //String usuario;
    //String pass;
    // Borrado por ELENA para usar token

    // Variables para mostrar los chefs
    RecyclerView recyclerView;
    Adapter_chef adapter;
    List<User> userList = new ArrayList<>(); // Lista para almacenar los chefs

    //ArrayList<String> items;

    // Variables para conectar con la API
    FastMethods mfastMethods;
    Retrofit retro;
    private User user; // ELENA
    private User user_logeado; // ELENA
    String token;
    String mTipoLogeado;
    /**
     * Método onCreate para la configuración incial de la actividad
     * @param savedInstanceState estado de la instancia guardada, un objeto Bundle que contiene el estado previamente guardado de la actividad
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establece el diseño de la actividad.
        setContentView(R.layout.activity_content);

        retro=FastClient.getClient();
        mfastMethods = retro.create(FastMethods.class);

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.rv_chefs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter_chef(this, userList);
        //adapter = new Adapter(this,items);
        recyclerView.setAdapter(adapter);

        // Obtener el token de SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MiPreferencia", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        // Llamar al método recuperarDatos
        recuperarChefs();

    }

    /**
     * Método para recuperar la lista de chefs del servidor
     */
    public void recuperarChefs(){
        // Compruebe el estado de la conexión de red
        if (!Utils.isNetworkAvailable(this)) {
            Utils.showToast(Activity_contenido.this, "No hay conexión a Internet");
            return;
        }
        // Call HTTP client para recuperar la información del usuario
        Call<List<User>> call = mfastMethods.recuperar_chefs(); //CAMBIAR
        call.enqueue(new Callback<List<User>>() { // Ejecutar la llamada de manera asíncrona
            /**
             * Método invocado cuando se recibe una respuesta de la solicitud HTTP
             * @param call llamada que generó la respuesta
             * @param response la respuesta recibida del servidor
             */
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userList.clear(); // Limpiar la lista actual
                    userList.addAll(response.body()); // Agregar todos los usuarios recuperados

                    // Notificar al adaptador que los datos han cambiado
                    adapter.notifyDataSetChanged();
                } else {
                    Utils.showToast(Activity_contenido.this, "No se encontraron chefs");
                }
            }
            /**
             *Método invocado cuando ocurre un error durante la ejecución de la llamada HTTP
             * @param call la llamada que generó el error
             * @param t la excepción que ocurrió
             */
            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                // Error en la llamada, muestra el mensaje de error y registra la excepción
                t.printStackTrace();
                Log.e(TAG, "Error en la llamada:" + t.getMessage());
                Utils.showToast(Activity_contenido.this, "Error en la llamada: " + t.getMessage());
            }
        });
    }

    /**
     * Método recuperarDatos
     * Para recuperar los datos del usuario logeado
     */
    public void recuperarDatos(){
        // Compruebe el estado de la conexión de red
        if (!Utils.isNetworkAvailable(this)) {
            Utils.showToast(Activity_contenido.this, "No hay conexión a Internet");
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
                    user_logeado = response.body(); // Recibe los datos del usuario
                    if (user_logeado != null) {
                        mTipoLogeado=user_logeado.getTipo();
                    } else {
                        // Obtención de datos incorrecta, muestra un mensaje de error
                        Utils.showToast(Activity_contenido.this, "Obtención de datos incorrecta");
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
                Utils.showToast(Activity_contenido.this, "Error en la llamada: " + t.getMessage());
            }
        });
    }


    /**
     * Método para manejar el clic del botón de modificación de perfil.
     * Dirige al usuario a la pantalla de modificacoón
     * @param view La vista (Button) que se hizo clic.
     */
    public void changeProfile (View view) {
        // Redirige al usuario a la pantalla de modificación de perfil
        //Utils.gotoActivity(MainActivity_contenido.this, MainActivity_mod_perfil.class);
        //Cambiado por EVA para poder recibir el usuario y la contraseña de la actividad anterior(login) y enviarlo a la siguiente actividad (modificación perfil)
        //Utils.gotoActivityMessage(Activity_contenido.this, Activity_mod_perfil.class, "usuario",usuario , "pass", pass, false);
        // Modificado por ELENA para usar el token
        // Redirige al usuario a la pantalla de modificación de perfil
        Utils.gotoActivity(Activity_contenido.this, Activity_mod_perfil.class);
    }
    public void search(View view) {
        // Redirige al usuario a la pantalla de busqueda
        /*
        Utils.gotoActivityWithResult(Activity_contenido.this, Activity_busqueda.class, REQUEST_CODE);

        if (requestCode == REQUEST_CODE_ACTIVITY_B && resultCode == Activity.RESULT_OK && data != null) {
            // Obtener datos devueltos por ActivityB usando Utils
            Bundle extras = data.getExtras();
            if (extras != null) {
                String value1 = extras.getString("key_value_1");
                String value2 = extras.getString("key_value_2");
                String value3 = extras.getString("key_value_3");

                // Usar los datos recibidos como desees
                // por ejemplo, mostrar en un TextView
                // textView.setText("Valor 1: " + value1 + "\nValor 2: " + value2 + "\nValor 3: " + value3);
            }
        }
         */
    }

    public void ajustes (View view) {
        // Obtenemos tipo de usuario del usuario logeado
        recuperarDatos();
        if (mTipoLogeado != null && !mTipoLogeado.isEmpty()) {
            // Redirige segun el usuario a la pantalla de configuracion de chef, admin o usuario
            if (mTipoLogeado.equalsIgnoreCase("chef")){
                Utils.gotoActivity(Activity_contenido.this, Activity_chef.class);
            } else if (mTipoLogeado.equalsIgnoreCase("client")) {
                Utils.gotoActivity(Activity_contenido.this, Activity_user.class);
            }else if (mTipoLogeado.equalsIgnoreCase("admin")) {   //DE MOMENTO SOLO MUESTSRA CHEFS
                Utils.gotoActivity(Activity_contenido.this, Activity_admin.class);
            } else {
                Utils.showToast(Activity_contenido.this, "Tipo de usuario incorrecto");
            }
        } else {
            Utils.showToast(Activity_contenido.this, "Error opteniendo el tipo de usuario");
        }
    }
    public void logout(View view){
        Utils.gotoActivity(Activity_contenido.this, MainActivity_inicio.class);
    }
}

