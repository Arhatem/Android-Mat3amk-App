package com.example.mat3amk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CategoryFragment extends Fragment implements CategoryAdapter.DetailsOnClickHandler {



    private List<Food> eats;
    private static final String TAG = CategoryFragment.class.getSimpleName();

    public CategoryFragment(){}

    @SuppressLint("ValidFragment")
    public CategoryFragment(List<Food> eats)
    {
        this.eats = eats;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_fragment,container,false);
        //Init  Facebook
        RecyclerView recyclerView = view.findViewById(R.id.fragment_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

      /*  DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(),
                        linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);*/

        recyclerView.setHasFixedSize(true);

        CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(),this);

        categoryAdapter.setEat(eats);
        recyclerView.setAdapter(categoryAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);


        return view;

    }

    @Override
    public void onClick(String key , String address , String res, String category) {
        Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra("key",key);
        intent.putExtra("address",address);
        intent.putExtra("res",res);
        intent.putExtra("cat",category);
        startActivity(intent);

    }


}
