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
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Para realizar los tests referentes a la muestra de chefs
 */
@RunWith(AndroidJUnit4.class)
public class Test_Contenido {
    @Rule
    public IntentsTestRule<Activity_login> activityRule = new IntentsTestRule<>(Activity_login.class);
    private Activity_contenido contenido;
    private Context context;

    @Before
    public void setUp() {
        FastClient.initialize(ApplicationProvider.getApplicationContext());
        context = ApplicationProvider.getApplicationContext();
        // Iniciar sesión como usuario de prueba client
        onView(withId(R.id.edt_usuario_login)).perform(typeText("client"));
        onView(withId(R.id.edt_contra_login)).perform(typeText("client"), closeSoftKeyboard());
        onView(withId(R.id.ibtn_entrar_login)).perform(click());
        UtilsTests.espera(10000);
        // Verificar que se abre Activity_contenido después del login
        intended(hasComponent(Activity_contenido.class.getName()));
        // Obtener la instancia de Activity_content
        contenido = ((Activity_contenido) UtilsTests.getActivityInstance(Activity_contenido.class));
    }

    // Recuperación de lista de chefs correcta (hay conexión)
    @Test
    public void testRecuperarChefsWithNetwork() {
        // Simular tener conexión de red (configurando el estado de red en true)
        Utils.setNetworkAvailable(true);
        UtilsTests.espera(10000);
        contenido.recuperarChefs();
        UtilsTests.espera(10000);
        // Verificar que contentSuccessful es true
        assertTrue(contenido.isContentSuccessful());
    }

    // Recuperación de lista de chefs incorrecta (no hay conexión)
    @Test
    public void testRecuperarChefsWhenNoNetwork() {
        // Simular no tener conexión de red (configurando el estado de red en falso)
        Utils.setNetworkAvailable(false);
        UtilsTests.espera(10000);
        contenido.recuperarChefs();
        UtilsTests.espera(10000);
        // Verificar que userList está vacía
        assertEquals(0, contenido.getUserList().size());
        // Verificar que contentSuccessful es falso
        assertFalse(contenido.isContentSuccessful());
        Utils.setNetworkAvailable(true);
    }

    // Recuperación de lista de chefs correcta, añadiendo un chef para comprobar que se modifica la lista
    @Test
    public void testRecuperarChefsSuccess() {
        int initialSize = contenido.userList.size();// Número de chefs antes del registro
        onView(withId(R.id.btn_logout_user)).perform(click()); // Logout
        // Registro, simulando la entrada de datos
        onView(withId(R.id.ibtn_registro)).perform(click());
        String randomUsername = UtilsTests.generateRandomUsername(); // Genera un nombre de usuario aleatorio
        onView(withId(R.id.edt_usuario_registro)).perform(replaceText(randomUsername));
        onView(withId(R.id.edt_contrasena_registro)).perform(replaceText("test"));
        onView(withId(R.id.edt_contrasena2_registro)).perform(replaceText("test"));
        onView(withId(R.id.switch_chef_registro)).perform(click()); // Tipo = Chef
        onView(withId(R.id.ibtn_registrarse_registro)).perform(click());
        UtilsTests.espera(10000);
        // Iniciar sesión como usuario de prueba
        onView(withId(R.id.edt_usuario_login)).perform(typeText("client"));
        onView(withId(R.id.edt_contra_login)).perform(typeText("client"), closeSoftKeyboard());
        onView(withId(R.id.ibtn_entrar_login)).perform(click());
        contenido.recuperarChefs();
        UtilsTests.espera(10000);
        int finalSize = contenido.userList.size(); // Número de chefs después del registro
        // Aseguramos que el el número de chefs inicial + 1 coincide con el actual
        assertEquals((initialSize + 1), finalSize);
        // Aseguramos que la recuperación sea exitosa
        assertTrue(contenido.isContentSuccessful());
    }

    // Recuperación de lista de chefs correcta, añadiendo un cliente para comprobar que no se modifica la lista
    @Test
    public void testRecuperarChefsClient() {
        int initialSize = contenido.userList.size();// Número de chefs antes del registro
        onView(withId(R.id.btn_logout_user)).perform(click()); // Logout
        // Registro, simulando la entrada de datos
        onView(withId(R.id.ibtn_registro)).perform(click());
        String randomUsername = UtilsTests.generateRandomUsername(); // Genera un nombre de usuario aleatorio
        onView(withId(R.id.edt_usuario_registro)).perform(replaceText(randomUsername));
        onView(withId(R.id.edt_contrasena_registro)).perform(replaceText("test"));
        onView(withId(R.id.edt_contrasena2_registro)).perform(replaceText("test"));
        onView(withId(R.id.ibtn_registrarse_registro)).perform(click());
        UtilsTests.espera(10000);
        // Iniciar sesión como usuario de prueba
        onView(withId(R.id.edt_usuario_login)).perform(typeText("client"));
        onView(withId(R.id.edt_contra_login)).perform(typeText("client"), closeSoftKeyboard());
        onView(withId(R.id.ibtn_entrar_login)).perform(click());
        contenido.recuperarChefs();
        UtilsTests.espera(10000);
        int finalSize = contenido.userList.size(); // Número de chefs después del registro
        // Aseguramos que el el número de chefs inicial coincide con el actual
        assertEquals((initialSize), finalSize);
        // Aseguramos que la recuperación sea exitosa
        assertTrue(contenido.isContentSuccessful());
    }

}
