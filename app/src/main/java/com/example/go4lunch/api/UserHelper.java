package com.example.go4lunch.api;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.go4lunch.model.firestore.UserFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class UserHelper {

    private static final String COLLECTION_NAME = "users";
    private static final String RESTAURANT_CHOOSED_FIELD = "restaurantChoosed";
    private static final String RESTAURANT_NAME_FIELD = "restaurantName";
    private static final String HAVE_CHOOSED_FIELD = "haveChoosed";

    // Get the Collection Reference
    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createUser(String uid, String username, String urlPicture) {
        UserFirebase userToCreate = new UserFirebase(uid, username, urlPicture);
        return UserHelper.getUsersCollection().document(uid).set(userToCreate);
    }

    // --- GET ---

    /*public static Task<DocumentSnapshot> getUser(String uid){
        return UserHelper.getUsersCollection().document(uid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                       UserFirebase user = documentSnapshot.toObject(UserFirebase.class);
                    }
                });
    }*/

    public static Task<QuerySnapshot> getAllUsers(List<UserFirebase> users){
        return UserHelper.getUsersCollection().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        UserFirebase user = document.toObject(UserFirebase.class);
                        users.add(user);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    // --- UPDATE ---


    public static void updateHaveChoosed(String uid, Boolean haveChoosed) {
        UserHelper.getUsersCollection().document(uid).update(HAVE_CHOOSED_FIELD, haveChoosed);
    }

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
