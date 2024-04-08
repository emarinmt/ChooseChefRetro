package com.example.choosechef;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class Activity_busqueda extends AppCompatActivity {
    private final String TAG = Activity_busqueda.class.getSimpleName();
    //variables campos de entrada
    Spinner spinner_prov;
    Spinner spinner_comida;
    Spinner spinner_servicio;
    ArrayList<String> provinciasList = new ArrayList<>(); // Lista para almacenar las provincias
    // Variables para conectar con la API
    FastMethods mfastMethods;
    Retrofit retro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establece el diseño de la actividad.
        setContentView(R.layout.activity_busqueda);

        retro=FastClient.getClient();
        mfastMethods = retro.create(FastMethods.class);

        recuperarProvincias();
        seleccionarProvincia();
        seleccionarComida();
        seleccionarServicio();
    }
    public void recuperarProvincias(){
        // Compruebe el estado de la conexión de red
        if (!Utils.isNetworkAvailable(this)) {
            Utils.showToast(Activity_busqueda.this, "No hay conexión a Internet");
            return;
        }
        // Call HTTP client para recuperar la información del usuario
        Call<List<String>> call = mfastMethods.recuperar_provincias();
        call.enqueue(new Callback<List<String>>() { // Ejecutar la llamada de manera asíncrona
            /**
             * Método invocado cuando se recibe una respuesta de la solicitud HTTP
             * @param call llamada que generó la respuesta
             * @param response la respuesta recibida del servidor
             */
            public void onResponse(@NonNull Call<List<String>> call, @NonNull Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    provinciasList.clear(); // Limpiar la lista actual
                    provinciasList.addAll(response.body()); // Agregar todas las provincias recuperadas
                    Log.e(TAG, provinciasList.toString());
                    // Notificar al adaptador que los datos han cambiado
                    //adapter1.notifyDataSetChanged();
                    //seleccionarProvincia();
                } else {
                    Utils.showToast(Activity_busqueda.this, "No se encontraron provincias con chefs");
                }
            }
            /**
             *Método invocado cuando ocurre un error durante la ejecución de la llamada HTTP
             * @param call la llamada que generó el error
             * @param t la excepción que ocurrió
             */
            @Override
            public void onFailure(@NonNull Call<List<String>> call, @NonNull Throwable t) {
                // Error en la llamada, muestra el mensaje de error y registra la excepción
                t.printStackTrace();
                Log.e(TAG, "Error en la llamada:" + t.getMessage());
                Utils.showToast(Activity_busqueda.this, "Error en la llamada: " + t.getMessage());
            }
        });
    }
    private void seleccionarProvincia() {
        spinner_prov = findViewById(R.id.spinner_provincias);
        //Aquí hay que decidir si hace falta hacer una clase de provincias, o directamente llamar al servidor y recuperar la lista de provincias
        ArrayList<String> provincias = provinciasList;
        /*
        provincias.add("Barcelona");
        provincias.add("LLeida");
        provincias.add("Madrid");
        provincias.add("Mallorca");
         */

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, provincias);
        spinner_prov.setAdapter(adapter1);

        spinner_prov.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String prov_seleccionada = (String) spinner_prov.getSelectedItem();
                String mensaje_prov = "Ha seleccionado la provincia " + prov_seleccionada;
                Utils.showToast(Activity_busqueda.this, mensaje_prov);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void seleccionarComida() {
        spinner_comida = findViewById(R.id.spinner_tipo_comida);
        //Aquí hay que decidir si hace falta hacer una clase de provincias, o directamente llamar al servidor y recuperar la lista de provincias
        ArrayList<String> comida = new ArrayList<>();
        comida.add("Italiana");
        comida.add("Tailandesa");
        comida.add("Japonesa");
        comida.add("Mediterranea");
        comida.add("Asiática");
        comida.add("Africana");
        comida.add("Coreana");

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, comida);
        spinner_comida.setAdapter(adapter2);

        spinner_comida.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String comida_seleccionada = (String) spinner_comida.getSelectedItem();
                String mensaje_comida = "Ha seleccionado el tipo de comida " + comida_seleccionada;
                Utils.showToast(Activity_busqueda.this, mensaje_comida);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void seleccionarServicio() {
        spinner_servicio = findViewById(R.id.spinner_servicios);
        //Aquí hay que decidir si hace falta hacer una clase de provincias, o directamente llamar al servidor y recuperar la lista de provincias
        ArrayList<String> servicio = new ArrayList<>();
        servicio.add("Cátering a domicilio");
        servicio.add("Chef a domicilio");
        servicio.add("Cátering para evento");

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, servicio);
        spinner_servicio.setAdapter(adapter3);

        spinner_comida.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String servicio_seleccionada = (String) spinner_comida.getSelectedItem();
                String mensaje_servicio = "Ha seleccionado el servicio " + servicio_seleccionada;
                Utils.showToast(Activity_busqueda.this, mensaje_servicio);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void confirmarBusqueda(View view) {
        Utils.showToast(Activity_busqueda.this, "Busqueda correcta!");
        Utils.gotoActivity(Activity_busqueda.this, Activity_contenido.class);

    }
       /* //Spinner spinner_prov = findViewById(R.id.lista_provincias);
        //List<String> spinnerProvincias = List.of("Barcelona", "Tarragona","Lleida", "Madrid", "Mallorca");
        //ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerProvincias);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.provincias_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_prov.setAdapter(adapter);

        //establece la posición del adapter seleccionada. Por omisión es la primera (0).
        spinner_prov.setSelection(3);
        //obtiene la posición del adapter seleccionada
        int posSelected = spinner_prov.getSelectedItemPosition();

        */



}
