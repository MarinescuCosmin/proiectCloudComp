package com.example.licenta.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.licenta.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddStepToAlarm extends BroadcastReceiver {
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
                        Log.d("Alarma","firestore collect pentru pasi realizat");

                        Date date = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
                        String formattedDate = df.format(date);
                        Map<String, Float> map = new HashMap<>();
                        String dataHardocdata= "18-Jun-2020";
                        map.put("steps", MainActivity.dailySteps);
                        dbfirestore.collection("users").document(userId).collection("MancaruriSiExercitii").document(formattedDate).set(map, SetOptions.merge());
                        Log.d("Alarma","firestore update pasi realizat");
                    }
                }
            }
        });
    }
}
