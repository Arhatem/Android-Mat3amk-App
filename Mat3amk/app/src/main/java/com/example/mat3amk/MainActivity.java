package com.example.mat3amk;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final int LOC_REQ_CODE = 1;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    public static final String TAG = "CurrentLocNearByPlaces";
    private String address;
    private Button findButton;
    private DatabaseReference mDatabase;
    private Toolbar mToolbar;
    private TextView userTextView;
    private FirebaseAuth mAuth;
    MenuItem login;
    MenuItem logout;
    MenuItem signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mAuth = FirebaseAuth.getInstance();
        View headerView = navigationView.getHeaderView(0);
        userTextView =headerView.findViewById(R.id.user_tv);
        FirebaseUser user = mAuth.getCurrentUser();
        Menu menu = navigationView.getMenu();
         login = menu.findItem(R.id.nav_in);
         logout = menu.findItem(R.id.nav_out);
         signup = menu.findItem(R.id.nav_up);
        if(user!=null)
        {


            login.setVisible(false);
            logout.setVisible(true);
            signup.setVisible(false);
            userTextView.setText(user.getDisplayName());

        }
        else
        {
            login.setVisible(true);
            logout.setVisible(false);
            signup.setVisible(true);

        }



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





    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cart) {
            Intent intent = new Intent(this,CartActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_home) {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);

        }  else if (id == R.id.nav_chat) {
            FirebaseUser user = mAuth.getCurrentUser();
            if(user!=null) {
                Intent intent = new Intent(this, ChatActivity.class);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(this, "You must login to enter chat room", Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(this,AboutActivity.class);
            startActivity(intent);
        }

        else if(id== R.id.nav_up){
            Intent intent = new Intent(this,SignupActivity.class);
            startActivity(intent);
        }
        else if(id== R.id.nav_in){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.nav_out)
        {

            mAuth.signOut();
            login.setVisible(true);
            logout.setVisible(false);
            signup.setVisible(true);


            userTextView.setText("Sign in to chat and order");

        }
        else if(id == R.id.nav_nearby)
        {
            Intent intent = new Intent(this,NearbyActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_search) {
            Intent intent = new Intent(this,SearchActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}
