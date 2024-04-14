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

public class Activity_chef extends AppCompatActivity {
    /*
    La idea de esta clase es, cuando accede le aparece la informacion que hay en bases de datos del servicio que tiene, si lo toca y confirma se modifica.
    Si no tiene servicio saldra en blanco y podra introducirlo
    de momento copio el codigo de la clase busqueda, porque he aprovacho los spinner ya que va a ser lo mismo
     */
    private boolean modifySuccessful = false; // Variable para rastrear el estado de la modificación
    private final String TAG = Activity_chef.class.getSimpleName();
    Spinner spinner_prov;
    Spinner spinner_comida;
    Spinner spinner_servicio;
    public EditText descripcion;
    public String prov_seleccionada;
    public String comida_seleccionada;
    public String servicio_seleccionada;
    public String descripcionInput;

    FastMethods mfastMethods;
    Retrofit retro;
    private User user;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef);

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


        configurarSpinnerProvincia();
        configurarSpinnerComida();
        configurarSpinnerServicio();

        recuperarDatos();

    }

    private void configurarSpinnerProvincia() {
        ArrayList<String> provinciasList = new ArrayList<>();
        provinciasList.add("Barcelona");
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
    private void configurarSpinnerComida() {
        ArrayList<String> comidaList = new ArrayList<>();
        comidaList.add("Italiana");
        comidaList.add("Tailandesa");
        comidaList.add("Japonesa");
        comidaList.add("Mediterránea");
        comidaList.add("Asiática");
        comidaList.add("Africana");
        comidaList.add("Coreana");

        ArrayAdapter<String> adapterComida = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, comidaList);
        adapterComida.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_comida.setAdapter(adapterComida);
    }
    private void configurarSpinnerServicio() {
        ArrayList<String> servicioList = new ArrayList<>();
        servicioList.add("Cátering a domicilio");
        servicioList.add("Chef a domicilio");
        servicioList.add("Cátering para evento");

        ArrayAdapter<String> adapterServicio = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, servicioList);
        adapterServicio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_servicio.setAdapter(adapterServicio);
    }
    public void recuperarDatos(){
        // Compruebe el estado de la conexión de red
        if (!Utils.isNetworkAvailable(this)) {
            Utils.showToast(Activity_chef.this, "No hay conexión a Internet");
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

                        // Mostrar datos en pantalla
                        spinner_prov.setSelection(getIndex(spinner_prov, user.getUbicacion()));
                        spinner_comida.setSelection(getIndex(spinner_comida, user.getComida()));
                        spinner_servicio.setSelection(getIndex(spinner_servicio, user.getServicio()));
                        descripcion.setText(user.getDescripcion());

                    } else {
                        // Obtención de datos incorrecta, muestra un mensaje de error
                        Utils.showToast(Activity_chef.this, "Obtención de datos incorrecta");
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
                Utils.showToast(Activity_chef.this, "Error en la llamada: " + t.getMessage());
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
     * @param view el visor para hacer click al botón de confirmar
     */
    public void confirmarServicio(View view) {
        // Obtener los valores seleccionados de los spinners y la descripción introducida
        prov_seleccionada = (String) spinner_prov.getSelectedItem();
        comida_seleccionada = (String) spinner_comida.getSelectedItem();
        servicio_seleccionada = (String) spinner_servicio.getSelectedItem();
        descripcionInput = descripcion.getText().toString();

        // Validar que los valores no sean nulos o vacíos antes de proceder
        if (prov_seleccionada != null && !prov_seleccionada.isEmpty() &&
                comida_seleccionada != null && !comida_seleccionada.isEmpty() &&
                servicio_seleccionada != null && !servicio_seleccionada.isEmpty()) {

            // Comprueba el estado de la conexión de red
            if (!Utils.isNetworkAvailable(this)) {
                Utils.showToast(Activity_chef.this, "No hay conexión a Internet");
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
                 *
                 * @param call     llamada que generó la respuesta
                 * @param response la respuesta recibida del servidor
                 */
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        modifySuccessful = true;
                        // String responseBody = response.body();
                        Utils.showToast(Activity_chef.this, "Modificación correcta!");
                        Utils.gotoActivity(Activity_chef.this, Activity_contenido.class);
                    } else {
                        Utils.showToast(Activity_chef.this, "Error al modificar usuario");
                    }
                }

                /**
                 * Método invocado cuando ocurre un error durante la ejecución de la llamada HTTP
                 *
                 * @param call la llamada que generó el error
                 * @param t    la excepción que ocurrió
                 */
                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    // Error en la llamada, muestra el mensaje de error y registra la excepción
                    t.printStackTrace();
                    Log.e(TAG, "Error en la llamada:" + t.getMessage());
                    Utils.showToast(Activity_chef.this, "Error en la llamada: " + t.getMessage());
                }
            });
        }

    }
    public void logout(View view){
        Utils.gotoActivity(Activity_chef.this, MainActivity_inicio.class);
    }
    public boolean isContentSuccessful() {
        return modifySuccessful;
    }
}
