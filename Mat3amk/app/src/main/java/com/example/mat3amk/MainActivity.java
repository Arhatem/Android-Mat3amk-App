package com.example.mat3amk;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;


public class MainActivity extends AppCompatActivity {

    private static final int LOC_REQ_CODE = 1;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    public static final String TAG = "CurrentLocNearByPlaces";
    private String address;
    private Button findButton;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle("Nearby Places");

        recyclerView = findViewById(R.id.places_list);

        LinearLayoutManager recyclerLayoutManager =
                new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerLayoutManager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(),
                        recyclerLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);*/
       findButton = findViewById(R.id.find_btn);

        final Spinner mySpinner = (Spinner) findViewById(R.id.spinner1);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.types));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                address =  (String)parent.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(address.equals("Select Your Area"))
                {
                    Toast.makeText(MainActivity.this, "Please select the Area first", Toast.LENGTH_SHORT).show();
                }
                else
                {
                   Intent intent = new Intent(MainActivity.this,RestaurantsActivity.class);
                   intent.putExtra("address",address);
                   startActivity(intent);



                }
            }
        });


    }








    private boolean isLocationAccessPermitted() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }


    private void requestLocationAccessPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOC_REQ_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == LOC_REQ_CODE)
        {
            if(resultCode == RESULT_OK)
            {

            }

        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.places_menu, menu);
        return true;
    }*/
}
