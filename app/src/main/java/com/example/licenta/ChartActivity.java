package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.licenta.models.Exercise;
import com.example.licenta.models.ExercisesDocument;
import com.example.licenta.models.Food;
import com.example.licenta.models.FoodsDocument;
import com.example.licenta.utils.Utils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.DescriptorProtos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ChartActivity extends AppCompatActivity {
    DrawerLayout drawerlayout;
    NavigationView navigationView;
    Toolbar toolbar;

    BarChart barChart;
    TextView titlu_chart;
    ArrayList<Food> lista_mancaruri;
    ArrayList<Exercise> lista_exercitii;
    ArrayList<String> lista_saptamani = new ArrayList<>();
    ArrayList<String> labelNamesZile = new ArrayList<>();
    ArrayList<Double> lista_calorii_saptamanala = new ArrayList<>();
    ArrayList<String> labelNames = new ArrayList<>();


    ArrayList barEntryArrayList = new ArrayList();
    ArrayList barEntryArrayListExercises = new ArrayList();
    ArrayList  barEntryArrayListWeight = new ArrayList();
    HashMap<String, Double> hashMap_calorii_saptamanale = new HashMap<>();
    HashMap<String, Double> hashMap_calorii_arse_saptamanale = new HashMap<>();


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chart, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        titlu_chart.setText(item.getTitle());
        switch (item.getItemId()) {

            case R.id.mancare_saptamanala: {
                setChart(R.id.mancare_saptamanala);
                return true;
            }

            case R.id.exercitii_saptamanale: {
                setChart(R.id.exercitii_saptamanale);
                return true;
            }
            case R.id.mancare_si_exercitii_saptamanale:
                setChart(R.id.mancare_si_exercitii_saptamanale);
                return true;
            case R.id.greutate_zilnica:
                setChart(R.id.greutate_zilnica);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        titlu_chart = findViewById(R.id.titlu_chart);

        for (int i = 0; i < 30; i++) {
            labelNamesZile.add("Day " + (i+1));
        }

        lista_saptamani.add("Week 1");
        lista_saptamani.add("Week 2");
        lista_saptamani.add("Week 3");
        lista_saptamani.add("Week 4");
        hashMap_calorii_saptamanale.put("Week 1", 0.0);
        hashMap_calorii_saptamanale.put("Week 2", 0.0);
        hashMap_calorii_saptamanale.put("Week 3", 0.0);
        hashMap_calorii_saptamanale.put("Week 4", 0.0);
        hashMap_calorii_arse_saptamanale.put("Week 1", 0.0);
        hashMap_calorii_arse_saptamanale.put("Week 2", 0.0);
        hashMap_calorii_arse_saptamanale.put("Week 3", 0.0);
        hashMap_calorii_arse_saptamanale.put("Week 4", 0.0);


        barChart = findViewById(R.id.barChart);


        Utils.CreateNavigationDrawer(ChartActivity.this);


        Date dateUnformatted = null;
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore dbfirestore = FirebaseFirestore.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-M-yyyy");


        dbfirestore.collection("users").document(uid).collection("MancaruriSiExercitii").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int j=0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.getId().contains("Jun")) {

                            String month = "Jun";
                            double weight_double= (double) document.get("weight");
                            barEntryArrayListWeight.add(new BarEntry(j,(float)weight_double));
                            j++;
                            //de facut hashmap<String,Double> (String = Weeek no. ; Double, calorii)
                            //parametri la functie: luna+ document + hashmap
                            //apel de o singura functie si tratarea cazurilor pentru fiecare saptamana in interiorul functiei
                            adaugaCaloriiSaptamanale(document, month, hashMap_calorii_saptamanale, hashMap_calorii_arse_saptamanale);
                        }
                    }
                    for (int i = 0; i < lista_saptamani.size(); i++) {
                        String week = lista_saptamani.get(i);
                        barEntryArrayListExercises.add(new BarEntry(i, hashMap_calorii_arse_saptamanale.get(week).intValue()));
                        barEntryArrayList.add(new BarEntry(i, hashMap_calorii_saptamanale.get(week).intValue()));
                        labelNames.add(week);
                    }


                    setChart(R.id.mancare_saptamanala);
