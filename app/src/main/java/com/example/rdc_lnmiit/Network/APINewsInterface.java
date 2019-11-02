package com.example.rdc_lnmiit.Network;

import com.example.rdc_lnmiit.Models.NewsFeedModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APINewsInterface {

    @GET("top-headlines?country=in")
    Call<NewsFeedModel> getGeneralNews(@Query("apiKey") String apiKey);

    @GET("top-headlines?country=in")
    Call<NewsFeedModel> getNews(@Query("category") String category, @Query("apiKey") String apiKey);

    @GET("everything")
    Call<NewsFeedModel> searchNews(@Query("q") String query, @Query("apiKey") String apiKey);

}
