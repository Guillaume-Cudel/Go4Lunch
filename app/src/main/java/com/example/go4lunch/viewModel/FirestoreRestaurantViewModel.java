package com.example.go4lunch.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.api.RestaurantHelper;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.firestore.UserFirebase;

import java.util.List;

public class FirestoreRestaurantViewModel extends ViewModel {

    private MutableLiveData<Restaurant> restaurantLiveData;
    private MutableLiveData<UserFirebase> userLiveData;
    private MutableLiveData<List<UserFirebase>> participantsListLiveData;

    private Restaurant mRestaurant;
    private UserFirebase mUser;
    private List<UserFirebase> mParticipants;

    public void createRestaurant(String placeID) {
        RestaurantHelper.createRestaurant(placeID);
    }

    public void createUserToRestaurant(String placeID, String uid, String username, String urlPicture) {
        RestaurantHelper.createUser(placeID, uid, username, urlPicture);
    }

    public LiveData<Restaurant> getRestaurant(String placeID) {
        if (restaurantLiveData == null) {
            restaurantLiveData = new MutableLiveData<Restaurant>();

            RestaurantHelper.getTargetedRestaurant(placeID, new RestaurantHelper.GetRestaurantsTargetedCallback() {
                @Override
                public void onSuccess(Restaurant restaurant) {
                    mRestaurant = restaurant;
                    restaurantLiveData.postValue(mRestaurant);
                }

                @Override
                public void onError(Exception exception) {
                    restaurantLiveData.postValue(null);
                }
            });
        }
        return restaurantLiveData;
    }

    public LiveData<UserFirebase> getUser(String placeID, String uid){
        //if(userLiveData == null){
            userLiveData = new MutableLiveData<UserFirebase>();

            RestaurantHelper.getTargetedUserCallback(placeID, uid, new RestaurantHelper.GetUserTargetedCallback() {
                @Override
                public void onSuccess(UserFirebase user) {
                    mUser = user;
                    userLiveData.postValue(mUser);
                }

                @Override
                public void onError(Exception exception) {
                    userLiveData.postValue(null);
                }
            });
       // }
        return userLiveData;
    }

    public LiveData<List<UserFirebase>> getParticipantsList(String placeID) {
        if (participantsListLiveData == null) {
            participantsListLiveData = new MutableLiveData<List<UserFirebase>>();
            RestaurantHelper.getAllUsers(placeID, new RestaurantHelper.GetAllUsersCallback() {
                @Override
                public void onSuccess(List<UserFirebase> list) {
                    mParticipants = list;
                    participantsListLiveData.postValue(mParticipants);
                }

                @Override
                public void onError(Exception exception) {
                    participantsListLiveData.postValue(null);
                }
            });
        }
        return participantsListLiveData;
    }

    public void deleteParticipant(String placeID, String uid) {
        RestaurantHelper.deleteParticipant(placeID, uid);
    }
}
