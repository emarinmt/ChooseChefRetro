package com.example.choosechef;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

/**
 * Clase búsqueda.
 * Gestiona los filtros de búsqueda y devuelve a la clase contenido el resultado
 */
public class Activity_busqueda extends AppCompatActivity {
    private final String TAG = Activity_busqueda.class.getSimpleName();
    private boolean provinciaSuccessful = false; // Variable para rastrear el estado de la muestra de provincias
    // Variables para los spinners y selecciones
    Spinner spinner_prov;
    Spinner spinner_comida;
    Spinner spinner_servicio;
    String prov_seleccionada;
    String comida_seleccionada;
    String servicio_seleccionada;
    // Lista para almacenar las provincias
    ArrayList<String> provinciasList = new ArrayList<>();
    // Variables para conectar con la API
    FastMethods mfastMethods;
    Retrofit retro;
    /**
     * Método onCreate para la configuración incial de la actividad
     * @param savedInstanceState estado de la instancia guardada, un objeto Bundle que contiene el estado previamente guardado de la actividad
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establece el diseño de la actividad.
        setContentView(R.layout.activity_busqueda);

        retro=FastClient.getClient();
        mfastMethods = retro.create(FastMethods.class);
        // Inicializar los spinners
        spinner_prov = findViewById(R.id.spinner_provincias);
        spinner_comida = findViewById(R.id.spinner_tipo_comida);
        spinner_servicio = findViewById(R.id.spinner_servicios);

        // Recuperar las provincias del servidor
        recuperarProvincias();

    }
    public void recuperarProvincias(){
        // Obtener el contexto de la actividad (this)
        Context context = this;
        // Compruebe el estado de la conexión de red
        if (!Utils.isNetworkAvailable(this)) {
            Utils.showToastSecond(Activity_busqueda.this, context, "No hay conexión a Internet");
            provinciaSuccessful = false;
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
                    provinciaSuccessful = true;
                    provinciasList.clear(); // Limpiar la lista actual
                    provinciasList.addAll(response.body()); // Agregar todas las provincias recuperadas

                    // Configurar adaptador para el spinner de provincias
                    ArrayAdapter<String> adapterProv = new ArrayAdapter<>(Activity_busqueda.this, android.R.layout.simple_spinner_item, provinciasList);
                    adapterProv.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_prov.setAdapter(adapterProv);

                } else {
                    Utils.showToastSecond(Activity_busqueda.this, context,"No se encontraron provincias con chefs");
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
                provinciaSuccessful = false;
                t.printStackTrace();
                Log.e(TAG, "Error en la llamada:" + t.getMessage());
                Utils.showToastSecond(Activity_busqueda.this, context,"Error en la llamada: " + t.getMessage());
            }
        });
    }

    /**
     *Método de ciclo de vida de una actividad que se llama cuando la actividad se vuele a mostrar al ausuario y se hace nuevamente interactiva después de haber estaod en pausa o detenida.
     * Está sobreescrito para ejecutar ciertas acciones cada vez que la actividad se reanuda.
     */
    @Override
    protected void onResume() {
        super.onResume();
        configurarSpinnerComida();
        configurarSpinnerServicio();
    }

    /**
     * Método para configurar las opciones disponibles en el spinner de comida
     */
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

    /**
     * Método para configurar las opciones disponibles en el spinner de servicio
     */
    private void configurarSpinnerServicio() {
        ArrayList<String> servicioList = new ArrayList<>();
        servicioList.add("Cátering a domicilio");
        servicioList.add("Chef a domicilio");
        servicioList.add("Cátering para evento");

        ArrayAdapter<String> adapterServicio = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, servicioList);
        adapterServicio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_servicio.setAdapter(adapterServicio);
    }

    /**
     * Método para confirmar los valores introducidos como filtros para la búsqueda y enviar los datos a la pantalla de contenido
     * @param view La vista (Button) a la que se hizo clic.
     */
    public void confirmarBusqueda(View view) {
        // Obtener los valores seleccionados de los spinners
        prov_seleccionada = (String) spinner_prov.getSelectedItem();
        comida_seleccionada = (String) spinner_comida.getSelectedItem();
        servicio_seleccionada = (String) spinner_servicio.getSelectedItem();

        // Validar que los valores no sean nulos o vacíos antes de proceder
        if (prov_seleccionada != null && !prov_seleccionada.isEmpty() &&
                comida_seleccionada != null && !comida_seleccionada.isEmpty() &&
                servicio_seleccionada != null && !servicio_seleccionada.isEmpty()) {

            // Crear un intent para enviar los datos de vuelta a Activity_contenido
            Intent intent = new Intent();
            intent.putExtra("provincia", prov_seleccionada);
            intent.putExtra("comida", comida_seleccionada);
            intent.putExtra("servicio", servicio_seleccionada);

            // Establecer el resultado como OK y adjuntar el intent con los datos
            setResult(Activity.RESULT_OK, intent);
            finish(); // Finalizar la actividad actual y volver a Activity_contenido
        } else {
            // Mostrar un mensaje de error si alguno de los valores seleccionados es nulo o vacío
            Utils.showToast(Activity_busqueda.this, "Por favor, seleccione una opción válida para cada campo.");
        }
    }

    /**
     * Método para hacer logout
     * Redirige al usuario a la pantalla de inicio
     * @param view La vista (Button) a la que se hizo clic.
     */
    public void logout(View view){
        Utils.gotoActivity(Activity_busqueda.this, MainActivity_inicio.class);
    }

    /**
     * Método para test
     * @return devuelve un booleano en función de si ha ido bien la obtención de la lista de provincias
     */
    public boolean isProvSuccessful() {
        return provinciaSuccessful;
    }

}
