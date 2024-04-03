package com.example.choosechef;
/**
 * Clase desarrollada por ELENA
 * para gestionar la información del usuario
 */
public class  ProfileResponse{
// ELIMINAR?
    //Eva añado usuario y contraseña, necesarios para la actividad de modificación de perfil
    private int id; // Elena
    String user;
    String password;
    String name;
    String adress;
    String phone;

    public ProfileResponse() {
    }

    public ProfileResponse(int id, String user, String name, String adress, String phone,String password) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.adress = adress;
        this.phone = phone;
        this.password = password;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
