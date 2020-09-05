package com.example.helperbuddy.HelperClasses;  //package

public class Features {  //Feature class

    //Declared attributes

    private String images;
    private String title;

    public Features(String images, String title) {  //constructor
        this.images = images;
        this.title = title;
    }

    public Features() {  //Default constructor
    }

    //getters and setters

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}  //class
