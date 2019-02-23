package com.example.mat3amk;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.ViewHolder> implements Filterable {

    private List<Restaurant> restList;
    private List<Restaurant> resListFull;
    private Context context;
    final private RestaurantOnClickHandler mClickHandler;
    private Filter resFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Restaurant> filteredList = new ArrayList<>();

            if(TextUtils.isEmpty(constraint))
            {
                filteredList.addAll(resListFull);
            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Restaurant restaurant : resListFull)
                {
                    if(restaurant.getName().toLowerCase().contains(filterPattern))
                        filteredList.add(restaurant);
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            restList.clear();
            restList.addAll((List)results.values);
            notifyDataSetChanged();

        }
    };

    @Override
    public Filter getFilter() {
        return resFilter;
    }

    public interface RestaurantOnClickHandler {
        void onClick(String key);
    }


    public RestaurantsAdapter(List<Restaurant> list, Context context,RestaurantOnClickHandler clickHandler)
    {
        restList = list;
        this.context = context;
        mClickHandler = clickHandler;
        resListFull = new ArrayList<>(restList);
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
        String phone = restaurant.getPhone();
        if(phone.contains("1") || phone.contains("0")) {
            String p="";
            if(phone.contains(",")) {
                p = phone.substring(0, phone.indexOf(","));
            }
            else
            {
                p =phone;
            }
            viewHolder.phoneTextView.setText(p);
        }
        else
        {
            viewHolder.phoneTextView.setVisibility(View.GONE);
        }
        Picasso.get().load(logoUrl).into(viewHolder.coverImageView);


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
            resListFull = new ArrayList<>(restList);
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
        TextView phoneTextView;
        /*TextView addressTextView;
        RatingBar ratingBar;*/
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.name_tv);
            ratingBar = itemView.findViewById(R.id.rating);
            coverImageView = itemView.findViewById(R.id.logo);
            phoneTextView = itemView.findViewById(R.id.phone_tv);
            itemView.setOnClickListener(this);
           /* addressTextView = itemView.findViewById(R.id.address);
            ratingBar = itemView.findViewById(R.id.rating);*/
        }

        @Override
        public void onClick(View v) {

            int adapterPosition = getAdapterPosition();
          String key =  restList.get(adapterPosition).getResKey();
          mClickHandler.onClick(key);

        }
    }
}
