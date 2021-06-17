package com.example.go4lunch.model;

import com.google.gson.annotations.SerializedName;

public class Restaurant {


    @SerializedName("name")
    private String name;
    @SerializedName("id")
    private int id;
    @SerializedName("kind")
    private String kind;
    @SerializedName("address")
    private String address;
    @SerializedName("information")
    private String information;
    @SerializedName("distance")
    private int distance;
    @SerializedName("note")
    private double note;
    @SerializedName("imageURL")
    private String imageURL;


    public Restaurant(String name, int id, String kind, String address, String information, int distance, double note, String imageURL) {
        this.name = name;
        this.id = id;
        this.kind = kind;
        this.address = address;
        this.information = information;
        this.distance = distance;
        this.note = note;
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getKind() {
        return kind;
    }

    public String getAddress() {
        return address;
    }

    public String getInformation() {
        return information;
    }

    public int getDistance() {
        return distance;
    }

    public double getNote() {
        return note;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setNote(double note) {
        this.note = note;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
