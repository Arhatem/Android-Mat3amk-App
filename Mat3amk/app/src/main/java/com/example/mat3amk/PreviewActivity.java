package com.example.mat3amk;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mat3amk.NetworkUtils.NetworkUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class PreviewActivity extends AppCompatActivity {
    private String key;
    private static final String TAG = PreviewActivity.class.getSimpleName();
    private DatabaseReference mDatabase;
    private String address;
    private ImageView imageView;
    private ProgressBar progressBar;
    private AppBarLayout appBarLayout;
    private List<List<String>> map;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView internetImage;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private  ViewPagerAdapter viewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageView = findViewById(R.id.cover_image);
        progressBar = findViewById(R.id.progress_bar);
        appBarLayout = findViewById(R.id.app_bar);
        collapsingToolbarLayout = findViewById(R.id.htab_collapse_toolbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);



        internetImage = findViewById(R.id.internet_image);

        key = getIntent().getStringExtra("name");
        address = getIntent().getStringExtra("address");
        getSupportActionBar().setTitle(key);
        map = new ArrayList<>();

       tabLayout = findViewById(R.id.tabs);
          viewPager = findViewById(R.id.pager);
          viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());



        mDatabase = FirebaseDatabase.getInstance().getReference()
                .child("restaurants").child(address).child(key);


                if(NetworkUtils.isConnectedToInternet(getApplicationContext())) {
                    internetImage.setVisibility(View.GONE);
                    loadData();


                }
                else
                {
                    internetImage.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    appBarLayout.setVisibility(View.GONE);
                    return;
                }









    }

    public void loadData()
    {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);

                String name = dataSnapshot.child("name").getValue().toString();
                String imageUrl = dataSnapshot.child("image_url").getValue().toString();
                Picasso.get().load(imageUrl).placeholder(R.drawable.background).fit().into(imageView);

                mDatabase.child("categories").addChildEventListener(new ChildEventListener() {

                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        String tabName = dataSnapshot.getKey();
                        List<Food> eats = new ArrayList<>();
                        for(DataSnapshot data : dataSnapshot.getChildren())
                        {
                            Food food =data.getValue(Food.class);
                            food.setKey(data.getKey());
                            food.setAddress(address);
                            food.setRes(key);
                            food.setCategory(dataSnapshot.getKey());
                            eats.add(food);
                        }
                        CategoryFragment fragment = new CategoryFragment(eats);
                        viewPagerAdapter.addFragment(fragment,tabName);

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

                viewPager.setAdapter(viewPagerAdapter);
                tabLayout.setupWithViewPager(viewPager);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                appBarLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setupViewPager(ViewPager viewPager, int noOfCategories,List<Food> eats)
    {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        for(int i=0;i<noOfCategories;i++)
        {
            CategoryFragment fragment = new CategoryFragment(eats);
            viewPagerAdapter.addFragment(fragment,"Additions");
        }
        viewPager.setAdapter(viewPagerAdapter);

    }
}
