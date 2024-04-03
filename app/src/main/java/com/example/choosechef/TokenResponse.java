package com.example.choosechef;

import com.google.gson.annotations.SerializedName;
/**
 * Clase desarrollada por ELENA
 * Se encarga de manejar la respuesta con el token enviado por el servidor
 */
public class TokenResponse {
// FUNCIONANDO Y REVISADA CON COMENTARIOS
    @SerializedName("token")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}