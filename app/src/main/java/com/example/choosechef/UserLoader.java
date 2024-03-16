package com.example.choosechef;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

//Extiende de AsyncTaskLoader y requiere un paràmetro tipo Srting
public class UserLoader extends AsyncTaskLoader<String> {
    private String mUserString; // Para contener la cadena usuario de la consulta
    private String mPasswordString; // Para contener la cadena password de la consulta

    //Constructor
    public UserLoader(Context context, String userString, String passwordString) {
        super(context);
        mUserString = userString;
        mPasswordString = passwordString;
    }

    @Nullable
    @Override
    public String loadInBackground() {
        return NetworkUtils.getCharacterInfo(mUserString, mPasswordString); //CAMBIAR
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        //to start the loadInBackground() method, si no no se inicia la búsqueda
        forceLoad();
    }

}