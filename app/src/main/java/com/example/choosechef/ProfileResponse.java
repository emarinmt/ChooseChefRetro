package com.example.choosechef;

public class  ProfileResponse{
    String user;
    String name;
    String adress;
    String phone;

    public ProfileResponse() {
    }

    public ProfileResponse(String user, String name, String adress, String phone) {
        this.user = user;
        this.name = name;
        this.adress = adress;
        this.phone = phone;
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