//                    BarDataSet barDataSet = new BarDataSet(barEntryArrayList, "Weekly calories mean");
//                    int[] calorii = getResources().getIntArray(R.array.calorii);
//
//                    barDataSet.setValueTextColor(getResources().getColor(R.color.white));
//                    barDataSet.setColors(ColorTemplate.createColors(calorii));
//                    Description description = new Description();
//                    description.setText("June Month");
//                    description.setTextColor(getResources().getColor(R.color.black));
//                    description.setTextSize(10);
//
//                    BarDataSet barDataSetExercises = new BarDataSet(barEntryArrayListExercises,"Weekly burned calories mean");
//                    int[] calorii_arse = getResources().getIntArray(R.array.calorii_arse);
//
//                    barDataSetExercises.setValueTextColor(getResources().getColor(R.color.blue));
//                    barDataSetExercises.setColors(ColorTemplate.createColors(calorii_arse));
//                    barChart.setDescription(description);
//                    BarData barData = new BarData(barDataSet,barDataSetExercises);
//                    barData.setValueTextSize(10);
//                    barData.setValueTextColor(getResources().getColor(R.color.white));
//                    barChart.setData(barData);
//                    barData.setBarWidth(0.37f);
//                    barData.groupBars(0,0.15f,0.01f);
//
//
//                    barChart.setScrollBarSize(5);
//                    XAxis xAxis = barChart.getXAxis();
//                    YAxis yAxis = barChart.getAxisLeft();
//                    yAxis.setTextColor(getResources().getColor(R.color.white));
//                    YAxis right = barChart.getAxisRight();
//                    right.setTextColor(getResources().getColor(R.color.white));
//                    xAxis.setValueFormatter(new IndexAxisValueFormatter(labelNames));
//
//                    xAxis.setTextColor(getResources().getColor(R.color.white));
//                    xAxis.setGridColor(getResources().getColor(R.color.white));
//                    xAxis.setAxisLineColor(getResources().getColor(R.color.white));
//                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//                    xAxis.setDrawGridLines(false);
//                    xAxis.setDrawAxisLine(false);
//                    xAxis.setCenterAxisLabels(true);
//                    xAxis.setLabelCount(labelNames.size());
//                    xAxis.setGranularity(1f);
//                    xAxis.setAxisMinimum(0);
//                    xAxis.setDrawLabels(true);
//                    Legend legend = barChart.getLegend();
//                    legend.setTextColor(getResources().getColor(R.color.white));
//
//                    xAxis.setLabelCount(labelNames.size());
//                    xAxis.setLabelRotationAngle(0);
//
//                    barChart.animateY(2000);
//                    barChart.invalidate();


                }
            }
        });


    }

    public void setChart(int itemId) {


        barChart.clear();
        switch (itemId) {

            case R.id.mancare_saptamanala: {

                BarDataSet barDataSet = new BarDataSet(barEntryArrayList, "Weekly calories mean");
                int[] calorii = getResources().getIntArray(R.array.calorii);

                barDataSet.setValueTextColor(getResources().getColor(R.color.white));
                barDataSet.setColors(ColorTemplate.createColors(calorii));
                Description description = new Description();
                description.setText("June Month");

                description.setTextColor(getResources().getColor(R.color.blue));
                description.setTextSize(13);

                barChart.setDescription(description);
                BarData barData = new BarData(barDataSet);

                barData.clearValues();
                barData.addDataSet(barDataSet);
                barData.setValueTextSize(10);
                barData.setValueTextColor(getResources().getColor(R.color.white));
                barChart.setData(barData);
                barData.setBarWidth(0.70f);
                barChart.setPadding(10, 0, 0, 0);
                XAxis xAxis = barChart.getXAxis();
                xAxis.resetAxisMinimum();
                YAxis yAxis = barChart.getAxisLeft();
                yAxis.setTextColor(getResources().getColor(R.color.white));
                YAxis right = barChart.getAxisRight();
                right.setTextColor(getResources().getColor(R.color.white));
                xAxis.setValueFormatter(new IndexAxisValueFormatter(labelNames));

                xAxis.setCenterAxisLabels(false);
                xAxis.setTextColor(getResources().getColor(R.color.white));
                xAxis.setGridColor(getResources().getColor(R.color.white));
                xAxis.setAxisLineColor(getResources().getColor(R.color.white));
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setDrawGridLines(false);
                xAxis.setDrawAxisLine(false);
                xAxis.setGranularity(1f);
                xAxis.setDrawLabels(true);
                xAxis.setLabelCount(labelNames.size());
                xAxis.setLabelRotationAngle(0);

                Legend legend = barChart.getLegend();
                legend.setTextColor(getResources().getColor(R.color.white));


                barChart.animateY(2000);

                barChart.invalidate();
                break;
            }
            case R.id.exercitii_saptamanale: {
                BarDataSet barDataSet = new BarDataSet(barEntryArrayListExercises, "Weekly burned calories mean");
                int[] calorii = getResources().getIntArray(R.array.calorii);

                barDataSet.setValueTextColor(getResources().getColor(R.color.white));
                barDataSet.setColors(ColorTemplate.createColors(calorii));
                Description description = new Description();
                description.setText("June Month");

                description.setTextColor(getResources().getColor(R.color.blue));
                description.setTextSize(13);

                barChart.setDescription(description);
                BarData barData = new BarData(barDataSet);

                barData.clearValues();
                barData.addDataSet(barDataSet);
                barData.setValueTextSize(10);
                barData.setValueTextColor(getResources().getColor(R.color.white));
                barChart.setData(barData);
                barData.setBarWidth(0.70f);
                barChart.setPadding(10, 0, 0, 0);
                XAxis xAxis = barChart.getXAxis();
                xAxis.resetAxisMinimum();
                YAxis yAxis = barChart.getAxisLeft();
                yAxis.setTextColor(getResources().getColor(R.color.white));
                YAxis right = barChart.getAxisRight();
                right.setTextColor(getResources().getColor(R.color.white));
                xAxis.setValueFormatter(new IndexAxisValueFormatter(labelNames));

                xAxis.setCenterAxisLabels(false);
                xAxis.setTextColor(getResources().getColor(R.color.white));
                xAxis.setGridColor(getResources().getColor(R.color.white));
                xAxis.setAxisLineColor(getResources().getColor(R.color.white));
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setDrawGridLines(false);
                xAxis.setDrawAxisLine(false);
                xAxis.setGranularity(1f);
                xAxis.setDrawLabels(true);
                xAxis.setLabelCount(labelNames.size());
                xAxis.setLabelRotationAngle(0);

                Legend legend = barChart.getLegend();
                legend.setTextColor(getResources().getColor(R.color.white));


                barChart.animateY(2000);

                barChart.invalidate();
                break;
            }
            case R.id.mancare_si_exercitii_saptamanale: {

                BarDataSet barDataSet = new BarDataSet(barEntryArrayList, "Weekly calories mean");
                int[] calorii = getResources().getIntArray(R.array.calorii);

                barDataSet.setValueTextColor(getResources().getColor(R.color.white));
                barDataSet.setColors(ColorTemplate.createColors(calorii));
                Description description = new Description();
                description.setText("June Month");
                description.setTextColor(getResources().getColor(R.color.red));
                description.setTextSize(10);

                BarDataSet barDataSetExercises = new BarDataSet(barEntryArrayListExercises, "Weekly burned calories mean");
                int[] calorii_arse = getResources().getIntArray(R.array.calorii_arse);

                barDataSetExercises.setValueTextColor(getResources().getColor(R.color.blue));
                barDataSetExercises.setColors(ColorTemplate.createColors(calorii_arse));
                barChart.setDescription(description);
                BarData barData = new BarData(barDataSet, barDataSetExercises);
                barData.setValueTextSize(10);
                barData.setValueTextColor(getResources().getColor(R.color.white));
                barChart.setData(barData);
                barData.setBarWidth(0.37f);
                barData.groupBars(0, 0.15f, 0.01f);


                barChart.setScrollBarSize(5);
                XAxis xAxis = barChart.getXAxis();
                YAxis yAxis = barChart.getAxisLeft();
                yAxis.setTextColor(getResources().getColor(R.color.white));
                YAxis right = barChart.getAxisRight();
                right.setTextColor(getResources().getColor(R.color.white));
                xAxis.setValueFormatter(new IndexAxisValueFormatter(labelNames));
                xAxis.setAxisMinimum(0);
                xAxis.setTextColor(getResources().getColor(R.color.white));
                xAxis.setGridColor(getResources().getColor(R.color.white));
                xAxis.setAxisLineColor(getResources().getColor(R.color.white));
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setDrawGridLines(false);
                xAxis.setDrawAxisLine(false);
                xAxis.setCenterAxisLabels(true);
                xAxis.setLabelCount(labelNames.size());
                xAxis.setGranularity(1f);

                xAxis.setDrawLabels(true);
                Legend legend = barChart.getLegend();
                legend.setTextColor(getResources().getColor(R.color.white));

                xAxis.setLabelCount(labelNames.size());
                xAxis.setLabelRotationAngle(0);

                barChart.animateY(2000);
                barChart.invalidate();
                break;


            }

            case R.id.greutate_zilnica: {
                BarDataSet barDataSet = new BarDataSet(barEntryArrayListWeight, "Daily weight");
                int[] calorii = getResources().getIntArray(R.array.calorii);

                barDataSet.setValueTextColor(getResources().getColor(R.color.white));
                barDataSet.setColors(ColorTemplate.createColors(calorii));
                Description description = new Description();
                description.setText("June Month");

                description.setTextColor(getResources().getColor(R.color.blue));
                description.setTextSize(13);

                barChart.setDescription(description);
                BarData barData = new BarData(barDataSet);

                barData.clearValues();
                barData.addDataSet(barDataSet);
                barData.setValueTextSize(12);
                barData.setValueTextColor(getResources().getColor(R.color.white));
                barChart.setData(barData);
                barData.setBarWidth(0.70f);



                XAxis xAxis = barChart.getXAxis();
                xAxis.resetAxisMinimum();
                YAxis yAxis = barChart.getAxisLeft();
                yAxis.setTextColor(getResources().getColor(R.color.white));
                YAxis right = barChart.getAxisRight();
                right.setTextColor(getResources().getColor(R.color.white));
                xAxis.setValueFormatter(new IndexAxisValueFormatter(labelNamesZile));

                xAxis.setCenterAxisLabels(false);
                xAxis.setTextColor(getResources().getColor(R.color.white));
                xAxis.setGridColor(getResources().getColor(R.color.white));
                xAxis.setAxisLineColor(getResources().getColor(R.color.white));
                xAxis.setPosition(XAxis.XAxisPosition.TOP);
                xAxis.setDrawGridLines(false);
                xAxis.setDrawAxisLine(false);
                xAxis.setGranularity(1f);
                xAxis.setDrawLabels(true);
                xAxis.setLabelCount(labelNamesZile.size());
                xAxis.setLabelRotationAngle(270);

                Legend legend = barChart.getLegend();
                legend.setTextColor(getResources().getColor(R.color.white));


                barChart.animateY(2000);

                barChart.invalidate();
                break;

            }
        }

    }


    protected void adaugaCaloriiSaptamanale(QueryDocumentSnapshot document, String month, HashMap<String, Double> hashMapCaloriiMancare, HashMap<String, Double> hashMapCaloriiArse) {

        if ((document.getId().compareToIgnoreCase("00-" + month + "-2020") > 0)) {
            if ((document.getId().compareToIgnoreCase("08-" + month + "-2020") <= 0)) {
                lista_mancaruri = document.toObject(FoodsDocument.class).mancaruri;
                lista_exercitii = document.toObject(ExercisesDocument.class).exercitii;
                double calorii_arse = 0;
                double calorii = 0;
                if (lista_mancaruri != null) {
                    for (Food f : lista_mancaruri) {
                        calorii += f.getCalorii();
                    }
                }
                if (lista_exercitii != null) {
                    for (Exercise e : lista_exercitii) {
                        calorii_arse += e.getCaloriiArse() / 8;
                    }
                }

                hashMapCaloriiArse.put("Week 1", hashMapCaloriiArse.get("Week 1") + calorii_arse);
                hashMapCaloriiMancare.put("Week 1", hashMapCaloriiMancare.get("Week 1") + calorii);
                if ((document.getId().compareToIgnoreCase("08-" + month + "-2020") == 0)) {
                    hashMapCaloriiMancare.put("Week 1", hashMapCaloriiMancare.get("Week 1") / 8);
                }
            }
        }
        if ((document.getId().compareToIgnoreCase("08-" + month + "-2020") > 0)) {
            if ((document.getId().compareToIgnoreCase("16-" + month + "-2020") <= 0)) {
                lista_mancaruri = document.toObject(FoodsDocument.class).mancaruri;
                double calorii = 0;
                double calorii_arse = 0;
                if (lista_mancaruri != null) {
                    for (Food f : lista_mancaruri) {
                        calorii += f.getCalorii();
                    }
                }
                if (lista_exercitii != null) {
                    for (Exercise e : lista_exercitii) {
                        calorii_arse += e.getCaloriiArse() / 8;
                    }
                }

                hashMapCaloriiArse.put("Week 2", hashMapCaloriiArse.get("Week 2") + calorii_arse);
                hashMapCaloriiMancare.put("Week 2", hashMapCaloriiMancare.get("Week 2") + calorii);
                if ((document.getId().compareToIgnoreCase("16-" + month + "-2020") == 0)) {
                    hashMapCaloriiMancare.put("Week 2", hashMapCaloriiMancare.get("Week 2") / 8);
                }
            }
        }
        if ((document.getId().compareToIgnoreCase("16-" + month + "-2020") > 0)) {
            if ((document.getId().compareToIgnoreCase("24-" + month + "-2020") <= 0)) {
                lista_mancaruri = document.toObject(FoodsDocument.class).mancaruri;
                double calorii = 0;
                double calorii_arse = 0;
                if (lista_mancaruri != null) {
                    for (Food f : lista_mancaruri) {
                        calorii += f.getCalorii();
                    }
                }

                if (lista_exercitii != null) {
                    for (Exercise e : lista_exercitii) {
                        calorii_arse += e.getCaloriiArse() / 8;
                    }
                }

                hashMapCaloriiArse.put("Week 3", hashMapCaloriiArse.get("Week 3") + calorii_arse);
                hashMapCaloriiMancare.put("Week 3", hashMapCaloriiMancare.get("Week 3") + calorii);
                if ((document.getId().compareToIgnoreCase("24-" + month + "-2020") == 0)) {
                    hashMapCaloriiMancare.put("Week 3", hashMapCaloriiMancare.get("Week 3") / 8);
                }
            }
        }
        if ((document.getId().compareToIgnoreCase("24-" + month + "-2020") > 0)) {
            if ((document.getId().compareToIgnoreCase("31-" + month + "-2020") <= 0)) {
                lista_mancaruri = document.toObject(FoodsDocument.class).mancaruri;
                double calorii = 0;
                double calorii_arse = 0;
                if (lista_mancaruri != null) {
                    for (Food f : lista_mancaruri) {
                        calorii += f.getCalorii();
                    }
                }
                if (lista_exercitii != null) {
                    for (Exercise e : lista_exercitii) {
                        calorii_arse += e.getCaloriiArse() / 7;
                    }
                }

                hashMapCaloriiArse.put("Week 4", hashMapCaloriiArse.get("Week 4") + calorii_arse);

                hashMapCaloriiMancare.put("Week 4", hashMapCaloriiMancare.get("Week 4") + calorii);
                if ((document.getId().compareToIgnoreCase("30-" + month + "-2020") == 0)) {
                    hashMapCaloriiMancare.put("Week 4", hashMapCaloriiMancare.get("Week 4") / 7);
                }

            }
        }
    }
}
