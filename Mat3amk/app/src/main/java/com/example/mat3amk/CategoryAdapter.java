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
    final private DetailsOnClickHandler mClickHandler;

    public interface DetailsOnClickHandler {
        void onClick(String name);
    }


    public CategoryAdapter(Context context, DetailsOnClickHandler clickHandler)
    {
        this.context = context;
        this.mClickHandler = clickHandler;
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
            String name = eat.get(position);
            mClickHandler.onClick(name);


        }
    }
}
