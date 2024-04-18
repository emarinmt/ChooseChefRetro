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
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Para realizar los tests referentes al login de usuarios
 */
@RunWith(AndroidJUnit4.class)
public class Test_Login {

    @Rule
    public IntentsTestRule<Activity_login> activityRule = new IntentsTestRule<>(Activity_login.class);

    private Activity_login login;
    private Context context;

    @Before
    public void setUp() {
        FastClient.initialize(ApplicationProvider.getApplicationContext());
        context = ApplicationProvider.getApplicationContext();
        login = activityRule.getActivity();
    }

    // Usuario existente y contrasena correcta, login correcto
    @Test
    public void testValidCredentials() {

        // Simulamos la entrada de datos en los campos de login
        onView(withId(R.id.edt_usuario_login)).perform(replaceText("client"));
        onView(withId(R.id.edt_contra_login)).perform(replaceText("client"));

        // Realizamos el clic en el botón de entrar
        onView(withId(R.id.ibtn_entrar_login)).perform(click());

        UtilsTests.espera(10000);

        // Aseguramos que el login sea exitoso
        assertTrue(login.isLoginSuccessful());

    }

    // Usuario existente y contrasena incorrecta, login incorrecto
    @Test
    public void testInvalidPassword() {

        // Simulamos la entrada de datos en los campos de login
        onView(withId(R.id.edt_usuario_login)).perform(replaceText("client"));
        onView(withId(R.id.edt_contra_login)).perform(replaceText("wrongPass"));

        // Realizamos el clic en el botón de entrar
        onView(withId(R.id.ibtn_entrar_login)).perform(click());

        // Aseguramos que el login sea fallido
        assertFalse(login.isLoginSuccessful());
    }

    // Usuario inexistente , login incorrecto
    @Test
    public void testInvalidUsername() {

        // Simulamos la entrada de datos en los campos de login
        onView(withId(R.id.edt_usuario_login)).perform(replaceText("clientwrong"));
        onView(withId(R.id.edt_contra_login)).perform(replaceText("pass"));

        // Realizamos el clic en el botón de entrar
        onView(withId(R.id.ibtn_entrar_login)).perform(click());

        // Aseguramos que el login sea fallido
        assertFalse(login.isLoginSuccessful());
    }

    // Faltan datos, login incorrecto
    @Test
    public void testEmptyFields() {

        // Simulamos la entrada de datos en los campos de login
        onView(withId(R.id.edt_usuario_login)).perform(replaceText(""));
        onView(withId(R.id.edt_contra_login)).perform(replaceText(""));

        // Realizamos el clic en el botón de entrar
        onView(withId(R.id.ibtn_entrar_login)).perform(click());

        // Aseguramos que el login sea fallido
        assertFalse(login.isLoginSuccessful());
    }

}
