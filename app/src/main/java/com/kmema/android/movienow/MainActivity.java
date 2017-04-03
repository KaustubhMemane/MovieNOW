package com.kmema.android.movienow;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import org.json.JSONException;
import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    int currentposition;
    GridView gridView;
    GridImageViewerClass adapter;
    GridImageViewerClass adapter1tosave;
    private ProgressBar pb_progressBar;
    public static SQLiteDatabase mDb;
    String MovieC;
    static Cursor cursor;
    private GoogleApiClient client;
    private static final String LIFE_KEY = "callbacks";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pb_progressBar = (ProgressBar) findViewById(R.id.pbloadingIndicator);
        MovieListDatabaseHelper dbHelper = new MovieListDatabaseHelper(this);

        mDb = dbHelper.getWritableDatabase();
        cursor = getAllMovie();
        gridView = (GridView) findViewById(R.id.gridView);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(LIFE_KEY)) {
                int postion = savedInstanceState.getInt(LIFE_KEY);
                gridView.smoothScrollToPosition(postion);
            }
        }

        setupSharedPreferences();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    public Cursor getAllMovie() {
        MovieListDatabaseHelper dbHelper = new MovieListDatabaseHelper(MainActivity.this);
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor1 =
                sqLiteDatabase.query(MovieDataBaseContract.MovieDatabaseEntry.TABLE_NAME,
                        new String[]{
                                MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_TITLE,
                                MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_OVERVIEW,
                                MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_RATINGS,
                                MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_RELEASE_DATE,
                                MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_BACKPATH,
                                MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_POSTER_LINK,
                                MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_ID
                        },
                        null,
                        null,
                        null,
                        null,
                        MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_ID
                );
        if (cursor1 != null && cursor1.getCount() > 0) {
            return cursor1;
        }
        return null;
    }


    @Override
    protected void onSaveInstanceState(Bundle saveInstanceState) {

        int currentposition = gridView.getFirstVisiblePosition();
        saveInstanceState.putInt(LIFE_KEY, currentposition);
        super.onSaveInstanceState(saveInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int currentpostion = savedInstanceState.getInt(LIFE_KEY);
        gridView.setSelection(currentpostion);
    }

    public void passingCursorGridView(Cursor cursor) {
        final String[] posterLinks = new String[cursor.getCount()];
        final String[] title = new String[cursor.getCount()];
        final String[] overview = new String[cursor.getCount()];
        final String[] ratings = new String[cursor.getCount()];
        final String[] release_date = new String[cursor.getCount()];
        final String[] backdrop_path = new String[cursor.getCount()];
        final String[] Id = new String[cursor.getCount()];
        int i = 0;

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                title[i] = cursor.getString(cursor.getColumnIndex("movieTitle"));
                overview[i] = cursor.getString(cursor.getColumnIndex("movieOverview"));
                ratings[i] = cursor.getString(cursor.getColumnIndex("movieRatings"));
                release_date[i] = cursor.getString(cursor.getColumnIndex("movieReleaseDate"));
                backdrop_path[i] = cursor.getString(cursor.getColumnIndex("movieBackPath"));
                posterLinks[i] = "http://image.tmdb.org/t/p/w500/" + cursor.getString(cursor.getColumnIndex("moviePosterLink"));
                Id[i] = cursor.getString(cursor.getColumnIndex("movieID"));
                cursor.moveToNext();
                i++;
            }
        }
        GridView gridView1 = (GridView) findViewById(R.id.gridView);
        adapter = new GridImageViewerClass(MainActivity.this, posterLinks);
        gridView1.setAdapter(adapter);

        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = MainActivity.this;
                Class ReceivingObject = ChildActivityPage.class;
                Intent newIntent = new Intent(context, ReceivingObject);
                newIntent.putExtra("TITLE", title[position]);
                newIntent.putExtra("OVERVIEW", overview[position]);
                newIntent.putExtra("RATING", ratings[position]);
                newIntent.putExtra("RELEASE_DATE", release_date[position]);
                newIntent.putExtra("BACKDROP_PATH", backdrop_path[position]);
                newIntent.putExtra("MOVIE_ID", Id[position]);
                newIntent.putExtra("POSTER_PATH", posterLinks[position]);
                startActivity(newIntent);
            }
        });
    }

    void gridView(final MovieDB[] s) {
        final String[] title = new String[s.length];
        final String[] overview = new String[s.length];
        final String[] ratings = new String[s.length];
        final String[] release_date = new String[s.length];
        final String[] backdrop_path = new String[s.length];
        final String[] posterLinks = new String[s.length];
        final String[] Id = new String[s.length];
        for (int i = 0; i < s.length; i++) {
            title[i] = s[i].originalTitle;
            overview[i] = s[i].overview;
            ratings[i] = s[i].vote_average;
            release_date[i] = s[i].release_date;
            backdrop_path[i] = s[i].backdrop_path;
            posterLinks[i] = "http://image.tmdb.org/t/p/w500/" + s[i].poster_path;
            Id[i] = s[i].id;
        }

        GridImageViewerClass adapter = new GridImageViewerClass(MainActivity.this, posterLinks);
        gridView.setAdapter(adapter);
        currentposition = gridView.getLastVisiblePosition();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = MainActivity.this;
                Class ReceivingObject = ChildActivityPage.class;
                Intent newIntent = new Intent(context, ReceivingObject);
                newIntent.putExtra("TITLE", title[position]);
                newIntent.putExtra("OVERVIEW", overview[position]);
                newIntent.putExtra("RATING", ratings[position]);
                newIntent.putExtra("RELEASE_DATE", release_date[position]);
                newIntent.putExtra("BACKDROP_PATH", backdrop_path[position]);
                newIntent.putExtra("MOVIE_ID", Id[position]);
                newIntent.putExtra("POSTER_PATH", posterLinks[position]);
                startActivity(newIntent);
            }
        });
    }

    void makeMovieDBquery(String link) {
        URL jsonResponse = ConnectingNetwork.buildUrl(link);
        new MovieDbAsyncTask().execute(jsonResponse);
    }

    void showErrorMessage() {
        Context context = MainActivity.this;
        String ErrorMessage = "Error Please Try Again";
        Toast.makeText(context, ErrorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.preference_movie_choice_value))) {
            loadMovieOnChoice(sharedPreferences);
        }
    }

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page")
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public class MovieDbAsyncTask extends AsyncTask<URL, Void, MovieDB[]> {

        @Override
        protected MovieDB[] doInBackground(URL... params) {
            Context context = MainActivity.this;
            URL searchURL = params[0];
            String jsonResponse;
            try {
                final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    jsonResponse = ConnectingNetwork.getResponseFromHttpUrl(searchURL);
                    return JsonToSimple.jsonConvertString(MainActivity.this, jsonResponse);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb_progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(MovieDB[] s) {
            super.onPostExecute(s);
            pb_progressBar.setVisibility(View.INVISIBLE);
            if (s != null) {
                gridView(s);
            } else {
                showErrorMessage();
             }
        }
    }

    public void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        loadMovieOnChoice(sharedPreferences);
    }

    public void loadMovieOnChoice(SharedPreferences sharedPreferences) {

        MovieC = sharedPreferences.getString(getString(R.string.preference_movie_choice_value), getString(R.string.preference_pop_value));

        if (MovieC.equals(getString(R.string.preference_top_value))) {
            String topRatedlink = "http://api.themoviedb.org/3/movie/top_rated?api_key=/*ENTER YOUR API HERE*/";
            makeMovieDBquery(topRatedlink);
        } else if (MovieC.equals(getString(R.string.preference_pop_value))) {

            String popularMovies = "http://api.themoviedb.org/3/movie/popular?api_key=/*ENTER YOUR API HERE*/";
            makeMovieDBquery(popularMovies);
        } else if (MovieC.equals(getString(R.string.preference_MyFav_value))) {
            cursor = getAllMovie();

            if (cursor != null) {
                passingCursorGridView(cursor);
            }
        } else {
            final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie?api_key=/*ENTER YOUR API HERE*/";
            makeMovieDBquery(MOVIE_BASE_URL);
        }
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemThatwasSelected = item.getItemId();

        if (itemThatwasSelected == R.id.id_favorite) {
            Intent startSettingActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static boolean checkDuplicate(String title, String posterpath) {
        Cursor cursor1 =
                mDb.query(
                        MovieDataBaseContract.MovieDatabaseEntry.TABLE_NAME,
                        null,
                        "movieTitle = ? AND moviePosterLink = ?", new String[]{title, posterpath},
                        null,
                        null,
                        null);

        if (cursor1 != null && cursor1.getCount() > 0) {
            return true;
        }
        return false;
    }
    public Cursor find(String title, String posterpath) {
        Cursor cursor1 =
                mDb.query(
                        MovieDataBaseContract.MovieDatabaseEntry.TABLE_NAME,
                        new String[]{MovieDataBaseContract.MovieDatabaseEntry._ID},
                        "movieTitle = ? AND moviePosterLink = ?", new String[]{title, posterpath},
                        null,
                        null,
                        null);

        if (cursor1 != null && cursor1.getCount() > 0) {
            return cursor1;
        }
        return null;
    }
}
