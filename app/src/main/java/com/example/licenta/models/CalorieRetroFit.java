package com.example.licenta.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CalorieRetroFit {
    @SerializedName("items")
    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return  items.get(0).toString();
    }
}

class Item{
    @SerializedName("name")
    private String name;
    @SerializedName("calories")
    private double calories;
    @SerializedName("protein_g")
    private double proteins_g;
    @SerializedName("fat_total_g")
    private double fat_total_g;
    @SerializedName("carbohydrates_total_g")
    private double carbohydrates_total_g;

    public double getProteins_g() {
        return proteins_g;
    }

    public void setProteins_g(double proteins_g) {
        this.proteins_g = proteins_g;
    }

    public double getFat_total_g() {
        return fat_total_g;
    }

    public void setFat_total_g(double fat_total_g) {
        this.fat_total_g = fat_total_g;
    }

    public double getCarbohydrates_total_g() {
        return carbohydrates_total_g;
    }

    public void setCarbohydrates_total_g(double carbohydrates_total_g) {
        this.carbohydrates_total_g = carbohydrates_total_g;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    @Override
    public String toString() {
        return
                "Name " + name  +
                "\n" + " Calories " + calories +
                "\n" + " Proteins " + proteins_g +
                "\n" + " Fat " + fat_total_g +
                "\n" + "Carbohydrates " + carbohydrates_total_g ;
    }
}
