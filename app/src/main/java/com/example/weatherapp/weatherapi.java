package com.example.weatherapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface weatherapi {
    @GET("weather")
    Call<Root> getweather(@Query("q") String cityname,
                             @Query("aapid") String apikey);
}
