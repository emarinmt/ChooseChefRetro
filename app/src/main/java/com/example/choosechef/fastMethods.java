package com.example.choosechef;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface fastMethods {

    //API's Login Method
    @GET("login")
    Call<LoginResponse> login(@Query("nombre") String nombre, @Query("password") String password);

}
