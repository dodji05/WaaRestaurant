package com.dga.services.waa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dga.services.waa.R;
import com.dga.services.waa.models.Restaurants;



import java.util.List;

public class RestaurantsAdapters  extends RecyclerView.Adapter<RestaurantsAdapters.CustomViewHolder>{
    private static final String TAG = "RestaurantsAdaptersRecycleViewAdapters";
    private Context mContext;
    private List<Restaurants> restaurantsList;

    public RestaurantsAdapters(Context mContext, List<Restaurants>restaurantsList) {
        this.mContext = mContext;
        this.restaurantsList = restaurantsList;
    }

    @NonNull
    @Override
    public RestaurantsAdapters.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_resto, null);

        return new RestaurantsAdapters.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantsAdapters.CustomViewHolder holder, int position) {

        holder.resto_name.setText(restaurantsList.get(position).getNom());
        holder.resto_note.setText("4.1");
        holder.resto_prix.setText("5000");


       Glide.with(mContext)
                .asBitmap()
                .load(restaurantsList.get(position).getImages().get(0))
                .into(holder.image_1);


    }

    @Override
    public int getItemCount() {
        return restaurantsList.size();
    }

    class  CustomViewHolder extends RecyclerView.ViewHolder {

        TextView resto_name,resto_prix,resto_note;
        ImageView image_1;


        CustomViewHolder (View view) {
            super(view);

            this.resto_name = view.findViewById(R.id.resto_name);
            this.resto_prix = view.findViewById(R.id.resto_prix);
            this.resto_note = view.findViewById(R.id.resto_note);
            this.image_1 = view.findViewById(R.id.image_1);


        }

    }
}
