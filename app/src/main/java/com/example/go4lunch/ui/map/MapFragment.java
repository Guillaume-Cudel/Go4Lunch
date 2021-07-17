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

import com.example.go4lunch.NavigationActivity;
import com.example.go4lunch.R;
import com.example.go4lunch.viewModel.LocationViewModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private LocationViewModel locationViewModel;
    private LatLng mLatlng;
    private SupportMapFragment mapFragment;


    public static MapFragment newInstance() {
        return (new MapFragment());
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        initLocationviewModel();

        mapFragment = SupportMapFragment.newInstance();
        mapFragment.getMapAsync(this);
        getChildFragmentManager().beginTransaction().replace(R.id.map_fragment, mapFragment).commit();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);*/
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (mLatlng == null){
            mLatlng = new LatLng(43.6536927, 1.4418556);
            googleMap.addMarker(new MarkerOptions()
                    .position(mLatlng)
                    .title("Marker"));
        }else {
            googleMap.addMarker(new MarkerOptions()
                    .position(mLatlng)
                    .title("Marker"));
        }
    }

    private void initLocationviewModel(){
        locationViewModel = new ViewModelProvider(requireActivity()).get(LocationViewModel.class);
        locationViewModel.locationLiveData.observe(requireActivity(), new Observer<LatLng>() {
            @Override
            public void onChanged(LatLng latLng) {
                mLatlng = latLng;
            }
        });
    }
}