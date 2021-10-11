package com.example.go4lunch.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;

public class LocationViewModel extends ViewModel {

    private final MutableLiveData<LatLng> _locationLiveData = new MutableLiveData<>();
    //private MutableLiveData<String> _radiusLiveData = new MutableLiveData<>();
    public LiveData<LatLng> locationLiveData = _locationLiveData;
    //public LiveData<String> radiusLiveData = _radiusLiveData;

    private LatLng mLocation;
    //private String mRadius;


    public void setLocation(double latitude, double longitude) {
        this.mLocation = new LatLng(latitude, longitude);
        _locationLiveData.postValue(mLocation);
    }

    public LatLng getLocation() {
        return mLocation;
    }


    /*
    public void setRadius(String radius) {
        this.mRadius = radius;
        _radiusLiveData.postValue(mRadius);
    }*/
}
