package com.example.choosechef;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import androidx.test.espresso.contrib.RecyclerViewActions;
/**
 * Para realizar los tests referentes a la valoración de una reserva
 * CORRESPONDERIA A LA CLASE ACTIVITY_RESERVA_AMPLIADO (VALORAR RESERVAS)
 */
@RunWith(AndroidJUnit4.class)
public class Test_AjustesClientAmpliado {
    @Rule
    public IntentsTestRule<Activity_login> activityRule = new IntentsTestRule<>(Activity_login.class);
    private Activity_client_reserva_ampliado clientAmpliado;
    private Activity_client_lista_reservas actUser;
    private Context context;
    @Before
    public void setUp() {
        FastClient.initialize(ApplicationProvider.getApplicationContext());
        context = ApplicationProvider.getApplicationContext();
        // Iniciar sesión como usuario de prueba
        onView(withId(R.id.edt_usuario_login)).perform(typeText("client"));
        onView(withId(R.id.edt_contra_login)).perform(typeText("client"), closeSoftKeyboard());
        onView(withId(R.id.ibtn_entrar_login)).perform(click());
        UtilsTests.espera(10000);
        onView(withId(R.id.btn_ajustes)).perform(click());
        UtilsTests.espera(10000);
        // Verificar que se abre Activity_client_lista_reservas después de clicar en ajustes
        intended(hasComponent(Activity_client_lista_reservas.class.getName()));
        // Obtener la instancia de Activity_client_lista_reservas
        actUser = ((Activity_client_lista_reservas) UtilsTests.getActivityInstance(Activity_client_lista_reservas.class));
    }

    // Ampliación de reserva incorrecta (fecha posterior)
    @Test
    public void testAmpliarReservaValid() {
        // Hacer clic en el primer elemento de la lista (suponiendo que hay al menos una reserva en la lista)
        onView(withId(R.id.rv_reservas))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // Verificar que la actividad reservar_ampliado se inicia correctamente
        intended(hasComponent(Activity_client_reserva_ampliado.class.getName()));
        // Obtener la instancia de Activity_client_reserva_ampliado
        clientAmpliado = ((Activity_client_reserva_ampliado) UtilsTests.getActivityInstance(Activity_client_reserva_ampliado.class));
        UtilsTests.espera(10000);
        assertFalse(clientAmpliado.isContentSuccessful());
    }

    // Ampliación de reserva incorrecta, simulamos un intent erroneo
    @Test
    public void testAmpliarReservaInvalid() {
        // Hacer clic en el primer elemento de la lista (suponiendo que hay al menos una reserva en la lista)
        onView(withId(R.id.rv_reservas))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // Verificar que la actividad reservar_ampliado se inicia correctamente
        intended(hasComponent(Activity_client_reserva_ampliado.class.getName()));
        // Obtener la instancia de Activity_client_reserva_ampliado
        clientAmpliado = ((Activity_client_reserva_ampliado) UtilsTests.getActivityInstance(Activity_client_reserva_ampliado.class));
        // Simular un Intent inválido
        Intent invalidIntent = null;
        // Llamar manualmente obtenerIntent() y pasar el Intent inválido
        clientAmpliado.obtenerIntent(invalidIntent);
        UtilsTests.espera(10000);
        // Verificar que contentSuccessful es false
        assertFalse(clientAmpliado.isContentSuccessful());
    }

    // Ampliar + Valoración de reserva correcta (anterior a fecha actual)
    @Test
    public void testValorarReservaValid() {
        // Filtramos las reservas, ya que solo se pueden valorar las pasadas
        actUser.buscar(2023);
        UtilsTests.espera(10000);
        // Hacer clic en el primer elemento de la lista
        onView(withId(R.id.rv_reservas))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        UtilsTests.espera(10000);
        // Obtener la instancia de Activity_client_reserva_ampliado
        clientAmpliado = ((Activity_client_reserva_ampliado) UtilsTests.getActivityInstance(Activity_client_reserva_ampliado.class));
        //onView(withId(R.id.comentario)).perform(replaceText("probando"));

        // Obtener texto actual del campo comentario (para no cambiarlo)
        String currentDesc = (clientAmpliado.getComentReserva());

        // Borrar el texto actual del campo comentario
        onView(withId(R.id.comentario)).perform(clearText());

        // Escribir un nuevo nombre en el campo comentario
        onView(withId(R.id.comentario)).perform(typeText(currentDesc), closeSoftKeyboard());

        // Realizamos el clic en el botón de confirmar
        onView(withId(R.id.ibtn_confirmar)).perform(click());
        UtilsTests.espera(10000);
        // Verificar que modifySuccessful es true
        assertTrue(clientAmpliado.isModifySuccessful());
    }
}

