package com.example.aezur.yama.data;

import android.provider.BaseColumns;

public final class MovieContract {
    private MovieContract(){}

    public static class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_RATING = "rating";
        public static final String COLUMN_NAME_POPULARITY = "popularity";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";

    }
}
