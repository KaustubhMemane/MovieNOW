package com.kmema.android.movienow;

import android.net.Uri;
import android.provider.BaseColumns;

class MovieDataBaseContract {

    static  final String AUTHORITY = "com.kmema.android.movienow";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://"+ AUTHORITY);
    static final String PATH_TO_TABLE_MOVIE = "movieList";

    static final class MovieDatabaseEntry implements BaseColumns
    {
        public static  final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TO_TABLE_MOVIE).build();
        static final String TABLE_NAME = "movieList";
        static final String COLUMN_MOVIE_TITLE = "movieTitle";
        static final String COLUMN_MOVIE_OVERVIEW = "movieOverview";
        static final String COLUMN_MOVIE_RATINGS = "movieRatings";
        static final String COLUMN_MOVIE_RELEASE_DATE = "movieReleaseDate";
        static final String COLUMN_MOVIE_BACKPATH = "movieBackPath";
        static final String COLUMN_MOVIE_POSTER_LINK = "moviePosterLink";
        static final String COLUMN_MOVIE_TOP = "movieTop";
        static final String COLUMN_MOVIE_POP = "moviePop";
        static final String COLUMN_MOVIE_MY_FAV = "movieMyFav";
        static final String COLUMN_MOVIE_ID = "movieID";
    }
}
