package com.example.go4lunch.ui.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.NavigationActivity;
import com.example.go4lunch.R;
import com.example.go4lunch.viewModel.LocationViewModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {

    private LocationViewModel locationViewModel;
    private LatLng mLatlng;
    private SupportMapFragment mapFragment;
    private GoogleMap map;


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


        this.map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        LatLng pos = new LatLng(43.5897, 1.3978);
        //43.5762004,1.3856668
        //43.6536576,1.4419125

        // set maximal and minimal zoom
        map.setMinZoomPreference(10.0f);
        map.setMaxZoomPreference(15.0f);

        LatLngBounds toulouseBounds = new LatLngBounds(
                new LatLng(43.3, 1.3), // SW bounds
                new LatLng(43.9, 1.6)  // NE bounds
        );
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(toulouseBounds.getCenter(), 12));


        // filter my position
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        map.setOnMyLocationButtonClickListener(this);
    }

    private void initLocationviewModel() {
        locationViewModel = new ViewModelProvider(requireActivity()).get(LocationViewModel.class);
        locationViewModel.locationLiveData.observe(requireActivity(), new Observer<LatLng>() {
            @Override
            public void onChanged(LatLng latLng) {
                mLatlng = latLng;
                plotBlueDot();
            }
        });
    }

    private void plotBlueDot() {
        if (mLatlng != null) {
            double latitude = mLatlng.latitude;
            double longitude = mLatlng.longitude;

            if (map != null) {
                LatLng myPosition = new LatLng(latitude, longitude);
                //map.addMarker(new MarkerOptions().position(myPosition));
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);
                map.moveCamera(CameraUpdateFactory.newLatLng(myPosition));
                map.animateCamera(zoom);
            }
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        //Toast.makeText(getActivity(), "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }
}