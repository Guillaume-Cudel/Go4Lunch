package com.example.go4lunch.model.firestore;

import androidx.annotation.Nullable;

public class UserFirebase {

    private String uid;
    private String username;
    //todo remove boolean
    private Boolean haveChoosed;
    @Nullable
    private String urlPicture;
    private String restaurantID;
    private String restaurantName;


    public UserFirebase() { }


    public UserFirebase(String uid, String username, @Nullable String urlPicture) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.haveChoosed = false;
        this.restaurantName = null;
        this.restaurantID = null;
    }

    // --- GETTERS ---
    public String getUid() { return uid; }
    public String getUsername() { return username; }
    @Nullable
    public String getUrlPicture() { return urlPicture; }
    public Boolean getHaveChoosed() { return haveChoosed; }
    public String getRestaurantID() { return restaurantID; }
    public String getRestaurantName() { return restaurantName; }

}
