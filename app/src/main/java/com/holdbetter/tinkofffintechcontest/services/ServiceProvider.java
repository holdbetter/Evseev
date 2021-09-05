package com.holdbetter.tinkofffintechcontest.services;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceProvider {
    private static final String BASE_URL = "https://developerslife.ru";
    private static Retrofit retrofit;
    private static DevelopersLiveApi api;

    private static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static DevelopersLiveApi getApi() {
        if (api == null) {
            api = getRetrofit().create(DevelopersLiveApi.class);
        }
        return api;
    }
}
