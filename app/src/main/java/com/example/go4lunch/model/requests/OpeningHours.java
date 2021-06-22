package com.example.go4lunch.model.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OpeningHours {

    @SerializedName("open_now")
    @Expose
    private String openNow;

    public String getOpenNow() {
        return openNow;
    }
}
