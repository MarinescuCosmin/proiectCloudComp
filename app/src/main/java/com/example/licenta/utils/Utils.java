package com.example.licenta.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.licenta.AboutActivity;
import com.example.licenta.CalendarActivity;
import com.example.licenta.ChartActivity;
import com.example.licenta.LoginActivity;
import com.example.licenta.MainActivity;
import com.example.licenta.ProfileActivity;
import com.example.licenta.R;
import com.example.licenta.StepsActivity;
import com.example.licenta.UserDetailsActivity;
import com.example.licenta.models.Exercise;
import com.example.licenta.models.ExercisesDocument;
import com.example.licenta.models.Food;
import com.example.licenta.models.FoodsDocument;
import com.example.licenta.models.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static void NavigationSwitchActivity(MenuItem menuItem, AppCompatActivity Activity, DrawerLayout drawerlayout) {
        switch (menuItem.getItemId()) {
            case R.id.home:
                if (Activity.getClass().equals(MainActivity.class)) {
                    break;
                } else {
                    Intent intent = new Intent(Activity.getApplicationContext(), MainActivity.class);
                    StartNewActivityNavigationDrawer(Activity, drawerlayout, intent);
                    break;
                }

            case R.id.calendar:
                if (Activity.getClass().equals(CalendarActivity.class)) {
                    break;
                } else {
                    Intent intent = new Intent(Activity.getApplicationContext(), CalendarActivity.class);
                    StartNewActivityNavigationDrawer(Activity, drawerlayout, intent);
                    break;
                }
            case R.id.steps:
                if (Activity.getClass().equals(StepsActivity.class)) {
                    break;
                } else {
                    Intent intent = new Intent(Activity.getApplicationContext(), StepsActivity.class);
                    StartNewActivityNavigationDrawer(Activity, drawerlayout, intent);
                    break;
                }

            case R.id.chart:
                if (Activity.getClass().equals(ChartActivity.class)) {
                    break;
                } else {
                    Intent intent = new Intent(Activity.getApplicationContext(), ChartActivity.class);
                    StartNewActivityNavigationDrawer(Activity, drawerlayout, intent);
                    break;
                }
            case R.id.profile:
                if (Activity.getClass().equals(ProfileActivity.class)) {
                    break;
                } else {
                    Intent intent = new Intent(Activity.getApplicationContext(), ProfileActivity.class);
                    StartNewActivityNavigationDrawer(Activity, drawerlayout, intent);
                    break;
                }
            case R.id.logout:
                    executeLogOut(Activity);
                break;

            case R.id.about:
                if (Activity.getClass().equals(AboutActivity.class)) {
                    break;
                } else {
                    Intent intent = new Intent(Activity.getApplicationContext(), AboutActivity.class);
                    StartNewActivityNavigationDrawer(Activity, drawerlayout, intent);
                    break;
                }


        }
    }

    public static void StartNewActivityNavigationDrawer(AppCompatActivity Activity, DrawerLayout drawerlayout, Intent intent) {
        Activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        drawerlayout.closeDrawer(GravityCompat.START);
        Activity.startActivity(intent);
        Activity.finish();
    }


    public static void setToolbarTitle(AppCompatActivity Activity, Toolbar toolbar) {
        if (Activity.getClass().equals(MainActivity.class)) {
            toolbar.setTitle("Home");
        }
        if (Activity.getClass().equals(ProfileActivity.class)) {
            toolbar.setTitle("Profile");
        }
        if (Activity.getClass().equals(CalendarActivity.class)) {
            toolbar.setTitle("Calendar");
        }
        if (Activity.getClass().equals(StepsActivity.class)) {
            toolbar.setTitle("Steps");
        }
        if (Activity.getClass().equals(ChartActivity.class)) {
            toolbar.setTitle("Chart");
        }
        if (Activity.getClass().equals(AboutActivity.class)) {
            toolbar.setTitle("About");
        }
    }

    public static void CreateNavigationDrawer(AppCompatActivity Activity) {

        NavigationView navigationView;
        DrawerLayout drawerlayout;
        Toolbar toolbar;
        drawerlayout = (DrawerLayout) Activity.findViewById(R.id.drawer_layout);
        toolbar = Activity.findViewById(R.id.toolbar);
        navigationView = (NavigationView) Activity.findViewById(R.id.navigationView);

        setToolbarTitle(Activity, toolbar);

        Activity.setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(Activity, drawerlayout, toolbar, R.string.open, R.string.close);
        drawerlayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                NavigationSwitchActivity(menuItem, Activity, drawerlayout);

                return true;
            }
        });

    }

    public static void getCurrentUserFoods() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore dbfirestore = FirebaseFirestore.getInstance();
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        String formattedDate = df.format(date);
        dbfirestore.collection("users").document(uid).collection("MancaruriSiExercitii").document(formattedDate).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    ArrayList<Food> foods = documentSnapshot.toObject(FoodsDocument.class).mancaruri;
                    if (foods != null) {
                        if (MainActivity.AllFoods == null) {
                            MainActivity.AllFoods = new ArrayList<>();
                        }
                        MainActivity.AllFoods=foods;
                        MainActivity.AllFoods.sort(Comparator.comparing(Food::getAliment));
                    }
                }
            }
        });
    }

    public static void getCurrentUserExercises() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore dbfirestore = FirebaseFirestore.getInstance();
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        String formattedDate = df.format(date);
        dbfirestore.collection("users").document(uid).collection("MancaruriSiExercitii").document(formattedDate).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot != null && documentSnapshot.exists()) {

                    ArrayList<Exercise> exercises = documentSnapshot.toObject(ExercisesDocument.class).exercitii;

                    if (exercises != null) {
                        if (MainActivity.AllExercises == null) {
                            MainActivity.AllExercises = new ArrayList<>();
                        }
                        MainActivity.AllExercises = exercises;
                        MainActivity.AllExercises.sort(Comparator.comparing(Exercise::getActivitate));

                    }
                }

            }

        });
    }

    public static void getCurrentUserDetails(AppCompatActivity CurrentActivity) {
        FirebaseFirestore dbfirestore = FirebaseFirestore.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference docRef = dbfirestore.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        MainActivity.currentUserDetails = document.toObject(UserDetails.class);
                    }


                    if (MainActivity.currentUserDetails == null) {
                        Intent intent = new Intent(CurrentActivity.getApplicationContext(), UserDetailsActivity.class);
                        CurrentActivity.startActivity(intent);
                    } else {
                        Intent intent = new Intent(CurrentActivity.getApplicationContext(), MainActivity.class);
                        CurrentActivity.startActivity(intent);
                    }
                    CurrentActivity.finish();
                } else {
                    Log.d("LoadingScreen", "eroare la firestore");
                }
            }
        });
    }

    public static void scheduleVerseNotificationService(Context context) {
        Log.d("Alarma", "a pornit");
        // Set the alarm to start at approximately 2:00 p.m.
        AlarmManager alarmMgr;
        PendingIntent alarmIntent;
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AutoStartUp.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 35);
        calendar.set(Calendar.SECOND, 0);


        if (Calendar.getInstance().getTimeInMillis() > calendar.getTimeInMillis()) {
            calendar.add(Calendar.DATE, 1);
        }

// With setInexactRepeating(), you have to use one of the AlarmManager interval
// constants--in this case, AlarmManager.INTERVAL_DAY.
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 4000, alarmIntent);
    }

    public static void doWork() {

        FirebaseFirestore dbfirestore = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        UserDetails userDetails = new UserDetails();

        dbfirestore.collection("users").document(userId).set(MainActivity.currentUserDetails, SetOptions.merge());
    }


    public static void executeLogOut(AppCompatActivity Activity){
        FirebaseAuth.getInstance().signOut();
        MainActivity.currentUserDetails = null;
        MainActivity.AllFoods.clear();
        MainActivity.AllExercises.clear();
        Intent logoutIntent = new Intent(Activity.getApplicationContext(), LoginActivity.class);
        Activity.startActivity(logoutIntent);
        Activity.finish();
    }
}
