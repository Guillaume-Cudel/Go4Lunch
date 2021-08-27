package com.example.go4lunch.ui.map;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.R;
import com.example.go4lunch.ui.restaurant_profil.RestaurantProfilActivity;
import com.example.go4lunch.di.Injection;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.requests.Photos;
import com.example.go4lunch.viewModel.LocationViewModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnInfoWindowClickListener {

    private LatLng mLatlng;
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private List<Restaurant> restaurantsList = new ArrayList<>();


    public static MapFragment newInstance() {
        return (new MapFragment());
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initLocationviewModel();

        mapFragment = SupportMapFragment.newInstance();
        mapFragment.getMapAsync(this);
        getChildFragmentManager().beginTransaction().replace(R.id.map_fragment, mapFragment).commit();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);


        // set maximal and minimal zoom
        map.setMinZoomPreference(10.0f);
        map.setMaxZoomPreference(18.0f);

        LatLngBounds toulouseBounds = new LatLngBounds(
                new LatLng(43.3, 1.3), // SW bounds
                new LatLng(43.9, 1.6)  // NE bounds
        );
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(toulouseBounds.getCenter(), 12));


        if (mLatlng != null){
            plotBlueDot();
            recoveAndMarkRestaurants();
        }

        // filter my position
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        map.setOnMyLocationButtonClickListener(this);
        map.setOnInfoWindowClickListener(this);
    }

    private void initLocationviewModel() {

        LocationViewModel locationViewModel = new ViewModelProvider(requireActivity()).get(LocationViewModel.class);
        locationViewModel.locationLiveData.observe(requireActivity(), new Observer<LatLng>() {
            @Override
            public void onChanged(LatLng latLng) {
                mLatlng = latLng;
                plotBlueDot();
                recoveAndMarkRestaurants();
            }
        });
    }

    private void recoveAndMarkRestaurants() {
        double latitude = mLatlng.latitude;
        double longitude = mLatlng.longitude;
        String locationText = latitude + "," + longitude;
        Log.d("localisation", locationText);

        // Get restaurants
        Injection.provideRestaurantViewModel(getActivity()).getRestaurants(locationText)
                .observe(this.getActivity(), new Observer<List<Restaurant>>() {
                    @Override
                    public void onChanged(List<Restaurant> restaurants) {
                        MapFragment.this.restaurantsList.clear();
                        MapFragment.this.restaurantsList.addAll(restaurants);

                        for (int i = 0; i < restaurantsList.size(); i++) {
                            Restaurant restaurant = restaurantsList.get(i);
                            String restaurantLatitude = restaurant.getGeometry().getLocation().getLat();
                            String restaurantLongitude = restaurant.getGeometry().getLocation().getLng();
                            String title = restaurant.getName();
                            double rLatitude = Double.parseDouble(restaurantLatitude);
                            double rLongitude = Double.parseDouble(restaurantLongitude);
                            LatLng restaurantLocation = new LatLng(rLatitude, rLongitude);
                            String infoRate;
                            if (restaurant.getRating() != null){
                                infoRate = "Rate: " + restaurant.getRating();
                            }else
                                infoRate = "No rating";

                            Bitmap bitmap = getBitmapFromVectorDrawable(getContext(),R.drawable.ic_restaurant_red);
                            BitmapDescriptor descriptor =BitmapDescriptorFactory.fromBitmap(bitmap);

                            Marker restaurantMarker = map.addMarker(new MarkerOptions()
                                    .position(restaurantLocation)
                                    .title(title)
                                    .snippet(infoRate)
                                    .icon(descriptor)
                            );
                            restaurantMarker.setTag(restaurant);
                        }
                    }
                });
    }


    private void plotBlueDot() {
        if (mLatlng != null) {
            double latitude = mLatlng.latitude;
            double longitude = mLatlng.longitude;

            if (map != null) {
                LatLng myPosition = new LatLng(latitude, longitude);

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




    @Override
    public void onInfoWindowClick(Marker marker) {
        Restaurant restaurant = (Restaurant) marker.getTag();
        if (restaurant != null) {
            String place_id = restaurant.getPlace_id();
            String rating = restaurant.getRating();

            Intent i = new Intent(getActivity(), RestaurantProfilActivity.class);
            i.putExtra("place_id", place_id);
            i.putExtra("name", restaurant.getName());
            if (restaurant.getPhotos() != null) {
                List<Photos> photoInformation = restaurant.getPhotos();
                String photoData = photoInformation.get(0).getPhotoReference();
                String photoWidth = photoInformation.get(0).getWidth();
                i.putExtra("photo", photoData);
                i.putExtra("photoWidth", photoWidth);
            }
            i.putExtra("vicinity", restaurant.getVicinity());
            i.putExtra("type", restaurant.getTypes().get(0));
            i.putExtra("rate", rating);
            getActivity().startActivity(i);
        }
    }

    // Convert vector drawable to bitmap for get personalized marker icon
    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable =  AppCompatResources.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}