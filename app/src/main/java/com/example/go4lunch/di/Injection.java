package com.example.go4lunch.di;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.go4lunch.model.RestaurantRepository;
import com.example.go4lunch.model.RestaurantRepositoryImpl;
import com.example.go4lunch.network.ApiService;
import com.example.go4lunch.network.RestaurantViewModel;
import com.example.go4lunch.network.ViewModelFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Injection {

    public static Retrofit provideRetrofit(){

        return new retrofit2.Retrofit.Builder()
                .baseUrl(ApiService.PLACES_API_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private static ApiService provideApiService() {
        Retrofit retrofit = provideRetrofit();
        return retrofit.create(ApiService.class);
    }

    public static RestaurantRepository provideRestaurantRepository(){

        ApiService apiService = provideApiService();
        return new RestaurantRepositoryImpl(apiService);
    }

    public static ViewModelFactory provideViewModelFactory(){
        RestaurantRepository repository = provideRestaurantRepository();
        return new ViewModelFactory(repository);
    }

    public static RestaurantViewModel provideRestaurantViewModel(FragmentActivity activity){
        ViewModelFactory mViewModelFactory = provideViewModelFactory();
        return new ViewModelProvider(activity, mViewModelFactory).get(RestaurantViewModel.class);
    }



}
