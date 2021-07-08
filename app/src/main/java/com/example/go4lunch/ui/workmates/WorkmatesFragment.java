package com.example.go4lunch.ui.workmates;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.NavigationActivity;
import com.example.go4lunch.R;
import com.example.go4lunch.viewModel.LocationViewModel;
import com.google.android.gms.maps.model.LatLng;

public class WorkmatesFragment extends Fragment {

    private LocationViewModel locationViewModel;
    private LatLng mLatlng;
    private String locationText;


    public WorkmatesFragment(){}

    public static WorkmatesFragment newInstance() {
        return (new WorkmatesFragment());
    }

    private void initLocationviewModel(){
        locationViewModel = new ViewModelProvider(requireActivity()).get(LocationViewModel.class);
        locationViewModel.locationLiveData.observe(requireActivity(), new Observer<LatLng>() {
            @Override
            public void onChanged(LatLng latLng) {
                mLatlng = latLng;
                double latitude = mLatlng.latitude;
                double longitude = mLatlng.longitude;
                locationText = latitude + "," + longitude;

            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workmates, container, false);

        TextView showLocation = (TextView) view.findViewById(R.id.show_location);

        initLocationviewModel();

        showLocation.setText(locationText);
        Log.d("localisation", locationText);

        return view;

    }
}