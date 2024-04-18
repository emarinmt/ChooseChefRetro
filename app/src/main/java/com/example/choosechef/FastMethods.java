package com.example.choosechef;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Interfaz FastMethods
 * Interfaz que define los métodos para realizar llamadas a la API
 */
public interface FastMethods {

    /**
     * Método desarrollado por ELENA
     * Para realizar el inicio de sesión en la API
     * La anotación @GET indica que esta solicitud se realiza con el método GET HTTP
     * La anotación @Path se utiliza para agregar parámetros a la solicitud
     * @param usuario nombre de usuario
     * @param password contrasena
     * @return token de sesión
     */
    @GET("usuario/login/token/{usuario}/{password}")
    Call<TokenResponse> login(@Path("usuario") String usuario, @Path("password") String password);

    /**
     * Método dessarrollado por EVA, modificado por ELENA para usar el token y recibir usuario
     * Para realizar una consulta al servidor y recuperar los datos de usuario
     * @return devuelve un usuario completo
     */
    @GET("usuario/perfil/")
    Call<User>recuperar_info(@Query("token") String token);

    /**
     * Método desarrollado por EVA, modificado por ELENA
     * Para realizar una modificación de los datos de usuario en la base de datos
     * @param user objeto con los datos del usuario
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


    /**
     * Método dessarrollado por ELENA, modificado por ELENA
     * Para realizar una consulta al servidor y recuperar los datos de los usuarios chefs
     * @return devuelve una lista con los usuarios que son chefs
     */
    @GET("chef/listar/")
    Call<List<User>>recuperar_chefs();

    /**
     * Método desarrollado por EVA,
     * Para realizar una consulta al servidor y recuperar las provincias donde hay chefs
     * Se utiliza para mostrar la lista de provincias disponibles en la búsqueda
     * @return devuelve una lista de provincias en las cuales hay chefs
     */
    @GET("provincias/conChef/")
    Call<List<String>>recuperar_provincias();

    /**
     * Método desarrollado por EVA,
     * Para realizar una consulta al servidor y recuperar todos los usuarios de la base de datos
     * @return devuelve una lista de todos los usuarios
     */
    @GET("admin/listar/")
    Call<List<User>>recuperar_todos_usuarios();

    /**
     * Método desarrollado por EVA,
     * Para realizar una consulta al servidor y modificar todos los datos de un usuario
     * @param usuario envía un usuario al cual le modificará los datos
     * @param user envía el objeto usuario con los datos modificados
     * @return devuelve un string si la modificación es correcta o no
     */
    @POST("admin/modificar/{usuario}")
    Call<String> modificarUsuario_admin(@Path("usuario") String usuario, @Body User user);

    /**
     * Método desarrollado por EVA,
     * Para borrar a un usuario del servidor
     * @param usuario usuario a borrar
     * @return devuelve un string si el borrado ha sido correcto o no
     */
    @DELETE("usuario/{usuario}")
    Call<String> borrar_usuario(@Path("usuario") String usuario);

    /**
     * Método desarrollado por Eva,
     * Para crear una reserva en el servidor
     * @param reserva objeto reserva a crear
     * @return devuelve un string si se ha creado correctamente o no
     */
    @POST("reserva/crear/")
    Call<String> crear_reserva(@Body Reserva reserva);

    /**
     * Método desarrollado por EVA,
     * Para listar las reservas de un usuario
     * @return devuelve una lista con las reservas del usuario
     */
    @GET("reserva/listar/{token}")
    Call<List<Reserva>>recuperar_reservas(@Path("token") String token);

    /**
     * Método desarrollado por EVA,
     * Para modificar los datos de una reserva
     * @param reserva objeto reserva con los datos a modificar
     * @return devuelve un string si la modificación se ha hecho correctamente o no.
     */
    @POST("reserva/modificar/{id}")
    Call<String> modificar_reserva(@Path("id") Integer id, @Body Reserva reserva);

}
