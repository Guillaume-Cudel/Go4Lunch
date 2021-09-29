package com.example.go4lunch.api;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.go4lunch.model.firestore.UserFirebase;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserHelper {

    private static final String COLLECTION_NAME = "users";
    private static final String RESTAURANT_CHOOSED_FIELD = "restaurantChoosed";
    private static final String RESTAURANT_NAME_FIELD = "restaurantName";


    // Get the Collection Reference
    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static void createUser(String uid, String username, String urlPicture) {
        UserFirebase userToCreate = new UserFirebase(uid, username, urlPicture);
        UserHelper.getUsersCollection().document(uid).set(userToCreate);
    }

    // --- GET ---

    public interface GetUsersListCallback{
        void onSuccess(List<UserFirebase> list);

        void onError(Exception exception);
    }

    public static void getAllUsers(GetUsersListCallback callback){
        //todo change to observe in real time
        UserHelper.getUsersCollection().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w(TAG, "Listen failed.", error);
                    callback.onError(new Exception());
                }
                List<UserFirebase> users = new ArrayList<>();
                for(QueryDocumentSnapshot doc : value){
                    UserFirebase user = doc.toObject(UserFirebase.class);
                    users.add(user);
                }
                callback.onSuccess(users);
            }
        });
    }

    public interface GetUserCallback{
        void onSuccess(UserFirebase user);

        void onError(Exception exception);
    }

    public static void getUser(String uid, GetUserCallback callback){
        DocumentReference docRef = UserHelper.getUsersCollection().document(uid);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    Log.w(TAG, "Listen failed.", error);
                    callback.onError(new Exception());
                }
                UserFirebase user = new UserFirebase();
                if (value != null && value.exists()){
                    //DocumentSnapshot document = (DocumentSnapshot) value.getData();
                    user = value.toObject(UserFirebase.class);
                }else{
                    callback.onError(new Exception());
                }
                callback.onSuccess(user);
            }
        });
    }


    // --- UPDATE ---


    public static void updateRestaurantChoosed(String uid, String restaurantChoosed){
        UserHelper.getUsersCollection().document(uid).update(RESTAURANT_CHOOSED_FIELD, restaurantChoosed);
    }

    public static void updateRestaurantName(String uid, String restaurantName){
        UserHelper.getUsersCollection().document(uid).update(RESTAURANT_NAME_FIELD, restaurantName);
    }


    // --- DELETE ---
    public static void deleteRestaurantChoosed(String uid) {
        UserHelper.getUsersCollection().document(uid).update(RESTAURANT_CHOOSED_FIELD, null);
    }

    public static void deleteRestaurantname(String uid) {
        UserHelper.getUsersCollection().document(uid).update(RESTAURANT_NAME_FIELD, null);
    }

}
