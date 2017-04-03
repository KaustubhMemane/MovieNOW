package com.kmema.android.movienow;
import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

final class JsonToSimple {

    static MovieDB[] jsonConvertString(Context context, String MovieJsonString) throws JSONException
    {
        MovieDB[] parseMovieData = null;

        JSONObject movieDBJson = new JSONObject(MovieJsonString);

        JSONArray movieArray = movieDBJson.getJSONArray("results");
        parseMovieData = new MovieDB[movieArray.length()];

        for(int i =0 ; i< movieArray.length();i++)
        {
            String poster_path ;
            String originalTitle;
            String backdrop_path;
            String overview;
            String vote_average;
            String release_date;
            String id;
            JSONObject oneMovieDB = movieArray.getJSONObject(i);

            poster_path = oneMovieDB.getString("poster_path");
            originalTitle  = oneMovieDB.getString("original_title");
            backdrop_path = oneMovieDB.getString("backdrop_path");
            overview = oneMovieDB.getString("overview");
            vote_average = oneMovieDB.getString("vote_average");
            release_date = oneMovieDB.getString("release_date");
            id = oneMovieDB.getString("id");
            parseMovieData[i] = new MovieDB(poster_path,originalTitle,backdrop_path,overview,vote_average,release_date,id);
        }
        return parseMovieData;
    }
}
