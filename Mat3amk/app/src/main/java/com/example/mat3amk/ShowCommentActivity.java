package com.example.mat3amk;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mat3amk.Model.Rating;
import com.example.mat3amk.ViewHolder.CommentViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ShowCommentActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference mDatabase;
    SwipeRefreshLayout swipeRefreshLayout;
    FirebaseRecyclerAdapter<Rating, CommentViewHolder> mAdapter;
    String foodName = "";
    private TextView textView;



    @Override
    protected void onStop() {
        super.onStop();
        if(mAdapter!=null)
            mAdapter.stopListening();
    }

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
        setContentView(R.layout.activity_show_comment);

        textView = findViewById(R.id.empty_tv);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Rating");
        recyclerView = findViewById(R.id.commentList);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        swipeRefreshLayout = findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(getIntent()!=null)
                {
                    foodName = getIntent().getStringExtra("food-name");
                }
                if(!TextUtils.isEmpty(foodName))
                {
                    // create request Query
                    Query query = mDatabase.orderByChild("foodName").equalTo(foodName);

                    FirebaseRecyclerOptions<Rating> options = new FirebaseRecyclerOptions.Builder<Rating>()
                            .setQuery(query,Rating.class)
                            .build();

                    mAdapter = new FirebaseRecyclerAdapter<Rating, CommentViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull CommentViewHolder holder, int position, @NonNull Rating model) {

                            holder.ratingBar.setRating(Float.parseFloat(model.getRateValue()));
                            holder.userTextView.setText(model.getUserName());
                            holder.commentTextView.setText(model.getComment());
                        }

                        @NonNull
                        @Override
                        public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.comment_layout,viewGroup,false);
                            return new CommentViewHolder(view);
                        }
                    };
                    loadComment(foodName);
                }


            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                if(getIntent()!=null)
                {
                    foodName = getIntent().getStringExtra("food-name");
                }
                if(!TextUtils.isEmpty(foodName))
                {
                    // create request Query
                    Query query = mDatabase.orderByChild("foodName").equalTo(foodName);

                    final FirebaseRecyclerOptions<Rating> options = new FirebaseRecyclerOptions.Builder<Rating>()
                            .setQuery(query,Rating.class)
                            .build();

                    mAdapter = new FirebaseRecyclerAdapter<Rating, CommentViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull CommentViewHolder holder, int position, @NonNull Rating model) {

                            holder.ratingBar.setRating(Float.parseFloat(model.getRateValue()));
                            holder.userTextView.setText(model.getUserName());
                            holder.commentTextView.setText(model.getComment());
                        }

                        @NonNull
                        @Override
                        public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.comment_layout,viewGroup,false);
                            return new CommentViewHolder(view);
                        }

                    };
                    loadComment(foodName);
                }

            }
        });
    }

    private void loadComment(String foodName) {
        mAdapter.startListening();
        recyclerView.setAdapter(mAdapter);
        swipeRefreshLayout.setRefreshing(false);


        mDatabase.orderByChild("foodName").equalTo(foodName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                if(count!=0)
                {
                    recyclerView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);

                }
                else
                {
                    recyclerView.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                    Log.v("else","called");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
