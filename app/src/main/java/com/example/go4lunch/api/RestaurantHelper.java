package com.example.go4lunch.api;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.firestore.UserFirebase;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
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

    public interface GetAllUsersCallback {
        void onSuccess(List<UserFirebase> list);

        void onError(Exception exception);
    }

    public static void getAllUsers(String placeID, GetAllUsersCallback callback){
        RestaurantHelper.getUsersCollection(placeID).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    Log.w(TAG, "Listen failed.", error);
                    callback.onError(new Exception());
                }

                List<UserFirebase> users = new ArrayList<>();
                for (QueryDocumentSnapshot doc : value) {
                    UserFirebase user = doc.toObject(UserFirebase.class);
                    users.add(user);
                }
                callback.onSuccess(users);
            }
        });
    }

    public interface GetUserTargetedCallback{
        void onSuccess(UserFirebase user);

        void onError( Exception exception);
    }

    public static void getTargetedUserCallback(String placeID, String uid, GetUserTargetedCallback callback){
        DocumentReference docRef = RestaurantHelper.getUsersCollection(placeID).document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                UserFirebase user = new UserFirebase();
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    user = document.toObject(UserFirebase.class);
                }else{
                    callback.onError(new Exception());
                }
                callback.onSuccess(user);
            }
        });
    }

    public interface GetRestaurantsTargetedCallback {
        void onSuccess(Restaurant restaurant);

        void onError(Exception exception);
    }

    public static void getTargetedRestaurant(String placeId, GetRestaurantsTargetedCallback callback){
        DocumentReference docRef = RestaurantHelper.getRestaurantsCollection().document(placeId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Restaurant restaurant = new Restaurant();
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    restaurant = document.toObject(Restaurant.class);
                }else{
                    callback.onError(new Exception());
                }
                callback.onSuccess(restaurant);
            }
        });
    }


    // --- DELETE ---
    public static void deleteParticipant(String placeID, String uid) {
        RestaurantHelper.getUsersCollection(placeID).document(uid).delete();
    }



}
