package com.example.choosechef;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Clase adaptador reserva chef
 * Para gestionar el recycler view de reservas (pantalla usuario cliente)
 */

public class Adapter_reseña_chef_amp extends RecyclerView.Adapter<Adapter_reseña_chef_amp.ViewHolder> {

    private final LayoutInflater layoutInflater;
    private final List<Reserva> data;
    /**
     *Constructor de la clase Adapter_reserva
     * @param context contexto de la aplicación
     * @param data la lista de datos de tipo user que se utilizará para poblar el adaptador
     */
    Adapter_reseña_chef_amp(Context context, List<Reserva> data){
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
    }
    /**
     *Crea y devuelve una nueva instancia de ViewHolder
     * @param viewGroup ViewGroup al cual se añadira la nueva vista después de que se haya enlazado a una posición del adaptador
     * @param i el tipo de vista de la nueva vista
     * @return devuelve una instancia de ViewHolder que contiene la vista inflada
     */
    @NonNull
    @Override
    public Adapter_reseña_chef_amp.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.custom_view_card_reservas_admin,viewGroup,false);
        return new Adapter_reseña_chef_amp.ViewHolder(view);
    }
    /**
     *Actualiza el ViewHolder proporcionado para representar el contenido del elemento en la posición dada en el conjunto de datos
     * @param viewHolder El ViewHolder que se debe actualizar para representar el contenido del elemento en la posición dada en el conjunto de datos.
     * @param i La posición del elemento dentro del conjunto de datos del adaptador.
     */
    @Override
    public void onBindViewHolder(@NonNull Adapter_reseña_chef_amp.ViewHolder viewHolder, int i) {
        Reserva reserva = data.get(i); //NUEVO

        // Establece los datos de la reserva en las vistas del ViewHolder
        viewHolder.nombre_usuario.setText(reserva.getUsuario_cliente());
        viewHolder.comentario_reseña.setText(reserva.getComentario());
        viewHolder.valoracion.setEnabled(false);
        viewHolder.valoracion.setRating(reserva.getValoracion());

    }
    /**
     * Método para contar el total de elementos en el conjunto de datos
     * @return devuelve el número total de elementos en el conjunto de datos
     */
    @Override
    public int getItemCount() {
        return data.size();
    }
    /**
     * Clase interna estática que representa una vista de elemento en el RecyclerView.
     * Contiene referencias a las vistas individuales que componen el elemento de la lista.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre_usuario, comentario_reseña;
        RatingBar valoracion;
        /**
         * Constructor de la clase ViewHolder
         * @param itemView la vista de elemento que se va a mantener en este ViewHolder
         */
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            nombre_usuario = itemView.findViewById(R.id.nombre_usuario);
            comentario_reseña = itemView.findViewById(R.id.comentario_reseña);
            valoracion = itemView.findViewById(R.id.rating_valoracion_reserva);
        }
    }
}
