package com.example.choosechef;

import java.util.List;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.POST;

/**
 * Interfaz desarrollada por ELENA, (EVA añade metodos)
 * Interfaz que define los métodos para realizar llamadas a la API
 */
public interface FastMethods {
    //PENDIENTE MODIFICAR MÉTODOS OBTENER USUARIO Y MODIFICAR PERFIL

    /**
     * Método desarrollado por ELENA
     * Para realizar el inicio de sesión en la API
     * La anotación @GET indica que esta solicitud se realiza con el método GET HTTP
     * La anotación @Path se utiliza para agregar parámetros a la solicitud
     * @param usuario nombre de usuario
     * @param password contraseña
     * @return token de sesión
     */
    @GET("usuario/login/token2/{usuario}/{password}")
    Call<TokenResponse> login(@Path("usuario") String usuario, @Path("password") String password);

    //COMPROBAR Y MODIFICAR RESPUESTA POR USER
    /**
     * Método dessarrollado por EVA, modificado por ELENA para usar el token y recibir usuario
     * Para realizar una consulta al servidor y recuperar los datos de usuario
     * @return devuelve una lista con el nombre del usuario, el teléfono y la dirección
     */
    @GET("usuario/perfil")
    Call<List<String>> recuperar_info(@Header("Authorization") String token);

    //PENDIENTE DE MODIFICACIÓN POR PARTE DE LAURA
    /**
     * Método desarrollado por EVA
     * Para realizar una modificación de los datos de usuario en la base de datos
     * @param user objeto con los datos del usuario ( clase ModificarUsuarioRequest ) para modificar los datos en base de datos
     * @return devuelve un String con los datos modificados
     */
    @POST("usuario/modificar/porusuario")
    Call<String> modificarUsuario(@Header("Authorization") String token, @Body User user);

    /**
     * Método desarrollado por ELENA
     * Para realizar el registro de usuario
     * @param user objeto con los datos del nuevo usuario
     * @return string si el registro es correcto o no
     */
    @POST("usuario/crear1")
    Call<String> crear(@Body User user);
}
