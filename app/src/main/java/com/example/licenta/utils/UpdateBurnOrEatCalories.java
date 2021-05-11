package com.example.licenta.utils;

import com.example.licenta.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.TimerTask;

public class UpdateBurnOrEatCalories  {
    FirebaseFirestore dbfirestore;

    public void doWork() {
        dbfirestore = FirebaseFirestore.getInstance();
        String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        MainActivity.currentUserDetails.setBurn_or_eat_calories(MainActivity.currentUserDetails.getRecommended_calories());
        dbfirestore.collection("users").document(userId).set(MainActivity.currentUserDetails, SetOptions.merge());
    }
}
