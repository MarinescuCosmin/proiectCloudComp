package com.example.licenta.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.licenta.MainActivity;
import com.example.licenta.models.Exercise;
import com.example.licenta.models.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.auth.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AutoStartUp extends BroadcastReceiver {

    public UserDetails userDetails= new UserDetails();

    @Override
    public void onReceive(Context context, Intent intent) {

        FirebaseFirestore dbfirestore = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference docRef = dbfirestore.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Alarma","firestore collect realizat");
                        userDetails = document.toObject(UserDetails.class);
                        userDetails.setBurn_or_eat_calories(userDetails.getRecommended_calories());
                        dbfirestore.collection("users").document(userId).set(userDetails, SetOptions.merge());
                        Date date = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
                        String formattedDate = df.format(date);
                        Map<String, Double> map = new HashMap<>();
                        String dataHardocdata= "18-Jun-2020";
                        Map<String, Float> mapPasi = new HashMap<>();

                        mapPasi.put("steps", MainActivity.dailySteps);
                        dbfirestore.collection("users").document(userId).collection("MancaruriSiExercitii").document(formattedDate).set(mapPasi, SetOptions.merge());
                        map.put("weight",userDetails.getWeight());
                        dbfirestore.collection("users").document(userId).collection("MancaruriSiExercitii").document(formattedDate).set(map, SetOptions.merge());
                        MainActivity.currentUserDetails=userDetails;
                        MainActivity.AllFoods.clear();
                        MainActivity.AllExercises.clear();
                        Log.d("Alarma","firestore update realizat");
                    }
                }
            }
        });

    }
}