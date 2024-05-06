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
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Para realizar los tests referentes a la lista de todos los usuarios
 */
@RunWith(AndroidJUnit4.class)
public class Test_AjustesAdminResenyas {

    @Rule
    public IntentsTestRule<Activity_login> activityRule = new IntentsTestRule<>(Activity_login.class);
    private Context context;
    private Activity_admin_lista_reservas actAdmin;

    @Before
    public void setUp() {
        FastClient.initialize(ApplicationProvider.getApplicationContext());
        context = ApplicationProvider.getApplicationContext();
        // Iniciar sesión como admin
        onView(withId(R.id.edt_usuario_login)).perform(typeText("admin"));
        onView(withId(R.id.edt_contra_login)).perform(typeText("admin"), closeSoftKeyboard());
        onView(withId(R.id.ibtn_entrar_login)).perform(click());
        UtilsTests.espera(10000);
        onView(withId(R.id.btn_ajustes)).perform(click());
        UtilsTests.espera(10000);
        //Clicar opción Gestión reseñas ofrecido del menú
        onView(withId(R.id.imb_gestion_resenyas)).perform(click());
        // Verificar que se abre Activity_admin_lista_reservas después de clicar
        intended(hasComponent(Activity_admin_lista_reservas.class.getName()));
        // Obtener la instancia de Activity_admin_lista_reservas
        actAdmin = ((Activity_admin_lista_reservas) UtilsTests.getActivityInstance(Activity_admin_lista_reservas.class));
    }
    // Recuperación de reservas  correcta (hay conexión)
    @Test
    public void testRecuperarReservaWithNetwork() {
        // Simular tener conexión de red (configurando el estado de red en true)
        Utils.setNetworkAvailable(true);
        UtilsTests.espera(10000);
        actAdmin.recuperarDatos();
        UtilsTests.espera(10000);
        // Verificar que contentSuccessful es true
        assertTrue(actAdmin.isContentSuccessful());
    }

    // Recuperación de reservas incorrecta (no hay conexión)
    @Test
    public void testRecuperarReservaWhenNoNetwork() {
        // Simular no tener conexión de red (configurando el estado de red en falso)
        Utils.setNetworkAvailable(false);
        UtilsTests.espera(10000);
        actAdmin.recuperarDatos();
        UtilsTests.espera(10000);
        // Verificar que contentSuccessful es falso
        assertFalse(actAdmin.isContentSuccessful());
        Utils.setNetworkAvailable(true);
    }

    // Recuperación de lista de reservas correcta con filtro
    @Test
    public void testRecuperarReservasFiltrados() {
        UtilsTests.espera(20000);
        //actChef.buscar(2023);
        onView(withId(R.id.edt_fecha_filtro)).perform(replaceText("2023"));
        onView(withId(R.id.btn_lupa3)).perform(click());
        UtilsTests.espera(20000);
        // Verificar que contentSuccessful es true
        assertTrue(actAdmin.isContentSuccessful());
    }


}