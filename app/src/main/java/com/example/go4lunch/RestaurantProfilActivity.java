package com.example.go4lunch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.go4lunch.ui.BaseActivity;

public class RestaurantProfilActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_profil_restaurant;
    }
}