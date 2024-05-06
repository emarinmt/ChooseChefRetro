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
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import androidx.test.espresso.contrib.RecyclerViewActions;
/**
 * Para realizar los tests referentes a la valoración de una reserva
 */
@RunWith(AndroidJUnit4.class)
public class Test_AdminResenyasAmpliado {
    @Rule
    public IntentsTestRule<Activity_login> activityRule = new IntentsTestRule<>(Activity_login.class);
    private Context context;
    private Activity_admin_lista_reservas actAdmin;
    private Activity_admin_reserva_ampliado reservasAmpliado;
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

    // Ampliación de reserva incorrecta, simulamos un intent erroneo
    @Test
    public void testAmpliarReservaInvalid() {
        UtilsTests.espera(10000);
        // Hacer clic en el primer elemento de la lista (suponiendo que hay al menos una reserva en la lista)
        onView(withId(R.id.rv_reservas_chef))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // Verificar que la actividad Activity_admin_reserva_ampliado se inicia correctamente
        intended(hasComponent(Activity_admin_reserva_ampliado.class.getName()));
        // Obtener la instancia de Activity_admin_reserva_ampliado
        reservasAmpliado = ((Activity_admin_reserva_ampliado) UtilsTests.getActivityInstance(Activity_admin_reserva_ampliado.class));
        // Simular un Intent inválido
        Intent invalidIntent = null;
        // Llamar manualmente obtenerIntent() y pasar el Intent inválido
        reservasAmpliado.obtenerIntent(invalidIntent);
        UtilsTests.espera(10000);
        // Verificar que contentSuccessful es false
        assertFalse(reservasAmpliado.isContentSuccessful());
    }

    // Ampliar + Valoración de reserva correcta (anterior a fecha actual)
    @Test
    public void testValorarReservaValid() {
        // Filtramos las reservas, ya que solo se pueden valorar las pasadas
        onView(withId(R.id.edt_fecha_filtro)).perform(replaceText("2023"));
        onView(withId(R.id.btn_lupa3)).perform(click());
        UtilsTests.espera(10000);
        // Hacer clic en el primer elemento de la lista
        onView(withId(R.id.rv_reservas_chef))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        UtilsTests.espera(10000);
        // Obtener la instancia de Activity_admin_reserva_ampliado
        reservasAmpliado = ((Activity_admin_reserva_ampliado) UtilsTests.getActivityInstance(Activity_admin_reserva_ampliado.class));
        //onView(withId(R.id.comentario)).perform(replaceText("probando"));

        // Obtener texto actual del campo comentario (para no cambiarlo)
        String currentDesc = (reservasAmpliado.getComentReserva());

        // Borrar el texto actual del campo comentario
        onView(withId(R.id.comentario)).perform(clearText());

        // Escribir un nuevo nombre en el campo comentario
        onView(withId(R.id.comentario)).perform(typeText(currentDesc), closeSoftKeyboard());

        // Realizamos el clic en el botón de confirmar
        onView(withId(R.id.ibtn_edit)).perform(click());
        UtilsTests.espera(10000);
        // Verificar que modifySuccessful es true
        assertTrue(reservasAmpliado.isModifySuccessful());
    }
}

