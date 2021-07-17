package com.example.go4lunch.model;

import com.example.go4lunch.model.requests.Geometry;
import com.example.go4lunch.model.requests.OpeningHours;
import com.example.go4lunch.model.requests.Photos;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Restaurant {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("vicinity")
    @Expose
    private String vicinity;
    @SerializedName("opening_hours")
    @Expose
    private OpeningHours opening_hours;
    @SerializedName("geometry")
    @Expose
    private Geometry geometry;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("photos")
    @Expose
    private List<Photos> photos;
    @SerializedName("types")
    @Expose
    private List<String> types;

    @SerializedName("place_id")
    @Expose
    private String place_id;


    public Restaurant(String name, String vicinity, OpeningHours opening_hours, Geometry geometry,
                      String rating, List<Photos> photos, List<String> types) {

        this.name = name;
        this.vicinity = vicinity;
        this.opening_hours = opening_hours;
        this.geometry = geometry;
        this.rating = rating;
        this.photos = photos;
        this.types = types;
    }

    public String getName() {
        return name;
    }

    public String getVicinity() {
        return vicinity;
    }

    public OpeningHours getOpening_hours() {
        return opening_hours;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public List<Photos> getPhotos() {
        return photos;
    }

    public String getRating() {
        return rating;
    }

    public List<String> getTypes() {
        return types;
    }

    public String getPlace_id() {
        return place_id;
    }
}
