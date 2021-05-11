package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ListView;

import com.example.licenta.adapters.ExercisesAdapter;
import com.example.licenta.adapters.FoodsAdapter;
import com.example.licenta.models.Exercise;
import com.example.licenta.models.ExercisesDocument;
import com.example.licenta.models.Food;
import com.example.licenta.models.FoodsDocument;
import com.example.licenta.utils.Utils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {
    DrawerLayout drawerlayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ListView listView_dialog_foods, listView_dialog_exercises;
    CalendarView calendarView;
    ArrayList<Food> foodsInSelectedDate = new ArrayList<>();
    ArrayList<Exercise> exercisesInSelectedDate = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);


        Utils.CreateNavigationDrawer(CalendarActivity.this);


        calendarView = findViewById(R.id.calendarView);

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        try {
            date = simpleDateFormat.parse(MainActivity.currentUserDetails.getStart_date());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            calendarView.setMinDate(date.getTime());
        }

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Date dateUnformatted = null;
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseFirestore dbfirestore = FirebaseFirestore.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd-M-yyyy");
                String date = dayOfMonth + "-" + (month + 1) + "-" + year;
                try {
                    dateUnformatted = df.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String formattedDate = simpleDateFormat.format(dateUnformatted);
                dbfirestore.collection("users").document(uid).collection("MancaruriSiExercitii").document(formattedDate).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            boolean exercisesExists = false;
                            boolean foodsExists = false;
                            exercisesInSelectedDate = documentSnapshot.toObject(ExercisesDocument.class).exercitii;
                            foodsInSelectedDate = documentSnapshot.toObject(FoodsDocument.class).mancaruri;
                            AlertDialog.Builder mBuilder = new AlertDialog.Builder(CalendarActivity.this, R.style.MyDialogTheme);
                            View mView = getLayoutInflater().inflate(R.layout.dialog_calendar_list, null);
                            listView_dialog_foods = mView.findViewById(R.id.dialog_listView_food);
                            listView_dialog_exercises = mView.findViewById(R.id.dialog_listView_exercises);
                            if (foodsInSelectedDate != null) {
                                if (foodsInSelectedDate.size() > 0) {
                                    foodsExists=true;
                                    FoodsAdapter foodsAdapter = new FoodsAdapter(CalendarActivity.this, R.layout.food_item, foodsInSelectedDate);
                                    listView_dialog_foods.setAdapter(foodsAdapter);
                                }
                            }
                            if (exercisesInSelectedDate != null) {
                                if (exercisesInSelectedDate.size() > 0) {
                                    exercisesExists=true;
                                    ExercisesAdapter exercisesAdapter = new ExercisesAdapter(CalendarActivity.this, R.layout.exercise_item, exercisesInSelectedDate);
                                    listView_dialog_exercises.setAdapter(exercisesAdapter);
                                }
                            }

                            mBuilder.setView(mView).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                            if (foodsExists|| exercisesExists) {


                                AlertDialog dialog = mBuilder.create();

                                dialog.show();
                            } else {

                                AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(CalendarActivity.this, R.style.MyDialogTheme);
                                mBuilder2.setMessage("Nu exista date de afisat pentru ziua respectiva").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                AlertDialog dialog = mBuilder2.create();

                                dialog.show();

                            }
                        } else {
                            AlertDialog.Builder mBuilder = new AlertDialog.Builder(CalendarActivity.this, R.style.MyDialogTheme);
                            mBuilder.setMessage("Nu exista date de afisat pentru ziua respectiva").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            AlertDialog dialog = mBuilder.create();

                            dialog.show();
                        }
                    }
                });
            }
        });


    }

    public void AfisareDialog() {
    }
}
