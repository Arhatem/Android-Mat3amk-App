package com.example.mat3amk;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mat3amk.Database.Database;
import com.example.mat3amk.Helper.RecyclerItemTouchHelper;
import com.example.mat3amk.Interface.RecyclerItemTouchHelperListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import info.hoang8f.widget.FButton;

public class CartActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    RecyclerView recyclerView;
    TextView totalTextView;
    CartAdapter cartAdapter;
    List<Order> orders = new ArrayList<>();
    FButton freeButton;
    RelativeLayout rootLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.list_cart);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        totalTextView = findViewById(R.id.total_tv);
        freeButton = findViewById(R.id.free);
        rootLayout = findViewById(R.id.root_layout);

        ItemTouchHelper.SimpleCallback callback = new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(callback).attachToRecyclerView(recyclerView);

        freeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = cartAdapter.getOrdersCount();
                if(count!=0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                    builder.setTitle("Free Cart").setMessage("Are You Sure You want to free the Cart?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new Database(CartActivity.this).cleanCart();
                                    cartAdapter.setData(new ArrayList<Order>());
                                    totalTextView.setText("EGP0.00");
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else
                {
                    Toast.makeText(CartActivity.this, "Cart is Already Free", Toast.LENGTH_SHORT).show();
                }


            }
        });
        loadListFood();
    }

    private void loadListFood() {

        orders = new Database(this).getCarts();
        cartAdapter = new CartAdapter(new ArrayList<Order>(),this);
        recyclerView.setAdapter(cartAdapter);
        cartAdapter.setData(orders);


        //calculate total price

        float total  = 0;
        for(Order order : orders)
        {
            total+= (Float.parseFloat(order.getQuantity()))*(Float.parseFloat(order.getPrice()));
        }
        Locale locale = new Locale("en","EG");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

        totalTextView.setText(numberFormat.format(total));

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if(viewHolder instanceof CartAdapter.CartViewHolder)
        {
            String name = ((CartAdapter)recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition()).getProductName();
            final Order deleteItem = ((CartAdapter)recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition());
            final int deleteIndex = viewHolder.getAdapterPosition();
            cartAdapter.removeItem(deleteIndex);
            new Database(this).removeFromCart(deleteItem.getProductName(),deleteItem.getQuantity());
            List<Order> orders = new Database(this).getCarts();


            float total  = 0;
            for(Order order : orders)
            {
                total+= (Float.parseFloat(order.getQuantity()))*(Float.parseFloat(order.getPrice()));
            }
            Locale locale = new Locale("en","EG");
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

            totalTextView.setText(numberFormat.format(total));

            Snackbar snackbar = Snackbar.make(rootLayout,name+ " removed from cart!",Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartAdapter.restoreItem(deleteItem,deleteIndex);
                    new Database(getApplicationContext()).addToCart(deleteItem);

                    List<Order> orders = new Database(getApplicationContext()).getCarts();


                    float total  = 0;
                    for(Order order : orders)
                    {
                        total+= (Float.parseFloat(order.getQuantity()))*(Float.parseFloat(order.getPrice()));
                    }
                    Locale locale = new Locale("en","EG");
                    NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

                    totalTextView.setText(numberFormat.format(total));
                }
            });
snackbar.setActionTextColor(Color.YELLOW);
snackbar.show();




        }

    }
}
