package com.example.licenta.utils;

import com.example.licenta.models.CalorieRetroFit;
import com.example.licenta.models.WeatherRetroFit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetDataService {
    @GET("/data/2.5/weather")
    Call<WeatherRetroFit> getWeather(@Query("q") String city, @Query("appid") String apiKey);
    @Headers({"X-RapidAPI-Key:e37a1be58emshf41be34c3fe0092p1119cejsn2d246d03f3fe", "X-RapidAPI-Host:calorieninjas.p.rapidapi.com"})
    @GET("/v1/nutrition")
    Call<CalorieRetroFit> getCalories(@Query("query") String query);
}
