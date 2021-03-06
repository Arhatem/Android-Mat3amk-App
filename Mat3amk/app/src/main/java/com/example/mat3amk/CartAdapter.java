package com.example.mat3amk;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {


    private List<Order> orders;
    private Context mContext;

    public CartAdapter(){}

    public CartAdapter(List<Order> orders, Context mContext) {
        this.orders = orders;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cart_item,viewGroup,false);
        CartViewHolder cartViewHolder = new CartViewHolder(view);
        return cartViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i) {

        TextDrawable textDrawable =TextDrawable.builder()
                .buildRound(""+orders.get(i).getQuantity(), Color.RED);

        cartViewHolder.countImage.setImageDrawable(textDrawable);
        Locale locale = new Locale("en","EG");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        float price = Float.parseFloat(orders.get(i).getPrice())* Float.parseFloat(orders.get(i).getQuantity());
        cartViewHolder.price.setText(numberFormat.format(price));
        cartViewHolder.name.setText(orders.get(i).getProductName());
    }

    public int getOrdersCount()
    {
        return orders.size();
    }

    public Order getItem(int position) { return orders.get(position); }
    @Override
    public int getItemCount() {
        if(orders.size()!=0)
            return orders.size();
        return 0;
    }

    public void setData(List<Order> orders)
    {
        this.orders = orders;
        notifyDataSetChanged();
    }

public   static   class CartViewHolder extends RecyclerView.ViewHolder{


    public     RelativeLayout view_background;
       public LinearLayout view_foreground;
        TextView name , price;
        ImageView countImage;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.cart_item_name);
            price = itemView.findViewById(R.id.cart_item_price);
            countImage = itemView.findViewById(R.id.cart_item_count);
            view_background = itemView.findViewById(R.id.view_background);
            view_foreground = itemView.findViewById(R.id.view_foreground);
        }
    }

    public void removeItem(int position)
    {
        orders.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Order item,int position)
    {
        orders.add(position,item);
        notifyItemInserted(position);
    }
}
