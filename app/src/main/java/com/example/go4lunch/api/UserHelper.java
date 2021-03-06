package com.example.go4lunch.api;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.go4lunch.model.Details;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.firestore.UserFirebase;
import com.example.go4lunch.model.requests.Geometry;
import com.example.go4lunch.model.requests.OpeningHours;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserHelper {

    private static final String COLLECTION_NAME = "users";
    private static final String RESTAURANT_CHOOSED_FIELD = "restaurantChoosed";
    private static final String RESTAURANT_NAME_FIELD = "restaurantName";
    private static final String COLLECTION_RESTAURANT = "restaurant";
    //private static final String COLLECTION_RADIUS = "radius";
    private static final String CURRENT_RADIUS = "currentRadius";


    // Get the Collection Reference
    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public static CollectionReference getRestaurantCollection(String uid){
        return UserHelper.getUsersCollection().document(uid).collection(COLLECTION_RESTAURANT);
    }

    /*public static CollectionReference getRadiusCollection(String uid){
        return UserHelper.getUsersCollection().document(uid).collection(COLLECTION_RADIUS);
    }*/

    // --- CREATE ---

    public static void createUser(String uid, String username, String urlPicture, String radius) {
        UserFirebase userToCreate = new UserFirebase(uid, username, urlPicture, radius);
        UserHelper.getUsersCollection().document(uid).set(userToCreate);
    }

    public static void createRestaurant(String uid, String placeID, String photoData, String photoWidth, String name,
                                        String vicinity, String type, String rating){
        Restaurant restaurantToCreate = new Restaurant(placeID, photoData, photoWidth, name,
                vicinity, type, rating);
        UserHelper.getRestaurantCollection(uid).document(placeID).set(restaurantToCreate);
    }

    /*public static void createRadius(String uid, String radius){
        Map<String, Object> mRadius = new HashMap<>();
        mRadius.put("currentRadius", "1000");
        UserHelper.getRadiusCollection(uid).document(radius).set(mRadius);
    }*/

    // --- GET ---

    public interface GetUsersListCallback{
        void onSuccess(List<UserFirebase> list);

        void onError(Exception exception);
    }

    public static void getAllUsers(GetUsersListCallback callback){
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
                    user = value.toObject(UserFirebase.class);
                }else{
                    callback.onError(new Exception());
                }
                callback.onSuccess(user);
            }
        });
    }

    public interface GetRestaurantCallback{
        void onSuccess(Restaurant restaurant);

        void onError(Exception exception);
    }

    public static void getRestaurant(String uid, String placeID, GetRestaurantCallback callback){
        DocumentReference docRef = UserHelper.getRestaurantCollection(uid).document(placeID);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    Log.w(TAG, "Listen failed.", error);
                    callback.onError(new Exception());
                }
                Restaurant restaurant = new Restaurant();
                if (value != null && value.exists()){
                    restaurant = value.toObject(Restaurant.class);
                }else{
                    callback.onError(new Exception());
                }
                callback.onSuccess(restaurant);
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

    public static void updateRadius(String uid, String currentRadius){
        UserHelper.getUsersCollection().document(uid).update(CURRENT_RADIUS, currentRadius);
        //UserHelper.getRadiusCollection(uid).document(radius).update(CURRENT_RADIUS, currentRadius);
    }


    // --- DELETE ---
    public static void deleteRestaurantChoosed(String uid) {
        UserHelper.getUsersCollection().document(uid).update(RESTAURANT_CHOOSED_FIELD, null);
    }

    public static void deleteRestaurantname(String uid) {
        UserHelper.getUsersCollection().document(uid).update(RESTAURANT_NAME_FIELD, null);
    }

    public static void deleteRestaurant(String uid, String placeID){
        UserHelper.getRestaurantCollection(uid).document(placeID).delete();
    }

}
