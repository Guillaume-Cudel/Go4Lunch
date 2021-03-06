package com.example.go4lunch.ui.restaurant_profil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.go4lunch.NavigationActivity;
import com.example.go4lunch.R;
import com.example.go4lunch.di.Injection;
import com.example.go4lunch.model.Details;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.firestore.UserFirebase;
import com.example.go4lunch.viewModel.FirestoreRestaurantViewModel;
import com.example.go4lunch.viewModel.FirestoreUserViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class RestaurantProfilActivity extends AppCompatActivity {

    private String placeID, photoReference, photoWidth, name, vicinity, type, rating, phoneNum, websiteURL;
    private TextView nameText, typeText, vicinityText, likeText;
    private ImageView restaurantPhoto, star1, star2, star3, callImage, likeImage, websiteImage;
    private FloatingActionButton choosedButton;

    @NonNull
    private List<UserFirebase> participantslist = new ArrayList<>();
    private UserFirebase mCurrentUser = null;

    private Restaurant mRestaurant = null;
    private RecyclerView recyclerView;
    private RestaurantProfilAdapter adapter = new RestaurantProfilAdapter(participantslist, this);
    private Context context;
    private FirestoreUserViewModel fUserViewModel;
    private FirestoreRestaurantViewModel fRestaurantViewModel;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();
    private String userUid = currentUser.getUid();

    private static final int REQUEST_CALL_PHONE_PERMISSION = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_restaurant);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);


        configureView();
        recoveData();
        setFieldsWithData();

        Injection.provideRestaurantViewModel(this).getDetails(placeID)
                .observe(this, new Observer<Details>() {
                    @Override
                    public void onChanged(Details details) {
                        if (details.getFormatted_phone_number() != null) {
                            phoneNum = details.getFormatted_phone_number();
                        }
                        if (details.getWebsite() != null) {
                            websiteURL = details.getWebsite();
                        }
                    }
                });


        choosedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateList();
            }
        });

        likeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLike();
            }
        });

        phoneRestaurant();
        onClickWebsite();
        configureRecyclerView();
    }

    private void updateLike() {
        fRestaurantViewModel.getUserRestaurantLiked(placeID, userUid).observe(RestaurantProfilActivity.this, new Observer<UserFirebase>() {
            @Override
            public void onChanged(UserFirebase userFirebase) {
                if (userFirebase == null) {
                    fRestaurantViewModel.createUserRestaurantLiked(placeID, mCurrentUser.getUid(), mCurrentUser.getUsername(), mCurrentUser.getUrlPicture());
                    likeImage.setImageResource(R.drawable.ic_baseline_star_rate);
                    likeText.setText(R.string.likeTextRestaurantProfilValidate);
                } else {
                    fRestaurantViewModel.deleteUserLiked(placeID, mCurrentUser.getUid());
                    likeImage.setImageResource(R.drawable.ic_baseline_star_border);
                    likeText.setText(R.string.likeTextRestaurantProfilInvalidate);
                }
            }
        });
    }

    private void configureView() {
        nameText = (TextView) findViewById(R.id.restaurant_title);
        typeText = (TextView) findViewById(R.id.restaurant_kind);
        vicinityText = (TextView) findViewById(R.id.restaurant_address);
        restaurantPhoto = (ImageView) findViewById(R.id.restaurant_image);
        star1 = (ImageView) findViewById(R.id.restaurant_star_1);
        star2 = (ImageView) findViewById(R.id.restaurant_star_2);
        star3 = (ImageView) findViewById(R.id.restaurant_star_3);
        callImage = (ImageView) findViewById(R.id.call_image);
        likeImage = (ImageView) findViewById(R.id.like_image);
        likeText = (TextView) findViewById(R.id.like_text);
        websiteImage = (ImageView) findViewById(R.id.website_image);
        choosedButton = findViewById(R.id.restaurant_choice_button);
        recyclerView = (RecyclerView) findViewById(R.id.profil_restaurant_recyclerView);
        fUserViewModel = Injection.provideFirestoreUserViewModel(this);
        fRestaurantViewModel = Injection.provideFirestoreRestaurantViewModel(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        fRestaurantViewModel.getParticipantsList(placeID)
                .observe(this, new Observer<List<UserFirebase>>() {
                    @Override
                    public void onChanged(List<UserFirebase> userFirebases) {
                        RestaurantProfilActivity.this.participantslist.clear();
                        RestaurantProfilActivity.this.participantslist.addAll(userFirebases);
                        updateParticipants();
                    }
                });

        fUserViewModel.getUser(userUid).observe(this, new Observer<UserFirebase>() {
            @Override
            public void onChanged(UserFirebase userFirebase) {
                mCurrentUser = userFirebase;
                String restaurantID = mCurrentUser.getRestaurantChoosed();

                // fix bug
                choosedButton.setImageResource(R.drawable.ic_go);
                if (placeID.equals(restaurantID)) {
                    choosedButton.setImageResource(R.drawable.ic_validated);
                }
            }
        });
    }

    private void configureRecyclerView() {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RestaurantProfilAdapter(participantslist, context);
        recyclerView.setAdapter(adapter);
    }

    private void updateParticipants() {
        adapter.updateData(participantslist);
    }


    private void updateList() {

        fRestaurantViewModel.getUser(placeID, userUid).observe(RestaurantProfilActivity.this, new Observer<UserFirebase>() {
            @Override
            public void onChanged(UserFirebase userFirebase) {
                if (mCurrentUser != null) {
                    boolean addParticipant;
                    if (mCurrentUser.getRestaurantChoosed() == null) {
                        addParticipant = true;
                        fUserViewModel.updateRestaurantChoosed(mCurrentUser.getUid(), placeID);
                        fUserViewModel.updateRestaurantName(mCurrentUser.getUid(), name);
                        fUserViewModel.createRestaurant(mCurrentUser.getUid(), placeID, photoReference, photoWidth, name, vicinity, type, rating);
                        fRestaurantViewModel.createUserToRestaurant(placeID, mCurrentUser.getUid(), mCurrentUser.getUsername(), mCurrentUser.getUrlPicture());
                        fRestaurantViewModel.updateParticipantNumber(placeID, addParticipant);
                        //choosedButton.setImageResource(R.drawable.ic_validated);

                    } else if (!mCurrentUser.getRestaurantChoosed().equals(placeID)) {
                        addParticipant = true;
                        boolean participantElsewhere = false;
                        fRestaurantViewModel.updateParticipantNumber(mCurrentUser.getRestaurantChoosed(), participantElsewhere);
                        fRestaurantViewModel.deleteParticipant(mCurrentUser.getRestaurantChoosed(), mCurrentUser.getUid());
                        fUserViewModel.deleteRestaurant(mCurrentUser.getUid(), mCurrentUser.getRestaurantChoosed());
                        fUserViewModel.updateRestaurantChoosed(mCurrentUser.getUid(), placeID);
                        fUserViewModel.updateRestaurantName(mCurrentUser.getUid(), name);
                        fUserViewModel.createRestaurant(mCurrentUser.getUid(), placeID, photoReference, photoWidth, name, vicinity, type, rating);
                        fRestaurantViewModel.createUserToRestaurant(placeID, mCurrentUser.getUid(), mCurrentUser.getUsername(), mCurrentUser.getUrlPicture());
                        fRestaurantViewModel.updateParticipantNumber(placeID, addParticipant);
                        //choosedButton.setImageResource(R.drawable.ic_validated);

                    } else {
                        addParticipant = false;
                        fUserViewModel.deleteRestaurantChoosed(mCurrentUser.getUid());
                        fUserViewModel.deleteRestaurantname(mCurrentUser.getUid());
                        fUserViewModel.deleteRestaurant(mCurrentUser.getUid(), placeID);
                        fRestaurantViewModel.deleteParticipant(placeID, mCurrentUser.getUid());
                        fRestaurantViewModel.updateParticipantNumber(placeID, addParticipant);
                        //choosedButton.setImageResource(R.drawable.ic_go);
                    }
                }
            }
        });


    }


    private void recoveData() {
        Bundle i = getIntent().getExtras();
        placeID = i.getString("place_id");
        photoReference = i.getString("photo");
        photoWidth = i.getString("photoWidth");
        name = i.getString("name");
        vicinity = i.getString("vicinity");
        type = i.getString("type");
        rating = i.getString("rate");
    }

    private void setFieldsWithData() {
        displayRestaurantName();
        context = RestaurantProfilActivity.this;
        typeText.setText(type);
        vicinityText.setText(vicinity);
        if (rating != null) {
            displayStarsRating();
        } else {
            noDisplayStars();
        }

        if (photoReference == null) {
            Glide.with(context).load(R.drawable.restaurantjardin).into(restaurantPhoto);
        } else {
            Glide.with(context).load(parseDataPhotoToImage())
                    .into(restaurantPhoto);
        }
    }

    private void displayStarsRating() {
        double dRating = Double.parseDouble(rating);
        if (dRating <= 1.67) {
            star3.setVisibility(View.INVISIBLE);
            star2.setVisibility(View.INVISIBLE);
        }
        if (dRating > 1.67 && dRating < 3.4) {
            star3.setVisibility(View.INVISIBLE);
        }
    }

    private void noDisplayStars() {
        star1.setVisibility(View.INVISIBLE);
        star2.setVisibility(View.INVISIBLE);
        star3.setVisibility(View.INVISIBLE);
    }

    private String parseDataPhotoToImage() {

        String API_KEY = "&key=AIzaSyBpPAJjNZ2X4q0xz3p_zK_uW3MdZCpD704";
        String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place/photo?";
        String MAX_WIDTH = "maxwidth=";
        String PHOTOREFERENCE = "&photoreference=";
        return new StringBuilder().append(PLACES_API_BASE).append(MAX_WIDTH).append(photoWidth)
                .append(PHOTOREFERENCE).append(photoReference).append(API_KEY).toString();

        //https://maps.googleapis.com/maps/api/place/photo?maxwidth=4032&photoreference=ATtYBwKU4gK6Y-aRIDxoRKq8wRs3se36xK49g0PS-O2iLg-NS_osj85NapJsY-aNHOk2MyZYisu2YN013wX5VlfvvbkyIfiYimscYJImUq_jfRCPjOOl3fNIuJyrFDnWoP64tbFfZF1Aja5WaWyISM6BOW0lvsmjySrvMiRUjC03_NCKxILK&key=AIzaSyBpPAJjNZ2X4q0xz3p_zK_uW3MdZCpD704
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //finish();
                Intent i = new Intent(this, NavigationActivity.class);
                //i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private int countCharacter(String word) {
        String string = word;
        int count = 0;

        //Counts each character except space
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) != ' ')
                count++;
        }
        return count;
    }

    private void displayRestaurantName() {
        int numberOfCharacter = countCharacter(name);
        if (numberOfCharacter > 20 && numberOfCharacter <= 25) {
            nameText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            nameText.setText(name);
        }
        if (numberOfCharacter > 25 && numberOfCharacter <= 30) {
            nameText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            nameText.setText(name);
        }
        if (numberOfCharacter > 30) {
            nameText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            nameText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(40)});
            nameText.setText(name);
        } else
            nameText.setText(name);
    }

    private void phoneRestaurant() {
        callImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNum = phoneNum.replaceAll("\\s+", "");

                if (ActivityCompat.shouldShowRequestPermissionRationale(RestaurantProfilActivity.this,
                        android.Manifest.permission.CALL_PHONE)) {

                    ActivityCompat.requestPermissions(RestaurantProfilActivity.this,
                            new String[]{Manifest.permission.READ_PHONE_STATE},
                            REQUEST_CALL_PHONE_PERMISSION);
                } else {

                    ActivityCompat.requestPermissions(RestaurantProfilActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            REQUEST_CALL_PHONE_PERMISSION);
                }
            }
        });
    }


    public void dialPhoneNumber(String phoneNumber) {
        String shemePhoneNumber = "tel:" + phoneNumber;
        Uri number = Uri.parse(shemePhoneNumber);
        Intent callIntent = new Intent(Intent.ACTION_CALL, number);
        if (callIntent.resolveActivity(getPackageManager()) != null) {
            try {
                startActivity(callIntent);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CALL_PHONE_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dialPhoneNumber(phoneNum);
                } else {

                }
                return;
            }
        }
    }

    private void onClickWebsite() {
        websiteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (websiteURL != null) {
                    openWebURL(websiteURL);
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(RestaurantProfilActivity.this);
                    dialog.setTitle("This restaurant doesn't have a website");
                    dialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    dialog.show();
                }
            }
        });
    }

    private void openWebURL(String url) {
        Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browse);
    }


}







