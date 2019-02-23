package com.example.mat3amk;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{

    private Context context;
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


    }

    @Override
    public int getItemCount() {
        if(eat!=null) {

            return eat.size();
        }
        return 0;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView dishTextView;
        private TextView priceTextView;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            dishTextView = itemView.findViewById(R.id.dish_name);
            priceTextView = itemView.findViewById(R.id.dish_price);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            String key = eat.get(position).getKey();
            String address =eat.get(position).getAddress();
            String res = eat.get(position).getRes();
            String category = eat.get(position).getCategory();
            mClickHandler.onClick(key,address,res,category);


        }
    }
}
