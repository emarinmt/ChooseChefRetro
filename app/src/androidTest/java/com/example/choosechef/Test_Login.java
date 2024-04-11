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
 * Clase desarrollada por ELENA
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

    // Usuario existente y contraseña correcta, login correcto
    @Test
    public void testValidCredentials() {

        // Simulamos la entrada de datos en los campos de login
        onView(withId(R.id.edt_usuario_login)).perform(replaceText("4"));
        onView(withId(R.id.edt_contra_login)).perform(replaceText("4"));

        // Realizamos el clic en el botón de entrar
        onView(withId(R.id.ibtn_entrar_login)).perform(click());

        espera();

        // Aseguramos que el login sea exitoso
        assertTrue(login.isLoginSuccessful());

    }

    // Usuario existente y contraseña incorrecta, login incorrecto
    @Test
    public void testInvalidPassword() {

        // Simulamos la entrada de datos en los campos de login
        onView(withId(R.id.edt_usuario_login)).perform(replaceText("1"));
        onView(withId(R.id.edt_contra_login)).perform(replaceText("3"));

        // Realizamos el clic en el botón de entrar
        onView(withId(R.id.ibtn_entrar_login)).perform(click());

        // Aseguramos que el login sea fallido
        assertFalse(login.isLoginSuccessful());
    }

    // Usuario inexistente , login incorrecto
    @Test
    public void testInvalidUsername() {

        // Simulamos la entrada de datos en los campos de login
        onView(withId(R.id.edt_usuario_login)).perform(replaceText("2"));
        onView(withId(R.id.edt_contra_login)).perform(replaceText("3"));

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
    public void espera() {
        // Esperar un tiempo suficiente para que se complete la operación asíncrona
        try {
            Thread.sleep(5000); // Espera 5 segundos
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
