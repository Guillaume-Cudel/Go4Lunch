package com.example.go4lunch.model;

import java.util.List;

public interface RestaurantRepository {

    public interface GetRestaurantsCallback{
        void onSuccess(List<Restaurant> restaurants);

        void onError(Exception exception);
    }

    void getRestaurants(String location, String radius, String type, String key, GetRestaurantsCallback callback);


}
