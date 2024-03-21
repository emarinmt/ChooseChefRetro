package com.example.choosechef;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

// Interfaz que define los métodos para realizar llamadas a la API
public interface FastMethods {
    /* Método para realizar el inicio de sesión en la API
    * La anotación @GET indica que esta solicitud se realiza con el método GET HTTP
    * La anotación @Query se utiliza para agregar parámetros a la solicitud
    * En este caso, se pasan el nombre de usuario y la contraseña como parámetros de consulta
    */
    @GET("usuario/login/respuesta/{usuario}/{password}")
    Call<Boolean> login(@Path("usuario") String usuario, @Path("password") String password);
    @GET("usuario/mostrar/porusuario/{usuario}")
    Call<List<String>> recuperar_info(@Path("usuario") String usuario);
    //@POST("usuario/modificar/porusuario")
    //Call<ProfileResponse> login(@Query("phone") String phone);  // enviamos usuario y nos devuelve datos

    //@POST("usuario/modificar/")
    //Call<Boolean> login(@Query("phone") String phone);  // envio datos modificados, devuelvo boleano
}
