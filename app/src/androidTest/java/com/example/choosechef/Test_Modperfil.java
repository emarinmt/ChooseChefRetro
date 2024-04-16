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
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

/**
 * Para realizar los tests referentes a la modificación del perfil de usuario
 */
@RunWith(AndroidJUnit4.class)
public class Test_Modperfil {

    @Rule
    public IntentsTestRule<Activity_login> activityRule = new IntentsTestRule<>(Activity_login.class);
    private Activity_mod_perfil modperfil;
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
        onView(withId(R.id.btn_mod_perfil)).perform(click());
        espera();
        // Verificar que se abre Activity_mod_perfil después de clicar el boton
        intended(hasComponent(Activity_mod_perfil.class.getName()));
        // Asegurarnos de que la actividad se ha cargado completamente
        onView(withId(R.id.edt_nombre_mod_perfil)).check(matches(isDisplayed()));

        // Obtener la instancia de Activity_mod_perfil
        modperfil = ((Activity_mod_perfil) getActivityInstance(Activity_mod_perfil.class));
    }

    // Modificación de datos correcta (sin contraseña)
    @Test
    public void testValidData() {
        // Simulamos la entrada de datos en los campos
        onView(withId(R.id.edt_nombre_mod_perfil)).perform(replaceText("5"));
        onView(withId(R.id.edt_direccion_mod_perfil)).perform(replaceText("5"));
        onView(withId(R.id.edt_telefono_mod_perfil)).perform(replaceText("5"));

        // Realizamos el clic en el botón de confirmar
        onView(withId(R.id.ibtn_confirmar_mod_perfil)).perform(click());

        espera();

        // Verificar si la modificación fue exitosa
        assertTrue(modperfil.isModifySuccessful());
    }

    // Modificación de datos correcta (con contraseña)
    @Test
    public void testValidDataPass() {
        // Simulamos la entrada de datos en los campos
        onView(withId(R.id.edt_nombre_mod_perfil)).perform(replaceText("5"));
        onView(withId(R.id.edt_direccion_mod_perfil)).perform(replaceText("5"));
        onView(withId(R.id.edt_telefono_mod_perfil)).perform(replaceText("5"));
        onView(withId(R.id.switch_cambio_contraseña)).perform(click());
        onView(withId(R.id.edt_contraseña_anterior_mod_perfil)).perform(replaceText("4"));
        onView(withId(R.id.edt_contraseña_nueva_mod_perfil)).perform(replaceText("newPass"));
        onView(withId(R.id.edt_contraseña2_nueva_mod_perfil)).perform(replaceText("newPass"));

        // Realizamos el clic en el botón de confirmar
        onView(withId(R.id.ibtn_confirmar_mod_perfil)).perform(click());
        espera();

        // Restaurar la contraseña original
        onView(withId(R.id.btn_mod_perfil)).perform(click());
        onView(withId(R.id.switch_cambio_contraseña)).perform(click());
        onView(withId(R.id.edt_contraseña_anterior_mod_perfil)).perform(replaceText("newPass"));
        onView(withId(R.id.edt_contraseña_nueva_mod_perfil)).perform(replaceText("4"));
        onView(withId(R.id.edt_contraseña2_nueva_mod_perfil)).perform(replaceText("4"));
        espera();

        // Realizamos el clic en el botón de confirmar
        onView(withId(R.id.ibtn_confirmar_mod_perfil)).perform(click());
        espera();

        // Aseguramos que la modificación sea exitosa
        assertTrue(modperfil.isModifySuccessful());
    }

    // Modificación de datos incorrecta (contraseña anterior no coincide)
    @Test
    public void testInvalidPass() {
        // Simulamos la entrada de datos en los campos
        onView(withId(R.id.edt_nombre_mod_perfil)).perform(replaceText("probando2"));
        onView(withId(R.id.edt_direccion_mod_perfil)).perform(replaceText("probando2"));
        onView(withId(R.id.edt_telefono_mod_perfil)).perform(replaceText("probando2"));
        onView(withId(R.id.switch_cambio_contraseña)).perform(click());
        onView(withId(R.id.edt_contraseña_anterior_mod_perfil)).perform(replaceText("3"));
        onView(withId(R.id.edt_contraseña_nueva_mod_perfil)).perform(replaceText("newPass"));
        onView(withId(R.id.edt_contraseña2_nueva_mod_perfil)).perform(replaceText("newPass"));

        // Realizamos el clic en el botón de confirmar
        onView(withId(R.id.ibtn_confirmar_mod_perfil)).perform(click());

        // Aseguramos que el registro sea fallido debido al nombre de usuario inválido
        assertFalse(modperfil.isModifySuccessful());
    }

    // Modificación de datos incorrecta (contraseñas nuevas no coincide)
    @Test
    public void testInvalidNewPass() {

        // Simulamos la entrada de datos en los campos
        onView(withId(R.id.edt_nombre_mod_perfil)).perform(replaceText("probando2"));
        onView(withId(R.id.edt_direccion_mod_perfil)).perform(replaceText("probando2"));
        onView(withId(R.id.edt_telefono_mod_perfil)).perform(replaceText("probando2"));
        onView(withId(R.id.switch_cambio_contraseña)).perform(click());
        onView(withId(R.id.edt_contraseña_anterior_mod_perfil)).perform(replaceText("4"));
        onView(withId(R.id.edt_contraseña_nueva_mod_perfil)).perform(replaceText("newPass"));
        onView(withId(R.id.edt_contraseña2_nueva_mod_perfil)).perform(replaceText("newPasswrong"));

        // Realizamos el clic en el botón de confirmar
        onView(withId(R.id.ibtn_confirmar_mod_perfil)).perform(click());

        // Aseguramos que el registro sea fallido debido al nombre de usuario inválido
        assertFalse(modperfil.isModifySuccessful());
    }

    // Faltan datos, no se modifica el perfil
    @Test
    public void testEmptyFields() {

        // Simulamos la entrada de datos en los campos
        onView(withId(R.id.edt_nombre_mod_perfil)).perform(replaceText("probando2"));
        onView(withId(R.id.edt_direccion_mod_perfil)).perform(replaceText("probando2"));
        onView(withId(R.id.edt_telefono_mod_perfil)).perform(replaceText("probando2"));
        onView(withId(R.id.switch_cambio_contraseña)).perform(click());
        onView(withId(R.id.edt_contraseña_anterior_mod_perfil)).perform(replaceText(""));
        onView(withId(R.id.edt_contraseña_nueva_mod_perfil)).perform(replaceText("newPass"));
        onView(withId(R.id.edt_contraseña2_nueva_mod_perfil)).perform(replaceText(""));

        // Realizamos el clic en el botón de confirmar
        onView(withId(R.id.ibtn_confirmar_mod_perfil)).perform(click());
        espera();

        // Aseguramos que el registro sea fallido debido al nombre de usuario inválido
        assertFalse(modperfil.isModifySuccessful());
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