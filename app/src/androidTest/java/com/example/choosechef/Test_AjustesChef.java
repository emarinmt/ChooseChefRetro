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
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.Collection;


/**
 * Para realizar los tests referentes a la modificación del perfil de usuario
 */
@RunWith(AndroidJUnit4.class)
public class Test_AjustesChef {
    // CORRESPONDERIA A LA CLASE ACTIVITY_CHEF
    @Rule
    public IntentsTestRule<Activity_login> activityRule = new IntentsTestRule<>(Activity_login.class);
    private Context context;
    private Activity_chef actChef;

    @Before
    public void setUp() {
        FastClient.initialize(ApplicationProvider.getApplicationContext());
        context = ApplicationProvider.getApplicationContext();
        // Iniciar sesión como chef
        onView(withId(R.id.edt_usuario_login)).perform(typeText("chef"));
        onView(withId(R.id.edt_contra_login)).perform(typeText("chef"), closeSoftKeyboard());
        onView(withId(R.id.ibtn_entrar_login)).perform(click());
        espera();
        onView(withId(R.id.btn_ajustes)).perform(click());
        espera();
        // Verificar que se abre Activity_chef después de clicar en ajustes
        intended(hasComponent(Activity_chef.class.getName()));
        // Obtener la instancia de Activity_chef
        actChef = ((Activity_chef) getActivityInstance(Activity_chef.class));
    }

    // Modificación opciones chef correcta
    @Test
    public void testOptionsChefsSuccess() {
        // Definir criterios de modificación
        String provincia = "Barcelona";
        String comida = "Italiana";
        String servicio = "Chef a domicilio";

        // Seleccionar un valor en el Spinner de provincias
        onView(withId(R.id.spinner_provincias)).perform(click()); // Abrir el Spinner
        onView(withText("Barcelona")).perform(click()); // Seleccionar un valor específico

        // Seleccionar un valor en el Spinner de tipo de comida
        onView(withId(R.id.spinner_tipo_comida)).perform(click()); // Abrir el Spinner
        onView(withText("Italiana")).perform(click()); // Seleccionar un valor específico

        // Seleccionar un valor en el Spinner de servicios
        onView(withId(R.id.spinner_servicios)).perform(click()); // Abrir el Spinner
        onView(withText("Chef a domicilio")).perform(click()); // Seleccionar un valor específico

        onView(withId(R.id.ibtn_confirmar)).perform(click());
        espera();

        // Verificar si la modificación fue exitosa
        assertTrue(actChef.isModifySuccessful());
    }

    // Recuperación de datos de chef incorrecta (no hay conexión)
    @Test
    public void testRecuperarChefsWhenNoNetwork() {
        // Simular no tener conexión de red (configurando el estado de red en falso)
        Utils.setNetworkAvailable(false);
        espera();
        actChef.recuperarDatos();
        espera();
        // Verificar que contentSuccessful es falso
        assertFalse(actChef.isContentSuccessful());
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
}
