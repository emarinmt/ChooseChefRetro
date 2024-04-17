package com.example.choosechef;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import android.app.Activity;

import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import java.util.Collection;
import java.util.Random;

public class UtilsTests {

    // Método para obtener la instancia de una actividad específica
    public static Activity getActivityInstance(Class<? extends Activity> activityClass) {
        final Activity[] currentActivity = new Activity[1];
        getInstrumentation().runOnMainSync(() -> {
            Collection<Activity> resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
            for (Activity activity : resumedActivities) {
                if (activityClass.isInstance(activity)) {
                    currentActivity[0] = activity;
                    break;
                }
            }
        });
        return currentActivity[0];
    }
    // Método para esperar a que se complete la operación asíncrona
    public static void espera(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Método para generar un nombre de usuario aleatorio
    public static String generateRandomUsername() {
        Random random = new Random();
        int randomNumber = random.nextInt(1000); // Genera un número aleatorio entre 0 y 999
        return "test" + randomNumber; // Devuelve un nombre de usuario único cada vez que se llama
    }
}
