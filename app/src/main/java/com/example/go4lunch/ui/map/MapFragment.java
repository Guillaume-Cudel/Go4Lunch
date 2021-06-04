package com.example.go4lunch.ui.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.R;

public class MapFragment extends Fragment {

    ImageView image;

    public static MapFragment newInstance() {
        return (new MapFragment());
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        image = (ImageView) view.findViewById(R.id.map_image_test);


        return view;
    }
}