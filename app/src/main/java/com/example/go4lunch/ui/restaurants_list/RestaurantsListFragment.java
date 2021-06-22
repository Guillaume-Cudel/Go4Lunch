package com.example.go4lunch.ui.restaurants_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.NavigationActivity;
import com.example.go4lunch.R;
import com.example.go4lunch.di.Injection;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.viewModel.RestaurantViewModel;

import java.util.ArrayList;
import java.util.List;


public class RestaurantsListFragment extends Fragment {


    private RestaurantViewModel viewModel;
    private RecyclerView recyclerView;
    @NonNull
    private final ArrayList<Restaurant> restaurantsList = new ArrayList<>();
    private RestaurantsListAdapter adapter = new RestaurantsListAdapter(restaurantsList, this.getActivity());


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

        Injection.provideRestaurantViewModel(getActivity()).getRestaurants(locationText).observe(this.getActivity(), new Observer<List<Restaurant>>() {
            @Override
            public void onChanged(List<Restaurant> restaurantsList) {
                RestaurantsListFragment.this.restaurantsList.clear();
                RestaurantsListFragment.this.restaurantsList.addAll(restaurantsList);
                updateRestaurants();
            }
        });

        configureRecyclerView();

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurants_list, container, false);


        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_restaurant_list);

        return view;
    }


    private void configureRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        this.adapter = new RestaurantsListAdapter(restaurantsList, this.getActivity());
        recyclerView.setAdapter(adapter);
    }

    private void updateRestaurants(){
        adapter.updateData(restaurantsList);
    }
    
}