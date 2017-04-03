package com.kmema.android.movienow;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
public class TestUtil {
    public  static  void insertFakeData(SQLiteDatabase db)
    {
        if(db == null)
        {
            return;
        }
        List<ContentValues> list = new ArrayList<>();
        ContentValues cv = new ContentValues();
        cv.put(MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_TITLE, "Kal_ho_na_ho");
        cv.put(MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_OVERVIEW, "GOOD GOOD GOOD");
        cv.put(MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_RATINGS, "10");
        cv.put(MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_RELEASE_DATE, "10-20-30");
        cv.put(MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_BACKPATH, "/phszHPFVhPHhMZgo0fWTKBDQsJA.jpg");
        cv.put(MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_POSTER_LINK, "/rXBB8F6XpHAwci2dihBCcixIHrK.jpg");
        cv.put(MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_ID, "100000");
        list.add(cv);
        cv = new ContentValues();
        cv.put(MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_TITLE, "Kal_ho_na_ho");
        cv.put(MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_OVERVIEW, "GOOD GOOD GOOD");
        cv.put(MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_RATINGS, "1");
        cv.put(MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_RELEASE_DATE, "10-20-30");
        cv.put(MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_BACKPATH, "/yIZ1xendyqKvY3FGeeUYUd5X9Mm.jpg");
        cv.put(MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_POSTER_LINK, "/aybgjbFbn6yUbsgUMnUbwc2jcWd.jpg");
        cv.put(MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_ID, "100000");
        list.add(cv);
        try
        {
            db.delete (MovieDataBaseContract.MovieDatabaseEntry.TABLE_NAME,null,null);
            for(ContentValues c:list){
                db.insert(MovieDataBaseContract.MovieDatabaseEntry.TABLE_NAME, null, c);
            }
          db.setTransactionSuccessful();
        }
        catch (SQLException ignored) {
        }
        finally
        {
            db.endTransaction();
        }
    }
}
