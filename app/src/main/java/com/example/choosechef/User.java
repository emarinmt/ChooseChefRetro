package com.example.choosechef;
/**
 * Clase desarrollada por ELENA
 * para gestionar la información del usuario y unificar las respuestas del servidor y los datos de envios
 */
public class User {
    private int id;
    private String usuario;
    private String nombre;
    private String password;
    private String descripcion;
    private String ubicacion;
    private String email;
    private String telefono;
    private String tipo;

    public User() {
    }
    public User(int id, String usuario, String nombre, String password, String descripcion, String ubicacion, String email, String telefono, String tipo) {
        this.id = id;
        this.usuario = usuario;
        this.nombre = nombre;
        this.password = password;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.email = email;
        this.telefono = telefono;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    // Setter para el campo id que no es público
    void setId(int id) {
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

}
