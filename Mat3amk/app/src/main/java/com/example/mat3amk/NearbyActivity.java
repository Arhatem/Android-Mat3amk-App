package com.example.mat3amk;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mat3amk.Model.Body;
import com.example.mat3amk.NetworkUtils.NetworkUtils;
import com.example.mat3amk.Service.ApiClient;
import com.example.mat3amk.Service.ServiceGenerator;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NearbyActivity extends AppCompatActivity implements LocationListener{

    private Toolbar mToolbar;
    private static final String API_KEY = "OeLaWMAtxdtCGGRGVEQtenrgRlauUNwA";
    LocationManager locationManager;



    MaterialSearchView materialSearchView;

    private RecyclerView recyclerView;
    private NearbyAdapter nearbyAdapter;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView internetImage;

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
        setContentView(R.layout.activity_nearby);
        mToolbar = (Toolbar) findViewById(R.id.main_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Nearby Restaurants");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.nearby_list);
        LinearLayoutManager recyclerLayoutManager =
                new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerLayoutManager);
        nearbyAdapter = new NearbyAdapter(this,new ArrayList<Body.Result>());
        recyclerView.setAdapter(nearbyAdapter);
        progressBar = findViewById(R.id.pb_near);
        swipeRefreshLayout = findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary
                , android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        internetImage = findViewById(R.id.internet_image);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkUtils.isConnectedToInternet(getApplicationContext())&&nearbyAdapter.getItemCount()==0) {
                    internetImage.setVisibility(View.GONE);

                    CheckPermission();


                }
                else if(nearbyAdapter.getItemCount()!=0)
                {
                    swipeRefreshLayout.setRefreshing(false);
                }
                else {
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
                    CheckPermission();


                } else {
                    internetImage.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    return;
                }

            }
        });

        materialSearchView = findViewById(R.id.searchView);
        materialSearchView.closeSearch();
    }


    public void CheckPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        } else
            getLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                progressBar.setVisibility(View.VISIBLE);
                getLocation();

            }

        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(NearbyActivity.this, "Please Enable permissions to get Nearby Restaurants", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    /* Request updates at startup */
    @Override
    public void onResume() {
        super.onResume();
        getLocation();
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    public void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        final double lat = location.getLatitude();
        double lon = location.getLongitude();

        ApiClient apiClient = ServiceGenerator.createService(ApiClient.class);
        Call<Body> call = apiClient.categorySearch(API_KEY, (float) lat, (float) lon, 20, 500);
        call.enqueue(new Callback<Body>() {
            @Override
            public void onResponse(Call<Body> call, Response<Body> response) {
                if (response.isSuccessful()) {
                    List<Body.Result> list = new ArrayList<>();
                    for (int i = 0; i < response.body().getResults().length; i++) {
                        list.add(response.body().getResults()[i]);
                    }
                    nearbyAdapter.setData(list);
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);

                }
            }

            @Override
            public void onFailure(Call<Body> call, Throwable t) {

            }
        });
    }


    @Override
    public void onProviderDisabled(String provider) {
        //if (nearbyAdapter.getData() == null&&nearbyAdapter.getItemCount()==0)
            Toast.makeText(NearbyActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        if(nearbyAdapter.getItemCount()==0)
        progressBar.setVisibility(View.VISIBLE);


    }
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
                nearbyAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }
}
