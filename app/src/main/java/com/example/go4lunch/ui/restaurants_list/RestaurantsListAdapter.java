package com.example.go4lunch.ui.restaurants_list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.go4lunch.NavigationActivity;
import com.example.go4lunch.R;
import com.example.go4lunch.RestaurantProfilActivity;
import com.example.go4lunch.di.Injection;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.viewModel.LocationViewModel;
import com.google.android.gms.maps.model.LatLng;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RestaurantsListAdapter extends RecyclerView.Adapter<RestaurantsListAdapter.RestaurantsListViewHolder> {

    private List<Restaurant> dataList;
    private Context context;
    private final String API_KEY = "&key=AIzaSyBpPAJjNZ2X4q0xz3p_zK_uW3MdZCpD704";
    private String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place/photo?";
    private String MAX_WIDTH = "maxwidth=";
    private String PHOTOREFERENCE = "&photoreference=";
    public static final int REQUEST_RESTAURANTS_DETAILS = 42;
    private String photoData, photoWidth, rating, restaurantLatitude, restaurantLongitude;
    private LatLng mlatlng;


    public RestaurantsListAdapter(final List<Restaurant> dataList, LatLng latlng, Context context) {
        this.dataList = dataList;
        this.mlatlng = latlng;
        this.context = context;
    }

    @NonNull
    @Override
    public RestaurantsListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_restaurant, viewGroup, false);
        return new RestaurantsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RestaurantsListViewHolder holder, int position) {

        Restaurant restaurant = dataList.get(holder.getAdapterPosition());
        String placeID = restaurant.getPlace_id();
        rating = restaurant.getRating();
        restaurantLatitude = restaurant.getGeometry().getLocation().getLat();
        restaurantLongitude = restaurant.getGeometry().getLocation().getLng();


        holder.nameField.setText(restaurant.getName());
        if (restaurant.getVicinity() != null) {
            holder.addressField.setText(restaurant.getVicinity());
        }

        if (restaurant.getTypes() != null) {
            holder.kindField.setText(restaurant.getTypes().get(0));
        }

        if (restaurant.getOpening_hours() != null) {
            if (restaurant.getOpening_hours().getOpenNow().equals("true")) {
                String open = "Open";
                holder.informationField.setText(open);
                holder.informationField.setTypeface(null, Typeface.BOLD);
                holder.informationField.setTextColor(Color.GREEN);
            } else {
                String close = "Close";
                holder.informationField.setText(close);
                holder.informationField.setTypeface(null, Typeface.BOLD);
                holder.informationField.setTextColor(Color.RED);
            }
        }

        if (restaurant.getRating() != null) {
            holder.displayStarsRating();
        }

        String distance = getDistanceInMeters() + " m";
        holder.distanceField.setText(distance);


        if (restaurant.getPhotos() != null) {
            photoData = restaurant.getPhotos().get(0).getPhotoReference();
            photoWidth = restaurant.getPhotos().get(0).getWidth();
        }

        /*Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load(parseDataPhotoToImage())
                .placeholder((R.drawable.ic_launcher_background))
                .error(R.drawable.ic_baseline_outdoor_grill_24)
                .into(holder.imageField);*/

        Glide.with(context).load(parseDataPhotoToImage())
                .into(holder.imageField);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(context, RestaurantProfilActivity.class);
                i.putExtra("place_id", placeID);
                i.putExtra("name", restaurant.getName());
                i.putExtra("photo", photoData);
                i.putExtra("photoWidth", photoWidth);
                i.putExtra("vicinity", restaurant.getVicinity());
                i.putExtra("type", restaurant.getTypes().get(0));
                i.putExtra("rate", rating);
                ((Activity) context).startActivityForResult(i, REQUEST_RESTAURANTS_DETAILS);

            }
        });

    }

    @Override
    public int getItemCount() {
        if (dataList.size() == 0) {
            return 0;
        }else
        return dataList.size();
    }

    public void updateData(List<Restaurant> restaurants) {
        this.dataList = restaurants;
        this.notifyDataSetChanged();
    }

    public void updateLocation(LatLng latLng) {
        this.mlatlng = latLng;
        this.notifyDataSetChanged();
    }


    class RestaurantsListViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        private final ImageView imageField;
        private final TextView nameField;
        private final TextView kindField;
        private final TextView addressField;
        private final TextView informationField;
        private final TextView distanceField;
        private final ImageView noteField2;
        private final ImageView noteField3;

        RestaurantsListViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            imageField = mView.findViewById(R.id.list_view_image);
            nameField = mView.findViewById(R.id.list_view_name);
            kindField = mView.findViewById(R.id.list_view_kind_restaurant);
            addressField = mView.findViewById(R.id.list_view_address);
            informationField = mView.findViewById(R.id.list_view_informations);
            distanceField = mView.findViewById(R.id.list_view_distance);
            noteField2 = mView.findViewById(R.id.list_view_star_2);
            noteField3 = mView.findViewById(R.id.list_view_star_3);
        }

        private void displayStarsRating() {
            double dRating = Double.parseDouble(rating);
            if (dRating <= 1.67) {
                noteField3.setVisibility(View.INVISIBLE);
                noteField2.setVisibility(View.INVISIBLE);
            }
            if (dRating > 1.67 && dRating < 3.4) {
                noteField3.setVisibility(View.INVISIBLE);
            }
        }
    }

    private String parseDataPhotoToImage() {

        return new StringBuilder().append(PLACES_API_BASE).append(MAX_WIDTH).append(photoWidth)
                .append(PHOTOREFERENCE).append(photoData).append(API_KEY).toString();

        //https://maps.googleapis.com/maps/api/place/photo?maxwidth=4032&photoreference=ATtYBwKU4gK6Y-aRIDxoRKq8wRs3se36xK49g0PS-O2iLg-NS_osj85NapJsY-aNHOk2MyZYisu2YN013wX5VlfvvbkyIfiYimscYJImUq_jfRCPjOOl3fNIuJyrFDnWoP64tbFfZF1Aja5WaWyISM6BOW0lvsmjySrvMiRUjC03_NCKxILK&key=AIzaSyBpPAJjNZ2X4q0xz3p_zK_uW3MdZCpD704
    }

    private String getDistanceInMeters() {

        // get current location
        double currentLatitude = mlatlng.latitude;
        double currentLongitude = mlatlng.longitude;

        double rLatitude = Double.parseDouble(String.valueOf(restaurantLatitude));
        double rLongitude = Double.parseDouble(String.valueOf(restaurantLongitude));

        Location loc1 = new Location("");
        loc1.setLatitude(currentLatitude);
        loc1.setLongitude(currentLongitude);

        Location loc2 = new Location("");
        loc2.setLatitude(rLatitude);
        loc2.setLongitude(rLongitude);

        float distanceInMeters = loc1.distanceTo(loc2);
        int distanceRound = (int) distanceInMeters;
        String distance = String.valueOf(distanceRound);

        return distance;
    }

}

