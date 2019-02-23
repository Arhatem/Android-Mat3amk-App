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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mat3amk.Model.Body;
import com.example.mat3amk.Service.ApiClient;
import com.example.mat3amk.Service.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NearbyActivity extends AppCompatActivity implements LocationListener {

    private Toolbar mToolbar;
    private static final String API_KEY = "OeLaWMAtxdtCGGRGVEQtenrgRlauUNwA";
    LocationManager locationManager;
    private RecyclerView recyclerView;
    private NearbyAdapter nearbyAdapter;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);
        mToolbar = (Toolbar)findViewById(R.id.main_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Nearby Restaurants");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.nearby_list);
        LinearLayoutManager recyclerLayoutManager =
                new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerLayoutManager);
        nearbyAdapter = new NearbyAdapter(this);
        recyclerView.setAdapter(nearbyAdapter);
        progressBar = findViewById(R.id.pb_near);

CheckPermission();

    }



    public void CheckPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
        else
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

        }
        else
        {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(NearbyActivity.this, "Please Enable permissions to get Nearby Restaurants", Toast.LENGTH_SHORT).show();
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
        Call<Body> call = apiClient.categorySearch(API_KEY,(float)lat,(float)lon,20,500);
        call.enqueue(new Callback<Body>() {
            @Override
            public void onResponse(Call<Body> call, Response<Body> response) {
                if(response.isSuccessful())
                {
                    List<Body.Result> list = new ArrayList<>();
                    for(int i=0;i<response.body().getResults().length;i++)
                    {
                        list.add(response.body().getResults()[i]);
                    }
                    nearbyAdapter.setData(list);
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<Body> call, Throwable t) {

            }
        });
    }


    @Override
    public void onProviderDisabled(String provider) {
        if(nearbyAdapter.getData()==null)
        Toast.makeText(NearbyActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        progressBar.setVisibility(View.VISIBLE);

    }

}
