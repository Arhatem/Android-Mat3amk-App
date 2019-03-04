package com.example.mat3amk;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mat3amk.Database.Database;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{

    private Context context;
    private Database localDB;
    private List<Food> eat;
    private static final String TAG = CategoryAdapter.class.getSimpleName();
    final private DetailsOnClickHandler mClickHandler;


    public interface DetailsOnClickHandler {
        void onClick(String name,String address , String res, String category);
    }


    public CategoryAdapter(Context context, DetailsOnClickHandler clickHandler)
    {
        this.context = context;
        this.mClickHandler = clickHandler;
        localDB = new Database(this.context);
    }

    public void setEat(List<Food> eat)
    {
        if(eat!=null)
        {
            this.eat = eat;
            notifyDataSetChanged();
        }
    }


    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_item,viewGroup,false);
        CategoryViewHolder categoryViewHolder
                = new CategoryViewHolder(view);
        return categoryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder categoryViewHolder
            , int i) {

        String currentDish = eat.get(i).getDish_name();
      //  String currentDish = currentEat.substring(0,currentEat.lastIndexOf(","));
        String currentPrice = eat.get(i).getPrice();

        categoryViewHolder
                .dishTextView.setText(currentDish);
        categoryViewHolder
                .priceTextView.setText(currentPrice);
        if(eat.get(i).isFav())
        {categoryViewHolder.favImage.setImageResource(R.drawable.ic_favorite_black_24dp);
        }


    }

    @Override
    public int getItemCount() {
        if(eat!=null) {

            return eat.size();
        }
        return 0;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{

        private TextView dishTextView;
        private TextView priceTextView;
        private ImageView favImage;
        private FirebaseAuth mAuth;
        private FirebaseUser user;
        private Toast mToast;


        public void makeToast(String msg)
        {
            if(mToast!=null)
                mToast.cancel();

            mToast = Toast.makeText(context,msg,Toast.LENGTH_SHORT);
            mToast.show();
        }

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            dishTextView = itemView.findViewById(R.id.dish_name);
            priceTextView = itemView.findViewById(R.id.dish_price);
            favImage = itemView.findViewById(R.id.fav);
            mAuth = FirebaseAuth.getInstance();
            user = mAuth.getCurrentUser();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    String key = eat.get(position).getKey();
                    String address = eat.get(position).getAddress();
                    String res = eat.get(position).getRes();
                    String category = eat.get(position).getCategory();

                    mClickHandler.onClick(key, address, res, category);

                }
            });

            favImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (user != null && user.isEmailVerified()) {
                        int position = getAdapterPosition();
                        String foodName = eat.get(position).getDish_name();
                        if (!localDB.isFavorite(foodName, user.getEmail())) {
                            localDB.addToFavorites(foodName, user.getEmail());
                            favImage.setImageResource(R.drawable.ic_favorite_black_24dp);
                            Toast.makeText(context, foodName + "was added to Favorites", Toast.LENGTH_SHORT).show();
                        } else {
                            localDB.removeFromFavorites(foodName, user.getEmail());
                            favImage.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                            Toast.makeText(context, foodName + "was removed from Favorites", Toast.LENGTH_SHORT).show();

                        }
                    }
                    else
                    {
                        makeToast("Please Login to add Foods to your favorites");
                    }
                }
            });

        }


    }
}
