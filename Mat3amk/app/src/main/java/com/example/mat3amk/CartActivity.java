package com.example.mat3amk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mat3amk.Database.Database;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView totalTextView;
    CartAdapter cartAdapter;
    List<Order> orders = new ArrayList<>();
    Button freeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.list_cart);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        totalTextView = findViewById(R.id.total_tv);
        freeButton = findViewById(R.id.free);


        freeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(CartActivity.this).cleanCart();
                cartAdapter.setData(new ArrayList<Order>());
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
}
