package com.example.choosechef;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Utils {
    /**
     * Desde el thread principal, muestra un toast con un mensaje concreto, tamaño y contexto
     * @param context toast context
     * @param message toast message
     */

    public static void showToast(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

    /**
     * Desde el thread secundario, muestra un toast con un mensaje concreto, tamaño y contexto
     * @param activity activity that will show the toast
     * @param context toast context
     * @param message toast message
     */
    public static void showToast(Activity activity, Context context, String message){
        activity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * opens an activity and finishes parent activity
     * @param parent  parent activity
     * @param destination destination activity
     */
    public static void gotoActivity(AppCompatActivity parent, Class destination){
        Intent i = new Intent(parent, destination);
        parent.startActivity(i);
        parent.finish();
    }


}
