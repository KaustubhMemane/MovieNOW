package com.kmema.android.movienow;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by kmema on 3/12/2017.
 */

public class DatabaseContract {

    public static final class dataBaseEntry implements BaseColumns {
        public static final String TABLE_NAME = "movieDataBase";
        public static final String COLUMN_MOVIE_NAME = "movieName";
        public static final String COLUMN_MOVIE_SUMMARY = "movieSummary";
        public static final String COLUMN_MOVIE_RATING = "movieRating";
        public static final String COLUMN_MOVIE_DATE = "movieReleaseDate";
        public static final String COLUMN_MOVIE_BACKDROP_PATH = "backdropLink";
        public static final String COLUMN_MOVIE_POSTER = "posterLink";
        public static final String COLUMN_MOVIE_ID = "movieID";
    }
}
