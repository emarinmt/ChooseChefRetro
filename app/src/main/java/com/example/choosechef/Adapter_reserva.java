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

public class Adapter_reserva extends RecyclerView.Adapter<Adapter_reserva.ViewHolder> {

    private final LayoutInflater layoutInflater;
    private final List<Reserva> data;

    Adapter_reserva(Context context, List<Reserva> data){
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @NonNull
    @Override
    public Adapter_reserva.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.custom_view_card_reservas,viewGroup,false);
        return new Adapter_reserva.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_reserva.ViewHolder viewHolder, int i) {
        Reserva reserva = data.get(i); //NUEVO
        // Establece los datos de la reserva en las vistas del ViewHolder

        viewHolder.nombre_chef.setText(reserva.getUsuario_chef());
        viewHolder.fecha_reserva.setText(reserva.getFecha().toString());

        // Manjear que al clicar una reserva se muestre esta ampliado
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener el chef actual que fue clicado
                Reserva currentReserva = data.get(viewHolder.getAdapterPosition());

                // Utilizar el método gotoActivityWithReserva para abrir la DetailActivity y pasar el objeto Reserva
                Utils.gotoActivityWithReserva(viewHolder.itemView.getContext(), Activity_reserva_ampliado.class, currentReserva);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre_chef, fecha_reserva;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            nombre_chef = itemView.findViewById(R.id.nombre_chef);
            fecha_reserva = itemView.findViewById(R.id.fecha_reserva);
        }
    }
}
