package com.example.mat3amk;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.ViewHolder> {

    private List<Restaurant> restList;
    private Context context;
    final private RestaurantOnClickHandler mClickHandler;

    public interface RestaurantOnClickHandler {
        void onClick(String name);
    }


    public RestaurantsAdapter(List<Restaurant> list, Context context,RestaurantOnClickHandler clickHandler)
    {
        restList = list;
        this.context = context;
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.restaurant_preview,viewGroup,false);

        RestaurantsAdapter.ViewHolder viewHolder = new RestaurantsAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Restaurant restaurant = restList.get(i);
        viewHolder.nameTextView.setText(restaurant.getName());
        viewHolder.ratingBar.setRating(restaurant.getRating());
        String logoUrl = restaurant.getCover();

        Picasso.get().load(logoUrl).placeholder(R.drawable.load).into(viewHolder.coverImageView);


       // viewHolder.addressTextView.setText(place.getAddress());
      /*  if(place.getRating() > -1){
            viewHolder.ratingBar.setNumStars((int)place.getRating());
        }else{
            viewHolder.ratingBar.setVisibility(View.GONE);
        }*/


    }

    public void setData(List<Restaurant> list)
    {
        if(list!=null)
        {
            restList = list;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        if(restList !=null)
        return restList.size();

        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView nameTextView;
        RatingBar ratingBar;
        ImageView coverImageView;
        /*TextView addressTextView;
        RatingBar ratingBar;*/
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.name_tv);
            ratingBar = itemView.findViewById(R.id.rating);
            coverImageView = itemView.findViewById(R.id.logo);
            itemView.setOnClickListener(this);
           /* addressTextView = itemView.findViewById(R.id.address);
            ratingBar = itemView.findViewById(R.id.rating);*/
        }

        @Override
        public void onClick(View v) {

            int adapterPosition = getAdapterPosition();
          String name =  restList.get(adapterPosition).getName();
          mClickHandler.onClick(name);

        }
    }
}
