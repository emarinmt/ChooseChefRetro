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

/**
 * Para realizar los tests referentes a la modificación del perfil de usuario
 */
@RunWith(AndroidJUnit4.class)
public class Test_AjustesTipo {
    // PARA COMPROBAR QUE LA PANTALLA POSTERIOR A CLIAR EN AJUSTES VARIA SEGÚN EL TIPO DE USUARIO
    @Rule
    public IntentsTestRule<Activity_login> activityRule = new IntentsTestRule<>(Activity_login.class);
    private Context context;

    @Before
    public void setUp() {
        FastClient.initialize(ApplicationProvider.getApplicationContext());
        context = ApplicationProvider.getApplicationContext();
    }

    // Redirección ajustes admin correcta
    @Test
    public void testAjustesAdminCorrecta() {
        // Iniciar sesión como admin
        onView(withId(R.id.edt_usuario_login)).perform(typeText("admin"));
        onView(withId(R.id.edt_contra_login)).perform(typeText("admin"), closeSoftKeyboard());
        onView(withId(R.id.ibtn_entrar_login)).perform(click());
        espera();
        onView(withId(R.id.btn_ajustes)).perform(click());
        espera();
        // Verificar que se abre Activity_admin después de clicar en ajustes
        intended(hasComponent(Activity_admin.class.getName()));
    }

    // Redirección ajustes chef correcta
    @Test
    public void testAjustesChefCorrecta() {
        // Iniciar sesión como admin
        onView(withId(R.id.edt_usuario_login)).perform(typeText("chef"));
        onView(withId(R.id.edt_contra_login)).perform(typeText("chef"), closeSoftKeyboard());
        onView(withId(R.id.ibtn_entrar_login)).perform(click());
        espera();
        onView(withId(R.id.btn_ajustes)).perform(click());
        // Verificar que se abre Activity_chef después de clicar en ajustes
        intended(hasComponent(Activity_chef.class.getName()));
    }

    // Redirección ajustes client correcta
    @Test
    public void testAjustesClientCorrecta() {
        // Iniciar sesión como admin
        onView(withId(R.id.edt_usuario_login)).perform(typeText("client"));
        onView(withId(R.id.edt_contra_login)).perform(typeText("client"), closeSoftKeyboard());
        onView(withId(R.id.ibtn_entrar_login)).perform(click());
        espera();
        onView(withId(R.id.btn_ajustes)).perform(click());
        // Verificar que se abre Activity_user después de clicar en ajustes
        intended(hasComponent(Activity_user.class.getName()));
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