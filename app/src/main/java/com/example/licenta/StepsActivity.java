package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.licenta.utils.Calculations;
import com.example.licenta.utils.Utils;
import com.google.android.material.navigation.NavigationView;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class StepsActivity extends AppCompatActivity implements SensorEventListener {


    TextView textViewSteps;
    SensorManager sensorManager;
    boolean running= false;
    CircularProgressBar progressCircular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        textViewSteps=(TextView)findViewById(R.id.tVsteps);
        progressCircular = findViewById(R.id.progress_bar_steps);
        sensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);

        Utils.CreateNavigationDrawer(StepsActivity.this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        running = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(countSensor !=null){
            sensorManager.registerListener(this,countSensor,SensorManager.SENSOR_DELAY_UI);

        }else{
            Toast.makeText(StepsActivity.this,"Sensor not found",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(running){
            Float currentsteps = 100 + event.values[0];

            double calorii_arse_pasi = (currentsteps-MainActivity.dailySteps) /1000 * MainActivity.currentUserDetails.getWeight() * 0.6;
            MainActivity.dailySteps=  currentsteps;
            Calculations.AdaugaCalorii(calorii_arse_pasi);
            textViewSteps.setText(currentsteps.toString());

            progressCircular.setProgressWithAnimation(currentsteps);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
