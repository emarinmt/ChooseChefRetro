package com.example.choosechef;
import android.app.Activity;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.Collection;
import java.util.Random;

/**
 * Clase desarrollada por ELENA
 * Para realizar los tests referentes a la modificación del perfil de usuario
 */
@RunWith(AndroidJUnit4.class)
public class Test_AdminAmpliado {
    @Rule
    public IntentsTestRule<Activity_login> activityRule = new IntentsTestRule<>(Activity_login.class);
    private Context context;
    private Activity_user_ampliado actAdminAmpl;
    @Before
    public void setUp() {
        FastClient.initialize(ApplicationProvider.getApplicationContext());
        context = ApplicationProvider.getApplicationContext();
        // Iniciar sesión como admin
        onView(withId(R.id.edt_usuario_login)).perform(typeText("admin"));
        onView(withId(R.id.edt_contra_login)).perform(typeText("admin"), closeSoftKeyboard());
        onView(withId(R.id.ibtn_entrar_login)).perform(click());
        espera();
        onView(withId(R.id.btn_ajustes)).perform(click());
        espera();
        // Verificar que se abre Activity_admin después de clicar en ajustes
        intended(hasComponent(Activity_admin.class.getName()));
        // Obtener la instancia de Activity_admin
       // actAdmin = ((Activity_admin) getActivityInstance(Activity_admin.class));

    }
    // Ampliación de usuario correcta
    @Test
    public void testAmpliarUserValid() {
        // Hacer clic en el primer elemento de la lista (suponiendo que hay al menos un usuario en la lista)
        onView(withId(R.id.rv_users))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // Verificar que la actividad user_ampliado se inicia correctamente
        intended(hasComponent(Activity_user_ampliado.class.getName()));
        // Obtener la instancia de Activity_user_ampliado
        actAdminAmpl = ((Activity_user_ampliado) getActivityInstance(Activity_user_ampliado.class));
        // Verificar que contentSuccessful es true
        espera();
        assertTrue(actAdminAmpl.isContentSuccessful());
    }

    // Ampliación de usuario incorrecta, simulamos un intent erroneo
    @Test
    public void testAmpliarUserInvalid() {
        // Hacer clic en el primer elemento de la lista (suponiendo que hay al menos un usuario en la lista)
        onView(withId(R.id.rv_users))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // Verificar que la actividad user_ampliado se inicia correctamente
        intended(hasComponent(Activity_user_ampliado.class.getName()));
        // Obtener la instancia de Activity_user_ampliado
        actAdminAmpl = ((Activity_user_ampliado) getActivityInstance(Activity_user_ampliado.class));
        // Simular un Intent inválido (null o sin el extra "user")
        Intent invalidIntent = null;
        // Llamar manualmente obtenerIntent() y pasar el Intent inválido
        actAdminAmpl.obtenerIntent(invalidIntent);
        espera();
        // Verificar que contentSuccessful es false
        assertFalse(actAdminAmpl.isContentSuccessful());
    }
    // Modificación datos usuario correcta
    @Test
    public void testModifyUserValid() {
        // Hacer clic en el primer elemento de la lista (suponiendo que hay al menos un usuario en la lista)
        onView(withId(R.id.rv_users))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // Verificar que la actividad user_ampliado se inicia correctamente
        intended(hasComponent(Activity_user_ampliado.class.getName()));
        // Obtener la instancia de Activity_user_ampliado
        actAdminAmpl = ((Activity_user_ampliado) getActivityInstance(Activity_user_ampliado.class));
        espera();
        // Realizamos el clic en el botón de editar
        onView(withId(R.id.ibtn_edit)).perform(click());
        espera();

        // Obtener texto actual del campo de nombre
        String currentDesc = actAdminAmpl.getDescUsuario(); // Ejemplo: método para obtener nombre de usuario

        // Borrar el texto actual del campo de nombre
        onView(withId(R.id.edt_descripcion_usuario)).perform(clearText());

        // Escribir un nuevo nombre en el campo de nombre
        onView(withId(R.id.edt_descripcion_usuario)).perform(typeText(currentDesc), closeSoftKeyboard());

        // Realizamos el clic en el botón de editar
        onView(withId(R.id.ibtn_edit)).perform(click());
        espera();
        // Verificar si la modificación fue exitosa
        assertTrue(actAdminAmpl.isModifySuccessful());
    }


    // CUIDADOOO!! NO PASAR!!!
    // No puedo especificar qué usuario se borra, solo si sé la posicion en la lista
    /*
    // Eliminación de usuario correcta
    @Test
    public void testDeleteUserValid() {
        int initialSize = actAdmin.userList.size();
        // Hacer clic en el primer elemento de la lista (suponiendo que hay al menos un usuario en la lista)
        onView(withId(R.id.rv_users))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // Verificar que la actividad user_ampliado se inicia correctamente
        intended(hasComponent(Activity_user_ampliado.class.getName()));
        // Obtener la instancia de Activity_user_ampliado
        actAdminAmpl = ((Activity_user_ampliado) getActivityInstance(Activity_user_ampliado.class));
        onView(withId(R.id.ibtn_delete)).perform(click());
        espera();
        int finalSize = actAdmin.userList.size();
        // Aseguramos que el el número de usuarios inicial - 1 coincide con el actual
        assertEquals((initialSize - 1), finalSize);
        // Verificar que deleteSuccessful es true
        assertTrue(actAdminAmpl.isDeleteSuccessful());
    }
*/

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
            Thread.sleep(15000); // Espera 5 segundos
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    // Método para generar un nombre de usuario aleatorio
    private String generateRandomUsername() {
        Random random = new Random();
        int randomNumber = random.nextInt(1000); // Genera un número aleatorio entre 0 y 999
        return "test" + randomNumber; // Devuelve un nombre de usuario único cada vez que se llama
    }
}