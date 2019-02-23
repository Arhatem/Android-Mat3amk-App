package com.example.mat3amk.Service;

import com.example.mat3amk.Model.Body;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiClient {

    @GET("/search/2/categorySearch/restaurant.json")
    Call<Body> categorySearch(@Query("key") String key,
                              @Query("lat") float lat,
                              @Query("lon") float lon,
                              @Query("limit") int limit,
                              @Query("radius") int radius);
}
