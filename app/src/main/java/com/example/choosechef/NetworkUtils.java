package com.example.choosechef;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {

    //Constante LOG_TAG con el nombre de la clase
    private static final String LOG_TAG =
            NetworkUtils.class.getSimpleName();

    // Constants for the various components of the API request.

    // Base endpoint URL for the API.
    private static final String API_URL = "https://choose-chef.vercel.app/login";

    //Correspondientes a los parametros requeridos y las restricciones que queramos establecer
    // Parámetro para la cadena de búsqueda
    private static final String QUERY_PARAM = "nombre";
    // Parámetro para la cadena de búsqueda
    private static final String QUERY_PARAM_PASS = "password";


    /**
     * Static method para realizar la consulta a la API.
     *
     * @param queryUserString the query string.
     * @param queryPasswordString the filter status.
     * @return the JSON response string from the query.
     */
    //toma el término de búsqueda como parámetro de cadena y devuelve la respuesta JSON String de la API

    static String getUserInfo(String queryUserString, String queryPasswordString){
        //Variables para conectarse a Internet, leer los datos de entrada y mantener la respuesta en un string
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String userJSONString = null;

        try {
            Uri builtURI;
            builtURI= Uri.parse(API_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, queryUserString)
                    .appendQueryParameter(QUERY_PARAM_PASS, queryPasswordString)
                    .build();

            // Convert the URI to a URL,
            URL requestURL = new URL(builtURI.toString());

            // Open the network connection.
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Get the InputStream.
            InputStream inputStream = urlConnection.getInputStream();

            // Create a buffered reader from that input stream.
            reader = new BufferedReader(new InputStreamReader(inputStream));

            // Use a StringBuilder to hold the incoming response.
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                // Add the current line to the string.
                builder.append(line);
                // Since it's JSON, adding a newline isn't necessary (it won't
                // affect parsing) but it does make debugging a *lot* easier
                // if you print out the completed buffer for debugging.
                builder.append("\n");
            }

            //check the string to see if there is existing response content. Return null if the response is empty
            if (builder.length() == 0) {
                // Stream was empty. No point in parsing.
                return null;
            }

            //Convert the StringBuilder object to a String and store it in the userJSONString variable
            userJSONString = builder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close the connection and the buffered reader.
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        // Write the final JSON response to the log
        Log.d(LOG_TAG, userJSONString);

        return userJSONString;
    }
}
