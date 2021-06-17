package com.example.go4lunch.network;

import com.example.go4lunch.model.Restaurant;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place/";

    @GET("nearbysearch/json?")
    Call<List<Restaurant>> getAllRestaurants(@Query("location") String location, @Query("radius") String radius,
                                             @Query("type") String type, @Query("key") String key);





}
