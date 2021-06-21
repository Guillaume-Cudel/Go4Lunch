package com.example.go4lunch.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Restaurant {


    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("kind")
    private String kind;
    @SerializedName("vicinity")
    private String vicinity;
    @SerializedName("opening_hours")
    private String opening_hours;
    @SerializedName("location")
    private double location;
    @SerializedName("rating")
    private double rating;
    @SerializedName("photo_reference")
    private String photos;


    public Restaurant(String name, String kind, String vicinity, String opening_hours, double location, double rating, String photos) {
        this.name = name;

        this.kind = kind;
        this.vicinity = vicinity;
        this.opening_hours = opening_hours;
        this.location = location;
        this.rating = rating;
        this.photos = photos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getOpening_hours() {
        return opening_hours;
    }

    public void setOpening_hours(String opening_hours) {
        this.opening_hours = opening_hours;
    }

    public double getLocation() {
        return location;
    }

    public void setLocation(double location) {
        this.location = location;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }
}
