package com.example.choosechef;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * Clase adaptador reserva.
 * Para gestionar el recycler view de reservas (pantalla usuario cliente)
 */
public class Adapter_reserva_client extends RecyclerView.Adapter<Adapter_reserva_client.ViewHolder> {

    private final LayoutInflater layoutInflater;
    private final List<Reserva> data;

    /**
     *Constructor de la clase Adapter_reserva
     * @param context contexto de la aplicación
     * @param data la lista de datos de tipo user que se utilizará para poblar el adaptador
     */
    Adapter_reserva_client(Context context, List<Reserva> data){
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
    public Adapter_reserva_client.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.custom_view_card_reservas_client,viewGroup,false);
        return new Adapter_reserva_client.ViewHolder(view);
    }

    /**
     *Actualiza el ViewHolder proporcionado para representar el contenido del elemento en la posición dada en el conjunto de datos
     * @param viewHolder El ViewHolder que se debe actualizar para representar el contenido del elemento en la posición dada en el conjunto de datos.
     * @param i La posición del elemento dentro del conjunto de datos del adaptador.
     */
    @Override
    public void onBindViewHolder(@NonNull Adapter_reserva_client.ViewHolder viewHolder, int i) {
        Reserva reserva = data.get(i); //NUEVO
        // Establece los datos de la reserva en las vistas del ViewHolder

        viewHolder.nombre_chef.setText(reserva.getUsuario_chef());
        viewHolder.fecha_reserva.setText(reserva.getFecha());

        // Manjear que al clicar una reserva se muestre esta ampliado
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            /**
             * Redirige a la pantalla reserva_ampliado cuando se clica encima de una reserva, enviando los datos de esa reserva a la siguiente pantalla.
             * @param view La vista (Button) a la que se hizo clic.
             */
            @Override
            public void onClick(View view) {
                // Obtener el chef actual que fue clicado
                Reserva currentReserva = data.get(viewHolder.getAdapterPosition());

                // Utilizar el método gotoActivityWithReserva para abrir la DetailActivity y pasar el objeto Reserva
                Utils.gotoActivityWithReserva(viewHolder.itemView.getContext(), Activity_client_reserva_ampliado.class, currentReserva);
            }
        });
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
        TextView nombre_chef, fecha_reserva;
        /**
         * Constructor de la clase ViewHolder
         * @param itemView la vista de elemento que se va a mantener en este ViewHolder
         */
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            nombre_chef = itemView.findViewById(R.id.nombre_chef);
            fecha_reserva = itemView.findViewById(R.id.fecha_reserva);
        }
    }
}
