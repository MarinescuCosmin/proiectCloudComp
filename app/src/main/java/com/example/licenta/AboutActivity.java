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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.licenta.models.CalorieRetroFit;
import com.example.licenta.models.WeatherRetroFit;
import com.example.licenta.utils.CaloriesClient;
import com.example.licenta.utils.GetDataService;
import com.example.licenta.utils.Utils;
import com.example.licenta.utils.WeatherClient;
import com.google.android.material.navigation.NavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutActivity extends AppCompatActivity {
    DrawerLayout drawerlayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Button btnGetWeather;
    Button btnGetFood;
    EditText editTextCityName;
    EditText editTextFoodName;
    TextView textViewWeather;
    TextView textViewNutritionFacts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Utils.CreateNavigationDrawer(AboutActivity.this);

        btnGetWeather= findViewById(R.id.btnGetTemperature);
        btnGetFood = findViewById(R.id.btnFoodAPI);
        editTextCityName = findViewById(R.id.editTextEnterCity);
        editTextFoodName = findViewById(R.id.editTextEnterFood);
        textViewWeather = findViewById(R.id.textViewWeather);
        textViewNutritionFacts = findViewById(R.id.textViewNutritionFacts);


        btnGetWeather.setOnClickListener(v -> {

        GetDataService service = WeatherClient.getRetrofitInstance().create(GetDataService.class);
        Call<WeatherRetroFit> call = service.getWeather(editTextCityName.getText().toString(), "dcefc7378210c484f8dac4d6a6e87b2d");
        call.enqueue(new Callback<WeatherRetroFit>() {
            @Override
            public void onResponse(Call<WeatherRetroFit> call, Response<WeatherRetroFit> response) {
                textViewWeather.setText(response.body().toString());
            }

            @Override
            public void onFailure(Call<WeatherRetroFit> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
        });



        btnGetFood.setOnClickListener(v -> {

            GetDataService service2 = CaloriesClient.getRetrofitInstance().create(GetDataService.class);
            Call<CalorieRetroFit> call2 = service2.getCalories(editTextFoodName.getText().toString());
            call2.enqueue(new Callback<CalorieRetroFit>() {
                @Override
                public void onResponse(Call<CalorieRetroFit> call, Response<CalorieRetroFit> response) {
                    textViewNutritionFacts.setText(response.body().toString());
                }

                @Override
                public void onFailure(Call<CalorieRetroFit> call, Throwable t) {

                    Toast.makeText(getApplicationContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            });

        });

    }
}
