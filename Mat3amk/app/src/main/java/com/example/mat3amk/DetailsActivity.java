package com.example.mat3amk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.mat3amk.Database.Database;
import com.example.mat3amk.Model.Rating;
import com.example.mat3amk.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import info.hoang8f.widget.FButton;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class DetailsActivity extends AppCompatActivity implements RatingDialogListener {

    TextView nameTextView ,priceTextView , descriptionTextView,totalPriceTextView;
    ImageView foodImage;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton ratingButton;
    ElegantNumberButton numberButton;
    DatabaseReference mDatabase;
    String key,address , res,category;
    RadioGroup radioGroup;
    RadioButton mediumButton , largeButton;
    String selectedPrice , mediumPrice , largePrice;
    RatingBar ratingBar;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference ratingRef;
    FButton commentButton;
    CounterFab cartButton;
    CoordinatorLayout coordinatorLayout;
    Food food;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/cf.otf")
                .setFontAttrId(R.attr.fontPath)
                .build());
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
        ratingButton = findViewById(R.id.btnRating);
        ratingBar = findViewById(R.id.ratingBar);
        coordinatorLayout = findViewById(R.id.root);
        commentButton =findViewById(R.id.commentBtn);
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ShowCommentActivity.class);
                intent.putExtra("food-name",key);
                startActivity(intent);
            }
        });
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        ratingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user!=null && user.isEmailVerified())
                showRatingDialog();
                else
                    Toast.makeText(DetailsActivity.this, "Please Login to Rate this Food", Toast.LENGTH_SHORT).show();
            }
        });


        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

         cartButton = (CounterFab) findViewById(R.id.fab);


         key = getIntent().getStringExtra("key");
         address = getIntent().getStringExtra("address");
         res = getIntent().getStringExtra("res");
         category = getIntent().getStringExtra("cat");

         ratingRef = FirebaseDatabase.getInstance().getReference().child("Rating");

        mDatabase = FirebaseDatabase.getInstance().getReference().child("restaurants").child(address).child(res)
                .child("categories").child(category).child(key);




        getFoodDetails();

        getRatingFood(key);



    }

    private void getRatingFood(String dish_name) {
        Query foodRating = ratingRef.orderByChild("foodName").equalTo(dish_name);
        foodRating.addValueEventListener(new ValueEventListener() {
            int count = 0;
            int sum = 0;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    Rating rating = data.getValue(Rating.class);
                    sum+= Integer.parseInt(rating.getRateValue());
                    count++;
                }
                if(count!=0) {
                    float average = sum / count;
                    ratingBar.setRating(average);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getFoodDetails()
    {
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

                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null && user.isEmailVerified()) {
                            Database database = new Database(DetailsActivity.this);
                            if (database.exists(currentDish,user.getEmail())) {
                                Log.v("yes", "exists");
                                int count = database.getProductCount(currentDish,user.getEmail());
                                count += Integer.parseInt(numberButton.getNumber());
                                if (count == 0)
                                    database.removeFromCart(currentDish, String.valueOf(database.getProductCount(currentDish,user.getEmail())));
                                database.updateProductCount(currentDish, count,user.getEmail());
                            } else {
                                new Database(DetailsActivity.this).addToCart(new Order(
                                        user.getEmail(),
                                        currentDish,
                                        numberButton.getNumber(),
                                        selectedPrice.trim()
                                ));
                            }
                          Snackbar snackbar = Snackbar.make(coordinatorLayout,"Added to Cart",Snackbar.LENGTH_LONG);
                            snackbar.setAction("Show Cart", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                                    startActivity(intent);
                                }
                            });
                            snackbar.setActionTextColor(Color.YELLOW);
                            snackbar.show();
                            cartButton.setCount(new Database(DetailsActivity.this).getCountCart(user.getEmail()));
                        }
                        else
                            Toast.makeText(getApplicationContext(), "You must login to access the cart", Toast.LENGTH_SHORT).show();

                    }

                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showRatingDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Very Bad","Not Good","Quite Ok","Very Good","Excellent"))
                .setDefaultRating(1)
                .setTitle("Rate this Food")
                .setDescription("please select some star and give your feedback")
                .setTitleTextColor(R.color.colorPrimary)
                .setDescriptionTextColor(R.color.colorPrimary)
                .setHint("please write your comment here...")
                .setHintTextColor(R.color.white)
                .setCommentTextColor(android.R.color.white)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(DetailsActivity.this)
                .show();
    }


    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(int i, @NotNull String s) {

        // Get Rating and upload to firebase
        final Rating rating = new Rating(user.getDisplayName(),food.getDish_name(),String.valueOf(i),s,user.getEmail());

      /* mDatabase.orderByChild("foodName").equalTo(food.getDish_name()).addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               int flag =0;
               for(DataSnapshot data : dataSnapshot.getChildren())
                   if(data.child("email").getValue().equals(user.getEmail()))
                   {
                       Log.v("yes","exists");
                       ratingRef.child(data.getKey()).child("comment").setValue(rating.getComment());
                       ratingRef.child(data.getKey()).child("rateValue").setValue(rating.getRateValue());
                       flag = 1;
                        break;
                   }
                   if(flag==0)
                   {

                   }
                   else
                       flag=0;
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });*/

        // Fix user can rate multiple times
        ratingRef.push().setValue(rating).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(DetailsActivity.this, "Thank you for submit Rating!!!", Toast.LENGTH_SHORT).show();

                getRatingFood(key);
            }
        });
        /*  ratingRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(user.getUid()).exists())
                {
                    //Remove old Value
                    ratingRef.child(user.getUid()).removeValue();
                    // Update new Value
                    ratingRef.child(user.getUid()).setValue(rating);

                }
                else
                {
                    // Update new Value
                    ratingRef.child(user.getUid()).setValue(rating);
                }
                Toast.makeText(DetailsActivity.this, "Thank you for submit Rating!!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(user!=null && user.isEmailVerified())
        cartButton.setCount(new Database(this).getCountCart(user.getEmail()));
    }

    @Override
    public void onNeutralButtonClicked() {

    }
}
