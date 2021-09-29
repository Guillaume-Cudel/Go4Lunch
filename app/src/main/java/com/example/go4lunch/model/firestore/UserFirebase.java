package com.example.go4lunch.model.firestore;

import androidx.annotation.Nullable;

public class UserFirebase {

    private String uid;
    private String username;
    //todo remove boolean
    @Nullable
    private String urlPicture;
    private String restaurantChoosed;
    private String restaurantName;


    public UserFirebase() { }


    public UserFirebase(String uid, String username, @Nullable String urlPicture) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.restaurantName = null;
        this.restaurantChoosed = null;
    }

    // --- GETTERS ---
    public String getUid() { return uid; }
    public String getUsername() { return username; }
    @Nullable
    public String getUrlPicture() { return urlPicture; }
    public String getRestaurantChoosed() { return restaurantChoosed; }
    public String getRestaurantName() { return restaurantName; }

}
