package com.example.go4lunch.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.model.Details;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.repository.RestaurantRepository;

import java.util.List;

public class RestaurantViewModel extends ViewModel {

    private static final String API_KEY = "AIzaSyBpPAJjNZ2X4q0xz3p_zK_uW3MdZCpD704";
    private static final String RADIUS = "1000";
    private static final String TYPE = "restaurant";
    private static final String FIELDS = "formatted_phone_number,opening_hours,website";
    private MutableLiveData<List<Restaurant>> restaurantListLiveData;
    public MutableLiveData<Details> detailsLiveData;


    private final RestaurantRepository restaurantRepository;

    public RestaurantViewModel(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }


    public LiveData<List<Restaurant>> getRestaurants(String location) {
        if (restaurantListLiveData == null) {
            restaurantListLiveData = new MutableLiveData<List<Restaurant>>();
            loadRestaurants(location);
        }
        return restaurantListLiveData;
    }


    private void loadRestaurants(String location) {
        restaurantRepository.getRestaurants(location, RADIUS, TYPE, API_KEY, new RestaurantRepository.GetRestaurantsCallback() {
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

    public LiveData<Details> getDetails(String placeID){
        if (detailsLiveData == null){
            detailsLiveData = new MutableLiveData<Details>();
            loadDetails(placeID);
        }
        return detailsLiveData;
    }

    private void loadDetails(String placeID){
        restaurantRepository.getDetails(placeID, FIELDS, API_KEY, new RestaurantRepository.GetDetailsCallback() {
            @Override
            public void onSuccess(Details details) {
                detailsLiveData.postValue(details);
            }

            @Override
            public void onError(Exception exception) {
                detailsLiveData.postValue(null);
            }
        });
    }
}

