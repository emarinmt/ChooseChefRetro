package com.example.choosechef;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Clase chef servicio
 * Permite rellenar el perfil del chef, ubicación, tipo de comida, tipo de servicio y descripción.
 */
public class Activity_chef_servicio extends AppCompatActivity {
    private boolean modifySuccessful = false; // Variable para rastrear el estado de la modificación
    private boolean contentSuccessful = false; // Variable para rastrear el estado de la muestra del listado
    private final String TAG = Activity_chef_servicio.class.getSimpleName();
    //Variables para los spinners, descripción y selecciones
    Spinner spinner_prov;
    Spinner spinner_comida;
    Spinner spinner_servicio;
    public EditText descripcion;
    public String prov_seleccionada;
    public String comida_seleccionada;
    public String servicio_seleccionada;
    public String descripcionInput;

    // Variables para conectar con la API
    FastMethods mfastMethods;
    Retrofit retro;
    private User user;
    String token;
    /**
     * Método onCreate para la configuración incial de la actividad
     * @param savedInstanceState estado de la instancia guardada, un objeto Bundle que contiene el estado previamente guardado de la actividad
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establece el diseño de la actividad.
        setContentView(R.layout.activity_chef_servicio);

        //Inicialización de variables
        spinner_prov = findViewById(R.id.spinner_provincias);
        spinner_comida = findViewById(R.id.spinner_tipo_comida);
        spinner_servicio = findViewById(R.id.spinner_servicios);
        descripcion = findViewById(R.id.edt_descripcion_servicio);

        retro=FastClient.getClient();
        mfastMethods = retro.create(FastMethods.class);
        user = new User();
        // Obtener el token de SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MiPreferencia", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        //Configuración de las opciones disponibles para elegir en los spinners
        configurarSpinnerProvincia();
        configurarSpinnerComida();
        configurarSpinnerServicio();

        //Recupera datos del servidor para mostrarlos seleccionados en los spinners y la descripción
        recuperarDatos();
    }

    /**
     * Método para configurar las opciones disponibles en el spinner de provincias
     */
    private void configurarSpinnerProvincia() {
        ArrayList<String> provinciasList = new ArrayList<>();
        provinciasList.add("Barcelona");
        provinciasList.add("Baleares");
        provinciasList.add("Lleida");
        provinciasList.add("Tarragona");
        provinciasList.add("Madrid");
        provinciasList.add("Mallorca");
        provinciasList.add("Zaragoza");
        provinciasList.add("Salamanca");

        ArrayAdapter<String> adapterProvincia = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, provinciasList);
        adapterProvincia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_prov.setAdapter(adapterProvincia);
    }
    /**
     * Método para configurar las opciones disponibles en el spinner de comida
     */
    private void configurarSpinnerComida() {
        ArrayList<String> comidaList = new ArrayList<>();
        comidaList.add("Africana");
        comidaList.add("Asiática");
        comidaList.add("Barbacoa");
        comidaList.add("Coreana");
        comidaList.add("Italiana");
        comidaList.add("Japonesa");
        comidaList.add("Mediterranea");
        comidaList.add("Mexicana");
        comidaList.add("Tailandesa");

        ArrayAdapter<String> adapterComida = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, comidaList);
        adapterComida.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_comida.setAdapter(adapterComida);
    }
    /**
     * Método para configurar las opciones disponibles en el spinner de servicio
     */
    private void configurarSpinnerServicio() {
        ArrayList<String> servicioList = new ArrayList<>();
        servicioList.add("Catering a domicilio");
        servicioList.add("Chef a domicilio");
        servicioList.add("Catering para evento");

        ArrayAdapter<String> adapterServicio = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, servicioList);
        adapterServicio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_servicio.setAdapter(adapterServicio);
    }

    /**
     * Método para recuperar los datos del servidor y mostrarlos en los spinners y descripción
     */
    public void recuperarDatos(){
        Context context = this; // Obtener el contexto de la actividad (this)
        // Compruebe el estado de la conexión de red
        if (!Utils.isNetworkAvailable(this)) {
            Utils.showToastSecond(Activity_chef_servicio.this, context,"No hay conexión a Internet");
            contentSuccessful = false;
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
                    user = response.body(); // Recibe los datos del usuario
                    if (user != null) {
                        contentSuccessful = true;
                        // Mostrar datos en pantalla
                        spinner_prov.setSelection(getIndex(spinner_prov, user.getUbicacion()));
                        spinner_comida.setSelection(getIndex(spinner_comida, user.getComida()));
                        spinner_servicio.setSelection(getIndex(spinner_servicio, user.getServicio()));
                        descripcion.setText(user.getDescripcion());
                    } else {
                        // Obtención de datos incorrecta, muestra un mensaje de error
                        Utils.showToastSecond(Activity_chef_servicio.this, context,"Obtención de datos incorrecta");
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
                contentSuccessful = false;
                t.printStackTrace();
                Log.e(TAG, "Error en la llamada:" + t.getMessage());
                Utils.showToastSecond(Activity_chef_servicio.this, context,"Error en la llamada: " + t.getMessage());
            }
        });
    }
    /**
     * Metodo para obtener el índice de un elemento en un Spinner
     * @param spinner el spinner donde buscar el índice
     * @param value el valor que estamos buscando dentro del Spinner
     * @return devuelve la posición donde está ese elemento
     */
    private int getIndex(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                return i;
            }
        }
        return 0; // Valor por defecto si no se encuentra el elemento
    }

    /**
     * Método para modificar la información del usuario en el servidor
     * @param view La vista (Button) a la que se hizo clic.
     */
    public void confirmarServicio(View view) {
        Context context = this; // Obtener el contexto de la actividad (this)
        // Obtener los valores seleccionados de los spinners y la descripción introducida
        prov_seleccionada = (String) spinner_prov.getSelectedItem();
        comida_seleccionada = (String) spinner_comida.getSelectedItem();
        servicio_seleccionada = (String) spinner_servicio.getSelectedItem();
        descripcionInput = descripcion.getText().toString();

        // Validar que los valores no sean nulos o vacíos antes de proceder ( la descripción permitimos que sea vacía)
        if (prov_seleccionada != null && !prov_seleccionada.isEmpty() &&
                comida_seleccionada != null && !comida_seleccionada.isEmpty() &&
                servicio_seleccionada != null && !servicio_seleccionada.isEmpty()) {

            // Comprueba el estado de la conexión de red
            if (!Utils.isNetworkAvailable(this)) {
                Utils.showToast(Activity_chef_servicio.this, "No hay conexión a Internet");
                return;
            }
            //Actualiza los datos del usuario con los nuevos valores
            user.setUbicacion(prov_seleccionada);
            user.setComida(comida_seleccionada);
            user.setServicio(servicio_seleccionada);
            user.setDescripcion(descripcionInput);

            // call HTTP client para modificar los datos de usuario
            Call<String> call = mfastMethods.modificarUsuario(token, user);
            call.enqueue(new Callback<String>() { // Ejecutar la llamada de manera asíncrona
                /**
                 * Método invocado cuando se recibe una respuesta de la solicitud HTTP
                 * @param call     llamada que generó la respuesta
                 * @param response la respuesta recibida del servidor
                 */
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        modifySuccessful = true;
                        // String responseBody = response.body();
                        Utils.showToastSecond(Activity_chef_servicio.this,context, "Modificación correcta!");
                        Utils.gotoActivity(Activity_chef_servicio.this, Activity_chef_menu.class);
                    } else {
                        Utils.showToastSecond(Activity_chef_servicio.this, context,"Error al modificar usuario");
                    }
                }

                /**
                 * Método invocado cuando ocurre un error durante la ejecución de la llamada HTTP
                 * @param call la llamada que generó el error
                 * @param t    la excepción que ocurrió
                 */
                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    // Error en la llamada, muestra el mensaje de error y registra la excepción
                    t.printStackTrace();
                    Log.e(TAG, "Error en la llamada:" + t.getMessage());
                    Utils.showToastSecond(Activity_chef_servicio.this, context,"Error en la llamada: " + t.getMessage());
                }
            });
        }
    }

    /**
     * Método para hacer logout
     * Redirige al usuario a la pantalla de inicio
     * @param view La vista (Button) a la que se hizo clic.
     */
    public void logout(View view){
        Utils.gotoActivity(Activity_chef_servicio.this, MainActivity_inicio.class);
    }

    /**
     * Método para test
     * @return devuelve un booleano en función de si ha ido bien la modificación de datos
     */
    public boolean isModifySuccessful() {
        return modifySuccessful;
    }
    /**
     * Método para test
     * @return devuelve un booleano en función de si ha ido bien la muestra de contenido
     */
    public boolean isContentSuccessful() {
        return contentSuccessful;
    }
}
