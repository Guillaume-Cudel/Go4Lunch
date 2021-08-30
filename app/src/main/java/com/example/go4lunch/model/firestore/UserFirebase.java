package com.example.go4lunch.model.firestore;

import androidx.annotation.Nullable;

public class UserFirebase {

    private String uid;
    private String username;
    private Boolean haveChoosed;
    @Nullable
    private String urlPicture;
    private String restaurantChoosed;
    private String restaurantName;

    public UserFirebase() { }


    public UserFirebase(String uid, String username, @Nullable String urlPicture) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.haveChoosed = false;
        this.restaurantName = null;
        this.restaurantChoosed = null;
    }

    // --- GETTERS ---
    public String getUid() { return uid; }
    public String getUsername() { return username; }
    @Nullable
    public String getUrlPicture() { return urlPicture; }
    public Boolean getHaveChoosed() { return haveChoosed; }
    public String getRestaurantChoosed() { return restaurantChoosed; }
    public String getRestaurantName() { return restaurantName; }

    // --- SETTERS ---
    public void setHaveChoosed(Boolean isChoosed) { this.haveChoosed = isChoosed; }
    public void setRestaurantChoosed(String restaurantChoosed) { this.restaurantChoosed = restaurantChoosed; }
    public void setRestaurantName(String restaurantName) { this.restaurantName = restaurantName; }
}
