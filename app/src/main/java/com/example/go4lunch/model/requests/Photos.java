package com.example.go4lunch.model.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Photos {

    @SerializedName("photo_reference")
    @Expose
    private String photo_reference;

    public String getPhoto() {
        return photo_reference;
    }
}
