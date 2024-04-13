package com.example.choosechef;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
    public String servicio_seleccionado;
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

        //recuperarDatos(); //NO SE SI RECUPERAR Y QUE MUESTRE EN LOS SPINNER ( ES FOLLON) O DIRECTAMENTE QUE ELIJA Y SE CAMBIARÁ

        prov_seleccionada = seleccionarProvincia();
        comida_seleccionada = seleccionarComida();
        servicio_seleccionado = seleccionarServicio();
        descripcionInput = descripcion.getText().toString();
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

                        //EJEMPLO DE IA.. MAÑANA LO REVISO. NO SE SI MOSTRAR EL QUE TIENE SELECCIONADO O SERA MUCHO FOLLON.. SI NO DIRECTAMENTE Q LO CAMBIE POR EL QUE ELIJA Y YA, TENGA O NO SERVICIO
                        /*
                        Spinner miSpinner = findViewById(R.id.mi_spinner);
                        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.opciones, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        miSpinner.setAdapter(adapter);



                        // Seleccionar el segundo elemento de la lista (posición 1)
                        miSpinner.setSelection(1);

                         */

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

    private String seleccionarProvincia() {
        //Rellenar array de posibles provincias
        ArrayList<String> provincias = new ArrayList<>();
        provincias.add("Barcelona");
        provincias.add("LLeida");
        provincias.add("Madrid");
        provincias.add("Mallorca");
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, provincias);
        spinner_prov.setAdapter(adapter1);

        spinner_prov.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                prov_seleccionada = (String) spinner_prov.getSelectedItem();
                String mensaje_prov = "Ha seleccionado la provincia " + prov_seleccionada;
                Utils.showToast(Activity_chef.this, mensaje_prov);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return prov_seleccionada;
    }
    private String seleccionarComida() {
        //Rellenar array de comidas posibles
        ArrayList<String> comida = new ArrayList<>();
        comida.add("Italiana");
        comida.add("Tailandesa");
        comida.add("Japonesa");
        comida.add("Mediterranea");

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, comida);
        spinner_comida.setAdapter(adapter2);

        spinner_comida.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                comida_seleccionada = (String) spinner_comida.getSelectedItem();
                String mensaje_comida = "Ha seleccionado el tipo de comida " + comida_seleccionada;
                Utils.showToast(Activity_chef.this, mensaje_comida);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return comida_seleccionada;
    }
    private String seleccionarServicio() {
        //Rellenar array posibles servicios
        ArrayList<String> servicio = new ArrayList<>();
        servicio.add("Cátering a domicilio");
        servicio.add("Chef a domicilio");
        servicio.add("Cátering para evento");

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, servicio);
        spinner_servicio.setAdapter(adapter3);

        spinner_comida.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                servicio_seleccionado = (String) spinner_comida.getSelectedItem();
                String mensaje_servicio = "Ha seleccionado el servicio " + servicio_seleccionado;
                Utils.showToast(Activity_chef.this, mensaje_servicio);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return servicio_seleccionado;
    }
    public void confirmarServicio(View view) {
        // Comprueba el estado de la conexión de red
        if (!Utils.isNetworkAvailable(this)) {
            Utils.showToast(Activity_chef.this, "No hay conexión a Internet");
            return;
        }
        //Actualiza los datos del usuario con los nuevos valores
        user.setUbicacion(prov_seleccionada);
        user.setComida(comida_seleccionada);
        user.setServicio(servicio_seleccionado);
        user.setDescripcion(descripcionInput);

        // call HTTP client para modificar los datos de usuario
        Call<String> call = mfastMethods.modificarUsuario(token, user);
        call.enqueue(new Callback<String>() { // Ejecutar la llamada de manera asíncrona
            /**
             * Método invocado cuando se recibe una respuesta de la solicitud HTTP
             * @param call llamada que generó la respuesta
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
             * @param call la llamada que generó el error
             * @param t la excepción que ocurrió
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
    public void logout(View view){
        Utils.gotoActivity(Activity_chef.this, MainActivity_inicio.class);
    }
    public boolean isContentSuccessful() {
        return modifySuccessful;
    }
}
