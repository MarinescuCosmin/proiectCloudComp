package com.example.licenta.models;

import com.google.gson.annotations.SerializedName;

public class WeatherRetroFit{
    @SerializedName("main")
    Weather main;

    public Weather getMain() {
        return main;
    }

    public void setMain(Weather main) {
        this.main = main;
    }

    @Override
    public String toString() {
        return  main.toString();
    }
}

 class Weather {
    @SerializedName("temp")
    private double temp;

    @SerializedName("feels_like")
    private double feels_like;

    @SerializedName("temp_min")
    private double temp_min;

    @SerializedName("temp_max")
    private double temp_max;

    public Weather(double temp, double feels_like, double temp_min, double temp_max) {
        this.temp = temp;
        this.feels_like = feels_like;
        this.temp_min = temp_min;
        this.temp_max = temp_max;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getFeels_like() {
        return feels_like;
    }

    public void setFeels_like(double feels_like) {
        this.feels_like = feels_like;
    }

    public double getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(double temp_min) {
        this.temp_min = temp_min;
    }

    public double getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(double temp_max) {
        this.temp_max = temp_max;
    }

    @Override
    public String toString() {
        return
                "Temp " + (int)(temp-273.15) +
                "\n" + " Feels like " + (int)(feels_like-273.15) +
                "\n" + " Min " + (int)(temp_min-273.15) +
                "\n" + " Max " + (int)(temp_max-273.15)
                ;
    }
}
