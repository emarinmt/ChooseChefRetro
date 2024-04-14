package com.example.choosechef;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

    /**
     * Método para comprobar si la fecha de la reserva es posterior o igual a hoy
     * @return devuelve un booleano en función de si se cumple o no la condición
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean esFechaHoyPosterior() {
        if (fecha == null) {
            return false;
        } else {
            //Obtener la fecha de hoy
            LocalDate hoy = LocalDate.now();
            DateTimeFormatter isoFecha = DateTimeFormatter.ISO_LOCAL_DATE;
            LocalDate fechaHoy = LocalDate.parse(hoy.format(isoFecha));

            //DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
            LocalDate fechaReserva = LocalDate.parse(String.format(String.valueOf(isoFecha)));


        //Comparar las fechas*/
            return !fechaReserva.isBefore(fechaHoy); // Si la fecha de reserva es hoy o posterior, devuelve true
        }
    }

}
