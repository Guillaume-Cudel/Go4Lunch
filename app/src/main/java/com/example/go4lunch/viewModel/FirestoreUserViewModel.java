package com.example.go4lunch.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.api.UserHelper;
import com.example.go4lunch.model.Details;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.firestore.UserFirebase;
import com.example.go4lunch.model.requests.Geometry;
import com.example.go4lunch.model.requests.OpeningHours;
import com.firebase.ui.auth.data.model.User;

import java.util.List;

public class FirestoreUserViewModel extends ViewModel {

    private MutableLiveData<List<UserFirebase>> usersListLiveData;

    private List<UserFirebase> workmates;
    private UserFirebase mUser;
    private Restaurant mRestaurant;


    public void createUser(String uid, String username, String urlPicture) {
        UserHelper.createUser(uid, username, urlPicture);
    }

    public void createRestaurant(String uid, String placeID, String photoData, String photoWidth, String name,
                                 String vicinity, String type, String rating){
        UserHelper.createRestaurant(uid, placeID, photoData, photoWidth, name,
                vicinity, type, rating);
    }

    public LiveData<List<UserFirebase>> getWorkmatesList() {
        if (usersListLiveData == null) {
            usersListLiveData = new MutableLiveData<List<UserFirebase>>();
            UserHelper.getAllUsers(new UserHelper.GetUsersListCallback() {
                @Override
                public void onSuccess(List<UserFirebase> list) {
                    workmates = list;
                    usersListLiveData.postValue(workmates);
                }

                @Override
                public void onError(Exception exception) {
                    usersListLiveData.postValue(null);
                }
            });
        }
        return usersListLiveData;
    }


    public LiveData<UserFirebase> getUser(String uid) {

        MutableLiveData<UserFirebase> getUserLiveData = new MutableLiveData<UserFirebase>();
        UserHelper.getUser(uid, new UserHelper.GetUserCallback() {
            @Override
            public void onSuccess(UserFirebase user) {
                mUser = user;
                getUserLiveData.postValue(mUser);
            }

            @Override
            public void onError(Exception exception) {
                getUserLiveData.postValue(null);
            }
        });

        return getUserLiveData;
    }

    public LiveData<Restaurant> getRestaurant(String uid, String placeID){

        MutableLiveData<Restaurant> getRestaurantLiveData = new MutableLiveData<Restaurant>();
        UserHelper.getRestaurant(uid, placeID, new UserHelper.GetRestaurantCallback() {
            @Override
            public void onSuccess(Restaurant restaurant) {
                mRestaurant = restaurant;
                getRestaurantLiveData.postValue(mRestaurant);
            }

            @Override
            public void onError(Exception exception) {
                getRestaurantLiveData.postValue(null);
            }
        });
        return getRestaurantLiveData;
    }

    public void updateRestaurantChoosed(String uid, String restaurantChoosed) {
        UserHelper.updateRestaurantChoosed(uid, restaurantChoosed);
    }

    public void updateRestaurantName(String uid, String restaurantName) {
        UserHelper.updateRestaurantName(uid, restaurantName);
    }

    public void deleteRestaurantChoosed(String uid) {
        UserHelper.deleteRestaurantChoosed(uid);
    }

    public void deleteRestaurantname(String uid) {
        UserHelper.deleteRestaurantname(uid);
    }

    public void deleteRestaurant(String uid, String placeID){
        UserHelper.deleteRestaurant(uid, placeID);
    }
}
