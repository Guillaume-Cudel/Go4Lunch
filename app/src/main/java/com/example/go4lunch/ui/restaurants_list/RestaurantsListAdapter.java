package com.example.go4lunch.ui.restaurants_list;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.NavigationActivity;
import com.example.go4lunch.R;
import com.example.go4lunch.model.Restaurant;
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
    private String photoData, photoWidth, rating, restaurantLatitude, restaurantLongitude;





    public RestaurantsListAdapter(final List<Restaurant> dataList, Context context) {
        this.dataList = dataList;
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

        Restaurant restaurant = dataList.get(position);

            if (restaurant.getPhotos() != null) {
                photoData = restaurant.getPhotos().get(0).getPhotoReference();
                photoWidth = restaurant.getPhotos().get(0).getWidth();
            }
        rating = restaurant.getRating();
        restaurantLatitude = restaurant.getGeometry().getLocation().getLat();
        restaurantLongitude = restaurant.getGeometry().getLocation().getLng();


        holder.nameField.setText(restaurant.getName());
        if (restaurant.getVicinity() != null){ holder.addressField.setText(restaurant.getVicinity());}
        //if (restaurant.getTypes() != null){holder.kindField.setText(restaurant.getTypes());}
        String opening = "Open: " + restaurant.getOpening_hours().getOpenNow();
        if (restaurant.getOpening_hours() != null){holder.informationField.setText(opening);}
        if(restaurant.getRating() != null){
            holder.displayStarsRating();
        }

        String distance = getDistanceInMeters() + " m";
        holder.distanceField.setText(distance);


            Picasso.Builder builder = new Picasso.Builder(context);
            builder.downloader(new OkHttp3Downloader(context));
            builder.build().load(parseDataPhotoToImage())
                           .placeholder((R.drawable.ic_launcher_background))
                           .error(R.drawable.ic_baseline_outdoor_grill_24)
                           .into(holder.imageField);


        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

    public void updateData(List<Restaurant> restaurants){
        this.dataList = restaurants;
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
        private final ImageView noteField1;
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
            noteField1 = mView.findViewById(R.id.list_view_star_1);
            noteField2 = mView.findViewById(R.id.list_view_star_2);
            noteField3 = mView.findViewById(R.id.list_view_star_3);
        }

        private void displayStarsRating(){
            double dRating = Double.parseDouble(rating);
            if (dRating <= 1.67){
                noteField3.setVisibility(View.INVISIBLE);
                noteField2.setVisibility(View.INVISIBLE);
            }
            if(dRating > 1.67 && dRating < 3.4){
                noteField3.setVisibility(View.INVISIBLE);
            }
        }
    }

    private String parseDataPhotoToImage(){

        return new StringBuilder().append(PLACES_API_BASE).append(MAX_WIDTH).append(photoWidth)
                .append(PHOTOREFERENCE).append(photoData).append(API_KEY).toString();

        //https://maps.googleapis.com/maps/api/place/photo?maxwidth=4032&photoreference=ATtYBwKU4gK6Y-aRIDxoRKq8wRs3se36xK49g0PS-O2iLg-NS_osj85NapJsY-aNHOk2MyZYisu2YN013wX5VlfvvbkyIfiYimscYJImUq_jfRCPjOOl3fNIuJyrFDnWoP64tbFfZF1Aja5WaWyISM6BOW0lvsmjySrvMiRUjC03_NCKxILK&key=AIzaSyBpPAJjNZ2X4q0xz3p_zK_uW3MdZCpD704
    }

    private String getDistanceInMeters(){
        double currentLatitude = 0;
        double currentLongitude = 0;
        if(context instanceof NavigationActivity){
            NavigationActivity activity = (NavigationActivity) context;
            currentLatitude =  activity.getDoubleLatitude() ;
            currentLongitude = activity.getDoubleLongitude();
        }

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

