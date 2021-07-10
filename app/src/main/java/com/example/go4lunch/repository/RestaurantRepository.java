package com.example.go4lunch.repository;

import com.example.go4lunch.model.Details;
import com.example.go4lunch.model.Restaurant;

import java.util.List;

public interface RestaurantRepository {

    interface GetRestaurantsCallback{
        void onSuccess(List<Restaurant> restaurants);

        void onError(Exception exception);
    }

    void getRestaurants(String location, String radius, String type, String key, GetRestaurantsCallback callback);


    interface GetDetailsCallback{
        void onSuccess(Details details);

        void onError(Exception exception);
    }
    void getDetails(String placeID, String fields, String key, GetDetailsCallback callback);
}
