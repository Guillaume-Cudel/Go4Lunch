package com.example.go4lunch.model.firestore;

import androidx.annotation.Nullable;

public class UserFirebase {

    private String uid;
    private String username;
    private Boolean isChoosed;
    @Nullable
    private String urlPicture;

    public UserFirebase() { }

    public UserFirebase(String uid, String username, @Nullable String urlPicture) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.isChoosed = false;
    }

    // --- GETTERS ---
    public String getUid() { return uid; }
    public String getUsername() { return username; }
    @Nullable
    public String getUrlPicture() { return urlPicture; }
    public Boolean getIsChoosed() { return isChoosed; }

    // --- SETTERS ---
    public void setUsername(String username) { this.username = username; }
    public void setUid(String uid) { this.uid = uid; }
    public void setUrlPicture(@Nullable String urlPicture) { this.urlPicture = urlPicture; }
    public void setIsChoosed(Boolean mentor) { isChoosed = mentor; }
}
