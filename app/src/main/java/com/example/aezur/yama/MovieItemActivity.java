/*
 * Created on 02/02/17 18:04 by aezur.
 * Copyright (c) 2017
 *
 * Last modified: 08/02/17 11:06
 */

package com.example.aezur.yama;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.aezur.yama.Utils.JsonUtils;
import com.example.aezur.yama.Utils.NetworkUtils;
import com.squareup.picasso.Picasso;

/**
 * Loads and displays movie data from passed in intent EXTRAs
 */
public class MovieItemActivity extends AppCompatActivity {
    private final static String TAG = MovieItemActivity.class.getSimpleName();

    private TextView mTitleTextView;
    private TextView mDescriptionTextView;
    private TextView mReleaseDateTextView;
    private TextView mRatingTextView;
    private ImageView mMovieImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_item);

        mTitleTextView = (TextView) findViewById(R.id.tv_movieItem_movieTitle);
        mDescriptionTextView = (TextView) findViewById(R.id.tv_movieItem_movieDescription);
        mReleaseDateTextView = (TextView) findViewById(R.id.tv_movieItem_movieReleaseDate);
        mRatingTextView = (TextView) findViewById(R.id.tv__rating);
        mMovieImageView = (ImageView) findViewById(R.id.iv_movieItem_movieImage);

        Intent intent = getIntent();

        if(intent.hasExtra("MovieData") && intent.hasExtra("MovieIndex")) {
            int index = intent.getIntExtra("MovieIndex", 0);
            String movieData = intent.getStringExtra("MovieData");
            String movieTitle = JsonUtils.getMovieData(movieData, "title", index);
            String movieImagePath = JsonUtils.getMovieData(movieData, "poster_path", index);
            String movieDesc = JsonUtils.getMovieData(movieData, "overview", index);
            String movieReleaseDate = JsonUtils.getMovieData(movieData, "release_date", index);
            String rating = JsonUtils.getMovieData(movieData, "vote_average", index);
            movieImagePath = NetworkUtils.buildPosterUrl(movieImagePath);
            Picasso.with(getApplicationContext())
                    .load(movieImagePath)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(mMovieImageView);
            mRatingTextView.setText(rating);
            mTitleTextView.setText(movieTitle);
            mDescriptionTextView.setText(movieDesc);
            mReleaseDateTextView.setText("Release Date: " + movieReleaseDate);
        }
    }
}
