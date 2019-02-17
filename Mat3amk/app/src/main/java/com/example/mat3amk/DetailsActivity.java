package com.example.mat3amk;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.mat3amk.Database.Database;
import com.example.mat3amk.R;
import com.google.firebase.database.DatabaseReference;


public class DetailsActivity extends AppCompatActivity {

    TextView nameTextView ,priceTextView , descriptionTextView,totalPriceTextView;
    ImageView foodImage;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton cartButton;
    ElegantNumberButton numberButton;
    DatabaseReference mDatabase;
    String name;
    RadioGroup radioGroup;
    RadioButton mediumButton , largeButton;
    String selectedPrice , mediumPrice , largePrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        numberButton = findViewById(R.id.number_button);
        descriptionTextView = findViewById(R.id.food_description);
        nameTextView = findViewById(R.id.food_name);
        priceTextView = findViewById(R.id.food_price);
        foodImage = findViewById(R.id.food_image);
        radioGroup = findViewById(R.id.radio_group);
        mediumButton = findViewById(R.id.radio_medium);
        largeButton = findViewById(R.id.radio_large);
        totalPriceTextView = findViewById(R.id.total_price);


        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

         cartButton = (FloatingActionButton) findViewById(R.id.fab);


         name = getIntent().getStringExtra("name");
        final String currentDish = name.substring(0,name.lastIndexOf(","));
        collapsingToolbarLayout.setTitle(currentDish);
        nameTextView.setText(currentDish);
        final String currentPrice = name.substring(name.lastIndexOf(",")+1,name.indexOf("E"));
        priceTextView.setText(currentPrice);


        selectedPrice = currentPrice;

        if(currentPrice.contains("-"))
        {
             mediumPrice = currentPrice.substring(0,currentPrice.indexOf("-"));
             largePrice = currentPrice.substring(currentPrice.indexOf("-")+1);
            radioGroup.setVisibility(View.VISIBLE);
            mediumButton.setText("Medium "+mediumPrice+" EGP");
            largeButton.setText("Large "+largePrice+" EGP");
            selectedPrice = mediumPrice;
            totalPriceTextView.setText(selectedPrice+ " EGP");

            mediumButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPrice = mediumPrice;
                    String totalPrice = String.valueOf(Integer.parseInt(selectedPrice.trim())*Integer.parseInt(numberButton.getNumber()));
                    totalPriceTextView.setText(totalPrice+" EGP");
                }
            });

            largeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPrice = largePrice;
                    String totalPrice = String.valueOf(Integer.parseInt(selectedPrice.trim())*Integer.parseInt(numberButton.getNumber()));

                    totalPriceTextView.setText(totalPrice+" EGP");
                }
            });
        }
        else
        {
            totalPriceTextView.setText(selectedPrice+" EGP");
        }
        numberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                String totalPrice = String.valueOf(Float.parseFloat(selectedPrice.trim())*Float.parseFloat(view.getNumber()));
                totalPriceTextView.setText(totalPrice+" EGP");

            }
        });


        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new Database(DetailsActivity.this).addToCart(new Order(
                    currentDish,
                        numberButton.getNumber(),
                        selectedPrice.trim()
                ));
                Snackbar.make(view, "Added to Cart", Snackbar.LENGTH_LONG)
                        .setAction("Show Cart", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(DetailsActivity.this,CartActivity.class);
                                startActivity(intent);
                            }
                        }).show();
            }
        });


    }


}
