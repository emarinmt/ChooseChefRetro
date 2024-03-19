package com.example.choosechef;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
// Agrega un encabezado de autorización a las solicitudes HTTP salientes
public class AuthInterceptor implements Interceptor {
    // Método para interceptar y modificar la solicitud saliente
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
