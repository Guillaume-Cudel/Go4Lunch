package com.example.go4lunch.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.api.RestaurantHelper;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.firestore.UserFirebase;
import com.facebook.internal.Mutable;

import java.util.List;

public class FirestoreRestaurantViewModel extends ViewModel {

    private MutableLiveData<List<Restaurant>> restaurantsListLiveData;
    private MutableLiveData<List<UserFirebase>> participantsListLiveData;

    private List<Restaurant> restaurants;
    private List<UserFirebase> participants;

    public void createRestaurant(String placeID){
        RestaurantHelper.createRestaurant(placeID);
    }

    public void createUserToRestaurant(String placeID, String uid, String username, String urlPicture){
        RestaurantHelper.createUser(placeID, uid, username, urlPicture);
    }

    public LiveData<List<Restaurant>> getRestaurantsList(){
        if(restaurantsListLiveData == null){
            restaurantsListLiveData = new MutableLiveData<List<Restaurant>>();

            RestaurantHelper.getAllRestaurants(new RestaurantHelper.GetRestaurantsListCallback() {
                @Override
                public void onSuccess(List<Restaurant> list) {
                    restaurants = list;
                    restaurantsListLiveData.postValue(restaurants);
                }

                @Override
                public void onError(Exception exception) {
                    restaurantsListLiveData.postValue(null);
                }
            });
        }
        return restaurantsListLiveData;
    }

    public LiveData<List<UserFirebase>> getParticipantsList(String placeID){
        if(participantsListLiveData == null){
            participantsListLiveData = new MutableLiveData<List<UserFirebase>>();
            RestaurantHelper.getAllUsers(placeID, new RestaurantHelper.GetUsersListCallback() {
                @Override
                public void onSuccess(List<UserFirebase> list) {
                    participants = list;
                    participantsListLiveData.postValue(participants);
                }

                @Override
                public void onError(Exception exception) {
                    participantsListLiveData.postValue(null);
                }
            });
        }
        return participantsListLiveData;
    }

    public void deleteParticipant(String placeID, String uid){
        RestaurantHelper.deleteParticipant(placeID, uid);
    }
}
