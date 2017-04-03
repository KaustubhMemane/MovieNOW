package com.kmema.android.movienow;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
public class ChildActivityPage extends AppCompatActivity {
    String title;
    String release_date;
    String rating;
    String overview;
    String backdrop_path;
    String poster_path;
    String id;
    private ProgressBar videopb_progressBar;
    Context contex;
    TextView mDisplayTextTitle;
    TextView mDisplayTextOverview;
    TextView mDisplayTextRating;
    TextView mDisplayTextReleaseDate;
    ImageView backpadthimage;
    ImageView posterImageHold;
    public Button acceptButton;
    RecyclerView waitlistRecyclerView;
    RecyclerView reviewListRecycleView;
    TextView tv_reviewTitle;
    Intent intentThatStartTheActivity;
    Button btunlike;
    TextView trailerText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);
        trailerText = (TextView) findViewById(R.id.videoId);
        mDisplayTextTitle = (TextView) findViewById(R.id.tvDisplay);
        mDisplayTextOverview = (TextView) findViewById(R.id.tvDisplayOverview);
        mDisplayTextRating = (TextView) findViewById(R.id.tvDisplayRatings);
        mDisplayTextReleaseDate = (TextView) findViewById(R.id.tvDisplayReleaseDate);
        backpadthimage = (ImageView) findViewById(R.id.backpathImage);
        posterImageHold = (ImageView) findViewById(R.id.posterImageViewHolder);
        acceptButton = (Button) findViewById(R.id.likeButton);
        videopb_progressBar = (ProgressBar) findViewById(R.id.videopbloadingIndicator1);
        tv_reviewTitle = (TextView) findViewById(R.id.reviewId);
        waitlistRecyclerView = (RecyclerView) this.findViewById(R.id.all_guests_list_view);
        waitlistRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewListRecycleView = (RecyclerView) this.findViewById(R.id.all_review_list_view);
        reviewListRecycleView.setLayoutManager(new LinearLayoutManager(this));
        intentThatStartTheActivity = getIntent();
        btunlike = (Button) findViewById(R.id.dislikeButton);


        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (intentThatStartTheActivity.hasExtra("TITLE")) {
            title = intentThatStartTheActivity.getStringExtra("TITLE");
            mDisplayTextTitle.setText(title);
        }

        if (intentThatStartTheActivity.hasExtra("BACKDROP_PATH")) {
            backdrop_path = intentThatStartTheActivity.getStringExtra("BACKDROP_PATH");
            Picasso.with(ChildActivityPage.this)
                    .load("http://image.tmdb.org/t/p/w780" + backdrop_path).fit()
                    .into(backpadthimage);
        }

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(title);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMyFav();
            }
        });

        btunlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMyFav();
            }
        });

        contex = ChildActivityPage.this;

        if (intentThatStartTheActivity.hasExtra("RELEASE_DATE")) {
            release_date = intentThatStartTheActivity.getStringExtra("RELEASE_DATE");
            release_date = "Release Date : " + release_date;
            mDisplayTextReleaseDate.setText(release_date);
        }
        if (intentThatStartTheActivity.hasExtra("RATING")) {
            rating = intentThatStartTheActivity.getStringExtra("RATING");
            rating = "Rating : " + rating + "/10";
            mDisplayTextRating.setText(rating);
        }
        if (intentThatStartTheActivity.hasExtra("OVERVIEW")) {
            overview = intentThatStartTheActivity.getStringExtra("OVERVIEW");
            mDisplayTextOverview.setText(overview);
        }

        if (intentThatStartTheActivity.hasExtra("MOVIE_ID")) {
            id = intentThatStartTheActivity.getStringExtra("MOVIE_ID");
        }
        if (intentThatStartTheActivity.hasExtra("POSTER_PATH")) {
            poster_path = intentThatStartTheActivity.getStringExtra("POSTER_PATH");
            poster_path = poster_path.replace("http://image.tmdb.org/t/p/w500/", "");

            Picasso.with(ChildActivityPage.this)
                    .load("http://image.tmdb.org/t/p/w500" + poster_path).fit()
                    .into(posterImageHold);
        }

        Boolean inserting = false;
        inserting = checkDuplicate(intentThatStartTheActivity.getStringExtra("TITLE"), poster_path.replace("http://image.tmdb.org/t/p/w500/", ""));

        if (inserting == true) {
            acceptButton.setVisibility(View.INVISIBLE);
            btunlike.setVisibility(View.VISIBLE);
        } else {
            acceptButton.setVisibility(View.VISIBLE);
            btunlike.setVisibility(View.INVISIBLE);
        }
        setMovieTrailer();
    }



    public void setMovieTrailer() {
        String movieLink = "http://api.themoviedb.org/3/movie/" + id + "/videos?api_key=/*ENTER YOUR API HERE*/";
        String movieReviewLink = "http://api.themoviedb.org/3/movie/" + id + "/reviews?api_key=/*ENTER YOUR API HERE*/";

        URL movieJSONURL = ConnectingNetwork.buildUrl(movieLink);
        URL movieReviewJSON = ConnectingNetwork.buildUrl(movieReviewLink);

        new MovieDBVideoAsyncCtask().execute(movieJSONURL);
        new MovieDBReviewAsyncCtask().execute(movieReviewJSON);
    }


    public class MovieDBReviewAsyncCtask extends AsyncTask<URL, Void, MovieDBReview[]> {

        @Override
        protected MovieDBReview[] doInBackground(URL... params) {
            Context context = ChildActivityPage.this;
            URL searchURL = params[0];
            String jsonReviewResponse;
            try {

                final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {

                    jsonReviewResponse = ConnectingNetwork.getResponseFromHttpUrl(searchURL);
                    return JsonToSimpleForReview.jsonConvertString(context, jsonReviewResponse);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            videopb_progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(MovieDBReview[] movieDBReviews) {
            super.onPostExecute(movieDBReviews);
            videopb_progressBar.setVisibility(View.INVISIBLE);

            if (movieDBReviews != null) {
                reviewView(movieDBReviews);
            } else {
                showErrorMessage();
            }
        }
    }
    void reviewView(final MovieDBReview[] movieDBReviews) {
        String[] reviewAuthor = new String[movieDBReviews.length];
        String[] reviewContent = new String[movieDBReviews.length];
        String[] reviewURL = new String[movieDBReviews.length];
        for (int i = 0; i < movieDBReviews.length; i++) {
            reviewAuthor[i] = movieDBReviews[i].reviewAuthor;
            reviewContent[i] = movieDBReviews[i].reviewContent;
            reviewURL[i] = movieDBReviews[i].reviewURL;
        }
        Context context = ChildActivityPage.this;
        if (reviewAuthor.length != 0) {
            ReviewListAdapter mReviewAdapter = new ReviewListAdapter(context, null, reviewAuthor, reviewContent, reviewURL);
            reviewListRecycleView.setAdapter(mReviewAdapter);
        } else {
            tv_reviewTitle.setVisibility(View.INVISIBLE);
        }
    }

    public class MovieDBVideoAsyncCtask extends AsyncTask<URL, Void, MovieDBVideo[]> {
        @Override
        protected MovieDBVideo[] doInBackground(URL... params) {
            Context context = ChildActivityPage.this;
            URL searchURL = params[0];
            String jsonVideoResponse;
            try {
                final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {

                    jsonVideoResponse = ConnectingNetwork.getResponseFromHttpUrl(searchURL);
                    return JsonToSimpleForVideo.jsonConvertString(context, jsonVideoResponse);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            videopb_progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPostExecute(MovieDBVideo[] movieDBVideos) {
            super.onPostExecute(movieDBVideos);
            videopb_progressBar.setVisibility(View.INVISIBLE);
            if (movieDBVideos != null) {
                videoView(movieDBVideos);
            } else {
                showErrorMessage();
            }
        }
    }

    void videoView(final MovieDBVideo[] movieDBVideos) {
        String[] videoKey = new String[movieDBVideos.length];
        String[] videoName = new String[movieDBVideos.length];

        for (int i = 0; i < movieDBVideos.length; i++) {
            videoKey[i] = movieDBVideos[i].videoKey;
            videoName[i] = movieDBVideos[i].videoName;
        }
        if(videoKey.length == 0 || videoKey.length < 0)
        {
                trailerText.setVisibility(View.INVISIBLE);
        }
        Context context = ChildActivityPage.this;
        GuestListAdapter mAdapter = new GuestListAdapter(context, null, videoKey, videoName);
        waitlistRecyclerView.setAdapter(mAdapter);
    }

    void showErrorMessage() {
        trailerText.setVisibility(View.INVISIBLE);
        Context context = ChildActivityPage.this;
        String ErrorMessage = "No Trailer Available";
        Toast.makeText(context, ErrorMessage, Toast.LENGTH_LONG).show();
    }

    boolean checkDuplicate(String title, String posterpath) {
        MovieListDatabaseHelper dbHelper = new MovieListDatabaseHelper(this);
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        Cursor cursor1 =
                sqLiteDatabase.query(
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

        MovieListDatabaseHelper dbHelper = new MovieListDatabaseHelper(this);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        Cursor cursor1 = sqLiteDatabase.query(
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

    public void deleteMyFav() {
        Cursor cursor1 = null;
        Cursor cursorfind = null;
        cursorfind = find(intentThatStartTheActivity.getStringExtra("TITLE"), poster_path.replace("http://image.tmdb.org/t/p/w500/", ""));
        String id = null;
        if (cursorfind != null) {
            if (cursorfind.moveToFirst()) {
                id = cursorfind.getString(cursorfind.getColumnIndex("_id"));
            }
            Uri uri = MovieDataBaseContract.MovieDatabaseEntry.CONTENT_URI;
            uri = uri.buildUpon().appendPath(id).build();
            getContentResolver().delete(uri, null, null);
            Toast.makeText(this, title + " Unliked", Toast.LENGTH_SHORT).show();
            btunlike.setVisibility(View.INVISIBLE);
            acceptButton.setVisibility(View.VISIBLE);

            MovieListDatabaseHelper dbHelper = new MovieListDatabaseHelper(this);
            SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

            cursor1 = sqLiteDatabase.query(MovieDataBaseContract.MovieDatabaseEntry.TABLE_NAME,
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
                            MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_RATINGS
                    );
        }
    }
    public void addMyFav() {
        Boolean inserting = false;
        inserting = MainActivity.checkDuplicate(title, poster_path);
        poster_path = poster_path.replace("http://image.tmdb.org/t/p/w500/", "");
        release_date = release_date.replace("Release Date : ", "");
        rating = intentThatStartTheActivity.getStringExtra("RATING");
        if (inserting == true) {
            Toast.makeText(contex, "ALREADY PRESENT", Toast.LENGTH_SHORT).show();
            return;
        } else {
            acceptButton.setVisibility(View.INVISIBLE);
            btunlike.setVisibility(View.VISIBLE);
            Toast.makeText(contex, title + " LIKED", Toast.LENGTH_SHORT).show();
            ContentValues cv = new ContentValues();
            cv.put(MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_TITLE, title);
            cv.put(MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_OVERVIEW, overview);
            cv.put(MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_RATINGS, rating);
            cv.put(MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_RELEASE_DATE, release_date);
            cv.put(MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_BACKPATH, backdrop_path);
            cv.put(MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_POSTER_LINK, poster_path);
            cv.put(MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_TOP, "NO");
            cv.put(MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_POP, "NO");
            cv.put(MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_MY_FAV, "YES");
            cv.put(MovieDataBaseContract.MovieDatabaseEntry.COLUMN_MOVIE_ID, id);
            Uri uri = getContentResolver().insert(MovieDataBaseContract.MovieDatabaseEntry.CONTENT_URI, cv);
        }
    }
}
