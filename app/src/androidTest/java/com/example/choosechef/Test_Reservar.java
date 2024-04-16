package com.example.choosechef;
import android.app.Activity;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;

import static androidx.test.espresso.action.ViewActions.typeText;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Collection;

import androidx.test.espresso.contrib.RecyclerViewActions;

/**
 * Para realizar los tests referentes a la modificación del perfil de usuario
 */
@RunWith(AndroidJUnit4.class)
public class Test_Reservar {

    @Rule
    public IntentsTestRule<Activity_login> activityRule = new IntentsTestRule<>(Activity_login.class);
    private Activity_reservar reservar;
    private Context context;

    @Before
    public void setUp() {
        FastClient.initialize(ApplicationProvider.getApplicationContext());
        context = ApplicationProvider.getApplicationContext();
        // Iniciar sesión como usuario de prueba
        onView(withId(R.id.edt_usuario_login)).perform(typeText("client"));
        onView(withId(R.id.edt_contra_login)).perform(typeText("client"), closeSoftKeyboard());
        onView(withId(R.id.ibtn_entrar_login)).perform(click());
        espera();
        // Hacer clic en el primer elemento de la lista (suponiendo que hay al menos un chef en la lista)
        onView(withId(R.id.rv_chefs))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // Botón reservar
        onView(withId(R.id.ibtn_reservar)).perform(click());
        // Verificar que la actividad reservar se inicia correctamente
        intended(hasComponent(Activity_reservar.class.getName()));
        // Obtener la instancia de Activity_reservar
        reservar = ((Activity_reservar) getActivityInstance(Activity_reservar.class));
    }

    // Reserva correcta
    @Test
    public void testReservaValid() {
        // Establecer el valor deseado de fechaStr
        String fechaDeseada = "2024-05-20";
        reservar.setFechaStr(fechaDeseada);
        espera();
        // Confirma la selección haciendo clic en el botón de confirmar reserva
        onView(withId(R.id.imageButton2)).perform(click());
        espera();
        // Verifica el estado de la reserva después de la acción
        // Verificar si la reserva fue exitosa
        assertTrue(reservar.isReservaSuccessful());
    }

    // Reserva incorrecta (fecha anterior a la actual)
    @Test
    public void testReservaInValidDate() {
        // Establecer el valor deseado de fechaStr
        String fechaDeseada = "2023-05-22";
        reservar.setFechaStr(fechaDeseada);
        espera();
        // Confirma la selección haciendo clic en el botón de confirmar reserva
        onView(withId(R.id.imageButton2)).perform(click());
        espera();
        // Verifica el estado de la reserva después de la acción
        // Verificar si la reserva fue exitosa
        assertFalse(reservar.isReservaSuccessful());
    }
    // Reserva incorrecta (fecha ya reservada)
    @Test
    public void testReservaInvalidOcupada() {
        // Establecer el valor deseado de fechaStr
        String fechaAocupar = "2024-05-20";
        reservar.setFechaStr(fechaAocupar);
        espera();
        // Confirma la selección haciendo clic en el botón de confirmar reserva
        onView(withId(R.id.imageButton2)).perform(click());
        espera();
        // Establecer el valor deseado de fechaStr
        String fechaDeseada = "2024-05-20";
        reservar.setFechaStr(fechaDeseada);
        espera();
        // Confirma la selección haciendo clic en el botón de confirmar reserva
        onView(withId(R.id.imageButton2)).perform(click());
        espera();
        // Verifica el estado de la reserva después de la acción
        // Verificar si la reserva fue exitosa
        assertTrue(reservar.isReservaSuccessful());
    }

    // Método para obtener la instancia de una actividad específica
    private Activity getActivityInstance(Class<? extends Activity> activityClass) {
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
    public void espera() {
        // Esperar un tiempo suficiente para que se complete la operación asíncrona
        try {
            Thread.sleep(25000); // Espera 5 segundos
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static long getDateInMillis(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTimeInMillis();
    }

}

