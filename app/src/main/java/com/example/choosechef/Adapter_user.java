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

    Adapter_user(Context context, List<User> data){
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.custom_view_card_users,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        User user = data.get(i); //NUEVO
        // Establece los datos del chef en las vistas del ViewHolder
        viewHolder.nombre_user.setText(user.getNombre());

        // Manjear que al clicar un usuario se muestre este ampliado
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener el usuario actual que fue clicado
                User currentUser = data.get(viewHolder.getAdapterPosition());

                // Utilizar el método gotoActivityWithUser para abrir la DetailActivity y pasar el objeto User
                Utils.gotoActivityWithUser(viewHolder.itemView.getContext(), Activity_user_ampliado.class, currentUser);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre_user;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            nombre_user = itemView.findViewById(R.id.nombre_usuario);
        }
    }
}
