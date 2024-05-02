package com.example.choosechef;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.CertificatePinner;
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

    /**
     * Método para establecer el contexto, llamado des de tu aplicación al inicializar
     * @param context contexto
     */
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

            // Agregar la validación del certificado del servidor
            CertificatePinner certificatePinner = new CertificatePinner.Builder()
                    .add("choose-chef.vercel.app", "sha256/Ewk2q8vq2vIcbk8vwG5Vh3OTClEtYGBNnSl78OqYmQA")
                    .build();
            httpClient.certificatePinner(certificatePinner);

            // Configurar OkHttpClient para que use el certificado
            httpClient.sslSocketFactory(getSSLConfig(mContext).getSocketFactory());

            // Construcción de la instancia de Retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient.build()) // Asignación del OkHttpClient configurado
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    // Método para obtener el SSLContext con el certificado
    private static SSLContext getSSLConfig(Context context) {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream cert = context.getResources().openRawResource(R.raw.vercel_cer);
            Certificate ca = cf.generateCertificate(cert);
            cert.close();

            KeyStore keyStore = KeyStore.getInstance("BKS");
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

            return sslContext;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}