package com.example.choosechef;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Clase desarrollada por ELENA
 * Agrega un encabezado de autorización a las solicitudes HTTP salientes
 */
public class AuthInterceptor implements Interceptor {
    private String token;

    // Constructor que recibe el token como parámetro
    public AuthInterceptor(String token) {
        this.token = token;
    }


    /**
     * Método para interceptar y modificar la solicitud saliente
     * @param chain cadena que contiene la solicitud original y permite la ejecución de la cadena de interceptores
     * @return el objeto Response que representa la respuesta recibida o el resultado final de la ejecución
     * @throws IOException se lanza si ocurre un error de entrada o salida durante el proceso de intercepción de la solicitud
     */
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request(); // Obtener la solicitud original

        // Crear una nueva solicitud y le añade el token de acceso al encabezado
        Request newRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + token)
                .build();

        // Procesar y devolver la nueva solicitud
        return chain.proceed(newRequest);
    }
}
