/*
 * Created on 27/01/17 15:13 by Peter Mulligan.
 * Copyright (c) 2017
 *
 * Last modified: 08/02/17 11:00
 */

package com.example.aezur.yama.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.example.aezur.yama.R;
import com.squareup.picasso.Picasso;

/**
 * MovieAdapter handles recycling and caching of the RecyclerView items
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private final static String TAG = MovieAdapter.class.getSimpleName();

    private final OnItemClickHandler mOnClickHandler;

    public interface OnItemClickHandler {
        void onClick(int pos);
    }

    private int mNumberOfItems;
    private String mQueryResults;

    /**
     * Container for individual movie views
     * @param numberOfItems Amount of Views to be created.
     * @param queryResults Json data pulled from server
     */
    public MovieAdapter(int numberOfItems, String queryResults, OnItemClickHandler clickHandler) {
        mNumberOfItems = numberOfItems;
        mQueryResults = queryResults;
        mOnClickHandler = clickHandler;
    }

    @Override
    public int getItemCount() {
        return mNumberOfItems;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        //Set the layout of the view
        int layoutID = R.layout.movie_view;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean attachImmediately = false;
        View view = inflater.inflate(layoutID, parent, attachImmediately);

        MovieViewHolder viewHolder = new MovieViewHolder(view, context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(position);
    }

    /**
     * Subclass of RecyclerView.ViewHolder. Helper class to
     * handle RecyclerView population and recycling.
     */
    public class MovieViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        //JSONObject keys
        private static final String POSTER_PATH = "poster_path";
        private ImageView mMovieListItemImage;

        private Context mContext;

        public MovieViewHolder(View view, Context context) {
            super(view);
            mContext = context;
            mMovieListItemImage = (ImageView) itemView.findViewById(R.id.iv_movieImage);
            view.setOnClickListener(this);
        }

        /**
         * Called by onBindViewHolder to keep things neat. Code to be
         * executed when a ViewHolder is bound is placed here. Updates
         * all the views.
         * @param index Index of the item being bound
         */
        public void bind(int index) {
            String posterPath = JsonUtils.getMovieData(mQueryResults, POSTER_PATH, index);
            String posterUrl = NetworkUtils.buildPosterUrl(posterPath);
            Picasso.with(mContext).load(posterUrl).into(mMovieListItemImage);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mOnClickHandler.onClick(adapterPosition);
        }
    }
}
