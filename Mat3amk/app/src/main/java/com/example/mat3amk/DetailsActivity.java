package com.example.mat3amk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.mat3amk.Database.Database;
import com.example.mat3amk.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class DetailsActivity extends AppCompatActivity {

    TextView nameTextView ,priceTextView , descriptionTextView,totalPriceTextView;
    ImageView foodImage;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton cartButton;
    ElegantNumberButton numberButton;
    DatabaseReference mDatabase;
    String key,address , res,category;
    RadioGroup radioGroup;
    RadioButton mediumButton , largeButton;
    String selectedPrice , mediumPrice , largePrice;
    Food food;
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


         key = getIntent().getStringExtra("key");
         address = getIntent().getStringExtra("address");
         res = getIntent().getStringExtra("res");
         category = getIntent().getStringExtra("cat");

        mDatabase = FirebaseDatabase.getInstance().getReference().child("restaurants").child(address).child(res)
                .child("categories").child(category).child(key);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 food = dataSnapshot.getValue(Food.class);

                final String currentDish = food.getDish_name();
                collapsingToolbarLayout.setTitle(currentDish);
                nameTextView.setText(currentDish);
                final String currentPrice = food.getPrice().substring(0,food.getPrice().indexOf("E"));
                priceTextView.setText(currentPrice);
                if(!TextUtils.isEmpty(food.getDescription())) {
                    descriptionTextView.setText(food.getDescription());
                }
                String url = food.getImage_URL();
                if(!TextUtils.isEmpty(url)) {
                    Picasso.get().load(url).into(foodImage);
                }
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });








    }


}
