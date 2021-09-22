package com.example.go4lunch.api;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.firestore.UserFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RestaurantHelper {

    private static final String COLLECTION_RESTAURANT = "restaurants";
    private static final String COLLECTION_RESTAURANT_LIKED = "restaurantsLiked";
    private static final String COLLECTION_USER = "usersToRestaurants";
    private static final String RESTAURANT_CHOOSED_FIELD = "restaurantChoosed";
    private static final String RESTAURANT_NAME_FIELD = "restaurantName";


    // Get the Collection Reference
    public static CollectionReference getRestaurantsCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_RESTAURANT);
    }

    public static CollectionReference getUsersCollection(String placeID){
        return getRestaurantsCollection().document(placeID).collection(COLLECTION_USER);
    }

    // --- CREATE ---

    public static void createRestaurant(String placeID) {
        Restaurant restaurantToCreate = new Restaurant(placeID);
        RestaurantHelper.getRestaurantsCollection().document(placeID).set(restaurantToCreate);
    }

    public static void createUser(String placeID, String uid, String username, String urlPicture) {
        UserFirebase userToCreate = new UserFirebase(uid, username, urlPicture);
        RestaurantHelper.getUsersCollection(placeID).document(uid).set(userToCreate);
    }

    // -------- GET ----------

    public interface GetUsersListCallback{
        void onSuccess(List<UserFirebase> list);

        void onError(Exception exception);
    }

    public static void getAllUsers(String placeID, RestaurantHelper.GetUsersListCallback callback){
        RestaurantHelper.getUsersCollection(placeID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<UserFirebase> users = new ArrayList<>();
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        UserFirebase user = document.toObject(UserFirebase.class);
                        users.add(user);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                    callback.onError(new Exception());
                }
                callback.onSuccess(users);
            }
        });
    }

    public interface GetRestaurantsListCallback{
        void onSuccess(List<Restaurant> list);

        void onError(Exception exception);
    }

    public static void getAllRestaurants(RestaurantHelper.GetRestaurantsListCallback callback){
        RestaurantHelper.getRestaurantsCollection().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Restaurant> restaurants = new ArrayList<>();
                if(task.isSuccessful()){
                    for(DocumentSnapshot document : task.getResult()){
                        Restaurant restaurant = document.toObject(Restaurant.class);
                        restaurants.add(restaurant);
                    }
                } else{
                    Log.d(TAG, "Error getting documents: ", task.getException());
                    callback.onError(new Exception());
                }
                callback.onSuccess(restaurants);
            }
        });
    }

    // --- DELETE ---
    public static void deleteParticipant(String placeID, String uid) {
        RestaurantHelper.getUsersCollection(placeID).document(uid).delete();
    }



}
