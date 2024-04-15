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
 * Para realizar los tests referentes a la muestra de chefs
 */
@RunWith(AndroidJUnit4.class)
public class Test_Contenido {
    @Rule
    public IntentsTestRule<Activity_login> activityRule = new IntentsTestRule<>(Activity_login.class);
    private Activity_contenido contenido;
    private Context context;


    @Before
    public void setUp() {
        FastClient.initialize(ApplicationProvider.getApplicationContext());
        context = ApplicationProvider.getApplicationContext();
        // Iniciar sesión como usuario de prueba
        onView(withId(R.id.edt_usuario_login)).perform(typeText("4"));
        onView(withId(R.id.edt_contra_login)).perform(typeText("4"), closeSoftKeyboard());
        onView(withId(R.id.ibtn_entrar_login)).perform(click());

        espera();

        // Verificar que se abre Activity_contenido después del login
        intended(hasComponent(Activity_contenido.class.getName()));
        // Obtener la instancia de Activity_content
        contenido = ((Activity_contenido) getActivityInstance(Activity_contenido.class));
    }
    // Recuperación de lista de chefs correcta
    @Test
    public void testRecuperarChefsWithNetwork() {
        // Simular tener conexión de red (configurando el estado de red en true)
        Utils.setNetworkAvailable(true);
        espera();
        contenido.recuperarChefs();
        espera();
        // Verificar que contentSuccessful es true
        assertTrue(contenido.isContentSuccessful());
    }

    // Recuperación de lista de chefs incorrecta, no hay conexión
    @Test
    public void testRecuperarChefsWhenNoNetwork() {
        // Simular no tener conexión de red (configurando el estado de red en falso)
        Utils.setNetworkAvailable(false);
        espera();
        contenido.recuperarChefs();
        espera();
        // Verificar que userList está vacía
        assertEquals(0, contenido.getUserList().size());
        // Verificar que contentSuccessful es falso
        assertFalse(contenido.isContentSuccessful());
    }
    // Recuperación de lista de chefs correcta, añadiendo un chef para comprobar que se mpdifica la lista
    @Test
    public void testRecuperarChefsSuccess() {
        int initialSize = contenido.userList.size();// Número de chefs antes del registro
        onView(withId(R.id.btn_logout_user)).perform(click()); // Logout
        // Registro, simulando la entrada de datos
        onView(withId(R.id.ibtn_registro)).perform(click());
        String randomUsername = generateRandomUsername(); // Genera un nombre de usuario aleatorio
        onView(withId(R.id.edt_usuario_registro)).perform(replaceText(randomUsername));
        onView(withId(R.id.edt_contraseña_registro)).perform(replaceText("test"));
        onView(withId(R.id.edt_contraseña2_registro)).perform(replaceText("test"));
        onView(withId(R.id.switch_chef_registro)).perform(click()); // Tipo = Chef
        onView(withId(R.id.ibtn_registrarse_registro)).perform(click());
        espera();
        // Iniciar sesión como usuario de prueba
        onView(withId(R.id.edt_usuario_login)).perform(typeText("4"));
        onView(withId(R.id.edt_contra_login)).perform(typeText("4"), closeSoftKeyboard());
        onView(withId(R.id.ibtn_entrar_login)).perform(click());
        contenido.recuperarChefs();
        espera();
        int finalSize = contenido.userList.size(); // Número de chefs después del registro
        // Aseguramos que el el número de chefs inicial + 1 coincide con el actual
        assertEquals((initialSize + 1), finalSize);
        // Aseguramos que la recuperación sea exitosa
        assertTrue(contenido.isContentSuccessful());
    }

    // Recuperación de lista de chefs correcta, añadiendo un cliente para comprobar que no se modifica la lista
    @Test
    public void testRecuperarChefsClient() {
        int initialSize = contenido.userList.size();// Número de chefs antes del registro
        onView(withId(R.id.btn_logout_user)).perform(click()); // Logout
        // Registro, simulando la entrada de datos
        onView(withId(R.id.ibtn_registro)).perform(click());
        String randomUsername = generateRandomUsername(); // Genera un nombre de usuario aleatorio
        onView(withId(R.id.edt_usuario_registro)).perform(replaceText(randomUsername));
        onView(withId(R.id.edt_contraseña_registro)).perform(replaceText("test"));
        onView(withId(R.id.edt_contraseña2_registro)).perform(replaceText("test"));
        onView(withId(R.id.ibtn_registrarse_registro)).perform(click());
        espera();
        // Iniciar sesión como usuario de prueba
        onView(withId(R.id.edt_usuario_login)).perform(typeText("4"));
        onView(withId(R.id.edt_contra_login)).perform(typeText("4"), closeSoftKeyboard());
        onView(withId(R.id.ibtn_entrar_login)).perform(click());
        contenido.recuperarChefs();
        espera();
        int finalSize = contenido.userList.size(); // Número de chefs después del registro
        // Aseguramos que el el número de chefs inicial + 1 no coincide con el actual
        assertEquals((initialSize + 1), finalSize);
        // Aseguramos que la recuperación sea exitosa
        assertTrue(contenido.isContentSuccessful());
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
