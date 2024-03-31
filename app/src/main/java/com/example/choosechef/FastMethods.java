package com.example.choosechef;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.POST;

/**
 * Interfaz desarrollada por ELENA, (EVA añade metodos)
 * Interfaz que define los métodos para realizar llamadas a la API
 */
public interface FastMethods {

    /**
     * Método desarrollado por ELENA
     * Para realizar el inicio de sesión en la API
     * La anotación @GET indica que esta solicitud se realiza con el método GET HTTP
     * La anotación @Path se utiliza para agregar parámetros a la solicitud
     * @param usuario nombre de usuario
     * @param password contraseña
     * @return token
     */

    @GET("usuario/login/token/{usuario}/{password}")
    Call<String> login(@Path("usuario") String usuario, @Path("password") String password);


    /**
     * Método dessarrollado por EVA
     * Para realizar una consulta al servidor y recuperar los datos de usuario
     * @param usuario nombre de usuario
     * @return devuelve una lista con el nombre del usuario, el teléfono y la dirección
     */

    @GET("usuario/mostrar/porusuario/{usuario}")
    Call<List<String>> recuperar_info(@Header("Authorization") String token, @Path("usuario") String usuario);

    /**
     * Método desarrollado por EVA
     * Para realizar una modificación de los datos de usuario en la base de datos
     * @param request objeto con los datos del usuario ( clase ModificarUsuarioRequest ) para modificar los datos en base de datos
     * @return devuelve un String con los datos modificados
     */

    @POST("usuario/modificar/porusuario")
    Call<String> modificarUsuario(@Body ModificarUsuarioRequest request);

    /**
     * Método desarrollado por ELENA
     * Para realizar el registro de usuario
     * La anotación @GET indica que esta solicitud se realiza con el método GET HTTP
     * La anotación @Path se utiliza para agregar parámetros a la solicitud
     * @param usuario nombre de usuario
     * @param password contraseña
     * @param tipo tipo de usuariuo
     * @return string si es correcto o no
     */
    @POST("usuario/modificar/porusuario")
    Call<String> registrarUsuario(@Path("usuario") String usuario, @Path("password") String password, @Path("tipo") String tipo);
}
