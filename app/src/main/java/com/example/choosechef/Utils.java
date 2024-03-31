package com.example.choosechef;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Clase desarrollada por ELENA
 * Clase de utilidades que proporciona métodos útiles para la navegación entre actividades y mostrar tostadas.
 */
public class Utils extends AppCompatActivity{

    /**
     * Método desarrollado por ELENA
     * Abre una actividad y finaliza la actividad padre.
     * @param parent  Actividad padre.
     * @param destination Actividad de destino.
     */
    public static void gotoActivity(AppCompatActivity parent, Class destination){
        Intent i = new Intent(parent, destination);
        parent.startActivity(i);
        parent.finish();
    }

    /**
     * Método añadido por EVA para abrir actividad y enviar mensaje, elige si se finaliza la actividad
     * @param parent Actividad padre
     * @param destination Actividad destino
     * @param msg1 String primer mensaje
     * @param inf1 String información primer mensaje
     * @param msg2 String segundo mensaje
     * @param inf2 String información segundo mensaje
     * @param finish Booleano para elegir si se finaliza actividad padre
     */
    public static void gotoActivityMessage(AppCompatActivity parent, Class destination, String msg1, String inf1, String msg2, String inf2, Boolean finish){
        Intent i = new Intent(parent, destination);
        i.putExtra(msg1,inf1);
        i.putExtra(msg2,inf2);
        parent.startActivity(i);
        if(finish) {
            parent.finish();
        }
    }

    /**
     * Método desarrollado por ELENA
     * Muestra un toast con un mensaje en el hilo principal.
     * @param context Contexto del toast.
     * @param message Mensaje del toast.
     */
    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /*
     * Método desarrollado por ELENA
     * Muestra un toast con un mensaje en el hilo secundario.
     * @param activity Actividad que mostrará el toast.
     * @param context Contexto del toast.
     * @param message Mensaje del toast.
    public static void showToast(Activity activity, Context context, String message){
        activity.runOnUiThread(() -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
    }
    */


}
