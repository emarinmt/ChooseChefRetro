package com.example.choosechef;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter_user extends RecyclerView.Adapter<Adapter_user.ViewHolder> {

    private final LayoutInflater layoutInflater;
    private final List<User> data;
    //private final List<String> data;

    Adapter_user(Context context, List<User> data){
        //Adapter(Context context, List<String> data){
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @NonNull
    @Override
    public Adapter_user.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.custom_view_card_users,viewGroup,false);
        return new Adapter_user.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_user.ViewHolder viewHolder, int i) {
        User user = data.get(i); //NUEVO
        // Establece los datos del chef en las vistas del ViewHolder
        viewHolder.nombre_user.setText(user.getNombre());

        /*
        String nombre = data.get(i);
        viewHolder.nombre_chef.setText(nombre);
        String comida = data.get(i);
        viewHolder.tipo_comida.setText(comida);
        String provinc = data.get(i);
        viewHolder.provincia.setText(provinc);
        //falta imagen
*/
        // Manjear que al clicar un usuario se muestre este ampliado
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener el chef actual que fue clicado
                User currentUser = data.get(viewHolder.getAdapterPosition());

                // Utilizar el método gotoActivityWithUser para abrir la DetailActivity y pasar el objeto User
                //CAMBIAR A CLASE USUARIO AMPLIADO CUANDO ESTE HECHA PARA MOSTRAR TODOS LOS DATOS DEL USUARIO Y Q EL ADMIN PUEDA VER, MODIFICAR O ELIMINAR
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
