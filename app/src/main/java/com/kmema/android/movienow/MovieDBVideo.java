package com.kmema.android.movienow;


public class MovieDBVideo {

    String videoId;
    String videoKey;
    String videoName;
    String videoSite;
    String videoSize;
    String videoType;

    MovieDBVideo(String videoId,String videoKey,String videoName,String videoSite,String videoSize,String videoType)
    {
        this.videoId =videoId;
        this.videoKey = videoKey;
        this.videoName = videoName;
        this.videoSite = videoSite;
        this.videoSize = videoSize;
        this.videoType = videoSize;
    }
}
