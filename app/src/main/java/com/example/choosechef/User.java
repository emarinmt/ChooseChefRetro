package com.example.choosechef;

import java.io.Serializable;

/**
 * Clase usuario
 * para gestionar la información del usuario y unificar las respuestas del servidor y los datos de envios
 */
public class User implements Serializable {
    private int id;
    private String usuario;
    private String nombre;
    private String password;
    private String descripcion;
    private String ubicacion;
    private String email;
    private String telefono;
    private String tipo;
    private String comida;
    private String servicio;
    private float valoracion;

    /**
     * Constructor vacío del objeto usuario
     */
    public User() {
    }

    /**
     * Constructor del objeto usuario
     * @param id id usuario
     * @param usuario usuario
     * @param nombre nombre usuario
     * @param password contrasena del usuario
     * @param descripcion descripcion del usuario
     * @param ubicacion ubicacion del usuario
     * @param email email del usuario
     * @param telefono telefono del usuario
     * @param tipo tipo de usuario
     * @param comida tipo de comida que ofrece un chef
     * @param servicio tipo de servicio que ofrece un chef
     * @param valoracion valoración a un chef obtenida por otros usuarios clientes
     */
    public User(int id, String usuario, String nombre, String password, String descripcion, String ubicacion, String email,
                String telefono, String tipo, String comida, String servicio, float valoracion) {
        this.id = id;
        this.usuario = usuario;
        this.nombre = nombre;
        this.password = password;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.email = email;
        this.telefono = telefono;
        this.tipo = tipo;
        this.comida = comida;
        this.servicio = servicio;
        this.valoracion = valoracion;
    }
//Setters y getters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getComida() {
        return comida;
    }

    public void setComida(String comida) {
        this.comida = comida;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public float getValoracion() {
        return valoracion;
    }
    public void setValoracion(float valoracion) {
        this.valoracion = valoracion;
    }

}
