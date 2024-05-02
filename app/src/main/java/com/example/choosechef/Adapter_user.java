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
 * Clase adaptador user.
 * Para gestionar el recycler view de usuarios (pantalla administrador)
 */
public class Adapter_user extends RecyclerView.Adapter<Adapter_user.ViewHolder> {

    private final LayoutInflater layoutInflater;
    private final List<User> data;

    /**
     *Constructor de la clase Adapter_user
     * @param context contexto de la aplicación
     * @param data la lista de datos de tipo user que se utilizará para poblar el adaptador
     */
    Adapter_user(Context context, List<User> data){
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
        View view = layoutInflater.inflate(R.layout.custom_view_card_users,viewGroup,false);
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
        //viewHolder.nombre_user.setText(user.getNombre());
        viewHolder.nombre_user.setText(user.getUsuario());
        // Manjear que al clicar un usuario se muestre este ampliado
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            /**
             * Redirige a la pantalla user_ampliado cuando se clica encima de un usuari, enviando los datos de ese usuario a la siguiente pantalla.
             * @param view La vista (Button) a la que se hizo clic.
             */
            @Override
            public void onClick(View view) {
                // Obtener el usuario actual que fue clicado
                User currentUser = data.get(viewHolder.getAdapterPosition());

                // Utilizar el método gotoActivityWithUser para abrir la DetailActivity y pasar el objeto User
                Utils.gotoActivityWithUser(viewHolder.itemView.getContext(), Activity_user_ampliado.class, currentUser);
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
        TextView nombre_user;
        /**
         * Constructor de la clase ViewHolder
         * @param itemView la vista de elemento que se va a mantener en este ViewHolder
         */
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            nombre_user = itemView.findViewById(R.id.nombre_usuario);
        }
    }

}
