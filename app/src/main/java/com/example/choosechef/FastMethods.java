package com.example.choosechef;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FastMethods {

    //API's Login Method
    @GET("login")
    Call<Boolean> login(@Query("nombre") String nombre, @Query("password") String password);

}
