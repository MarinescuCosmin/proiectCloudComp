package com.example.licenta.utils;


import com.example.licenta.MainActivity;
import com.example.licenta.models.Exercise;
import com.example.licenta.models.Food;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class Calculations {

    public static double CaloriiArseUser(double weight, double MET) {
        double CaloriiArseIn60Min;
        CaloriiArseIn60Min = weight * MET;
        return CaloriiArseIn60Min;
    }

    public static void ScadeCalorii(double calorii_mancare) {
        FirebaseFirestore dbfirestore = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        MainActivity.currentUserDetails.setBurn_or_eat_calories(MainActivity.currentUserDetails.getBurn_or_eat_calories() - calorii_mancare);
        dbfirestore.collection("users").document(userId).set(MainActivity.currentUserDetails, SetOptions.merge());
    }

    public static void AdaugaCalorii(double calorii_exercitiu) {
        FirebaseFirestore dbfirestore = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        MainActivity.currentUserDetails.setBurn_or_eat_calories(MainActivity.currentUserDetails.getBurn_or_eat_calories() + calorii_exercitiu);
        dbfirestore.collection("users").document(userId).set(MainActivity.currentUserDetails, SetOptions.merge());
    }

    public static void SetNewBurnOrEatCalories() {
        double burn_or_eat_calories = MainActivity.recommendedCalories;
        FirebaseFirestore dbfirestore = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        for (Food f : MainActivity.AllFoods) {
            burn_or_eat_calories -= f.getCalorii();
        }
        for (Exercise e : MainActivity.AllExercises) {
            burn_or_eat_calories += e.getCaloriiArse();
        }
        MainActivity.currentUserDetails.setBurn_or_eat_calories(burn_or_eat_calories);


        //MainActivity.currentUserDetails.setBurn_or_eat_calories(MainActivity.currentUserDetails.getBurn_or_eat_calories()-calorii_mancare);
        dbfirestore.collection("users").document(userId).set(MainActivity.currentUserDetails, SetOptions.merge());

    }
}
