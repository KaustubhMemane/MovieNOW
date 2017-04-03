package com.kmema.android.movienow;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;


public class MovieContentProvider extends ContentProvider {
    public  static final int MOVIES = 100;
    public static final int MOVIE_WITH_ID = 101;

    private MovieListDatabaseHelper mMovieDBHelper;
    private     static  final UriMatcher sUriMatcher = buildURIMatcher();


    public static UriMatcher buildURIMatcher()
    {
      UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieDataBaseContract.AUTHORITY, MovieDataBaseContract.PATH_TO_TABLE_MOVIE, MOVIES);
        uriMatcher.addURI(MovieDataBaseContract.AUTHORITY, MovieDataBaseContract.PATH_TO_TABLE_MOVIE + "/#", MOVIE_WITH_ID);
        return  uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mMovieDBHelper = new MovieListDatabaseHelper(context);
        return true;
    }



    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase sqLiteDatabase = mMovieDBHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor returnCursor;
        switch(match)
        {
            case MOVIES:
               returnCursor = sqLiteDatabase.query(MovieDataBaseContract.MovieDatabaseEntry.TABLE_NAME,
                        new String[]{
                                MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_TITLE,
                                MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_OVERVIEW,
                                MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_RATINGS,
                                MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_RELEASE_DATE,
                                MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_BACKPATH,
                                MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_POSTER_LINK,
                                MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_TOP,
                                MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_POSTER_LINK,
                                MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_MY_FAV,
                                MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_ID
                        },
                        null,
                        null,
                        null,
                        null,
                        MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_RATINGS
                );

                break;

            default:
                throw new UnsupportedOperationException("Unknown URI ::"+uri);
        }
        returnCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase sqLiteDatabase = mMovieDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match)
        {
            case MOVIES:
                     long id = sqLiteDatabase.insert(MovieDataBaseContract.MovieDatabaseEntry.TABLE_NAME,null,values);
                if(id > 0)
                {
                    returnUri = ContentUris.withAppendedId(MovieDataBaseContract.MovieDatabaseEntry.CONTENT_URI,id);
                }
                else
                {
                    throw new android.database.SQLException("Failed To insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI"+ uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = mMovieDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int movieUnliked;
        switch (match)
        {
            case MOVIE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                movieUnliked = sqLiteDatabase.delete(MovieDataBaseContract.MovieDatabaseEntry.TABLE_NAME, "_id=?",new String[]{id});
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if(movieUnliked != 0)
        {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return movieUnliked;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
