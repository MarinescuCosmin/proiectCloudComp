package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.licenta.adapters.SearchAdapterExercise;
import com.example.licenta.adapters.SearchAdapterFood;
import com.example.licenta.models.Exercise;
import com.example.licenta.models.Food;
import com.example.licenta.utils.Calculations;
import com.example.licenta.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddExerciseActivity extends AppCompatActivity implements SearchAdapterExercise.OnExerciseClickListener {

    SearchView searchViewExercise;
    RecyclerView recyclerViewExercise;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    ArrayList<Exercise> lista_search_exercitii;
    SearchAdapterExercise searchAdapterExercise;
    ProgressBar progressBar;
    ArrayList<Exercise> myList = new ArrayList<>();
    FirebaseFirestore dbfirestore;
    String userId;

    public static boolean SEARCH_PRESSED = false;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SEARCH_PRESSED = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        lista_search_exercitii = new ArrayList<>();
        progressBar = findViewById(R.id.progressBarAddExercises);
        searchViewExercise = findViewById(R.id.searchViewExercises);
        recyclerViewExercise = findViewById(R.id.recyclerViewExercises);

        dbfirestore=FirebaseFirestore.getInstance();
        userId=FirebaseAuth.getInstance().getCurrentUser().getUid();





        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerViewExercise.setHasFixedSize(true);
        recyclerViewExercise.setLayoutManager(new LinearLayoutManager((this)));
        recyclerViewExercise.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        searchAdapterExercise = new SearchAdapterExercise(AddExerciseActivity.this, LoginActivity.lista_search_exercitii, this);

//        if(MainActivity.InfoDatabaseGotExercise==false) {
//            FirebaseFirestore dbfirestore = FirebaseFirestore.getInstance();
//            dbfirestore.collection("exercitii").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    if (task.isSuccessful()) {
//                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                            Exercise exercise_temp = documentSnapshot.toObject(Exercise.class);
//
//                            LoginActivity.lista_search_exercitii.add(exercise_temp);
//                        }
//                        LoginActivity.lista_search_exercitii.sort(Comparator.comparing(Exercise::getActivitate));
//                        recyclerViewExercise.setAdapter(searchAdapterExercise);
//                        MainActivity.InfoDatabaseGotExercise=true;
//                        progressBar.setVisibility(View.INVISIBLE);
//                    }
//                }
//            });
//        } else {
//            searchAdapterExercise.notifyDataSetChanged();
//            recyclerViewExercise.setAdapter(searchAdapterExercise);
//            progressBar.setVisibility(View.INVISIBLE);
//        }
//
        searchViewExercise.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SEARCH_PRESSED = true;
                search(query);

                Toast.makeText(AddExerciseActivity.this, "." + lista_search_exercitii.size() + "." + LoginActivity.lista_search_exercitii.size(), Toast.LENGTH_LONG).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                SEARCH_PRESSED = true;
                search(newText);

                return true;
            }
        });

        if (MainActivity.InfoDatabaseGotExercise == false) {
            databaseReference.child("Mancaruri").child("1").child("exercitii").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    String aliment = snapshot.child("Aliment").getValue().toString();
//                    Double calorii = Double.valueOf(snapshot.child("Calorii").getValue().toString());
//                    Double carbo = Double.valueOf(snapshot.child("Carbohidrati").getValue().toString());
//                    Double lipide = Double.valueOf(snapshot.child("Lipide").getValue().toString());
//                    Double proteine = Double.valueOf(snapshot.child("Proteine").getValue().toString());

//                    if(aliment.contains(string)){
//                        Food temp_Food = new Food(aliment,calorii,carbo,lipide,proteine);
//                        lista_search_exercitii.add(temp_Food);
//
//                    }
                        LoginActivity.lista_search_exercitii.add(snapshot.getValue(Exercise.class));


                        searchAdapterExercise.notifyDataSetChanged();
                        recyclerViewExercise.setAdapter(searchAdapterExercise);

                    }
                    progressBar.setVisibility(View.INVISIBLE);
                    MainActivity.InfoDatabaseGotExercise = true;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            searchAdapterExercise.notifyDataSetChanged();
            recyclerViewExercise.setAdapter(searchAdapterExercise);
            progressBar.setVisibility(View.INVISIBLE);
        }


    }


    private void search(String str) {
        myList.clear();

        for (Exercise f : LoginActivity.lista_search_exercitii) {
            if (f.getActivitate().toLowerCase().startsWith(str.toLowerCase())) {
                myList.add(f);
            }

        }

        for (Exercise f : LoginActivity.lista_search_exercitii) {
            if (f.getActivitate().toLowerCase().startsWith(str.toLowerCase())) {

            } else if (f.getActivitate().toLowerCase().contains(str.toLowerCase())) {
                myList.add(f);
            }
        }
        SearchAdapterExercise searchAdapterExercise = new SearchAdapterExercise(AddExerciseActivity.this, myList, this);
        recyclerViewExercise.setAdapter(searchAdapterExercise);


    }


    @Override
    public void onExerciseItemClick(int position) {
        searchViewExercise.clearFocus();
        Exercise exercise;
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(10);
        if (SEARCH_PRESSED) {
            exercise = (myList.get(position)).deepCopy();

        } else {
            exercise = (LoginActivity.lista_search_exercitii.get(position)).deepCopy();
        }
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddExerciseActivity.this,R.style.MyDialogTheme);
        View mView = getLayoutInflater().inflate(R.layout.dialog_add_exercise, null);
        EditText editText_timp_exercitiu = (EditText) mView.findViewById(R.id.editTextTimp_exercitiu_dialog);
        TextView nume_exercitiu_dialog = (TextView) mView.findViewById(R.id.nume_exercitiu_dialog);
        TextView preview_calorii_arse= (TextView) mView.findViewById(R.id.calorii_arse_exercitiu_dialog);
        Button buttonPreviewValoriDialog = (Button) mView.findViewById(R.id.buttonPreviewValoriDialogExercise);

        nume_exercitiu_dialog.setText(exercise.getActivitate());
        buttonPreviewValoriDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText_timp_exercitiu.getText().toString().isEmpty()) {


                    Double caloriiarse = Calculations.CaloriiArseUser(MainActivity.currentUserDetails.getWeight(), exercise.getMET()) * Double.valueOf(editText_timp_exercitiu.getText().toString()) / 60;
                    preview_calorii_arse.setText(caloriiarse.toString());


                } else {
                    showToast("Introduceti cantitatea!");
                }
            }
        });



        mBuilder.setView(mView).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!editText_timp_exercitiu.getText().toString().isEmpty()) {
                    Double caloriiarse = Calculations.CaloriiArseUser(MainActivity.currentUserDetails.getWeight(), exercise.getMET()) * Double.valueOf(editText_timp_exercitiu.getText().toString()) / 60;
                    exercise.setCaloriiArse(caloriiarse);
                    exercise.setTimp(Double.valueOf(editText_timp_exercitiu.getText().toString()));
                    MainActivity.AllExercises.add(exercise);
                    Calculations.AdaugaCalorii(exercise.getCaloriiArse());
                    Date date = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
                    String formattedDate = df.format(date);
                    Map<String,ArrayList<Exercise>> map = new HashMap<>();
                    String dataHardocdata= "18-Jun-2020";
                    map.put("exercitii",MainActivity.AllExercises);
                    dbfirestore.collection("users").document(userId).collection("MancaruriSiExercitii").document(formattedDate).set(map, SetOptions.merge());

                } else {
                    showToast("Nu ati introdus timpul pentru exercitiul respectiv!");
                }
            }
        }).setNegativeButton("CANCEL", null);

        AlertDialog dialog = mBuilder.create();


        dialog.show();

        MainActivity.foodsAdapter.notifyDataSetChanged();

    }

    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}

