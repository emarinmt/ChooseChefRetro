package com.example.choosechef;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;

import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Para realizar los tests referentes a la modificación de datos de usuarios por parte del admin
 * CORRESPONDERIA A LA CLASE ACTIVITY_USER_AMPLIADO
 */
@RunWith(AndroidJUnit4.class)
public class Test_AdminAmpliado {
// PROBANDO PARA HACER EL TEST DE ELIMINAR, NO ACABADA
    @Rule
    public IntentsTestRule<Activity_login> activityRule = new IntentsTestRule<>(Activity_login.class);
    private Context context;
    private Activity_admin actAdmin;
    private Activity_user_ampliado actAdminAmpl;
    int initialSize;
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
        initialSize = actAdmin.userList.size();
        /* Buscar y hacer clic en un usuario específico en la lista
        Clicaremos en aquellos usuarios cucyo nombre empiezan por test
        ya que son los creados en los test y no hay problema en eliminarlos
        NO SE PUEDE IMPLEMENTAR DEBIDO AL EMULADOR, NO PERMITE ANIMACIONES, POR LO TANTO
        NO PODEMOS HACER SCROLL. SI EL NOMBRE ESTÁ EN LA PANTALLA SÍ SE CLICA
        //String nombreUsuario = "4"; // Nombre del usuario deseado (parcial)
        //clickOnItemWithText(nombreUsuario);
         */

    }

    // Ampliación de usuario correcta
    @Test
    public void testAmpliarUserValid() {
        // Hacer clic en el primer elemento de la lista (suponiendo que hay al menos un usuario en la lista)
        onView(withId(R.id.rv_users))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // Verificar que la actividad user_ampliado se inicia correctamente
        intended(hasComponent(Activity_user_ampliado.class.getName()));
        // Obtener la instancia de Activity_user_ampliado
        actAdminAmpl = ((Activity_user_ampliado) UtilsTests.getActivityInstance(Activity_user_ampliado.class));
        // Verificar que contentSuccessful es true
        UtilsTests.espera(10000);
        assertTrue(actAdminAmpl.isContentSuccessful());
    }
    // Ampliación de usuario incorrecta, simulamos un intent erroneo
    @Test
    public void testAmpliarUserInvalid() {
        // Hacer clic en el primer elemento de la lista (suponiendo que hay al menos un usuario en la lista)
        onView(withId(R.id.rv_users))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // Verificar que la actividad user_ampliado se inicia correctamente
        intended(hasComponent(Activity_user_ampliado.class.getName()));
        // Obtener la instancia de Activity_user_ampliado
        actAdminAmpl = ((Activity_user_ampliado) UtilsTests.getActivityInstance(Activity_user_ampliado.class));
        // Simular un Intent inválido (null o sin el extra "user")
        Intent invalidIntent = null;
        // Llamar manualmente obtenerIntent() y pasar el Intent inválido
        actAdminAmpl.obtenerIntent(invalidIntent);
        UtilsTests.espera(10000);
        // Verificar que contentSuccessful es false
        assertFalse(actAdminAmpl.isContentSuccessful());
    }

    // Modificación datos usuario correcta
    @Test
    public void testModifyUserValid() {
        // Hacer clic en el primer elemento de la lista (suponiendo que hay al menos un usuario en la lista)
        onView(withId(R.id.rv_users))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // Verificar que la actividad user_ampliado se inicia correctamente
        intended(hasComponent(Activity_user_ampliado.class.getName()));
        // Obtener la instancia de Activity_user_ampliado
        actAdminAmpl = ((Activity_user_ampliado) UtilsTests.getActivityInstance(Activity_user_ampliado.class));
        UtilsTests.espera(10000);
        // Realizamos el clic en el botón de editar
        onView(withId(R.id.ibtn_edit)).perform(click());
        UtilsTests.espera(10000);

        // Obtener texto actual del campo descripción
        String currentDesc = actAdminAmpl.getDescUsuario(); // Ejemplo: método para obtener descripción de usuario

        // Borrar el texto actual del campo descripción
        onView(withId(R.id.edt_descripcion_usuario)).perform(clearText());

        // Escribir un nuevo nombre en el campo descripción
        onView(withId(R.id.edt_descripcion_usuario)).perform(typeText(currentDesc), closeSoftKeyboard());

        // Realizamos el clic en el botón de editar
        onView(withId(R.id.ibtn_edit)).perform(click());
        UtilsTests.espera(10000);
        // Verificar si la modificación fue exitosa
        assertTrue(actAdminAmpl.isModifySuccessful());
    }

    // Eliminación de usuario correcta
    // Hemos creado un filtro para que se muestren solo los usuarios con la palabra
    // "test" y así poder eliminarlos sin problema
    @Test
    public void testDeleteUserValid() {
        // Filtramos los usuarios
        actAdmin.buscar("test");
        UtilsTests.espera(10000);
        // Hacer clic en el SEGUNDO elemento de la lista (HAY UN ERROR CON EL PRIMERO, NO EXISTE)
        onView(withId(R.id.rv_users))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        UtilsTests.espera(10000);
        // Obtener la instancia de Activity_user_ampliado
        actAdminAmpl = ((Activity_user_ampliado) UtilsTests.getActivityInstance(Activity_user_ampliado.class));
        onView(withId(R.id.ibtn_delete)).perform(click());
        UtilsTests.espera(20000);
        // Verificar que deleteSuccessful es true
        assertTrue(actAdminAmpl.isDeleteSuccessful());
        actAdmin.recuperarDatos();
        UtilsTests.espera(20000);
        int finalSize = actAdmin.userList.size();
        // Aseguramos que el el número de usuarios inicial - 1 coincide con el actual
        assertEquals((initialSize - 1), finalSize);
    }

/*
    // RECYCLER VIEW TEXTO QUE COINCIDA
    // DE MOMENTO NO LO PODEMOS IMPLEMENTAR

    public static ViewAction actionOnItemViewWithText(final String itemText, final ViewAction action) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(RecyclerView.class);
            }

            @Override
            public String getDescription() {
                return "Action on item with text: " + itemText;
            }

            @Override
            public void perform(UiController uiController, View view) {
                RecyclerView recyclerView = (RecyclerView) view;
                RecyclerView.Adapter adapter = recyclerView.getAdapter();
                if (adapter != null) {
                    for (int position = 0; position < adapter.getItemCount(); position++) {
                        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
                        if (viewHolder != null) {
                            View itemView = viewHolder.itemView;
                            // Buscar un TextView dentro del itemView
                            TextView textView = itemView.findViewById(R.id.nombre_usuario);
                            if (textView != null && textView.getText().toString().equals(itemText)) {
                                // Realizar la acción sobre el itemView
                                action.perform(uiController, itemView);
                                return;
                            }
                        }
                    }
                }
                // Si no se encuentra el elemento, lanzar una excepción
                throw new PerformException.Builder()
                        .withActionDescription(getDescription())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new RuntimeException("Item with text '" + itemText + "' not found."))
                        .build();
            }
        };
    }

    // Método para realizar clic en un elemento con texto específico en la RecyclerView
    public void clickOnItemWithText(String itemText) {
        onView(withId(R.id.rv_users)).perform(actionOnItemViewWithText(itemText, click()));
    }
*/
}