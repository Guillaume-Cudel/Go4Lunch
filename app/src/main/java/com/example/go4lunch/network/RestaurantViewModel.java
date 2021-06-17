package com.example.go4lunch.network;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.NavigationActivity;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.RestaurantRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantViewModel extends ViewModel {

    private static final String API_KEY = "AIzaSyBpPAJjNZ2X4q0xz3p_zK_uW3MdZCpD704";
    private static final String RADIUS = "1000";
    private static final String TYPE = "restaurant";
    public MutableLiveData<List<Restaurant>> restaurantListLiveData;
    private final RestaurantRepository repository;


    public RestaurantViewModel(RestaurantRepository repository) {
        this.repository = repository;
    }


    public LiveData<List<Restaurant>> getRestaurants(String location) {
        if (restaurantListLiveData == null) {
            restaurantListLiveData = new MutableLiveData<List<Restaurant>>();
            loadRestaurants(location);
        }
        return restaurantListLiveData;
    }


    private void loadRestaurants(String location) {

        repository.getRestaurants(location, RADIUS, TYPE, API_KEY, new RestaurantRepository.GetRestaurantsCallback() {
            @Override
            public void onSuccess(List<Restaurant> restaurants) {
                restaurantListLiveData.postValue(restaurants);
            }

            @Override
            public void onError(Exception exception) {
                restaurantListLiveData.postValue(null);
            }
        });
    }
}

