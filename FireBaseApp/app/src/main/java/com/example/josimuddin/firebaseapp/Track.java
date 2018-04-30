package com.example.josimuddin.firebaseapp;

/**
 * Created by JosimUddin on 10/11/2017.
 */

public class Track {
    private String trackId;
    private String trackName;
    private int rating;

    public Track(String trackId, String trackName, int rating) {
        this.trackId = trackId;
        this.trackName = trackName;
        this.rating = rating;
    }

    public Track() {
        //empty constructor.......
    }


    public String getTrackName() {
        return trackName;
    }

    public int getRating() {
        return rating;
    }


}
