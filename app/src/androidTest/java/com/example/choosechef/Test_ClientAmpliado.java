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
 * Para realizar los tests referentes a la modificación del perfil de usuario
 */
@RunWith(AndroidJUnit4.class)
public class Test_ClientAmpliado {
    @Rule
    public IntentsTestRule<Activity_login> activityRule = new IntentsTestRule<>(Activity_login.class);
    private Activity_reserva_ampliado clientAmpliado;
    private Context context;
    @Before
    public void setUp() {
        FastClient.initialize(ApplicationProvider.getApplicationContext());
        context = ApplicationProvider.getApplicationContext();
        // Iniciar sesión como usuario de prueba
        onView(withId(R.id.edt_usuario_login)).perform(typeText("4"));
        onView(withId(R.id.edt_contra_login)).perform(typeText("4"), closeSoftKeyboard());
        onView(withId(R.id.ibtn_entrar_login)).perform(click());
        UtilsTests.espera(10000);
        onView(withId(R.id.btn_ajustes)).perform(click());
        UtilsTests.espera(10000);
        // Verificar que se abre Activity_user después de clicar en ajustes
        intended(hasComponent(Activity_chef.class.getName()));
        // Hacer clic en el primer elemento de la lista (suponiendo que hay al menos un chef en la lista)
        onView(withId(R.id.rv_reservas))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // Verificar que la actividad reservar_ampliado se inicia correctamente
        intended(hasComponent(Activity_chef_ampliado.class.getName()));
        // Obtener la instancia de Activity_reserva_ampliado
        clientAmpliado = ((Activity_reserva_ampliado) UtilsTests.getActivityInstance(Activity_reserva_ampliado.class));

    }

    // Ampliación de reserva correcta
    @Test
    public void testAmpliarReservaValid() {
        assertTrue(clientAmpliado.isContentSuccessful());
    }
    // Ampliación de reserva incorrecta, simulamos un intent erroneo
    @Test
    public void testAmpliarReservaInvalid() {
        // Simular un Intent inválido (null o sin el extra "user")
        Intent invalidIntent = null;
        // Llamar manualmente obtenerIntent() y pasar el Intent inválido
        clientAmpliado.obtenerIntent(invalidIntent);
        UtilsTests.espera(10000);
        // Verificar que contentSuccessful es false
        assertFalse(clientAmpliado.isContentSuccessful());
    }


}

