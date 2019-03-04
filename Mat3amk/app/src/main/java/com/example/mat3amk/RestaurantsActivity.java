package com.example.mat3amk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.example.mat3amk.NetworkUtils.NetworkUtils;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.facebook.FacebookSdk.getApplicationContext;

public class RestaurantsActivity extends AppCompatActivity implements RestaurantsAdapter.RestaurantOnClickHandler {

    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private RestaurantsAdapter mAdapter;
    private List<Restaurant> res;
    private ProgressBar progressBar;
    private String resKey;
    String address = "";
    private Toolbar mToolbar;
    private ImageView internetImage;
    private SwipeRefreshLayout swipeRefreshLayout;
    List<String> suggestList = new ArrayList<>();
    private RestaurantsAdapter mSearchAdapter;
    MaterialSearchView materialSearchView;
    MaterialSearchBar searchBar;
    CallbackManager callbackManager;
    ShareDialog shareDialog;


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
        setContentView(R.layout.activity_restaurants);
        address = getIntent().getStringExtra("address");

        mToolbar = (Toolbar) findViewById(R.id.main_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(address);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swipeRefreshLayout = findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary
                , android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkUtils.isConnectedToInternet(getApplicationContext())) {
                    internetImage.setVisibility(View.GONE);

                    loadData();


                } else {
                    internetImage.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    return;
                }

            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (NetworkUtils.isConnectedToInternet(getApplicationContext())) {
                    internetImage.setVisibility(View.GONE);
                    loadData();


                } else {
                    internetImage.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    return;
                }

            }
        });

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager =  CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        recyclerView = findViewById(R.id.list);
        progressBar = findViewById(R.id.pb);
        internetImage = findViewById(R.id.internet_image);

        LinearLayoutManager recyclerLayoutManager =
                new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerLayoutManager);

        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(recyclerView.getContext(),
                R.anim.layout_fall_down);
        recyclerView.setLayoutAnimation(controller);
       /* DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(),
                        recyclerLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);*/
        res = new ArrayList<>();
        mAdapter = new RestaurantsAdapter(new ArrayList<Restaurant>(), this, this);
        recyclerView.setAdapter(mAdapter);


        mDatabase = FirebaseDatabase.getInstance()
                .getReference().child("restaurants").child(address);

    /*    materialSearchBar = findViewById(R.id.searchBar);
        materialSearchBar.setHint("Enter Your Restaurant");
        materialSearchBar.setSpeechMode(false);
        loadSuggest();
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // when user type their text, we will change suggest list
                List<String> suggest = new ArrayList<>();
                for (String search : suggestList)
                {
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                materialSearchBar.setLastSuggestions(suggest);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //when search bar is closed
                //restore original suggest adapter
                if(!enabled)
                {
                    recyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                //when search finish
                //show result of search adapter
                startSearch(text);

            }

            @Override
            public void onButtonClicked(int buttonCode) {
                if(buttonCode == MaterialSearchBar.BUTTON_BACK)
                {
                    mToolbar.setVisibility(View.VISIBLE);
                    materialSearchBar.setVisibility(View.INVISIBLE);
                }

            }
        });*/


    materialSearchView = findViewById(R.id.searchView);
    materialSearchView.closeSearch();
    }

    public void loadData() {
        res = new ArrayList<>();
        mAdapter.setData(res);
        mDatabase.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);
                resKey = dataSnapshot.getKey();
                restaurant.setResKey(resKey);


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
                swipeRefreshLayout.setRefreshing(false);

                //Animation
                recyclerView.getAdapter().notifyDataSetChanged();
                recyclerView.scheduleLayoutAnimation();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                progressBar.setVisibility(View.GONE);
                Toast.makeText(RestaurantsActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onClick(String key) {

        Intent intent = new Intent(RestaurantsActivity.this, PreviewActivity.class);
        intent.putExtra("name", key);
        intent.putExtra("address", address);
        startActivity(intent);
    }

    @Override
    public void shareOnClick(String imageUrl) {
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.v("target","called");
                SharePhoto photo = new SharePhoto.Builder()
                        .setBitmap(bitmap)
                        .build();

                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();
                if (ShareDialog.canShow(SharePhotoContent.class))
                    shareDialog.show(content);

            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.get().load(imageUrl).into(target);
    }

    private void loadSuggest() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Restaurant restaurant = data.getValue(Restaurant.class);
                    resKey = data.getKey();
                    restaurant.setResKey(resKey);
                    suggestList.add(restaurant.getName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
   /* private void startSearch(CharSequence text)
    {
        final List<Restaurant> restaurants = new ArrayList<>();
        mDatabase.orderByChild("name").equalTo(text.toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Restaurant restaurant = data.getValue(Restaurant.class);
                    resKey = data.getKey();
                    restaurant.setResKey(resKey);
                    restaurants.add(restaurant);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mSearchAdapter = new RestaurantsAdapter(restaurants,this,this);
        recyclerView.setAdapter(mSearchAdapter);


    }*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation,menu);
        MenuItem seachItem = menu.findItem(R.id.menu_search);
        materialSearchView.setMenuItem(seachItem);


      materialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }


}
