/*
 * Created on 27/01/17 14:24 by Peter Mulligan.
 * Copyright (c) 2017
 *
 * Last modified: 31/01/17 16:54
 */

package com.example.aezur.yama;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.aezur.yama.utils.JsonUtils;
import com.example.aezur.yama.utils.MovieAdapter;
import com.example.aezur.yama.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Main activity holds the RecyclerView and pulls the data
 * from the server using NetworkUtils' helper methods inside
 * an AsyncTask.
 */
public class MainActivity extends AppCompatActivity implements MovieAdapter.OnItemClickHandler {
    private final static String TAG = MainActivity.class.getSimpleName();

    //JSONObject keys
    public static final String MOVIE_DATA = "MovieData";
    public static final String MOVIE_INDEX = "MovieIndex";

    //Flag for sorting results by rating/popularity
    private static String SAVED_SORT_ORDER = "SavedSortOrder";

    //UI Views
    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;

    private MovieAdapter mMovieAdapter; //Adapter to hold movie item Views
    private MovieQueryTask mQueryTask;  //Task to get movie data from server
    private String mQueryResults;   //String for storing the movie data

    private static int mSortOrder = 0;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        /*If this isn't the first activity in the activity lifecycle
            set the sort order to whatever is selected
         */
        if (savedInstanceState != null) {
            mSortOrder = savedInstanceState.getInt(SAVED_SORT_ORDER);
        }
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movies);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        //Pull data as Json from server with MovieQueryTask if device is connected to internet
        mQueryTask = new MovieQueryTask();
        try{
            mQueryResults = mQueryTask.execute(NetworkUtils.buildMovieQueryUrl(mSortOrder)).get();
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates an activity for the movie that has been selected
     * @param index The index of the movie in the RecyclerView
     */
    @Override
    public void onClick(int index) {
        Intent mMovieIntent = new Intent(this, MovieItemActivity.class);
        mMovieIntent.putExtra(MOVIE_DATA, mQueryResults);
        mMovieIntent.putExtra(MOVIE_INDEX, index);
        startActivity(mMovieIntent);
    }

    ///// QUERY TASK STUFF /////
    /**
     * A subclass of AsyncTask that queries TMDb and returns Json
     * containing the query results.
     */
    private class MovieQueryTask extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Show the loading wheel
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrls = urls[0];
            String movieSearchResults = null;
            try {
                //Load the data
                movieSearchResults = NetworkUtils.getResponseFromMovieQueryUrl(searchUrls);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return movieSearchResults;
        }
        @Override
        protected void onPostExecute(String result) {
            if(result != null) {
                //Create a GridLayout amount_of_columns wide
                int columns = getResources().getInteger(R.integer.amount_of_columns);
                GridLayoutManager layoutManager = new GridLayoutManager(context, columns);

                mRecyclerView.setLayoutManager(layoutManager);
                mRecyclerView.setHasFixedSize(true);    //helps with optimization

                //Create a MovieAdapter to handle the recycler views
                int itemsPerPage = JsonUtils.getMovieCount(mQueryResults);
                mMovieAdapter = new MovieAdapter(itemsPerPage, mQueryResults, MainActivity.this);
                mRecyclerView.setAdapter(mMovieAdapter);
            }
            else {
                Toast notConnected = Toast.makeText(context,
                        "Could not get movie data from server.\nCheck online status.",
                        Toast.LENGTH_LONG);
                notConnected.show();
            }

            //Hide the loading wheel
            mLoadingIndicator.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Restarts the Activity with the new sort order if the sort
     * order is changed.
     * @param sortOrder Integer to select query sort order
     */
    private void changeSortOrder(int sortOrder) {
        if(sortOrder != mSortOrder) {
            mSortOrder = sortOrder;
            this.recreate();
        }
    }

    ///// ACTIVITY RESTART STUFF /////

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_SORT_ORDER, mSortOrder);
    }

    ///// MENU STUFF /////

    /**
     * Creates the options menu on the Activity header
     * @param menu The menu object to be created
     * @return Returns true after successful task completion
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    /**
     * Handles menu onClicks. Restarts Activity with the correct data
     * based on sort preference. Also checks the appropriate checkbox.
     * @param item Selected MenuItem
     * @return Returns true if recognized item is selected, and false if not.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.sort_rating:
                changeSortOrder(0);
                return true;
            case R.id.sort_popularity:
                changeSortOrder(1);
                return true;
        }
        return false;
    }

    /**
     * Used to change the checked item menu
     * @param menu Menu to be prepared
     * @return Returns true if successful
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        switch(mSortOrder) {
            case 0 :
                menu.findItem(R.id.sort_rating).setChecked(true);
                break;
            case 1 :
                menu.findItem(R.id.sort_popularity).setChecked(true);
                break;
        }
        return true;
    }
}
