package com.example.go4lunch.di;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.repository.RestaurantRepository;
import com.example.go4lunch.network.ApiService;
import com.example.go4lunch.repository.RestaurantRepositoryImpl;
import com.example.go4lunch.viewModel.LocationViewModel;
import com.example.go4lunch.viewModel.RestaurantViewModel;
import com.example.go4lunch.viewModel.ViewModelFactory;

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

    public static LocationViewModel provideLocationViewModel(FragmentActivity activity){
        return new ViewModelProvider(activity).get(LocationViewModel.class);
    }



}
