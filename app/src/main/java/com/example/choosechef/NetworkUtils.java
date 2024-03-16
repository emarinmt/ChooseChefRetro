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
    //LOG_TAG variable with the name of the class
    private static final String LOG_TAG =
            NetworkUtils.class.getSimpleName();

    // Constants for the various components of the Books API request.

    // Base endpoint URL for the Books API.
    private static final String RICK_URL = "https://rickandmortyapi.com/api/character";
    // Parameter for the search string.
    private static final String QUERY_PARAM = "name";
    // Parameter to filter by alive

    private static final String STATUS = "alive";


    /**
     * Static method to make the actual query to the rickandmorty API.
     *
     * @param queryString the query string.
     * @param aliveString the filter status.
     * @return the JSON response string from the query.
     */
    //takes the search term as a String parameter and returns the JSON String response from the API

    static String getCharacterInfo(String queryString, String aliveString){
        //variables for connecting to the internet, reading the incoming data, and holding the response string
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String characterJSONString = null;

        try {
            Uri builtURI;
            if (aliveString != null){
                // checkbox alive clicked
                builtURI= Uri.parse(RICK_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, queryString)
                        .appendQueryParameter(STATUS, aliveString)
                        .build();
            } else {
                builtURI = Uri.parse(RICK_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, queryString)
                        .build();
            }

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

            //Convert the StringBuilder object to a String and store it in the bookJSONString variable
            characterJSONString = builder.toString();



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
        Log.d(LOG_TAG, characterJSONString);

        return characterJSONString;
    }
}
