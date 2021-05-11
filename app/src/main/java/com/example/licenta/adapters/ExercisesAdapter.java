package com.example.licenta.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licenta.R;
import com.example.licenta.models.Exercise;
import com.example.licenta.models.Food;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class ExercisesAdapter extends ArrayAdapter  {


    private List<Exercise> exercises;
    private final int layoutResource;
    private final LayoutInflater layoutInflater;


    public ExercisesAdapter(@NonNull Context context, int resource , List<Exercise> exercises) {
        super(context, resource);
        this.layoutResource = resource;
        this.layoutInflater = LayoutInflater.from(context);
        this.exercises = exercises;
    }

    @Override
    public int getCount() {
        return exercises.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView= layoutInflater.inflate(layoutResource,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        TextView nume_exercitiu= (TextView) convertView.findViewById(R.id.nume_exercitiu);
        TextView calorii_arse = (TextView) convertView.findViewById(R.id.calorii_arse);
        TextView timp_exercitiu = (TextView)convertView.findViewById(R.id.timp_exercitiu);


        Exercise currentExercise = exercises.get(position);
        viewHolder.nume_exercitiu.setText(currentExercise.getActivitate());
        viewHolder.calorii_arse.setText(String.format("%.0f",currentExercise.getCaloriiArse())+" Calorii Arse");
        viewHolder.timp_exercitiu.setText(String.format("%.0f",currentExercise.getTimp())+ " Minute");



        return convertView;
    }



    private class ViewHolder {
        final TextView nume_exercitiu;
        final TextView calorii_arse;
        final TextView timp_exercitiu;

        ViewHolder(View v) {
            this.nume_exercitiu = (TextView) v.findViewById(R.id.nume_exercitiu);
            this.calorii_arse = (TextView) v.findViewById(R.id.calorii_arse);
            this.timp_exercitiu = (TextView) v.findViewById(R.id.timp_exercitiu);

        }
    }





}

