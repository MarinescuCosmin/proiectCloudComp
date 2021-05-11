package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.licenta.models.Food;
import com.example.licenta.utils.Utils;
import com.google.android.material.navigation.NavigationView;

public class ProfileActivity extends AppCompatActivity {
    DrawerLayout drawerlayout, drawerlayoutt;
    NavigationView navigationView, navigationVieww;
    Toolbar toolbar, toolbarr;
    Button buttonEditProfile,btnSave,btnCancel;
    TextView textViewAge,textViewHeight,textViewGender,textViewLifestyle,textViewLoseOrGain, textViewLoseOrGainWeight,textViewWeight;

    @Override
    protected void onResume() {
        super.onResume();
        textViewAge.setText(String.format("%.0f",MainActivity.currentUserDetails.getAge()));
        textViewWeight.setText(String.format("%.0f",MainActivity.currentUserDetails.getWeight())+" kg");
        textViewLoseOrGainWeight.setText(String.valueOf(MainActivity.currentUserDetails.getLoss_or_gain_weight()));
        textViewHeight.setText(String.format("%.0f",MainActivity.currentUserDetails.getHeight())+" cm");
        textViewLoseOrGain.setText(MainActivity.currentUserDetails.getLoss_or_gain());
        textViewLifestyle.setText(MainActivity.currentUserDetails.getLifestyle_type());
        textViewGender.setText(MainActivity.currentUserDetails.getGender());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        buttonEditProfile = (Button) findViewById(R.id.buttonEditProfile);

        Utils.CreateNavigationDrawer(ProfileActivity.this);
        textViewAge=findViewById(R.id.tVProfileAge);
        textViewGender=findViewById(R.id.tVProfileGender);
        textViewHeight= findViewById(R.id.tVProfileHeight);
        textViewLifestyle=findViewById(R.id.tVProfileLifestyle);
        textViewLoseOrGain=findViewById(R.id.tVProfileLossOrGain);
        textViewLoseOrGainWeight=findViewById(R.id.tVProfileLoseOrGainWeight);
        textViewWeight=findViewById(R.id.tvProfileWeight);


        textViewAge.setText(String.format("%.0f",MainActivity.currentUserDetails.getAge()));
        textViewWeight.setText(String.format("%.2f",MainActivity.currentUserDetails.getWeight()));
        textViewLoseOrGainWeight.setText(String.valueOf(MainActivity.currentUserDetails.getLoss_or_gain_weight()));
        textViewHeight.setText(String.format("%.0f",MainActivity.currentUserDetails.getHeight()));
        textViewLoseOrGain.setText(MainActivity.currentUserDetails.getLoss_or_gain());
        textViewLifestyle.setText(MainActivity.currentUserDetails.getLifestyle_type());
        textViewGender.setText(MainActivity.currentUserDetails.getGender());




        buttonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Food f = new Food();
                Intent intent = new Intent(ProfileActivity.this,UserDetailsActivity.class);
                intent.putExtra("profile","profile");

                startActivity(intent);
            }
        });



    }


}
