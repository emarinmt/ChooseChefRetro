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
 * Clase adaptador chef.
 * Para gestionar el recycler view de chefs (pantalla contenido)
 */
public class Adapter_chef_contenido extends RecyclerView.Adapter<Adapter_chef_contenido.ViewHolder> {

    private final LayoutInflater layoutInflater;
    private final List<User> data;

    /**
     *Constructor de la clase Adapter_chef_contenido
     * @param context contexto de la aplicación
     * @param data la lista de datos de tipo user que se utilizará para poblar el adaptador
     */
    Adapter_chef_contenido(Context context, List<User> data){
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.custom_view_card_chefs_contenido,viewGroup,false);
        return new ViewHolder(view);
    }

    /**
     *Actualiza el ViewHolder proporcionado para representar el contenido del elemento en la posición dada en el conjunto de datos
     * @param viewHolder El ViewHolder que se debe actualizar para representar el contenido del elemento en la posición dada en el conjunto de datos.
     * @param i La posición del elemento dentro del conjunto de datos del adaptador.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        User user = data.get(i); //NUEVO
        // Establece los datos del chef en las vistas del ViewHolder
        viewHolder.nombre_chef.setText(user.getNombre());
        viewHolder.tipo_comida.setText(user.getComida());
        viewHolder.servicio.setText(user.getServicio());
        viewHolder.provincia.setText(user.getUbicacion());
        viewHolder.valoracion.setRating(user.getValoracion());
        viewHolder.valoracion.setEnabled(false);

        //falta imagen

        // Manjear que al clicar un chef se muestre este ampliado
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            /**
             * Redirige a la pantalla chef_ampliado cuando se clica encima de un chef, enviando los datos de ese chef a la siguiente pantalla.
             * @param view La vista (Button) a la que se hizo clic.
             */
            @Override
            public void onClick(View view) {
                // Obtener el chef actual que fue clicado
                User currentChef = data.get(viewHolder.getAdapterPosition());

                // Utilizar el método gotoActivityWithUser para abrir la DetailActivity y pasar el objeto User
                Utils.gotoActivityWithUser(viewHolder.itemView.getContext(), Activity_contenido_chef_ampliado.class, currentChef);
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
        TextView nombre_chef, tipo_comida, provincia, servicio;
        RatingBar valoracion;

        /**
         * Constructor de la clase ViewHolder
         * @param itemView la vista de elemento que se va a mantener en este ViewHolder
         */
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            nombre_chef = itemView.findViewById(R.id.nombre_chef);
            tipo_comida = itemView.findViewById(R.id.tipo_comida);
            servicio = itemView.findViewById(R.id.tipo_servicio);
            provincia = itemView.findViewById(R.id.provincia);
            valoracion = itemView.findViewById(R.id.ratingBar);
        }
    }
}
