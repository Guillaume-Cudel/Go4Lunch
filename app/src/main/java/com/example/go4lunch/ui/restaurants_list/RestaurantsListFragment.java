package com.example.go4lunch.ui.restaurants_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.R;
import com.google.android.libraries.places.api.Places;

public class RestaurantsListFragment extends Fragment {

    private TextView textView;


    public static RestaurantsListFragment newInstance() {
        return (new RestaurantsListFragment());
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurants_list, container, false);

        textView = (TextView) view.findViewById(R.id.restau_location_text);

        return view;


    }
}