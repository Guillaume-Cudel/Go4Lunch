package com.example.go4lunch.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.api.UserHelper;
import com.example.go4lunch.model.firestore.UserFirebase;
import com.firebase.ui.auth.data.model.User;

import java.util.List;

public class FirestoreUserViewModel extends ViewModel {

    private MutableLiveData<List<UserFirebase>> usersListLiveData;
    //todo remove it
    private MutableLiveData<List<UserFirebase>> participantsListLiveData = new MutableLiveData<List<UserFirebase>>();
    private MutableLiveData<UserFirebase> _userLiveData;

    private List<UserFirebase> workmates;
    private List<UserFirebase> participants;
    private UserFirebase mUser;


    public void createUser(String uid, String username, String urlPicture) {
        UserHelper.createUser(uid, username, urlPicture);
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
        /*if (_userLiveData == null) {
            _userLiveData = new MutableLiveData<UserFirebase>();
        }*/
        MutableLiveData<UserFirebase> getUserLiveData = new MutableLiveData<UserFirebase>();
        UserHelper.getUser(uid, new UserHelper.GetUserCallback() {
            @Override
            public void onSuccess(UserFirebase user) {
                mUser = user;
                getUserLiveData.postValue(mUser);
                //_userLiveData.postValue(mUser);
            }

            @Override
            public void onError(Exception exception) {
                getUserLiveData.postValue(null);
            }
        });

        return getUserLiveData;
    }

    public LiveData<List<UserFirebase>> getParticipantsListLiveData() {
        return participantsListLiveData;
    }

    public void addToParticipantsList(UserFirebase user) {
        participants.add(user);
        participantsListLiveData.postValue(participants);

    }

    public void removeToParticipantsList(UserFirebase user) {
        participants.remove(user);
        participantsListLiveData.postValue(participants);

    }


    public void updateUserStatus(String uid, Boolean haveChoosed) {
        UserHelper.updateHaveChoosed(uid, haveChoosed);
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


}
