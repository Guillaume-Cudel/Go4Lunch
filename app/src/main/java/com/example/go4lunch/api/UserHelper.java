package com.example.go4lunch.api;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.go4lunch.model.firestore.UserFirebase;
import com.example.go4lunch.ui.workmates.WorkmatesAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class UserHelper {

    private static final String COLLECTION_NAME = "users";
    private static final String RESTAURANT_CHOOSED_FIELD = "restaurantChoosed";
    private static final String RESTAURANT_NAME_FIELD = "restaurantName";
    private static final String IS_CHOOSED_FIELD = "isChoosed";

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


    public static Task<Void> updateIsChoosed(String uid, Boolean isChoosed) {
        return UserHelper.getUsersCollection().document(uid).update(IS_CHOOSED_FIELD, isChoosed);
    }

    public static Task<Void> updateRestaurantChoosed(String uid, String restaurantChoosed){
        return UserHelper.getUsersCollection().document(uid).update(RESTAURANT_CHOOSED_FIELD, restaurantChoosed);
    }

    public static Task<Void> updateRestaurantName(String uid, String restaurantName){
        return UserHelper.getUsersCollection().document(uid).update(RESTAURANT_NAME_FIELD, restaurantName);
    }

    // --- DELETE ---
    public static Task<Void> deleteRestaurantChoosed(String uid) {
        return UserHelper.getUsersCollection().document(uid).update(RESTAURANT_CHOOSED_FIELD, FieldValue.delete());
    }

    public static Task<Void> deleteRestaurantname(String uid) {
        return UserHelper.getUsersCollection().document(uid).update(RESTAURANT_NAME_FIELD, FieldValue.delete());
    }

}
