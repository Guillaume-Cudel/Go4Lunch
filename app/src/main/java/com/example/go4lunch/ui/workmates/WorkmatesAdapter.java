package com.example.go4lunch.ui.workmates;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.R;
import com.example.go4lunch.model.firestore.UserFirebase;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WorkmatesAdapter extends RecyclerView.Adapter<WorkmatesAdapter.WorkmatesViewHolder> {

    private List<UserFirebase> workmatesList;
    private final Context context;

    public WorkmatesAdapter(final List<UserFirebase> workmatesList, Context context) {
        this.workmatesList = workmatesList;
        this.context = context;
    }

    @NotNull
    @Override
    public WorkmatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_workmate, parent, false);
        return new WorkmatesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull WorkmatesViewHolder holder, int position) {

        UserFirebase user = workmatesList.get(holder.getAdapterPosition());

        String photo = user.getUrlPicture();
        if (photo != null){
            Glide.with(context).load(photo)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.userPhoto);
        }else{
            Glide.with(context).load(R.drawable.ic_user_profile)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.userPhoto);
        }

        String username = user.getUsername();
        String restaurantLess = username + " hasn't decided yet.";
        holder.usernameText.setText(restaurantLess);
        /*boolean choosed = user.getHaveChoosed();
        if(choosed){
            // todo add text if user have choosed a restaurant

        }else {
            String restaurantLess = " hasn't decided yet.";
            holder.usernameText.setText(username + restaurantLess);
        }*/
    }

    @Override
    public int getItemCount() {
        if (workmatesList.size() == 0){
            return 0;
        }else
        return workmatesList.size();
    }

    class WorkmatesViewHolder extends RecyclerView.ViewHolder{

        public final View mView;
        private final TextView usernameText;
        private final ImageView userPhoto;



        public WorkmatesViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            mView = itemView;

            usernameText = mView.findViewById(R.id.workmate_text_item);
            userPhoto = mView.findViewById(R.id.workmate_image_item);
        }
    }
}
