package com.example.choosechef;

import android.content.Context;
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
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;



/**
 * Para realizar los tests referentes a la modificación de servicios del chef
 * CORRESPONDERIA A LA CLASE ACTIVITY_CHEF
 */
@RunWith(AndroidJUnit4.class)
public class Test_AjustesChef {

    @Rule
    public IntentsTestRule<Activity_login> activityRule = new IntentsTestRule<>(Activity_login.class);
    private Context context;
    private Activity_chef_servicio actChef;

    @Before
    public void setUp() {
        FastClient.initialize(ApplicationProvider.getApplicationContext());
        context = ApplicationProvider.getApplicationContext();
        // Iniciar sesión como chef
        onView(withId(R.id.edt_usuario_login)).perform(typeText("chef"));
        onView(withId(R.id.edt_contra_login)).perform(typeText("chef"), closeSoftKeyboard());
        onView(withId(R.id.ibtn_entrar_login)).perform(click());
        UtilsTests.espera(10000);
        onView(withId(R.id.btn_ajustes)).perform(click());
        UtilsTests.espera(10000);
        //Clicar opción Gestión servicio ofrecido del menú
        onView(withId(R.id.imb_gestion_perfil_chef)).perform(click());
        // Verificar que se abre Activity_chef_servicio después de clicar
        intended(hasComponent(Activity_chef_servicio.class.getName()));
        // Obtener la instancia de Activity_chef_servicio
        actChef = ((Activity_chef_servicio)UtilsTests.getActivityInstance(Activity_chef_servicio.class));
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
        UtilsTests.espera(10000);

        // Verificar si la modificación fue exitosa
        assertTrue(actChef.isModifySuccessful());
    }

    // Recuperación de datos de chef incorrecta (no hay conexión)
    @Test
    public void testRecuperarChefsWhenNoNetwork() {
        // Simular no tener conexión de red (configurando el estado de red en falso)
        Utils.setNetworkAvailable(false);
        UtilsTests.espera(10000);
        actChef.recuperarDatos();
        UtilsTests.espera(10000);
        // Verificar que contentSuccessful es falso
        assertFalse(actChef.isContentSuccessful());
        Utils.setNetworkAvailable(true);
    }

}
