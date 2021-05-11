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

import com.example.licenta.R;
import com.example.licenta.models.Food;

import java.util.List;
import java.util.concurrent.TimeoutException;

public class FoodsAdapter extends ArrayAdapter {

    private final int layoutResource;
    private final LayoutInflater layoutInflater;
    private List<Food> foods;

    public FoodsAdapter(@NonNull Context context, int resource , List<Food> foods) {
        super(context, resource);
        this.layoutResource = resource;
        this.layoutInflater = LayoutInflater.from(context);
        this.foods = foods;
    }

    @Override
    public int getCount() {
        return foods.size();
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

        TextView nume_aliment= (TextView) convertView.findViewById(R.id.nume_aliment);
        TextView calorii_aliment = (TextView) convertView.findViewById(R.id.calorii_aliment);
        TextView proteine_aliment = (TextView)convertView.findViewById(R.id.proteine_aliment);
        TextView grasimi_aliment = (TextView) convertView.findViewById(R.id.grasimi_aliment);
        TextView carbohidrati_aliment = (TextView) convertView.findViewById(R.id.carbohidrati_aliment);

        Food currentFood = foods.get(position);
        viewHolder.nume_aliment.setText(currentFood.getAliment());
        viewHolder.calorii_aliment.setText(String.format("%.0f",currentFood.getCalorii())+"Calorii");
        viewHolder.proteine_aliment.setText(String.format("%.0f",currentFood.getProteine())+ " Proteine");
        viewHolder.grasimi_aliment.setText(String.format("%.0f",currentFood.getLipide())+" Grasimi");
        viewHolder.carbohidrati_aliment.setText(String.format("%.0f",currentFood.getCarbohidrati())+" Carbohidrati");


        return convertView;
    }



    private class ViewHolder {
        final TextView nume_aliment;
        final TextView calorii_aliment;
        final TextView proteine_aliment;
        final TextView grasimi_aliment;
        final TextView carbohidrati_aliment;

        ViewHolder(View v) {
            this.nume_aliment = (TextView) v.findViewById(R.id.nume_aliment);
            this.calorii_aliment = (TextView) v.findViewById(R.id.calorii_aliment);
            this.proteine_aliment = (TextView) v.findViewById(R.id.proteine_aliment);
            this.grasimi_aliment=(TextView) v.findViewById(R.id.grasimi_aliment);
            this.carbohidrati_aliment=(TextView) v.findViewById(R.id.carbohidrati_aliment);
        }
    }
}
