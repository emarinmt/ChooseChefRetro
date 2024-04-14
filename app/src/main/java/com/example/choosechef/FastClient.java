package com.example.choosechef;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Clase desarrollada por ELENA
 * Se encarga de crear y configurar una instancia de Retrofit.
 */

public class FastClient {
    private static Retrofit retrofit;
    // URL base de la API a la que realizaremos las solicitudes.
    private static final String BASE_URL = "https://choose-chef.vercel.app/";
    // Agrega una referencia al contexto para poder acceder a SharedPreferences
    private static Context mContext;

    // Método para establecer el contexto, llamado desde tu aplicación al inicializar
    public static void initialize(Context context) { //NUEVO
        mContext = context.getApplicationContext();
    }

    /**
     * Devuelve una instancia de Retrofit.
     * Si la instancia aún no se ha creado, crea una nueva utilizando `Retrofit.Builder`
     * con la URL base y un convertidor Gson para manejar los datos JSON.
     * Además, añade un interceptor de log y un interceptor de autenticación a OkHttpClient.
     * Esto permite la impresión de logs de las solicitudes y respuestas, así como la adición de un encabezado de autorización.
     */
    public static Retrofit getClient() {
        if (retrofit == null) {
            // Configuración de OkHttpClient con interceptor de log y interceptor de autenticación
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));

            // Obtener el token de SharedPreferences
            if (mContext != null) {
                // Accede a SharedPreferences aquí
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("MiPreferencia", Context.MODE_PRIVATE);
                String token = sharedPreferences.getString("token", "");
                httpClient.addInterceptor(new AuthInterceptor(token));
                // Resto del código
            } else {
                // Manejar el caso cuando mContext es null
                Log.e("FastClient", "Context is null");
            }
            //SharedPreferences sharedPreferences = mContext.getSharedPreferences("MiPreferencia", Context.MODE_PRIVATE);
            //String token = sharedPreferences.getString("token", "");

            //httpClient.addInterceptor(new AuthInterceptor(token)); // Agregar interceptor de autenticación
            // "token" es el token de acceso que hemos almacenado localmente)

            // Construcción de la instancia de Retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient.build()) // Asignación del OkHttpClient configurado
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
