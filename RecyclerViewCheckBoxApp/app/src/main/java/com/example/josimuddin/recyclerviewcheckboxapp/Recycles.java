package com.example.josimuddin.recyclerviewcheckboxapp;

/**
 * Created by JosimUddin on 19/11/2017.
 */

public class Recycles {
    private String name, position;
    private int images;

    public Recycles() {
    }

    public Recycles(String name, String position, int images) {
        this.name = name;
        this.position = position;
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getImages() {
        return images;
    }

    public void setImages(int images) {
        this.images = images;
    }
}
