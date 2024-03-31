package com.example.choosechef;

/**
 * Clase desarrollada por EVA
 * contiene la estructura de los datos del usuario en base de datos
 * Se utiliza para enviar y recibir los datos en una modificación de datos de usuario
 */
public class RegistroUsuarioRequest {
    private int id;
    private String usuario;
    private String password;
    private String tipo;
    public RegistroUsuarioRequest() {
    }
    public RegistroUsuarioRequest(int id, String usuario,  String password, String tipo) {
        this.id = id;
        this.usuario = usuario;
        this.password = password;
        this.tipo = tipo;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
