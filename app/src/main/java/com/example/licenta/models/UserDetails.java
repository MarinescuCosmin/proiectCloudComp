package com.example.licenta.models;

public class UserDetails {
    double height;
    double weight;
    double age;
    double loss_or_gain_weight;
    String loss_or_gain;
    String lifestyle_type;
    String gender;
    double recommended_calories;
    double burn_or_eat_calories;
    String start_date;


    public UserDetails() {
    }

    public UserDetails(double height, double weight, double age, double loss_or_gain_weight, String loss_or_gain, String lifestyle_type, String gender, double recommended_calories, double burn_or_eat_calories, String start_date) {
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.loss_or_gain_weight = loss_or_gain_weight;
        this.loss_or_gain = loss_or_gain;
        this.lifestyle_type = lifestyle_type;
        this.gender = gender;
        this.recommended_calories = recommended_calories;
        this.burn_or_eat_calories = burn_or_eat_calories;
        this.start_date = start_date;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public double getBurn_or_eat_calories() {
        return burn_or_eat_calories;
    }

    public void setBurn_or_eat_calories(double burn_or_eat_calories) {
        this.burn_or_eat_calories = burn_or_eat_calories;
    }

    public String getLoss_or_gain() {
        return loss_or_gain;
    }

    public void setLoss_or_gain(String loss_or_gain) {
        this.loss_or_gain = loss_or_gain;
    }

    public double getLoss_or_gain_weight() {
        return loss_or_gain_weight;
    }

    public void setLoss_or_gain_weight(double loss_or_gain_weight) {
        this.loss_or_gain_weight = loss_or_gain_weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getAge() {
        return age;
    }

    public void setAge(double age) {
        this.age = age;
    }


    public String getLifestyle_type() {
        return lifestyle_type;
    }

    public void setLifestyle_type(String lifestyle_type) {
        this.lifestyle_type = lifestyle_type;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getRecommended_calories() {
        return recommended_calories;
    }

    public void setRecommended_calories(double recommended_calories) {
        this.recommended_calories = recommended_calories;
    }
}
