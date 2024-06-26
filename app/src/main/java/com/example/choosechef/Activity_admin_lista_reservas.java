package com.example.choosechef;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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
 * Clase lista de reservas administrador
 * Muestra una lista de las reservas de todos los usuarios
 * Permite filtrar por año de la reserva o entrar a la reserva y modificarla o borrarla
 */
public class Activity_admin_lista_reservas extends AppCompatActivity {
    private boolean contentSuccessful = false; // Variable para rastrear el estado de la muestra del listado
    private final String TAG = Activity_admin_lista_reservas.class.getSimpleName();

    // Variables para mostrar las reservas
    RecyclerView recyclerView;
    Adapter_reserva_admin adapter;
    List<Reserva> reservasList = new ArrayList<>(); // Lista para almacenar las reservas
    List<Reserva> originalReservasList = new ArrayList<>(); // Lista para almacenar las reservas

    // Variables para conectar con la API
    FastMethods mfastMethods;
    Retrofit retro;
    String token;

    //variable filtro reservas
    private EditText fecha_filtro;

    /**
     * Método onCreate para la configuración incial de la actividad
     * @param savedInstanceState estado de la instancia guardada, un objeto Bundle que contiene el estado previamente guardado de la actividad
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establece el diseño de la actividad.
        setContentView(R.layout.activity_admin_lista_reservas);

        //inicializar variables
        fecha_filtro = findViewById(R.id.edt_fecha_filtro);

        retro=FastClient.getClient();
        mfastMethods = retro.create(FastMethods.class);

        // Obtener el token de SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MiPreferencia", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.rv_reservas_chef);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter_reserva_admin(this, reservasList);
        recyclerView.setAdapter(adapter);

        // Llamar al método recuperarDatos
        recuperarDatos();
    }

    /**
     * Método para recuperar datos del servidor
     * LLama al servidor y recupera la lista de reservas
     */
    public void recuperarDatos(){
        Context context = this; // Obtener el contexto de la actividad (this)
        // Compruebe el estado de la conexión de red
        if (!Utils.isNetworkAvailable(this)) {
            Utils.showToastSecond(Activity_admin_lista_reservas.this, context,"No hay conexión a Internet");
            contentSuccessful = false;
            return;
        }
        // Call HTTP client para recuperar las reservas de todos los usuarios
        Call<List<Reserva>> call = mfastMethods.recuperar_reservas_admin();
        call.enqueue(new Callback<List<Reserva>>() { // Ejecutar la llamada de manera asíncrona
            /**
             * Método invocado cuando se recibe una respuesta de la solicitud HTTP
             * @param call llamada que generó la respuesta
             * @param response la respuesta recibida del servidor
             */
            @SuppressLint("NotifyDataSetChanged")
            public void onResponse(@NonNull Call<List<Reserva>> call, @NonNull Response<List<Reserva>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    contentSuccessful = true;
                    reservasList.clear(); // Limpiar la lista actual
                    reservasList.addAll(response.body()); // Agregar todos los usuarios recuperados

                    //Hacer una copia de la lista original sin filtrar
                    originalReservasList.clear();
                    originalReservasList.addAll(reservasList);

                    // Notificar al adaptador que los datos han cambiado
                    adapter.notifyDataSetChanged();
                } else {
                    Utils.showToastSecond(Activity_admin_lista_reservas.this, context,"No se encontraron reservas");
                }
            }
            /**
             *Método invocado cuando ocurre un error durante la ejecución de la llamada HTTP
             * @param call la llamada que generó el error
             * @param t la excepción que ocurrió
             */
            @Override
            public void onFailure(@NonNull Call<List<Reserva>> call, @NonNull Throwable t) {
                // Error en la llamada, muestra el mensaje de error y registra la excepción
                contentSuccessful = false;
                t.printStackTrace();
                Log.e(TAG, "Error en la llamada:" + t.getMessage());
                Utils.showToastSecond(Activity_admin_lista_reservas.this, context,"Error en la llamada: " + t.getMessage());
            }
        });
    }

    /**
     * Método para hacer logout
     * Redirige al usuario a la pantalla de inicio
     * @param view La vista (Button) a la que se hizo clic.
     */
    public void logout(View view){
        Utils.gotoActivity(Activity_admin_lista_reservas.this, MainActivity_inicio.class);
    }
    /**
     * Método para retroceder de pantalla
     * Redirige al usuario a la pantalla anterior
     * @param view La vista (Button) a la que se hizo clic.
     */
    public void atras(View view){
        Utils.gotoActivity(Activity_admin_lista_reservas.this, Activity_admin_menu.class);
    }

    /**
     * Método para test
     * @return devuelve un boleano en función de si ha ido bien la muestra de reservas
     */
    public boolean isContentSuccessful() {
        return contentSuccessful;
    }

    /**
     * Método para filtrar las reservas por fecha
     * @param view La vista (Button) a la que se hizo clic.
     */
    public void buscar_reserva(View view){
        String fecha_str = String.valueOf(fecha_filtro.getText());
        if(fecha_str.isEmpty()){
            Utils.showToast(this, "Introduce un año para filtrar las reservas por año");
        }else{
            if(fecha_str.length() != 4){
                Utils.showToast(this, fecha_str);
                Utils.showToast(this, "Introduce un año en formato de 4 digitos");
            }else{
                int fecha = Integer.parseInt(fecha_str);
                buscar(fecha);
            }
        }
    }

    /**
     * Método para filtrar la lista de reservas localmente por fecha
     * @param year año a filtrar
     */
    @SuppressLint("NotifyDataSetChanged")
    public void buscar(int year) {

        // Filtrar reservasList localmente con la fecha de búsqueda
        List<Reserva> filteredList = filterReservas(originalReservasList, year);

        // Actualizar reservasList con la lista filtrada
        reservasList.clear();
        reservasList.addAll(filteredList);

        // Notificar al adaptador que los datos han cambiado en el hilo principal
        runOnUiThread(() -> {
            adapter.notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado
        });

        if(reservasList.isEmpty()){
            Utils.showToast(this, "No se encontraron reservas para esa fecha");
        }

        // Actualizar el estado de contentSuccessful basado en si se encontraron reservas después del filtro
        contentSuccessful = !reservasList.isEmpty(); // Si la lista filtrada no está vacía, entonces el contenido fue exitoso
    }

    /**
     * Método para filtrar la lista de reservas localmente por fecha.
     * @param reservasList Lista actual de reservas
     * @param year año a filtrar
     */
    public List<Reserva> filterReservas(List<Reserva> reservasList, int year) {
        List<Reserva> filteredList = new ArrayList<>();

        for (Reserva reserva : reservasList) {
            // Obtener la fecha de la reserva
            String reservaDate = reserva.getFecha();

            // Extraer el año de la fecha (asumiendo que la fecha está en formato "YYYY-MM-DD")
            String[] parts = reservaDate.split("-");
            if (parts.length >= 1) {
                int reservaYear = Integer.parseInt(parts[0]); // Año de la reserva

                // Verificar si la reserva es del año especificado (por ejemplo, 2023)
                if (reservaYear == year) {
                    filteredList.add(reserva);
                }
            }
        }
        return filteredList;
    }
}
