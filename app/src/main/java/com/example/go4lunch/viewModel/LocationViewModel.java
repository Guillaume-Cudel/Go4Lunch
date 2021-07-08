package com.example.go4lunch.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;

public class LocationViewModel extends ViewModel {

   private final MutableLiveData<LatLng> _locationLiveData = new MutableLiveData<>();
   public LiveData<LatLng> locationLiveData = _locationLiveData;

    private LatLng location;


    public void setLocation(double latitude, double longitude){
        this.location = new LatLng(latitude, longitude);
        _locationLiveData.postValue(location);
    }

    public LatLng getLocation(){
        return location;
    }


}
