package com.example.go4lunch.ui.restaurants_list;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.NavigationActivity;
import com.example.go4lunch.R;
import com.example.go4lunch.di.Injection;
import com.example.go4lunch.model.Details;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.firestore.UserFirebase;
import com.example.go4lunch.viewModel.FirestoreRestaurantViewModel;
import com.example.go4lunch.viewModel.LocationViewModel;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;


public class RestaurantsListFragment extends Fragment {



    private RecyclerView recyclerView;
    @NonNull
    private final ArrayList<Restaurant> restaurantsList = new ArrayList<>();
    private ArrayList<Restaurant> restaurantsListSaved = new ArrayList<>();
    private LatLng mLatlng;
    private RestaurantsListAdapter adapter = new RestaurantsListAdapter(restaurantsList, mLatlng, this.getActivity());
    private FirestoreRestaurantViewModel mFirestoreRestaurantVM;
    private ProgressDialog loading;
    private NavigationActivity navActivity;


    public RestaurantsListFragment( ) {
    }

    public static RestaurantsListFragment newInstance() {
        return (new RestaurantsListFragment());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navActivity = (NavigationActivity) getActivity();

        loading = ProgressDialog.show(navActivity, "", getString(R.string.messageRecovingRestaurants), true);
        mFirestoreRestaurantVM = Injection.provideFirestoreRestaurantViewModel(getActivity());

        recoveLocation();


        double latitude = mLatlng.latitude;
        double longitude = mLatlng.longitude;
        String locationText = latitude + "," + longitude;
        Log.d("localisation", locationText);

        mFirestoreRestaurantVM.getAllRestaurants().observe(navActivity, new Observer<List<Restaurant>>() {
            @Override
            public void onChanged(List<Restaurant> restaurants) {
                RestaurantsListFragment.this.restaurantsList.clear();
                RestaurantsListFragment.this.restaurantsList.addAll(restaurants);
                //recoveParticipants();
                updateRestaurants();
                loading.cancel();
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

    private void recoveLocation(){

        LocationViewModel locationViewModel = new ViewModelProvider(navActivity).get(LocationViewModel.class);
        locationViewModel.locationLiveData.observe(requireActivity(), new Observer<LatLng>() {
            @Override
            public void onChanged(LatLng latLng) {
                mLatlng = latLng;
                updateLocation();
            }
        });
    }

    private void configureRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        this.adapter = new RestaurantsListAdapter(restaurantsList, mLatlng, this.getActivity());
        recyclerView.setAdapter(adapter);
    }


    private void updateRestaurants(){
        adapter.updateData(restaurantsList);
    }

    private void updateLocation(){
        adapter.updateLocation(mLatlng);
    }


}