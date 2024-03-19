package com.example.choosechef;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// Interfaz que define los métodos para realizar llamadas a la API
public interface FastMethods {
    /* Método para realizar el inicio de sesión en la API
    * La anotación @GET indica que esta solicitud se realiza con el método GET HTTP
    * La anotación @Query se utiliza para agregar parámetros a la solicitud
    * En este caso, se pasan el nombre de usuario y la contraseña como parámetros de consulta
    */
    @GET("login")
    Call<Boolean> login(@Query("nombre") String nombre, @Query("password") String password);
}
