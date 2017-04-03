package com.kmema.android.movienow;
class MovieDB {

    String poster_path;
    String originalTitle;
    String backdrop_path;
    String overview;
    String vote_average;
    String release_date;
    String id;
    MovieDB(String MPoster_path, String MOriginalTitle, String MBackdrop_path,
            String MOverview, String MVote_average, String MRelease_date, String MId)
    {
       this.poster_path =MPoster_path;
        this.originalTitle = MOriginalTitle;
        this.backdrop_path = MBackdrop_path;
        this.overview = MOverview;
        this.vote_average =MVote_average;
        this.release_date = MRelease_date;
        this.id = MId;
    }
}
