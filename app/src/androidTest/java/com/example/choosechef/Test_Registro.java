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

import java.util.Random;

/**
 * Clase desarrollada por ELENA
 * Para realizar los tests referentes al registro de usuarios
 */
@RunWith(AndroidJUnit4.class)
public class Test_Registro {
    @Rule
    public IntentsTestRule<Activity_registro> activityRule = new IntentsTestRule<>(Activity_registro.class);
    private Activity_registro registro;
    private Context context;

    @Before
    public void setUp() {
        FastClient.initialize(ApplicationProvider.getApplicationContext());
        context = ApplicationProvider.getApplicationContext();
        registro = activityRule.getActivity();
    }



    // Nuevo usuario, se crea correctamente (hay conexión)
    @Test
    public void testValidCredentials() {
        // Genera un nombre de usuario aleatorio
        String randomUsername = generateRandomUsername();

        // Simulamos la entrada de datos en los campos de registro
        onView(withId(R.id.edt_usuario_registro)).perform(replaceText(randomUsername));
        onView(withId(R.id.edt_contraseña_registro)).perform(replaceText("correctPass"));
        onView(withId(R.id.edt_contraseña2_registro)).perform(replaceText("correctPass"));

        // Realizamos el clic en el botón de registro
        onView(withId(R.id.ibtn_registrarse_registro)).perform(click());

        espera();

        // Aseguramos que el registro sea exitoso
        assertTrue(registro.isRegistrationSuccessful());
    }
    // // Nuevo usuario, no se crea correctamente (no hay conexión)
    @Test
    public void testRegisterWhenNoNetwork() {
        // Simular no tener conexión de red (configurando el estado de red en falso)
        Utils.setNetworkAvailable(false);
        espera();
        // Genera un nombre de usuario aleatorio
        String randomUsername = generateRandomUsername();

        // Simulamos la entrada de datos en los campos de registro
        onView(withId(R.id.edt_usuario_registro)).perform(replaceText(randomUsername));
        onView(withId(R.id.edt_contraseña_registro)).perform(replaceText("correctPass"));
        onView(withId(R.id.edt_contraseña2_registro)).perform(replaceText("correctPass"));
        // Realizamos el clic en el botón de registro
        onView(withId(R.id.ibtn_registrarse_registro)).perform(click());
        espera();
        // Aseguramos que el registro sea fallido por falta de conexión
        assertFalse(registro.isRegistrationSuccessful());
    }

    // Las contraseñas no coinciden, no se crea el usuario
    @Test
    public void testInvalidPassword() {

        // Simulamos la entrada de datos en los campos de registro
        onView(withId(R.id.edt_usuario_registro)).perform(replaceText("user"));
        onView(withId(R.id.edt_contraseña_registro)).perform(replaceText("password"));
        onView(withId(R.id.edt_contraseña2_registro)).perform(replaceText("wrongpassword"));

        // Realizamos el clic en el botón de registro
        onView(withId(R.id.ibtn_registrarse_registro)).perform(click());

        // Aseguramos que el registro sea fallido debido a la contraseña inválida
        assertFalse(registro.isRegistrationSuccessful());
    }

    // El usuario ya existe, no se crea el usuario
    @Test
    public void testInvalidUsername() {

        // Simulamos la entrada de datos en los campos de registro
        onView(withId(R.id.edt_usuario_registro)).perform(replaceText("1"));
        onView(withId(R.id.edt_contraseña_registro)).perform(replaceText("pruebappass"));
        onView(withId(R.id.edt_contraseña2_registro)).perform(replaceText("pruebappass"));

        // Realizamos el clic en el botón de registro
        onView(withId(R.id.ibtn_registrarse_registro)).perform(click());

        // Aseguramos que el registro sea fallido debido al nombre de usuario inválido
        assertFalse(registro.isRegistrationSuccessful());
    }

    // Faltan datos, no se crea el usuario
    @Test
    public void testEmptyFields() {

        // Simulamos la entrada de datos en los campos de registro
        onView(withId(R.id.edt_usuario_registro)).perform(replaceText(""));
        onView(withId(R.id.edt_contraseña_registro)).perform(replaceText(""));
        onView(withId(R.id.edt_contraseña2_registro)).perform(replaceText(""));

        // Realizamos el clic en el botón de registro
        onView(withId(R.id.ibtn_registrarse_registro)).perform(click());

        // Aseguramos que el registro sea fallido debido al nombre de usuario inválido
        assertFalse(registro.isRegistrationSuccessful());
    }

    // Método para generar un nombre de usuario aleatorio
    private String generateRandomUsername() {
        Random random = new Random();
        int randomNumber = random.nextInt(1000); // Genera un número aleatorio entre 0 y 999
        return "test" + randomNumber; // Devuelve un nombre de usuario único cada vez que se llama
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