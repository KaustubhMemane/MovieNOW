package com.kmema.android.movienow;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


class JsonToSimpleForVideo {
    static MovieDBVideo[] jsonConvertString(Context context, String MovieJsonVideoString) throws JSONException
    {
        MovieDBVideo[] parseMovieVideoData = null;
        JSONObject movieDBJson = new JSONObject(MovieJsonVideoString);
        JSONArray movieArray = movieDBJson.getJSONArray("results");
        // here we are reading the size of result array from JSON which will vary for sure
        parseMovieVideoData = new MovieDBVideo[movieArray.length()];
        for(int i =0 ; i< movieArray.length();i++)
        {
            String id ;
            String key;
            String name;
            String site;
            String size;
            String type;
            JSONObject oneMovieVideoData = movieArray.getJSONObject(i);
            id = oneMovieVideoData.getString("id");
            key  = oneMovieVideoData.getString("key");
            name = oneMovieVideoData.getString("name");
            site = oneMovieVideoData.getString("site");
            size = oneMovieVideoData.getString("size");
            type = oneMovieVideoData.getString("type");
            parseMovieVideoData[i] = new MovieDBVideo(id,key,name,site,size,type);
        }
        return parseMovieVideoData;
    }
}
