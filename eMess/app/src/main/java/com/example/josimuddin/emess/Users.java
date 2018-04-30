package com.example.josimuddin.emess;

/**
 * Created by JosimUddin on 08/11/2017.
 */

public class Users {
    private String name;
    private String thumb_image;
    private String email;
    private String mess;


    public Users() {
    }

    public Users(String name, String image, String email) {
        this.name = name;
        this.thumb_image = image;
        this.email = email;
    }

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getthumb_image() {
        return thumb_image;
    }

    public void setthumb_image(String image) {
        this.thumb_image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
