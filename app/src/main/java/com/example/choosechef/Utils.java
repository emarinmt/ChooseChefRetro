package com.example.choosechef;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;

/**
 * Clase utilidades
 * Contiene métodos reutilizables por otras clases con funcionalidades comunes.
 */
//DEJO COMENTARIO ASÍ ENCIMA DE MÉTODOS QUE NO FUNCIONAN PARA BORRARLOS AL ACABAR
public class Utils extends AppCompatActivity{

private static boolean isNetworkAvailable = true; // Estado predeterminado de la conexión

    /**
     * Método desarrollado por ELENA
     * Abre una actividad y finaliza la actividad padre.
     * ACTIVAR SECOND ACTIVITY SIN FEEDBACK
     * @param parent  Actividad padre.
     * @param destination Actividad de destino.
     */
    public static void gotoActivity(AppCompatActivity parent, Class destination){
        Intent i = new Intent(parent, destination);
        parent.startActivity(i);
        parent.finish();
    }

    /**
     * Método desarrollado por EVA
     * Abre una actividad enviando un string
     * @param context contexto des de donde se llama
     * @param destination actividad de destino
     * @param mens mensaje a enviar
     */
    public static void gotoActivityWithString(Context context, Class destination, String mens) {
        Intent i = new Intent(context, destination);
        i.putExtra("string", mens);
        context.startActivity(i);
    }

    /**
     * Método desarrollado por ELENA
     * Abre una actividad en adapter y envia usuario
     * ACTIVAR SECOND ACTIVITY Y PASAR USER SIN FEEDBACK (SOLO PARA ADAPTER)
     * @param context  Context desde donde se llama
     * @param user  Usuario
     * @param destination Actividad de destino.
     */
    public static void gotoActivityWithUser(Context context, Class destination, User user) {
        Intent i = new Intent(context, destination);
        i.putExtra("user", user);
        context.startActivity(i);
    }

    /**
     * Método desarrollado por EVA
     * Abre una actividad en adapter y envia reserva
     * Activa second activity y pasa RESERVA sin feedback (solo para adapter
     * @param context contexto des de donde se llama
     * @param destination actividad de destino
     * @param reserva objeto reserva
     */
    public static void gotoActivityWithReserva(Context context, Class destination, Reserva reserva) {
        Intent i = new Intent(context, destination);
        i.putExtra("reserva", reserva);
        context.startActivity(i);
    }

    /**
     * Método desarrollado por ELENA
     * Abre una actividad y envia información
     * ACTIVAR SECOND ACTIVITY Y PASAR INFO SIN FEEDBACK
     * @param parent  Actividad padre.
     * @param destination Actividad de destino.
     * @param extras info a enviar
     */
    public static void gotoActivityWithExtras(AppCompatActivity parent, Class destination, Bundle extras) {
        Intent i = new Intent(parent, destination);
        i.putExtras(extras);
        parent.startActivity(i);
    }
//BORRAR SI NO SE UTILIZA
    /**
     * Método desarrollado por ELENA
     * Abre una actividad y espera info
     * ACTIVAR SECOND ACTIVITY CON FEEDBACK
     * @param parent  Actividad padre.
     * @param destination Actividad de destino.
     */
    public static void gotoActivityWithResult(AppCompatActivity parent, Class destination, int requestcode){
        Intent i = new Intent(parent, destination);
        parent.startActivityForResult(i, requestcode);
    }
//BORRAR SI NO SE UTILIZA
    // Enviar datos de vuelta a la actividad anterior
    public static void sendActivityResult(AppCompatActivity parent, Bundle extras, int resultCode) {
        Intent i = new Intent();
        if (extras != null) {
            i.putExtras(extras);
        }
        parent.setResult(resultCode, i);
        parent.finish();
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
     * Muestra un toast en un hilo secundario utilizando la actividad proporcionada.
     * @param activity Actividad en la que se mostrará el toast.
     * @param context Contexto para el toast.
     * @param message Mensaje del toast.
     */
    public static void showToastSecond(Activity activity, Context context, String message) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * Método desarrollado por ELENA
     * Compruebe el estado de la conexión de red.
     * @param context Contexto de la clase
     * @return true si hay conexión o false si no la hay
     */
    public static boolean isNetworkAvailable(Context context) {
        if (isNetworkAvailable) {
            // Verificar la disponibilidad de la red utilizando ConnectivityManager
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connMgr != null) {
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();
            }
        }
        return false;
    }

    /**
     * Método desarrollado por ELENA
     * Para establecer el estado de conexión de red
     * @param available booleano para establecer el estado de conexión
     */
    public static void setNetworkAvailable(boolean available) {
        // Método para establecer el estado de la conexión de red
        isNetworkAvailable = available;
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

    /**
     * Método desarrollado por EVA
     * Comprueba si la fecha pasada por parámetro es anterior o igual a hoy
     * @param fecha_reserva fecha de la reserva
     * @return devuelve un boleano true si la fecha pasada por parámetro es anterior o igual a hoy, en caso contrario false
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean esFechaHoyPosterior(String fecha_reserva) {
        if (fecha_reserva == null) {
            return false;
        } else {
            //Obtener la fecha de hoy
            LocalDate hoy = LocalDate.now();

            //Convertir la fecha de la reserva a LocalDate
            LocalDate fechaReserva = LocalDate.parse(fecha_reserva);

            //Comparar las fechas
            return fechaReserva.isBefore(hoy) || fechaReserva.isEqual(hoy);
        }
    }

}
