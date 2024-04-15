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
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.Collection;
import java.util.Random;

/**
 * Para realizar los tests referentes a la modificación del perfil de usuario
 */
@RunWith(AndroidJUnit4.class)
public class Test_AjustesAdmin {
    @Rule
    public IntentsTestRule<Activity_login> activityRule = new IntentsTestRule<>(Activity_login.class);
    private Context context;
    private Activity_admin actAdmin;

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
        actAdmin = ((Activity_admin) getActivityInstance(Activity_admin.class));
    }

    // Recuperación de lista de usuarios correcta
    @Test
    public void testRecuperarChefsWithNetwork() {
        // Simular tener conexión de red (configurando el estado de red en true)
        Utils.setNetworkAvailable(true);
        espera();
        actAdmin.recuperarDatos();
        espera();
        // Verificar que contentSuccessful es true
        assertTrue(actAdmin.isContentSuccessful());
    }

    // Recuperación de lista de usuarios incorrecta, no hay conexión
    @Test
    public void testRecuperarChefsWhenNoNetwork() {
        // Simular no tener conexión de red (configurando el estado de red en falso)
        Utils.setNetworkAvailable(false);
        espera();
        actAdmin.recuperarDatos();
        espera();
        // Verificar que userList está vacía
        assertEquals(0, actAdmin.userList.size());
        // Verificar que contentSuccessful es falso
        assertFalse(actAdmin.isContentSuccessful());
    }

    // Recuperación de lista de usuarios correcta, añadiendo un chef para comprobar que se mpdifica la lista
    @Test
    public void testRecuperarChefsSuccess() {
        int initialSize = actAdmin.userList.size();// Número de usuarios antes del registro
        onView(withId(R.id.btn_logout_user)).perform(click()); // Logout
        // Registro, simulando la entrada de datos
        onView(withId(R.id.ibtn_registro)).perform(click());
        String randomUsername = generateRandomUsername(); // Genera un nombre de usuario aleatorio
        onView(withId(R.id.edt_usuario_registro)).perform(replaceText(randomUsername));
        onView(withId(R.id.edt_contraseña_registro)).perform(replaceText("test"));
        onView(withId(R.id.edt_contraseña2_registro)).perform(replaceText("test"));
        onView(withId(R.id.ibtn_registrarse_registro)).perform(click());
        espera();
        // Iniciar sesión como admin
        onView(withId(R.id.edt_usuario_login)).perform(typeText("admin"));
        onView(withId(R.id.edt_contra_login)).perform(typeText("admin"), closeSoftKeyboard());
        onView(withId(R.id.ibtn_entrar_login)).perform(click());
        onView(withId(R.id.btn_ajustes)).perform(click());
        actAdmin.recuperarDatos();
        espera();
        int finalSize = actAdmin.userList.size(); // Número de usuarios después del registro
        // Aseguramos que el el número de usuarios inicial + 1 coincide con el actual
        assertEquals((initialSize + 1), finalSize);
        // Aseguramos que la recuperación sea exitosa
        assertTrue(actAdmin.isContentSuccessful());
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