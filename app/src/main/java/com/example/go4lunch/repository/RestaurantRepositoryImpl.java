package com.example.go4lunch.repository;

import com.example.go4lunch.model.requests.GetDetailsResponse;
import com.example.go4lunch.model.requests.GetRestaurantsResponse;
import com.example.go4lunch.network.ApiService;

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
        Call<GetRestaurantsResponse> call = apiService.getAllRestaurants(location, radius, type, key);
        call.enqueue(new Callback<GetRestaurantsResponse>() {
            @Override
            public void onResponse(Call<GetRestaurantsResponse> call, Response<GetRestaurantsResponse> response) {
                callback.onSuccess(response.body().getResults());
            }

            @Override
            public void onFailure(Call<GetRestaurantsResponse> call, Throwable t) {
                callback.onError(new Exception(t));
            }
        });
    }

    @Override
    public void getDetails(String placeID, String fields, String key, GetDetailsCallback callback){
        Call<GetDetailsResponse> call = apiService.getDetails(placeID, fields, key);
        call.enqueue(new Callback<GetDetailsResponse>() {
            @Override
            public void onResponse(Call<GetDetailsResponse> call, Response<GetDetailsResponse> response) {
                callback.onSuccess(response.body().getResult());
            }

            @Override
            public void onFailure(Call<GetDetailsResponse> call, Throwable t) {
                callback.onError(new Exception(t));
            }
        });
    }
}
