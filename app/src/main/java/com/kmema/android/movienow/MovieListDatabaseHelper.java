package com.kmema.android.movienow;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MovieListDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_FILE_NAME = "movieList.db";
    public static final int DATABASE_VERSION_NUMBER = 4;


    MovieListDatabaseHelper(Context context)
    {
        super(context,DATABASE_FILE_NAME,null, DATABASE_VERSION_NUMBER);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIELIST_TABLE = "CREATE TABLE " +
                MovieDataBaseContract.MovieDatabaseEntry.TABLE_NAME + " (" +
                MovieDataBaseContract.MovieDatabaseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL," +
                MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL," +
                MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_RATINGS + " TEXT NOT NULL," +
                MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL," +
                MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_BACKPATH + " TEXT NOT NULL," +
                MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_POSTER_LINK + " TEXT NOT NULL," +
                MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_TOP + " TEXT NOT NULL," +
                MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_POP + " TEXT NOT NULL," +
                MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_MY_FAV + " TEXT," +
                MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL" +
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIELIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieDataBaseContract.MovieDatabaseEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
