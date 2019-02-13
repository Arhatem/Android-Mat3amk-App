package com.example.mat3amk;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RestaurantsActivity extends AppCompatActivity implements RestaurantsAdapter.RestaurantOnClickHandler {

    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private RestaurantsAdapter mAdapter;
    private List<Restaurant> res;
    private ProgressBar progressBar;
    String address = "";
    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);
        address = getIntent().getStringExtra("address");

        mToolbar = (Toolbar)findViewById(R.id.main_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(address);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerView = findViewById(R.id.list);
        progressBar = findViewById(R.id.pb);

        LinearLayoutManager recyclerLayoutManager =
                new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerLayoutManager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(),
                        recyclerLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        res = new ArrayList<>();
        mAdapter = new RestaurantsAdapter(new ArrayList<Restaurant>(),this,this);
        recyclerView.setAdapter(mAdapter);


        mDatabase = FirebaseDatabase.getInstance()
                .getReference().child("restaurants").child(address);


        mDatabase.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);


                // Log.v("ResActivity",name);
                res.add(restaurant);
                mAdapter.setData(res);


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(String name) {

        Intent intent = new Intent(RestaurantsActivity.this,PreviewActivity.class);
        intent.putExtra("name",name);
        startActivity(intent);
    }
}
