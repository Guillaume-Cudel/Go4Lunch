package com.example.go4lunch.network;

import com.example.go4lunch.model.requests.GetRestaurantsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place/";

    @GET("nearbysearch/json?")
    Call<GetRestaurantsResponse> getAllRestaurants(@Query("location") String location, @Query("radius") String radius,
                                                   @Query("type") String type, @Query("key") String key);




    // https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=43.6536576,1.4419125&radius=1000&type=restaurant&key=AIzaSyBpPAJjNZ2X4q0xz3p_zK_uW3MdZCpD704
}
