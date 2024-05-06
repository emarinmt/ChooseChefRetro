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
 * CORRESPONDERIA A LA CLASE ACTIVITY_CHEF_AMPLIADO
 */
@RunWith(AndroidJUnit4.class)
public class Test_ContenidoAmpliado {

    @Rule
    public IntentsTestRule<Activity_login> activityRule = new IntentsTestRule<>(Activity_login.class);
    private Activity_contenido_chef_ampliado contenidoAmpliado;
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
        // Verificar que se abre Activity_contenido después del login
        intended(hasComponent(Activity_contenido.class.getName()));
        // Hacer clic en el primer elemento de la lista (suponiendo que hay al menos un chef en la lista)
        onView(withId(R.id.rv_chefs))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // Verificar que la actividad chef_ampliado se inicia correctamente
        intended(hasComponent(Activity_contenido_chef_ampliado.class.getName()));
        // Obtener la instancia de Activity_contenido_chef_ampliado
        contenidoAmpliado = ((Activity_contenido_chef_ampliado) UtilsTests.getActivityInstance(Activity_contenido_chef_ampliado.class));
    }

    // Ampliación de chef correcta
    @Test
    public void testAmpliarChefValid() {
        // Verificar que contentSuccessful es true
        UtilsTests.espera(10000);
        assertTrue(contenidoAmpliado.isContentSuccessful());
    }

    // Ampliación de chef incorrecta, simulamos un intent erroneo
    @Test
    public void testAmpliarChefInvalid() {
        // Simular un Intent inválido (null o sin el extra "user")
        Intent invalidIntent = null;
        // Llamar manualmente obtenerIntent() y pasar el Intent inválido
        contenidoAmpliado.obtenerIntent(invalidIntent);
        UtilsTests.espera(10000);
        // Verificar que contentSuccessful es false
        assertFalse(contenidoAmpliado.isContentSuccessful());
    }

}

