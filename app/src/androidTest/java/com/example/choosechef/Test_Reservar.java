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
    private static int daysToAdd = 0; // Contador para los días a agregar
    @Before
    public void setUp() {
        FastClient.initialize(ApplicationProvider.getApplicationContext());
        context = ApplicationProvider.getApplicationContext();
        // Iniciar sesión como usuario de prueba client
        onView(withId(R.id.edt_usuario_login)).perform(typeText("client"));
        onView(withId(R.id.edt_contra_login)).perform(typeText("client"), closeSoftKeyboard());
        onView(withId(R.id.ibtn_entrar_login)).perform(click());
        espera();
        // PARA MÁS ADELANTE IMPLEMENTAR QUE CLIQUE EN UN CHEF DETERMINADO
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
    // De momomento, no hemos implementado un método para obtener las fechas ocupadas,
    // por lo tanto usamos una fecha muy posterior y vamos añadiendo días para asegurar que esté libre
    @Test
    public void testReservaValid() {
        // Obtener una fecha posterior libre
        String fechaDeseada = obtenerFechaDeseada();
        // Establecer la fecha deseada en Activity_reservar mediante el método setFechaStr()
        reservar.setFechaStr(fechaDeseada);
        espera();
        // Confirma la selección haciendo clic en el botón de confirmar reserva
        onView(withId(R.id.imageButton2)).perform(click());
        espera();
        // Verificar si la reserva fue exitosa
        assertTrue(reservar.isReservaSuccessful());
        daysToAdd++; // para evitar errores con futuros tests
    }

    // Reserva incorrecta (fecha anterior a la actual)
    @Test
    public void testReservaInValidDate() {
        // Establecemos una fecha anterior a la actual
        String fechaDeseada = "2023-05-23";
        // Establecer la fecha deseada en Activity_reservar mediante el método setFechaStr()
        reservar.setFechaStr(fechaDeseada);
        espera();
        // Confirma la selección haciendo clic en el botón de confirmar reserva
        onView(withId(R.id.imageButton2)).perform(click());
        espera();
        // Verificar si la reserva fue fallida
        assertFalse(reservar.isReservaSuccessful());
    }

    // Reserva incorrecta (fecha ya reservada)
    @Test
    public void testReservaInvalidOcupada() {
        // Obtener una fecha posterior libre
        String fechaDeseada = obtenerFechaDeseada();
        // Establecer la fecha deseada en Activity_reservar mediante el método setFechaStr()
        reservar.setFechaStr(fechaDeseada);
        espera();
        // Confirma la selección haciendo clic en el botón de confirmar reserva
        onView(withId(R.id.imageButton2)).perform(click());
        espera();

        // Repetimos el proceso con la misma fecha
        reservar.setFechaStr(fechaDeseada);
        espera();
        // Confirma la selección haciendo clic en el botón de confirmar reserva
        onView(withId(R.id.imageButton2)).perform(click());
        espera();
        // Verificar si la reserva fue fallida
        assertFalse(reservar.isReservaSuccessful());
        daysToAdd++; // Para evitar errores con futuros tests
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

    // Método para esperar a que se complete la operación asíncrona
    public void espera() {
        try {
            Thread.sleep(5000); // Espera 5 segundos
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    // Método para obtener la fecha deseada en el formato "YYYY-MM-DD"
    public String obtenerFechaDeseada() {
        // Crear una instancia de Calendar para la fecha base (1 de enero de 2025)
        Calendar calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.JANUARY, 02);

        // Agregar días adicionales (según el contador daysToAdd)
        calendar.add(Calendar.DAY_OF_MONTH, daysToAdd);

        // Obtener año, mes y día de la fecha resultante
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH); // Mes actual (zero-based)
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Formatear la fecha en formato deseado (YYYY-MM-DD)
        String fechaDeseada = String.format("%d-%02d-%02d", year, month + 1, dayOfMonth);

        return fechaDeseada;
    }

}

