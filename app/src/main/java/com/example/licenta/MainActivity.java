package com.example.licenta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.licenta.adapters.ExercisesAdapter;
import com.example.licenta.adapters.FoodsAdapter;
import com.example.licenta.models.Exercise;
import com.example.licenta.models.Food;
import com.example.licenta.models.UserDetails;
import com.example.licenta.utils.AddStepToAlarm;

import com.example.licenta.utils.AutoStartUp;
import com.example.licenta.utils.Calculations;
import com.example.licenta.utils.Utils;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {
    public static boolean InfoDatabaseGot = false;
    public static boolean InfoDatabaseGotExercise = false;
    Button buttonAddFood, buttonAddExercise;
    TextView textViewEatOrBurn, textViewCalories, textViewTipCalories;
    ListView listViewFoods, listViewExercises;
    DrawerLayout drawerlayout;
    NavigationView navigationView;
    Toolbar toolbar;
    LinearLayout mainLayout;
    AnimationDrawable animationDrawablelayout;
    AlarmManager alarmMgr;
    AlarmManager alarmMgrPasi;
    PendingIntent alarmIntent;
    CardView cardViewProfile, cardViewAbout, cardViewLogOut, cardViewCalendar, cardViewSteps, cardViewChart;

    public static ArrayAdapter foodsAdapter, exercisesAdapter;
    public static ArrayList<Food> FoodList;
    public static ArrayList<Food> AllFoods;
    public static ArrayList<Exercise> AllExercises;
    public static UserDetails currentUserDetails;
    public static double recommendedCalories;
    public static Float dailySteps=0f;
    FirebaseFirestore dbfirestore;
    String userId;


    @Override
    protected void onResume() {
        super.onResume();
        foodsAdapter.notifyDataSetChanged();
        exercisesAdapter.notifyDataSetChanged();
        if (currentUserDetails.getBurn_or_eat_calories() >= 0) {
            textViewEatOrBurn.setText("You have to EAT");
            textViewCalories.setTextColor(getColor(R.color.white));
            textViewCalories.setText(String.format("%.0f", currentUserDetails.getBurn_or_eat_calories()) + " Calories");
        } else {
            textViewEatOrBurn.setText("You have to BURN");
            textViewCalories.setTextColor(getColor(R.color.red));
            textViewCalories.setText(String.format("%.0f", currentUserDetails.getBurn_or_eat_calories() * (-1)) + " Calories");
        }
        scheduleResetCalories(MainActivity.this);

        // scheduleAddSteps(MainActivity.this);


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FoodList = new ArrayList<>();
        if (AllExercises == null) {
            AllExercises = new ArrayList<>();
        }
        if (AllFoods == null) {
            AllFoods = new ArrayList<>();
        }

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = dateFormatter.parse("2020-05-08 05:30:40");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Now create the time and schedule it
        Timer timer = new Timer();

        //Use this if you want to execute it once
//        OncePerDayAppWorkExecutor probaTask = new OncePerDayAppWorkExecutor("Cosmin",6,17,30);
//        probaTask.start();

//        Intent intentService= new Intent(this,SendDataService.class);
//        startService(intentService);
        //stopService(intentService);


        mainLayout = (LinearLayout) findViewById(R.id.mainLinearLayout);
        buttonAddExercise = findViewById(R.id.buttonAddExercise);
        buttonAddFood = findViewById(R.id.buttonAddFood);
        textViewEatOrBurn = (TextView) findViewById(R.id.textViewEatOrBurn);
        textViewCalories = (TextView) findViewById(R.id.textViewCalories);
        textViewTipCalories = (TextView) findViewById(R.id.textViewTipCalories);
        listViewExercises = (ListView) findViewById(R.id.listview_exercises);
        listViewFoods = (ListView) findViewById(R.id.listview_food);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbfirestore = FirebaseFirestore.getInstance();
        //incarcaGreutateInFirestore();

        cardViewChart = findViewById(R.id.cardViewChart);
        cardViewAbout = findViewById(R.id.cardViewAbout);
        cardViewCalendar = findViewById(R.id.cardViewCalendar);
        cardViewLogOut = findViewById(R.id.cardViewLogOut);
        cardViewProfile = findViewById(R.id.cardViewProfile);
        cardViewSteps = findViewById(R.id.cardViewSteps);


        Utils.CreateNavigationDrawer(MainActivity.this);


        foodsAdapter = new FoodsAdapter(MainActivity.this, R.layout.food_item, AllFoods);
        listViewFoods.setAdapter(foodsAdapter);
        exercisesAdapter = new ExercisesAdapter(MainActivity.this, R.layout.exercise_item, AllExercises);
        listViewExercises.setAdapter(exercisesAdapter);

        // scheduleResetCalories(MainActivity.this);
        Calculations.AdaugaCalorii(MainActivity.dailySteps / 1000 * currentUserDetails.getWeight() * 0.6);
        textViewEatOrBurn.setText("You have to EAT");
        textViewCalories.setText(String.format("%.0f", currentUserDetails.getBurn_or_eat_calories()) + " Calories");
        textViewTipCalories.setText("Calories recommended between " + String.format("%.0f", currentUserDetails.getRecommended_calories() - 100) + " and " + String.format("%.0f", currentUserDetails.getRecommended_calories() + 100));


//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                Utils.NavigationSwitchActivity(menuItem, MainActivity.this, drawerlayout);
//
//                return true;
//
//
////                switch (menuItem.getItemId()) {
////                    case R.id.home:
////                        break;
////                    case R.id.calendar:
////                        Intent calendarIntent = new Intent(MainActivity.this, CalendarActivity.class);
////                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
////                        drawerlayout.closeDrawer(GravityCompat.START);
////                        startActivity(calendarIntent);
////                        finish();
////
////
////                        break;
////                    case R.id.steps:
////                        Intent stepsIntent = new Intent(MainActivity.this, StepsActivity.class);
////                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
////                        drawerlayout.closeDrawer(GravityCompat.START);
////                        startActivity(stepsIntent);
////                        finish();
////                        break;
////                    case R.id.sleep_hours:
////                        Intent sleep_hoursIntent = new Intent(MainActivity.this, ChartActivity.class);
////                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
////                        drawerlayout.closeDrawer(GravityCompat.START);
////                        startActivity(sleep_hoursIntent);
////                        finish();
////                        break;
////                    case R.id.profile:
////                        Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
////                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
////                        drawerlayout.closeDrawer(GravityCompat.START);
////                        startActivity(profileIntent);
////                        finish();
////                        break;
////                    case R.id.logout:
////                        FirebaseAuth.getInstance().signOut();
////                        MainActivity.currentUserDetails=null;
////                        Intent logoutIntent = new Intent(MainActivity.this, LoginActivity.class);
////                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
////                        drawerlayout.closeDrawer(GravityCompat.START);
////                        startActivity(logoutIntent);
////                        finish();
////
////                        break;
////                    case R.id.about:
////                        Intent aboutIntent = new Intent(MainActivity.this, AboutActivity.class);
////                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
////                        drawerlayout.closeDrawer(GravityCompat.START);
////                        startActivity(aboutIntent);
////                        finish();
////                        break;
////
////
////                }
//
//            }
//        });


        buttonAddFood.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddFoodActivity.class);
            startActivity(intent);
        });

        buttonAddExercise.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddExerciseActivity.class);
            startActivity(intent);
        });

        cardViewSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StepsActivity.class);
                startActivity(intent);
            }
        });
        cardViewCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        cardViewAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });
        cardViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        cardViewLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.executeLogOut(MainActivity.this);
            }
        });
        cardViewChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChartActivity.class);
                startActivity(intent);
            }
        });

        listViewExercises.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Calculations.ScadeCalorii(MainActivity.AllExercises.get(position).getCaloriiArse());
                MainActivity.AllExercises.remove(position);
                Date date = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
                String formattedDate = df.format(date);
                Map<String, ArrayList<Exercise>> map = new HashMap<>();
                String dataHardocdata = "18-Jun-2020";
                map.put("exercitii", MainActivity.AllExercises);
                dbfirestore.collection("users").document(userId).collection("MancaruriSiExercitii").document(formattedDate).set(map, SetOptions.merge());
                exercisesAdapter.notifyDataSetChanged();
                if (currentUserDetails.getBurn_or_eat_calories() >= 0) {
                    textViewEatOrBurn.setText("You have to EAT");
                    textViewCalories.setTextColor(getColor(R.color.white));
                    textViewCalories.setText(String.format("%.0f", currentUserDetails.getBurn_or_eat_calories()) + " Calories");
                } else {
                    textViewEatOrBurn.setText("You have to BURN");
                    textViewCalories.setTextColor(getColor(R.color.red));
                    textViewCalories.setText(String.format("%.0f", currentUserDetails.getBurn_or_eat_calories() * (-1)) + " Calories");
                }
                return true;
            }
        });

        listViewFoods.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Calculations.AdaugaCalorii(MainActivity.AllFoods.get(position).getCalorii());
                MainActivity.AllFoods.remove(position);
                Date date = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
                String formattedDate = df.format(date);
                String dataHardCodata = "18-Jun-2020";
                HashMap<String, ArrayList<Food>> map = new HashMap<>();
                map.put("mancaruri", MainActivity.AllFoods);
                dbfirestore.collection("users").document(userId).collection("MancaruriSiExercitii").document(formattedDate).set(map, SetOptions.merge());
