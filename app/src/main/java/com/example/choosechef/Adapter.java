package com.example.choosechef;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private final LayoutInflater layoutInflater;
    private final List<User> data;
    //private final List<String> data;
    Adapter(Context context, List<User> data){
        //Adapter(Context context, List<String> data){
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.custom_view,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        User user = data.get(i); //NUEVO
        // Establece los datos del chef en las vistas del ViewHolder
        viewHolder.nombre_chef.setText(user.getNombre());
        //viewHolder.tipo_comida.setText(user.getTipoComida());
        viewHolder.tipo_comida.setText("italiana");//De momento para que no de error
        //viewHolder.provincia.setText(user.getProvincia());
        viewHolder.provincia.setText("Barcelona"); //De momento para que no de error

        /*
        String nombre = data.get(i);
        viewHolder.nombre_chef.setText(nombre);
        String comida = data.get(i);
        viewHolder.tipo_comida.setText(comida);
        String provinc = data.get(i);
        viewHolder.provincia.setText(provinc);
        //falta imagen
*/

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView nombre_chef, tipo_comida, provincia;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            nombre_chef = itemView.findViewById(R.id.nombre_chef);
            tipo_comida = itemView.findViewById(R.id.tipo_comida);
            provincia = itemView.findViewById(R.id.provincia);
        }

    }
}
