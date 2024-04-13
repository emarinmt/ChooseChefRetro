package com.example.choosechef;

import java.util.Date;

public class Reserva {
    private int id;
    private String usuario_chef;
    private String usuario_client;
    private Date fecha;
    private float valoracion;
    private String comentario;

    public Reserva() {
    }

    public Reserva(int id, String usuario_chef, String usuario_client, Date fecha, float valoracion, String comentario) {
        this.id = id;
        this.usuario_chef = usuario_chef;
        this.usuario_client = usuario_client;
        this.fecha = fecha;
        this.valoracion = valoracion;
        this.comentario = comentario;
    }

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

    public String getUsuario_client() {
        return usuario_client;
    }

    public void setUsuario_client(String usuario_client) {
        this.usuario_client = usuario_client;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
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
