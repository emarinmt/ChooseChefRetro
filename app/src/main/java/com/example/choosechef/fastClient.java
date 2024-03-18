package com.example.choosechef;

import android.util.Log;

import androidx.annotation.NonNull;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//se encarga de crear y configurar una instancia de Retrofit.
public class fastClient {
    private static Retrofit retrofit;
    //es la URL base de la API a la que realizaremos las solicitudes.
    private static final String BASE_URL = "https://choose-chef.vercel.app/";

    /**
     * Devuelve una instancia de Retrofit. Si la instancia aún no se ha creado,
     *crea una nueva instancia de Retrofit utilizando `Retrofit.Builder` con la URL base y un convertidor Gson
     * para manejar los datos JSON.
     */
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }



}
