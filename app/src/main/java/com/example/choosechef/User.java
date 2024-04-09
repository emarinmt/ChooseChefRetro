package com.example.choosechef;

import java.io.Serializable;

/**
 * Clase desarrollada por ELENA
 * para gestionar la información del usuario y unificar las respuestas del servidor y los datos de envios
 */
public class User implements Serializable {
// PROBAR SI FUNCIONA Setter para el campo id que no es público
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
    private int valoracion;

    public User() {
    }
    public User(int id, String usuario, String nombre, String password, String descripcion, String ubicacion, String email,
                String telefono, String tipo, String comida, String servicio, int valoracion) {
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

    public int getValoracion() {
        return valoracion;
    }
    public void setValoracion(int valoracion) {
        this.valoracion = valoracion;
    }

}
