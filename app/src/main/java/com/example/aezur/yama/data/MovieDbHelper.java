package com.example.aezur.yama.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDbHelper extends SQLiteOpenHelper {
    //Name and version number to be passed in the super constructor
    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;

    //Constructor just to call the super constructor and pass
    //the context into
    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //SQL code for creating table
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                MovieContract.MovieEntry.TABLE_NAME + " (" +
                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieContract.MovieEntry.COLUMN_NAME_TITLE + " TEXT NOT NULL," +
                MovieContract.MovieEntry.COLUMN_NAME_DESCRIPTION + " TEXT NOT NULL," +
                MovieContract.MovieEntry.COLUMN_NAME_RATING + " FLOAT NOT NULL," +
                MovieContract.MovieEntry.COLUMN_NAME_POPULARITY + " FLOAT NOT NULL," +
                MovieContract.MovieEntry.COLUMN_NAME_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
