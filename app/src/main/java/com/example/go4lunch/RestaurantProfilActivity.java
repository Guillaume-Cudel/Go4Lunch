package com.example.go4lunch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.go4lunch.di.Injection;
import com.example.go4lunch.model.Details;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RestaurantProfilActivity extends AppCompatActivity {

    private String placeID, photoReference, photoWidth, name, vicinity, type, rating, phoneNum, websiteText;
    private List<String> weekDayText;
    private Details mDetails;
    private TextView nameText, typeText, vicinityText;
    private ImageView restaurantPhoto, star2, star3;
    private Context context;
    private final String API_KEY = "&key=AIzaSyBpPAJjNZ2X4q0xz3p_zK_uW3MdZCpD704";
    private String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place/photo?";
    private String MAX_WIDTH = "maxwidth=";
    private String PHOTOREFERENCE = "&photoreference=";
    private FrameLayout viewCall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_restaurant);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        nameText = (TextView) findViewById(R.id.restaurant_title);
        typeText = (TextView) findViewById(R.id.restaurant_kind);
        vicinityText = (TextView) findViewById(R.id.restaurant_address);
        restaurantPhoto = (ImageView) findViewById(R.id.restaurant_image);
        star2 = (ImageView) findViewById(R.id.restaurant_star_2);
        star3 = (ImageView) findViewById(R.id.restaurant_star_3);
        viewCall = (FrameLayout) findViewById(R.id.view_call);

        recoveData();
        setFieldsWithData();



        Injection.provideRestaurantViewModel(this).getDetails(placeID)
                .observe(this, new Observer<Details>() {
                    @Override
                    public void onChanged(Details details) {
                        // todo do something when clicking to call and website
                        //mDetails = details;
                        phoneNum = details.getFormatted_phone_number();
                        //phoneNum = RestaurantProfilActivity.this.details.getFormatted_phone_number();
                        // websiteText = RestaurantProfilActivity.this.details.getWebsite();
                        // weekDayText = RestaurantProfilActivity.this.details.getOpening_hours().getWeekday_text();
                    }
                });

        viewCall.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                phoneNum = phoneNum.replaceAll("\\s+", "");
                String stringPhoneNum = "tel:" + phoneNum;
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(stringPhoneNum));
                startActivity(callIntent);
                return true;
            }
        });

        // todo   setResult() to send weekDay

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
        typeText.setText(type);
        vicinityText.setText(vicinity);
        if (rating != null) {
            displayStarsRating();
        }
        context = RestaurantProfilActivity.this;

        Glide.with(context).load(parseDataPhotoToImage())
                .into(restaurantPhoto);
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

    private String parseDataPhotoToImage() {

        return new StringBuilder().append(PLACES_API_BASE).append(MAX_WIDTH).append(photoWidth)
                .append(PHOTOREFERENCE).append(photoReference).append(API_KEY).toString();

        //https://maps.googleapis.com/maps/api/place/photo?maxwidth=4032&photoreference=ATtYBwKU4gK6Y-aRIDxoRKq8wRs3se36xK49g0PS-O2iLg-NS_osj85NapJsY-aNHOk2MyZYisu2YN013wX5VlfvvbkyIfiYimscYJImUq_jfRCPjOOl3fNIuJyrFDnWoP64tbFfZF1Aja5WaWyISM6BOW0lvsmjySrvMiRUjC03_NCKxILK&key=AIzaSyBpPAJjNZ2X4q0xz3p_zK_uW3MdZCpD704
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
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


}






