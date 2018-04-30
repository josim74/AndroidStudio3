package com.example.josimuddin.firebaseapp;

/**
 * Created by JosimUddin on 10/11/2017.
 */

public class Artist {
    private String artistId;
    private String artistName;
    private String artistGener;

    public Artist(String artistId, String artistName, String artistGener) {
        this.artistId = artistId;
        this.artistName = artistName;
        this.artistGener = artistGener;
    }

    public Artist() {
    }

    public String getArtistId() {
        return artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getArtistGener() {
        return artistGener;
    }
}
