package com.example.licenta;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.licenta.models.UserDetails;
import com.example.licenta.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import io.grpc.okhttp.internal.Util;

public class LoadingScreen extends AppCompatActivity {
    FirebaseUser user;

    @Override
    protected void onStart() {
        super.onStart();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            Utils.getCurrentUserFoods();
            Utils.getCurrentUserExercises();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Utils.getCurrentUserDetails(LoadingScreen.this);
                }
            }, 2000);



        }
        else{
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
//            FirebaseFirestore dbfirestore = FirebaseFirestore.getInstance();
//            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//            DocumentReference docRef = dbfirestore.collection("users").document(uid);
//            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot document = task.getResult();
//                        if (document.exists()) {
//                            MainActivity.currentUserDetails = document.toObject(UserDetails.class);
//                            Intent intent = new Intent(LoadingScreen.this,LoginActivity.class);
//                            startActivity(intent);
//                            finish();
//                        }
//                    }
//                }
//            });