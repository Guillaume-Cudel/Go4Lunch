package com.example.go4lunch.network;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.RestaurantRepository;

public class ViewModelFactory implements ViewModelProvider.Factory {


    private final RestaurantRepository repository;

    public ViewModelFactory(RestaurantRepository repository) {
        this.repository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RestaurantViewModel.class)) {
            return (T) new RestaurantViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}