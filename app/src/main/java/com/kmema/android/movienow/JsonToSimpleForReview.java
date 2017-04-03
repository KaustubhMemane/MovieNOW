package com.kmema.android.movienow;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class JsonToSimpleForReview {
    static MovieDBReview[] jsonConvertString(Context context, String MovieJsonReviewString) throws JSONException
    {
        MovieDBReview[] parseMovieReviewData = null;
        JSONObject movieDBJson = new JSONObject(MovieJsonReviewString);
        JSONArray movieReviewArray = movieDBJson.getJSONArray("results");
        // here we are reading the size of result array from JSON which will vary for sure
        parseMovieReviewData = new MovieDBReview[movieReviewArray.length()];
        for(int i =0 ; i< movieReviewArray.length();i++)
        {
            String id;
            String author;
            String content;
            String url;
            JSONObject oneMovieVideoData = movieReviewArray.getJSONObject(i);
            id = oneMovieVideoData.getString("id");
            author  = oneMovieVideoData.getString("author");
            content = oneMovieVideoData.getString("content");
            url = oneMovieVideoData.getString("url");
            parseMovieReviewData[i] = new MovieDBReview(id,author,content,url);
        }
        return parseMovieReviewData;
    }
}
