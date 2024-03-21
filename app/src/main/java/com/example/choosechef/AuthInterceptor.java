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

    /**
     * Método para interceptar y modificar la solicitud saliente
     * @param chain cadena que contiene la solicitud original y permite la ejecución de la cadena de interceptores
     * @return el objeto Response que representa la respuesta recibida o el resultado final de la ejecución
     * @throws IOException se lanza si ocurre un error de entrada o salida durante el proceso de intercepción de la solicitud
     */
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request(); // Obtener la solicitud original
        // Obtenemos el token de acceso de ... (SharedPreferences o de donde lo almacenes) *PENDIENTE
        // Por ahora, se utiliza un token de acceso estático *PENDIENTE
        String accessToken = "your_access_token"; // *PENDIENTE

        // Crear una nueva solicitud con el encabezado de autorización
        Request newRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .build();

        // Procesar y devolver la nueva solicitud
        return chain.proceed(newRequest);
    }
}
