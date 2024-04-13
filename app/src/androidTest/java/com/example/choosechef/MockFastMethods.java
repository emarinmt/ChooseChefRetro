package com.example.choosechef;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MockFastMethods implements FastMethods {

    @Override
    public Call<TokenResponse> login(String usuario, String password) {
        return null;
    }

    @Override
    public Call<User> recuperar_info(String token) {
        return null;
    }

    @Override
    public Call<String> modificarUsuario(String token, User user) {
        return null;
    }

    @Override
    public Call<String> crear(User user) {
        return null;
    }

    @Override
    public Call<List<User>> recuperar_chefs() {
        return new Call<List<User>>() {
            @Override
            public Response<List<User>> execute() {
                // Simular una respuesta exitosa con una lista predefinida de chefs
                List<User> testUsers = new ArrayList<>();
                testUsers.add(new User(0, "Chef1", "nameChef1", "passChef1", "descChef1", "Location1", "emailChef1", "phoneChef1",
                        "chef", "Food1", "Service1", 0));
                testUsers.add(new User(1, "Chef2", "nameChef2", "passChef2", "descChef2", "Location2", "emailChef2", "phoneChef2",
                        "chef", "Food2", "Service2", 0));
                return Response.success(testUsers);
            }

            @Override
            public void enqueue(Callback<List<User>> callback) {
                // No es necesario implementar para la simulación
            }

            @Override
            public boolean isExecuted() {
                return false;
            }

            @Override
            public void cancel() {
                // No es necesario implementar para la simulación
            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public Call<List<User>> clone() {
                return null;
            }

            @Override
            public Request request() {
                return null;
            }

        };

    }

    @Override
    public Call<List<String>> recuperar_provincias() {
        return null;
    }

    @Override
    public Call<String> crear(Reserva reserva) {
        return null;
    }
}