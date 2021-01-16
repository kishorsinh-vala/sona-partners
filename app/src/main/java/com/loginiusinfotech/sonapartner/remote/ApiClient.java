package com.loginiusinfotech.sonapartner.remote;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loginiusinfotech.sonapartner.utils.AppConstants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit getRetrofitClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(30000, TimeUnit.SECONDS).connectTimeout(30000, TimeUnit.SECONDS).addInterceptor(interceptor).build();
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit;
    }

    public static <S> S getClient(Class<S> serviceClass) {
        return getRetrofitClient().create(serviceClass);
    }
}