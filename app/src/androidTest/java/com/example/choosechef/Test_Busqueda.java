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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.Collection;
import java.util.List;

/**
 * Para realizar los tests referentes a la modificación del perfil de usuario
 */
@RunWith(AndroidJUnit4.class)
public class Test_Busqueda {
    @Rule
    public IntentsTestRule<Activity_login> activityRule = new IntentsTestRule<>(Activity_login.class);
    private Activity_contenido contenido;
    private Activity_busqueda busqueda;
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

    // Muestra chefs filtrados correcta
    @Test
    public void testFiltrarChefsSuccess() {
        // Definir criterios de búsqueda
        String provincia = "Barcelona";
        String comida = "Italiana";
        String servicio = "Chef a domicilio";
        // Filtrar la lista de usuarios por los criterios definidos
        List<User> chefsFiltrados = contenido.filterUsers(contenido.userList, provincia, comida, servicio);

        int initialSize = chefsFiltrados.size(); // Número esperado de chefs con estos criterios

        onView(withId(R.id.btn_lupa)).perform(click());
        // Obtener la instancia de Activity_busqueda
        busqueda = ((Activity_busqueda) getActivityInstance(Activity_busqueda.class));

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
        int finalSize = contenido.userList.size(); // Número de chefs después del registro
        // Aseguramos que el el número de chefs inicial + 1 coincide con el actual
        assertEquals(initialSize, finalSize);
    }

    // Recuperación de provincias correcta
    @Test
    public void testRecuperarProvWithNetwork() {
        // Simular tener conexión de red (configurando el estado de red en true)
        Utils.setNetworkAvailable(true);
        espera();
        onView(withId(R.id.btn_lupa)).perform(click());
        // Obtener la instancia de Activity_busqueda
        busqueda = ((Activity_busqueda) getActivityInstance(Activity_busqueda.class));

        busqueda.recuperarProvincias();
        espera();
        // Verificar que provinciaSuccessful es true
        assertTrue(busqueda.isProvSuccessful());
    }

    // Recuperación de provincias incorrecta, no hay conexión
    @Test
    public void testRecuperarProvWhenNoNetwork() {
        // Simular no tener conexión de red (configurando el estado de red en falso)
        Utils.setNetworkAvailable(false);
        espera();
        onView(withId(R.id.btn_lupa)).perform(click());
        // Obtener la instancia de Activity_busqueda
        busqueda = ((Activity_busqueda) getActivityInstance(Activity_busqueda.class));
        busqueda.recuperarProvincias();
        espera();
        // Verificar que userList está vacía
        assertEquals(0, busqueda.provinciasList.size());
        // Verificar que contentSuccessful es falso
        assertFalse(busqueda.isProvSuccessful());
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
}