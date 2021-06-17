package com.example.go4lunch.ui.restaurants_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.NavigationActivity;
import com.example.go4lunch.R;
import com.example.go4lunch.di.Injection;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.network.ApiService;
import com.example.go4lunch.network.RestaurantViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantsListFragment extends Fragment {


    private RestaurantsListAdapter adapter;
    private RecyclerView recyclerView;



    public RestaurantsListFragment() {
    }


    public static RestaurantsListFragment newInstance() {
        return (new RestaurantsListFragment());
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavigationActivity navigationActivity = (NavigationActivity) getActivity();
        double mLongitude = navigationActivity.getDoubleLongitude();
        double mLatitude = navigationActivity.getDoubleLatitude();
        String longitudeText = Double.toString(mLongitude);
        String latitudeText =  Double.toString(mLatitude);
        String locationText = latitudeText + "," + longitudeText;

        // todo mettre la location
        Injection.provideRestaurantViewModel(getActivity()).getRestaurants(locationText);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurants_list, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_restaurant_list);

        return view;

    }

    /*private void generateDataList(List<Restaurant> restaurantList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RestaurantsListAdapter(restaurantList, this.getActivity());
        recyclerView.setAdapter(adapter);
    }*/

}