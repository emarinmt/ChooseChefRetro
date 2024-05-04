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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Para realizar los tests referentes a la muestra de reservas de un cliente
 * CORRESPONDERIA A LA CLASE ACTIVITY_USER (GESTION RESERVAS)
 */
@RunWith(AndroidJUnit4.class)
public class Test_AjustesClient {
    @Rule
    public IntentsTestRule<Activity_login> activityRule = new IntentsTestRule<>(Activity_login.class);
    private Context context;
    private Activity_client_lista_reservas actClient;

    @Before
    public void setUp() {
        FastClient.initialize(ApplicationProvider.getApplicationContext());
        context = ApplicationProvider.getApplicationContext();
        // Iniciar sesión como cliente
        onView(withId(R.id.edt_usuario_login)).perform(typeText("client"));
        onView(withId(R.id.edt_contra_login)).perform(typeText("client"), closeSoftKeyboard());
        onView(withId(R.id.ibtn_entrar_login)).perform(click());
        UtilsTests.espera(10000);
        onView(withId(R.id.btn_ajustes)).perform(click());
        UtilsTests.espera(10000);
        // Verificar que se abre Activity_user después de clicar en ajustes
        intended(hasComponent(Activity_client_lista_reservas.class.getName()));
        // Obtener la instancia de Activity_user
        actClient = ((Activity_client_lista_reservas) UtilsTests.getActivityInstance(Activity_client_lista_reservas.class));
    }

    // Recuperación de reservas  correcta (hay conexión)
    @Test
    public void testRecuperarReservaWithNetwork() {
        // Simular tener conexión de red (configurando el estado de red en true)
        Utils.setNetworkAvailable(true);
        UtilsTests.espera(10000);
        actClient.recuperarDatos();
        UtilsTests.espera(10000);
        // Verificar que contentSuccessful es true
        assertTrue(actClient.isContentSuccessful());
    }

    // Recuperación de reservas incorrecta (hay conexión)
    @Test
    public void testRecuperarReservaWhenNoNetwork() {
        // Simular no tener conexión de red (configurando el estado de red en falso)
        Utils.setNetworkAvailable(false);
        UtilsTests.espera(10000);
        actClient.recuperarDatos();
        UtilsTests.espera(10000);
        // Verificar que contentSuccessful es falso
        assertFalse(actClient.isContentSuccessful());
        Utils.setNetworkAvailable(true);
    }

    // FILTRO  (PARA SIGUIENTE TEA IMPLEMENTAR BOTON)
    // Recuperación de lista de reservas correcta con filtro
    @Test
    public void testRecuperarReservasFiltrados() {
        UtilsTests.espera(20000);
        actClient.buscar(2023);
        UtilsTests.espera(20000);
        // Verificar que contentSuccessful es true
        assertTrue(actClient.isContentSuccessful());
    }

    /*
    PARA COMPROBAR QUE LA LISTA DE RESERVAS AUMENTA AL CREAR UNA, SE REALIZARA EL TEST
    EN LA CLASE TEST_RESERVAR, YA QUE ES DONDE SE SIMULA UNA RESERVA
    NO PODEMOS CLICAR A UN CHEF EN CONCRETO
     */

}