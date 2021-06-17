package com.example.go4lunch.model;

import com.example.go4lunch.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final ApiService apiService;

    public RestaurantRepositoryImpl(ApiService apiService){
        this.apiService = apiService;
    }

    @Override
    public void getRestaurants(String location, String radius, String type, String key, GetRestaurantsCallback callback) {
        Call<List<Restaurant>> call = apiService.getAllRestaurants(location, radius, type, key);
        call.enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                callback.onError(new Exception(t));
            }
        });
    }
}
