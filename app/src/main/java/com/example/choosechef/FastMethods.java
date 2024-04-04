package com.example.choosechef;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.POST;
import retrofit2.http.Query;

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
     * @return token de sesión
     */
    @GET("usuario/login/token/{usuario}/{password}")
    Call<TokenResponse> login(@Path("usuario") String usuario, @Path("password") String password);

    //COMPROBAR Y MODIFICAR RESPUESTA POR USER
    /**
     * Método dessarrollado por EVA, modificado por ELENA para usar el token y recibir usuario
     * Para realizar una consulta al servidor y recuperar los datos de usuario
     * @return devuelve una lista con el nombre del usuario, el teléfono y la dirección
     */
    @GET("usuario/perfil/")
    Call<User>recuperar_info(@Query("token") String token);

    /**
     * Método desarrollado por EVA
     * Para realizar una modificación de los datos de usuario en la base de datos
     * @param user objeto con los datos del usuario ( clase ModificarUsuarioRequest ) para modificar los datos en base de datos
     * @return devuelve un String con los datos modificados
     */
    @POST("usuario/modificar/")
    Call<String> modificarUsuario(@Query("token") String token, @Body User user);

    /**
     * Método desarrollado por ELENA
     * Para realizar el registro de usuario
     * @param user objeto con los datos del nuevo usuario
     * @return string si el registro es correcto o no
     */
    @POST("usuario/crear/")
    Call<String> crear(@Body User user);
}
