package com.example.choosechef;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Clase de utilidades que proporciona métodos útiles para la navegación entre actividades y mostrar tostadas.
 */
public class Utils {

    /**
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
     * Muestra un toast con un mensaje en el hilo principal.
     * @param context Contexto del toast.
     * @param message Mensaje del toast.
     */
    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /*
    /**
     * Muestra un toast con un mensaje en el hilo secundario.
     * @param activity Actividad que mostrará el toast.
     * @param context Contexto del toast.
     * @param message Mensaje del toast.
    public static void showToast(Activity activity, Context context, String message){
        activity.runOnUiThread(() -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
    }
    */
}
