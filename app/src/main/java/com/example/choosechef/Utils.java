package com.example.choosechef;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Clase desarrollada por ELENA
 * Contiene métodos reutilizables por otras clases con funcionalidades comunes.
 */
public class Utils extends AppCompatActivity{
// QUITAR EL MÉTODO QUE PASA INFO ENTRE ACTIVIDADES SI NO LO USAMOS?
// COMPROBAR SI EL DE OCULTAR EL BOTÓN ESTÁ FUNCIONANDO BIEN O ES EL EMULADOR
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

    // QUITAR PARA TOKEN
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

    /**
     * Método desarrollado por ELENA
     * Compruebe el estado de la conexión de red.
     * @param context Contexto de la clase
     * @return true si hay conexión o false si no la hay
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Método desarrollado por ELENA
     * Oculta el teclado cuando el usuario toca el botón
     * @param context Contexto de la clase
     * @param view La vista (Button) que se hizo clic.
     */
    public static void hideKeyboard(Context context, View view) {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
