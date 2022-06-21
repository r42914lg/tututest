package com.r42914lg.tutu.service;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RestClient {

    public static final String API_URL = "https://jservice.io/api/";
    private static final RestClient instance = new RestClient();

    public static RestClient getInstance() { return instance; }

    public static Gson gson() {
        return new Gson();
    }

    private API_JService service;

    public RestClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .client(logLevel())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(API_JService.class);
    }

    public void setApi(API_JService apiJService) {
        this.service = apiJService;
    }

    public API_JService getApi() {
        return service;
    }

    private static OkHttpClient logLevel() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        return client;
    }
}
