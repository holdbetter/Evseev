package com.holdbetter.tinkofffintechcontest.services;

import com.holdbetter.tinkofffintechcontest.model.Meme;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Response;
import retrofit2.http.GET;

public interface DevelopersLiveApi {
    @GET("/random?json=true")
    Single<Response<Meme>> getMeme();
}
