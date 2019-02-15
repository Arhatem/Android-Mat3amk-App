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
    private List<String> eat;
    private static final String TAG = CategoryAdapter.class.getSimpleName();



    public CategoryAdapter(List<String> eat)
    {
        this.eat = eat;
    }
    public CategoryAdapter(Context context)
    {
        this.context = context;
    }

    public void setEat(List<String> eat)
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

        String currentEat = eat.get(i);
        String currentDish = currentEat.substring(0,currentEat.lastIndexOf(","));
        String currentPrice = currentEat.substring(currentEat.lastIndexOf(",")+1);

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

    public class CategoryViewHolder extends RecyclerView.ViewHolder{

        private TextView dishTextView;
        private TextView priceTextView;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            dishTextView = itemView.findViewById(R.id.dish_name);
            priceTextView = itemView.findViewById(R.id.dish_price);
        }
    }
}
