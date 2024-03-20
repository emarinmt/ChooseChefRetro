package com.example.choosechef;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

// Interfaz que define los métodos para realizar llamadas a la API
public interface FastMethods {
    /* Método para realizar el inicio de sesión en la API
    * La anotación @GET indica que esta solicitud se realiza con el método GET HTTP
    * La anotación @Query se utiliza para agregar parámetros a la solicitud
    * En este caso, se pasan el nombre de usuario y la contraseña como parámetros de consulta
    */
    @GET("usuario/login/respuesta")
    Call<Boolean> login(@Query("usuario") String usuario, @Query("password") String password);
    //Call<Boolean> login(String nombre,String password);
    //@POST("usuario/modificar/")
    //Call<ProfileResponse> login(@Query("phone") String phone);  // enviamos usuario y nos devuelve datos

    //@POST("usuario/modificar/")
    //Call<Boolean> login(@Query("phone") String phone);  // envio datos modificados, devuelvo boleano
}
