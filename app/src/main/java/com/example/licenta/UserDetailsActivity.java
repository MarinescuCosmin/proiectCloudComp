package com.example.licenta;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.licenta.models.UserDetails;
import com.example.licenta.utils.Calculations;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UserDetailsActivity extends AppCompatActivity {
    Button btnNext, btnInfo, btnSave, btnCancel;
    Spinner spinnerLifestyle;
    EditText editTextAge, editTextHeight, editTextWeight;
    RadioGroup radioGroupGender, radioGroupLoseOrGainWeight, radioGroupLoseGain;
    RadioButton maleButton, femaleButton, easyButton, mediumButton, extremeButton, gainButton, loseButton;
    FirebaseFirestore dbfirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        UserDetails userDetails = new UserDetails();
        dbfirestore = FirebaseFirestore.getInstance();

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        editTextAge = findViewById(R.id.editTextAge);
        editTextHeight = findViewById(R.id.editTextHeight);
        editTextWeight = findViewById(R.id.editTextWeight);

        radioGroupGender = findViewById(R.id.radioGroupGender);
        maleButton = findViewById(R.id.radio_male);
        femaleButton = findViewById(R.id.radio_female);

        radioGroupLoseGain = findViewById(R.id.radioGroupGainLose);
        gainButton = findViewById(R.id.radio_gain);
        loseButton = findViewById(R.id.radio_lose);

        radioGroupLoseOrGainWeight = findViewById(R.id.radioGroupLoseWeight);
        easyButton = findViewById(R.id.radio_easy);
        mediumButton = findViewById(R.id.radio_medium);
        extremeButton = findViewById(R.id.radio_extreme);

        spinnerLifestyle = findViewById(R.id.spinnerLifestyle);
        btnNext = (Button) findViewById(R.id.button_user_detail_next);
        btnInfo = (Button) findViewById(R.id.buttonInfo);
        btnSave = findViewById(R.id.button_user_detail_save);
        btnCancel = findViewById(R.id.button_user_detail_cancel);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.lifestyles, R.layout.spinner_lifestyle);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLifestyle.setAdapter(adapter);


        String activitate = "";
        Intent intent = getIntent();
        if (intent.hasExtra("profile")) {
            activitate = intent.getStringExtra("profile");
        }


        if (activitate.equalsIgnoreCase("profile")) {
            btnNext.setClickable(false);
            btnNext.setVisibility(View.INVISIBLE);
            editTextAge.setText(String.format("%.0f", MainActivity.currentUserDetails.getAge()));
            editTextHeight.setText(String.format("%.0f", MainActivity.currentUserDetails.getHeight()));
            editTextWeight.setText(String.format("%.0f", MainActivity.currentUserDetails.getWeight()));
            if (MainActivity.currentUserDetails.getLoss_or_gain().equalsIgnoreCase("lose")) {
                loseButton.setChecked(true);
            } else {
                gainButton.setChecked(true);
            }
            if (MainActivity.currentUserDetails.getLoss_or_gain_weight() == 0.25) {
                easyButton.setChecked(true);
            } else if (MainActivity.currentUserDetails.getLoss_or_gain_weight() == 0.5) {
                mediumButton.setChecked(true);
            } else {
                extremeButton.setChecked(true);
            }
            if (MainActivity.currentUserDetails.getGender().equalsIgnoreCase("male")) {
                maleButton.setChecked(true);
            } else {
                femaleButton.setChecked(true);
            }
            if (MainActivity.currentUserDetails.getLifestyle_type().equalsIgnoreCase(spinnerLifestyle.getItemAtPosition(0).toString())) {
                spinnerLifestyle.setSelection(0);
            } else if (MainActivity.currentUserDetails.getLifestyle_type().equalsIgnoreCase(spinnerLifestyle.getItemAtPosition(1).toString())) {
                spinnerLifestyle.setSelection(1);
            } else if (MainActivity.currentUserDetails.getLifestyle_type().equalsIgnoreCase(spinnerLifestyle.getItemAtPosition(2).toString())) {
                spinnerLifestyle.setSelection(2);
            } else if (MainActivity.currentUserDetails.getLifestyle_type().equalsIgnoreCase(spinnerLifestyle.getItemAtPosition(3).toString())) {
                spinnerLifestyle.setSelection(3);
            } else if (MainActivity.currentUserDetails.getLifestyle_type().equalsIgnoreCase(spinnerLifestyle.getItemAtPosition(4).toString())) {
                spinnerLifestyle.setSelection(4);
            } else {
                spinnerLifestyle.setSelection(5);
            }


        } else {
            btnSave.setClickable(false);
            btnCancel.setClickable(false);
            btnSave.setVisibility(View.INVISIBLE);
            btnCancel.setVisibility(View.INVISIBLE);
        }

        btnCancel.setOnClickListener(v -> {
            finish();
        });

        btnSave.setOnClickListener(v -> {
            if ((editTextAge.getText().toString().isEmpty()) || editTextHeight.getText().toString().isEmpty() || editTextWeight.getText().toString().isEmpty()) {
                Toast.makeText(UserDetailsActivity.this, "Please fill all the empty fields!", Toast.LENGTH_LONG).show();
            } else if (radioGroupGender.getCheckedRadioButtonId() == -1) {
                Toast.makeText(UserDetailsActivity.this, "Please choose male/female!", Toast.LENGTH_LONG).show();
            } else if (radioGroupLoseGain.getCheckedRadioButtonId() == -1) {
                Toast.makeText(UserDetailsActivity.this, "Please choose gain/lose!", Toast.LENGTH_LONG).show();
            } else if (radioGroupLoseOrGainWeight.getCheckedRadioButtonId() == -1) {
                Toast.makeText(UserDetailsActivity.this, "Please choose 0.25 / 0.5 / 1!", Toast.LENGTH_LONG).show();
            } else {
                MainActivity.currentUserDetails.setAge(Double.valueOf(editTextAge.getText().toString()));
                MainActivity.currentUserDetails.setHeight(Double.valueOf(editTextHeight.getText().toString()));
                MainActivity.currentUserDetails.setWeight(Double.valueOf(editTextWeight.getText().toString()));
                MainActivity.currentUserDetails.setLifestyle_type(spinnerLifestyle.getSelectedItem().toString());

                if (maleButton.isChecked()) {
                    MainActivity.currentUserDetails.setGender("male");
                } else {
                    MainActivity.currentUserDetails.setGender("female");
                }

                if (gainButton.isChecked()) {
                    MainActivity.currentUserDetails.setLoss_or_gain("gain");
                } else {
                    MainActivity.currentUserDetails.setLoss_or_gain("lose");
                }

                if (easyButton.isChecked()) {
                    MainActivity.currentUserDetails.setLoss_or_gain_weight(0.25);
                } else if (mediumButton.isChecked()) {
                    MainActivity.currentUserDetails.setLoss_or_gain_weight(0.5);
                } else {
                    MainActivity.currentUserDetails.setLoss_or_gain_weight(1);
                }
                MainActivity.recommendedCalories = (10 * MainActivity.currentUserDetails.getWeight()) + (6.25 * MainActivity.currentUserDetails.getHeight()) - (5 * MainActivity.currentUserDetails.getAge());
                if (spinnerLifestyle.getSelectedItemId() == 0) {
                    MainActivity.recommendedCalories *= 1.1;
                } else if (spinnerLifestyle.getSelectedItemId() == 1) {
                    MainActivity.recommendedCalories *= 1.3;
                } else if (spinnerLifestyle.getSelectedItemId() == 2) {
                    MainActivity.recommendedCalories *= 1.5;
                } else if (spinnerLifestyle.getSelectedItemId() == 3) {
                    MainActivity.recommendedCalories *= 1.7;
                } else if (spinnerLifestyle.getSelectedItemId() == 4) {
                    MainActivity.recommendedCalories *= 1.8;
                } else {
                    MainActivity.recommendedCalories *= 1.9;
                }
                if (MainActivity.currentUserDetails.getGender().equalsIgnoreCase("male")) {
                    MainActivity.recommendedCalories += 5;
                } else {
                    MainActivity.recommendedCalories -= 161;
                }
//                if (easyButton.isChecked()) {
//                    MainActivity.currentUserDetails.setLoss_or_gain_weight(0.25);
//                } else if (mediumButton.isChecked()) {
//                    MainActivity.currentUserDetails.setLoss_or_gain_weight(0.5);
//                } else {
//                    MainActivity.currentUserDetails.setLoss_or_gain_weight(1);
//                }

                if (MainActivity.currentUserDetails.getLoss_or_gain().equalsIgnoreCase("lose")) {
                    MainActivity.recommendedCalories -= (MainActivity.currentUserDetails.getLoss_or_gain_weight() * 1000);

                } else {
                    MainActivity.recommendedCalories += (MainActivity.currentUserDetails.getLoss_or_gain_weight() * 1000);
                }
                MainActivity.currentUserDetails.setRecommended_calories(MainActivity.recommendedCalories);
                Calculations.SetNewBurnOrEatCalories();
//                userDetails.setRecommended_calories(MainActivity.recommendedCalories);
//                userDetails.setBurn_or_eat_calories(MainActivity.currentUserDetails.getBurn_or_eat_calories());
                Map<String, Date> map = new HashMap<>();
                map.put("last_edit", Calendar.getInstance().getTime());
//                        dbfirestore.collection("users").document(userId).set(map,SetOptions.merge());
                Date date = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
                String formattedDate = df.format(date);
                Map<String, Double> map_greutate = new HashMap<>();
                String dataHardocdata= "18-Jun-2020";
                map_greutate.put("weight",MainActivity.currentUserDetails.getWeight());
                dbfirestore.collection("users").document(userId).collection("MancaruriSiExercitii").document(formattedDate).set(map_greutate, SetOptions.merge());
                dbfirestore.collection("users").document(userId).set(MainActivity.currentUserDetails, SetOptions.merge());
                dbfirestore.collection("users").document(userId).set(map, SetOptions.merge());


                finish();
            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((editTextAge.getText().toString().isEmpty()) || editTextHeight.getText().toString().isEmpty() || editTextWeight.getText().toString().isEmpty()) {
                    Toast.makeText(UserDetailsActivity.this, "Please fill all the empty fields!", Toast.LENGTH_LONG).show();
                } else if (radioGroupGender.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(UserDetailsActivity.this, "Please choose male/female!", Toast.LENGTH_LONG).show();
                } else if (radioGroupLoseGain.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(UserDetailsActivity.this, "Please choose gain/lose!", Toast.LENGTH_LONG).show();
                } else if (radioGroupLoseOrGainWeight.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(UserDetailsActivity.this, "Please choose 0.25 / 0.5 / 1!", Toast.LENGTH_LONG).show();
                } else {
                    userDetails.setAge(Double.valueOf(editTextAge.getText().toString()));
                    userDetails.setHeight(Double.valueOf(editTextHeight.getText().toString()));
                    userDetails.setWeight(Double.valueOf(editTextWeight.getText().toString()));
                    userDetails.setLifestyle_type(spinnerLifestyle.getSelectedItem().toString());

                    if (maleButton.isChecked()) {
                        userDetails.setGender("male");
                    } else {
                        userDetails.setGender("female");
                    }

                    if (gainButton.isChecked()) {
                        userDetails.setLoss_or_gain("gain");
                    } else {
                        userDetails.setLoss_or_gain("lose");
                    }

                    if (easyButton.isChecked()) {
                        userDetails.setLoss_or_gain_weight(0.25);
                    } else if (mediumButton.isChecked()) {
                        userDetails.setLoss_or_gain_weight(0.5);
                    } else {
                        userDetails.setLoss_or_gain_weight(1);
                    }

                    MainActivity.recommendedCalories = (10 * userDetails.getWeight()) + (6.25 * userDetails.getHeight()) - (5 * userDetails.getAge());
                    if (spinnerLifestyle.getSelectedItemId() == 0) {
                        MainActivity.recommendedCalories *= 1.1;
                    } else if (spinnerLifestyle.getSelectedItemId() == 1) {
                        MainActivity.recommendedCalories *= 1.3;
                    } else if (spinnerLifestyle.getSelectedItemId() == 2) {
                        MainActivity.recommendedCalories *= 1.5;
                    } else if (spinnerLifestyle.getSelectedItemId() == 3) {
                        MainActivity.recommendedCalories *= 1.7;
                    } else if (spinnerLifestyle.getSelectedItemId() == 4) {
                        MainActivity.recommendedCalories *= 1.8;
                    } else {
                        MainActivity.recommendedCalories *= 1.9;
                    }
                    if (userDetails.getGender().equalsIgnoreCase("male")) {
                        MainActivity.recommendedCalories += 5;
                    } else {
                        MainActivity.recommendedCalories -= 161;
                    }

                    if (userDetails.getLoss_or_gain().equalsIgnoreCase("lose")) {
                        MainActivity.recommendedCalories -= (userDetails.getLoss_or_gain_weight() * 1000);

                    } else {
                        MainActivity.recommendedCalories += (userDetails.getLoss_or_gain_weight() * 1000);
                    }

                    userDetails.setRecommended_calories(MainActivity.recommendedCalories);
                    userDetails.setBurn_or_eat_calories(userDetails.getRecommended_calories());
                    Date date = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                    String formattedDate = df.format(date);

                    userDetails.setStart_date(formattedDate);
                    Map<String, Date> map = new HashMap<>();
                    map.put("last_edit", Calendar.getInstance().getTime());
//                        dbfirestore.collection("users").document(userId).set(map,SetOptions.merge());
                    dbfirestore.collection("users").document(userId).set(userDetails, SetOptions.merge());
                    MainActivity.currentUserDetails = userDetails;
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserDetailsActivity.this);
                builder.setTitle("Information").setMessage("•	Exercise: 15-30 minutes of elevated heart rate activity." +
                        "\n" +
                        "•	Intense exercise: 45-120 minutes of elevated heart rate activity" +
                        "\n" +
                        "•	Very intense exercise: 2+ hours of elevated heart rate activity.")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.create();
                builder.show();
            }
        });


    }


}
