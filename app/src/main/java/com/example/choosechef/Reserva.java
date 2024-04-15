package com.example.choosechef;

import java.io.Serializable;

/**
 * Clase reserva
 * Gestiona el objeto Reserva
 */
public class Reserva implements Serializable {
    private int id;
    private String usuario_cliente;
    private String usuario_chef;
    private String fecha;
    private float valoracion;
    private String comentario;

    /**
     * Constructor vacío del objeto reserva
     */
    public Reserva() {
    }

    /**
     * Constructor del objeto reserva
     * @param id id de la reserva
     * @param usuario_chef usuario chef
     * @param usuario_cliente usuario cliente
     * @param fecha fecha de la reserva
     * @param valoracion valoración del usuario
     * @param comentario comentario (reseña)
     */
    public Reserva(int id, String usuario_chef, String usuario_cliente, String fecha, float valoracion, String comentario) {
        this.id = id;
        this.usuario_chef = usuario_chef;
        this.usuario_cliente = usuario_cliente;
        this.fecha = fecha;
        this.valoracion = valoracion;
        this.comentario = comentario;
    }
//Setters y getters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario_chef() {
        return usuario_chef;
    }

    public void setUsuario_chef(String usuario_chef) {
        this.usuario_chef = usuario_chef;
    }

    public String getUsuario_cliente() {
        return usuario_cliente;
    }

    public void setUsuario_cliente(String usuario_cliente) {
        this.usuario_cliente = usuario_cliente;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public float getValoracion() {
        return valoracion;
    }

    public void setValoracion(float valoracion) {
        this.valoracion = valoracion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

}