//                if (MainActivity.AllFoods.size() > 0) {
//                    dbfirestore.collection("users").document(userId).collection("MancaruriSiExercitii").document(formattedDate).set(map, SetOptions.merge());
//                } else {
//
//                    HashMap<String,Integer> nullMap= new HashMap<>();
//                    nullMap.put("mancaruri",0);
//                    dbfirestore.collection("users").document(userId).collection("MancaruriSiExercitii").document(formattedDate).set(nullMap, SetOptions.merge());
//                }
                foodsAdapter.notifyDataSetChanged();
                if (currentUserDetails.getBurn_or_eat_calories() >= 0) {
                    textViewEatOrBurn.setText("You have to EAT");
                    textViewCalories.setTextColor(getColor(R.color.white));
                    textViewCalories.setText(String.format("%.0f", currentUserDetails.getBurn_or_eat_calories()) + " Calories");
                } else {
                    textViewEatOrBurn.setText("You have to BURN");
                    textViewCalories.setTextColor(getColor(R.color.red));
                    textViewCalories.setText(String.format("%.0f", currentUserDetails.getBurn_or_eat_calories() * (-1)) + " Calories");
                }
                return true;
            }
        });


    }


    public void scheduleResetCalories(Context context) {

        // Set the alarm to start at approximately 0:00 a.m.

        Intent intent = new Intent(context, AutoStartUp.class);
        final PendingIntent alarmIntent =
                PendingIntent.getBroadcast(this, 0, intent,
                        PendingIntent.FLAG_NO_CREATE);
        if (alarmIntent == null) {
            Log.d("Alarma", "a fost setata");
            PendingIntent alarmIntentnou = PendingIntent.getBroadcast(context, 0, intent, 0);
            alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            if (Calendar.getInstance().getTimeInMillis() > calendar.getTimeInMillis()) {
                calendar.add(Calendar.DATE, 1);
            }

            //alarmMgr.set(AlarmManager.RTC_WAKEUP, 1, alarmIntentnou);
            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntentnou);
        } else {
            Log.d("Alarma", "alarma exista");
           // Toast.makeText(this, "Alarma exista", Toast.LENGTH_SHORT).show();
        }

        if (MainActivity.AllExercises.size() == 0 && MainActivity.AllFoods.size() == 0 && MainActivity.dailySteps==0) {
            if (MainActivity.currentUserDetails.getRecommended_calories() != MainActivity.currentUserDetails.getBurn_or_eat_calories()) {
                MainActivity.currentUserDetails.setBurn_or_eat_calories(MainActivity.currentUserDetails.getRecommended_calories());
                PendingIntent alarmIntentnou = PendingIntent.getBroadcast(context, 0, intent, 0);
                alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                alarmMgr.set(AlarmManager.RTC_WAKEUP, 1, alarmIntentnou);
                Log.d("Alarma", "Alarma fortata a fost setata");
            }
        }
    }


    public void scheduleAddSteps(Context context) {
        Intent intent2 = new Intent(context, AddStepToAlarm.class);
        final PendingIntent alarmIntent2 =
                PendingIntent.getBroadcast(this, 1, intent2,
                        PendingIntent.FLAG_NO_CREATE);
        if (alarmIntent2 == null) {
            Log.d("Alarma", "a fost setata pentru pasi");
            PendingIntent alarmIntentnou2 = PendingIntent.getBroadcast(context, 1, intent2, 0);
            alarmMgrPasi = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 2);
            calendar.set(Calendar.MINUTE, 39);
            calendar.set(Calendar.SECOND, 0);
            Log.d("Alarma", "la ora : " + calendar.getTime());


            alarmMgrPasi.set(AlarmManager.RTC_WAKEUP, 10, alarmIntentnou2);
            //alarmMgrPasi.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_HALF_HOUR, alarmIntentnou);
        } else {
            Log.d("Alarma", "alarma pasi exista");
            //Toast.makeText(this, "Alarma pasi exista", Toast.LENGTH_SHORT).show();
        }
    }


    public void incarcaGreutateInFirestore() {

        FirebaseFirestore dbfirestore;

        dbfirestore = FirebaseFirestore.getInstance();

        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        formatter.setMaximumFractionDigits(1);
        formatter.setRoundingMode(RoundingMode.HALF_UP);
        for (int i = 26; i <= 30; i++) {
            Double weight = 78.0;
            Random rd = new Random();
            weight += rd.nextFloat();
            Double weightFormatted = new Double(formatter.format(weight));
            Map<String, Double> map_greutate = new HashMap<>();
            String dataHardocdata = i + "-Jun-2020";
            map_greutate.put("weight", weightFormatted);
            dbfirestore.collection("users").document(userId).collection("MancaruriSiExercitii").document(dataHardocdata).set(map_greutate, SetOptions.merge());
        }
    }
}
