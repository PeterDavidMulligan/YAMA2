/*
 * Created on 28/01/17 12:26 by Peter Mulligan.
 * Copyright (c) 2017
 *
 * Last modified: 30/01/17 08:13
 */

package com.example.aezur.yama.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * NetworkUtils is a class for static helper methods
 * for building URLs and querying the TMDb servers.
 */
public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

   //String and int to hold sort order selection parameter for query URL
    private static String mSortOrder = "";
    //Base url for image lookups
    private static String mImageBaseUrl = "http://image.tmdb.org/t/p/w185/";

    /**
     * Set the String for the sort paramenter of the query URL
     * and Integer for sort selection
     * @param sortBy
     */
    public static void setMovieSortOrder(int sortBy) {
        switch(sortBy) {
            case 0:
                mSortOrder = "top_rated";
                break;
            case 1:
                mSortOrder = "popular";
                break;
            default:
                mSortOrder = "top_rated";
                break;
        }
    }

    /**
     * Uses Uri.Builder to construct the query URL
     * @return Url created by Uri.Builder
     */
    public static URL buildMovieQueryUrl(int sortOrder) {
        setMovieSortOrder(sortOrder);
        //Strings for building the query URL
        String mQueryScheme = "https";
        String mQueryBaseUrl = "api.themoviedb.org";
        String mQueryApiVersion = "3";
        String mSearchParam = "movie";
        String mApiKey = "api_key";
        String mApiKeyValue = "251dd872ddce793e87ad5d51001d4e85";

        Uri uri = new Uri.Builder().
                scheme(mQueryScheme).    //Protocol being used
                authority(mQueryBaseUrl).    //server being queryed
                path(mQueryApiVersion).  //path & appendPath insert a / before the string
                appendPath(mSearchParam).
                appendPath(mSortOrder).
                appendQueryParameter(mApiKey, mApiKeyValue).  //inserts q(key)=(value)_
                build();
        URL url = null;
        try {
            url = new URL (uri.toString());
            Log.d(TAG, "buildSearchUrl: Success");
            Log.d(TAG, "buildSearchUrl: " + url.toString());
        }
        catch (MalformedURLException e) {
            Log.d(TAG, "buildSearchUrl: Failure");
            e.printStackTrace();
        }
        return url;
    }

    public static String buildPosterUrl(String moviePath) {
        return mImageBaseUrl + moviePath;
    }
    /**
     * Query the TMDb server
     * @param url Url to query
     * @return String returned by query
     * @throws IOException
     */
    public static String getResponseFromMovieQueryUrl(URL url) throws IOException {
        //Open the connection
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {   //Scan the input
            InputStream in = connection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if(hasInput) {
                String data = scanner.next();
                Log.d(TAG, "getResponseFromHttpUrl : Success");
                Log.d(TAG, "getResponseFromHttpUrl - Response : " + data);
                return data;
            }
            else {
                Log.d(TAG, "getResponseFromHttpUrl : Failure");
                return null;
            }
        }
        finally {
            Log.d(TAG, "getResponseFromHttpUrl: Disconnecting.");
            connection.disconnect();
        }
    }
}
