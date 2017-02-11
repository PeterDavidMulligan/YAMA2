/*
 * Created on 02/02/17 00:02 by aezur.
 * Copyright (c) 2017
 *
 * Last modified: 02/02/17 00:02
 */

package com.example.aezur.yama.Utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {
    private static final String TAG = JsonUtils.class.getSimpleName();
    private static String RESULTS = "results";  //JSONArray name

    /**
     * Returns a usable JSON Array to allow data to be
     * easily searched.
     * @param jsonData String containing the JSON data
     * @return JSONArray containing parsed data
     */
    public static JSONArray getJsonArray(String jsonData) {
        JSONObject root = null;
        JSONArray movieArray = null;
        try {
            root = new JSONObject(jsonData);
            movieArray = root.getJSONArray(RESULTS);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return movieArray;
    }

    /**
     * Returns a single movie from the json string
     * @param jsonData Json query data
     * @param index movie's place in array
     * @return
     */
    public static JSONObject getMovie(String jsonData, int index) {
        JSONArray movies = getJsonArray(jsonData);
        JSONObject movie = null;
        try {
            movie = movies.getJSONObject(index);
        }
        catch(JSONException e) {
            e.printStackTrace();
        }
        return movie;
    }

    /**
     * Get the value from a single movie by key and array index from query result
     * @param jsonData json data from query
     * @param key key name of key/value pair
     * @param index place in array
     * @return value from key/value pair
     */
    public static String getMovieData(String jsonData, String key, int index) {
        JSONObject movie = getMovie(jsonData, index);
        String movieData = null;
        try{
            movieData = movie.getString(key);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return movieData;
    }

    /**
     * Return the amount of movies returned by query result
     * @param jsonData Query result
     * @return Count
     */
    public static int getMovieCount(String jsonData) {
        JSONArray movies = getJsonArray(jsonData);
        int length = movies.length();
        return length;
    }
}
