package com.example.choosechef;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import androidx.test.espresso.contrib.RecyclerViewActions;

/**
 * Para realizar los tests referentes a la creación de reservas
 * COORESPONDE A LA CLASSE ACTIVITY_RESERVA
 */
@RunWith(AndroidJUnit4.class)
public class Test_Reservar {

    @Rule
    public IntentsTestRule<Activity_login> activityRule = new IntentsTestRule<>(Activity_login.class);
    private Activity_contenido_reservar_chef reservar;
    private Context context;
    private static final String PREFS_NAME = "MiPreferencia"; // Nombre del archivo de preferencias
    private static final String DAYS_TO_ADD_KEY = "daysToAdd"; // Clave para daysToAdd en SharedPreferences
    @Before
    public void setUp() {
        FastClient.initialize(ApplicationProvider.getApplicationContext());
        context = ApplicationProvider.getApplicationContext();
        // Iniciar sesión como usuario de prueba client
        onView(withId(R.id.edt_usuario_login)).perform(typeText("client"));
        onView(withId(R.id.edt_contra_login)).perform(typeText("client"), closeSoftKeyboard());
        onView(withId(R.id.ibtn_entrar_login)).perform(click());
        UtilsTests.espera(10000);
        // PARA MAS ADELANTE IMPLEMENTAR QUE CLIQUE EN UN CHEF DETERMINADO
        // Hacer clic en el primer elemento de la lista (suponiendo que hay al menos un chef en la lista)
        onView(withId(R.id.rv_chefs))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // Botón reservar
        onView(withId(R.id.ibtn_reservar)).perform(click());
        // Verificar que la actividad reservar se inicia correctamente
        intended(hasComponent(Activity_contenido_reservar_chef.class.getName()));
        // Obtener la instancia de Activity_reservar
        reservar = ((Activity_contenido_reservar_chef) UtilsTests.getActivityInstance(Activity_contenido_reservar_chef.class));
    }

    // Reserva incorrecta (fecha ya reservada)
    @Test
    public void testReservaInvalidOcupada() {
        // Obtener una fecha posterior libre
        String fechaDeseada = obtenerFechaDeseada();
        // Establecer la fecha deseada en Activity_reservar mediante el método setFechaStr()
        reservar.setFechaStr(fechaDeseada);
        UtilsTests.espera(10000);
        // Confirma la selección haciendo clic en el botón de confirmar reserva
        onView(withId(R.id.imageButton2)).perform(click());
        UtilsTests.espera(10000);
        onView(withId(R.id.ibtn_reservar)).perform(click());  // Botón reservar
        // Repetimos el proceso con la misma fecha
        reservar.setFechaStr(fechaDeseada);
        UtilsTests.espera(10000);
        // Confirma la selección haciendo clic en el botón de confirmar reserva
        onView(withId(R.id.imageButton2)).perform(click());
        UtilsTests.espera(20000);
        // Verificar si la reserva fue fallida
        assertFalse(reservar.isReservaSuccessful());
        incrementDaysToAdd(1); // Incrementar daysToAdd en 1 día // Para evitar errores con futuros tests
    }

    // Reserva incorrecta (fecha anterior a la actual)
    @Test
    public void testReservaInValidDate() {
        // Establecemos una fecha anterior a la actual
        String fechaDeseada = "2023-05-23";
        // Establecer la fecha deseada en Activity_reservar mediante el método setFechaStr()
        reservar.setFechaStr(fechaDeseada);
        UtilsTests.espera(10000);
        // Confirma la selección haciendo clic en el botón de confirmar reserva
        onView(withId(R.id.imageButton2)).perform(click());
        UtilsTests.espera(10000);
        // Verificar si la reserva fue fallida
        assertFalse(reservar.isReservaSuccessful());
    }

    // Reserva correcta (fecha libre)
    // De momomento, no hemos implementado un método para obtener las fechas ocupadas,
    // por lo tanto usamos una fecha muy posterior y vamos añadiendo días para asegurar que esté libre
    @Test
    public void testReservaValid() {
        // Obtener una fecha posterior libre
        String fechaDeseada = obtenerFechaDeseada();
        // Establecer la fecha deseada en Activity_reservar mediante el método setFechaStr()
        reservar.setFechaStr(fechaDeseada);
        UtilsTests.espera(10000);
        // Confirma la selección haciendo clic en el botón de confirmar reserva
        onView(withId(R.id.imageButton2)).perform(click());
        UtilsTests.espera(20000);
        // Verificar si la reserva fue exitosa
        assertTrue(reservar.isReservaSuccessful());
        incrementDaysToAdd(1); // Incrementar daysToAdd en 1 día // para evitar errores con futuros tests
    }

    // Método para obtener la fecha deseada en el formato "YYYY-MM-DD"
    public String obtenerFechaDeseada() {
        // Crear una instancia de Calendar para la fecha base (5 de enero de 2025)
        Calendar calendar = Calendar.getInstance();
        calendar.set(2026, Calendar.APRIL, 17);

        // Agregar días adicionales (según el contador daysToAdd)
        calendar.add(Calendar.DAY_OF_MONTH, getDaysToAdd());

        // Obtener año, mes y día de la fecha resultante
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH); // Mes actual (zero-based)
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Formatear la fecha en formato deseado (YYYY-MM-DD)
        String fechaDeseada = String.format("%d-%02d-%02d", year, month + 1, dayOfMonth);

        return fechaDeseada;
    }

    // Método estático para obtener el valor actual de daysToAdd desde SharedPreferences
    private int getDaysToAdd() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(DAYS_TO_ADD_KEY, 0);
    }

    // Método estático para incrementar daysToAdd y guardar el nuevo valor en SharedPreferences
    private void incrementDaysToAdd(int days) {
        int currentDaysToAdd = getDaysToAdd();
        currentDaysToAdd += days;
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(DAYS_TO_ADD_KEY, currentDaysToAdd);
        editor.apply(); // Guardar el nuevo valor de daysToAdd en SharedPreferences
    }

}

