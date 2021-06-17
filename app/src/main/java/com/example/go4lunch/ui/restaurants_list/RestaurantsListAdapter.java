package com.example.go4lunch.ui.restaurants_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;
import com.example.go4lunch.model.Restaurant;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RestaurantsListAdapter extends RecyclerView.Adapter<RestaurantsListAdapter.RestaurantsListViewHolder> {

    private List<Restaurant> dataList;
    private Context context;

    public RestaurantsListAdapter(List<Restaurant> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public RestaurantsListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
       // View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_restaurant, viewGroup, false);
        View view = LayoutInflater.from(context).inflate(R.layout.item_restaurant, viewGroup, false);
        return new RestaurantsListViewHolder(view);
    }

        @Override
        public void onBindViewHolder(RestaurantsListViewHolder holder, int position) {

        Restaurant restaurant = dataList.get(position);

        holder.nameField.setText(restaurant.getName());
        if (restaurant.getAddress() != null){ holder.addressField.setText(restaurant.getAddress());}
        if (restaurant.getKind() != null){holder.kindField.setText(restaurant.getKind());}
        if (restaurant.getInformation() != null){holder.informationField.setText(restaurant.getInformation());}
           // if (restaurant.getDistance().equals(null)){ holder.distanceField.setText(restaurant.getDistance());}
            if (restaurant.getImageURL() != null){
                Picasso.Builder builder = new Picasso.Builder(context);
                builder.downloader(new OkHttp3Downloader(context));
                builder.build().load(restaurant.getImageURL())
                        .placeholder((R.drawable.ic_launcher_background))
                        .error(R.drawable.ic_launcher_background)
                        .into(holder.imageField);
            }
        }

        @Override
        public int getItemCount() {
            return dataList.size();
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
    }



    }

