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
 * Para realizar los tests referentes a la lista de todos los usuarios
 * CORRESPONDERIA A LA CLASE ACTIVITY_ADMIN
 */
@RunWith(AndroidJUnit4.class)
public class Test_AjustesAdmin {

    @Rule
    public IntentsTestRule<Activity_login> activityRule = new IntentsTestRule<>(Activity_login.class);
    private Context context;
    private Activity_admin actAdmin;

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
        // Verificar que se abre Activity_admin después de clicar en ajustes
        intended(hasComponent(Activity_admin.class.getName()));
        // Obtener la instancia de Activity_admin
        actAdmin = ((Activity_admin) UtilsTests.getActivityInstance(Activity_admin.class));
    }

    // Recuperación de lista de usuarios correcta (hay conexión)
    @Test
    public void testRecuperarUsersWithNetwork() {
        // Simular tener conexión de red (configurando el estado de red en true)
        Utils.setNetworkAvailable(true);
        UtilsTests.espera(10000);
        actAdmin.recuperarDatos();
        UtilsTests.espera(10000);
        // Verificar que contentSuccessful es true
        assertTrue(actAdmin.isContentSuccessful());
    }

    // Recuperación de lista de usuarios incorrecta (no hay conexión)
    @Test
    public void testRecuperarUsersWhenNoNetwork() {
        // Simular no tener conexión de red (configurando el estado de red en falso)
        Utils.setNetworkAvailable(false);
        UtilsTests.espera(10000);
        actAdmin.recuperarDatos();
        UtilsTests.espera(10000);
        // Verificar que userList está vacía
        assertEquals(0, actAdmin.userList.size());
        // Verificar que contentSuccessful es falso
        assertFalse(actAdmin.isContentSuccessful());
    }

    // Recuperación de lista de usuarios correcta, añadiendo un chef para comprobar que se modifica la lista
    @Test
    public void testRecuperarChefsSuccess() {
        int initialSize = actAdmin.userList.size();// Número de usuarios antes del registro, para poder comparar
        onView(withId(R.id.btn_logout_user)).perform(click()); // Logout
        // Registro, simulando la entrada de datos
        onView(withId(R.id.ibtn_registro)).perform(click());
        String randomUsername = UtilsTests.generateRandomUsername(); // Genera un nombre de usuario aleatorio
        onView(withId(R.id.edt_usuario_registro)).perform(replaceText(randomUsername));
        onView(withId(R.id.edt_contraseña_registro)).perform(replaceText("test"));
        onView(withId(R.id.edt_contraseña2_registro)).perform(replaceText("test"));
        onView(withId(R.id.ibtn_registrarse_registro)).perform(click());
        UtilsTests.espera(10000);;
        // Iniciar sesión como admin
        onView(withId(R.id.edt_usuario_login)).perform(typeText("admin"));
        onView(withId(R.id.edt_contra_login)).perform(typeText("admin"), closeSoftKeyboard());
        onView(withId(R.id.ibtn_entrar_login)).perform(click());
        onView(withId(R.id.btn_ajustes)).perform(click());
        actAdmin.recuperarDatos();
        UtilsTests.espera(10000);
        int finalSize = actAdmin.userList.size(); // Número de usuarios después del registro
        // Aseguramos que el el número de usuarios inicial + 1 coincide con el actual
        assertEquals((initialSize + 1), finalSize);
        // Aseguramos que la recuperación sea exitosa
        assertTrue(actAdmin.isContentSuccessful());
    }

    // PROBANDO FILTRO  (PARA SIGUIENTE TEA IMPLEMENTAR BOTON)
    // Recuperación de lista de usuarios correcta con filtro
    @Test
    public void testRecuperarUsersFiltrados() {
        UtilsTests.espera(20000);
        actAdmin.buscar("test");
        UtilsTests.espera(20000);
        // Verificar que contentSuccessful es true
        assertTrue(actAdmin.isContentSuccessful());
    }

}