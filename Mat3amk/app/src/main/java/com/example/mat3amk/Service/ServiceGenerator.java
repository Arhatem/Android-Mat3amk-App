package com.example.mat3amk.Service;

import com.example.mat3amk.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static final String BASE_URL = "https://api.tomtom.com";


    // create OkHttp client
    private static  OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();

    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    // create Retrofit instance
    private static  Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    public static Retrofit retrofit = builder.build();



    public static <S> S createService(Class<S> serviceClass)
    {
        if(!okBuilder.interceptors().contains(logging)) {
            if (BuildConfig.DEBUG) {
                okBuilder.addInterceptor(logging);
            }
            builder = builder.client(okBuilder.build());

            retrofit = builder.build();

        }

        return  retrofit.create(serviceClass);
    }
}
