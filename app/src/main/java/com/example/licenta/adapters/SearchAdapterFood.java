package com.example.licenta.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licenta.R;
import com.example.licenta.models.Food;

import java.util.ArrayList;

public class SearchAdapterFood extends RecyclerView.Adapter<SearchAdapterFood.SearchViewHolder> {
    Context context;
    ArrayList<Food> lista_search_mancare;
    public OnFoodClickListener onFoodClickListener;

    public
    class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nume_mancare, numar_calorii, numar_proteine, numar_lipide, numar_carbohidrati;
        Button button_add_food;
        OnFoodClickListener onFoodClickListener;

        public SearchViewHolder(@NonNull View itemView, OnFoodClickListener onFoodClickListener) {
            super(itemView);
            button_add_food = itemView.findViewById(R.id.add_button_food);
            this.button_add_food.setOnClickListener(this);
            nume_mancare = itemView.findViewById(R.id.tVAdapterFoodNume);
            numar_calorii = itemView.findViewById(R.id.tVAdapterFoodCalorii);
            numar_proteine = itemView.findViewById(R.id.tVAdapterFoodProteine);
            numar_lipide = itemView.findViewById(R.id.tVAdapterFoodLipide);
            numar_carbohidrati = itemView.findViewById(R.id.tVAdapterFoodCarbohidrati);
            this.onFoodClickListener = onFoodClickListener;

        }

        @Override
        public void onClick(View v) {
            onFoodClickListener.onFoodItemClick(getAdapterPosition());
        }
    }

    public SearchAdapterFood(Context context, ArrayList<Food> lista_search_mancare, OnFoodClickListener onFoodClickListener) {
        this.context = context;
        this.onFoodClickListener = onFoodClickListener;
        this.lista_search_mancare = lista_search_mancare;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_food_items, parent, false);

        return new SearchViewHolder(view, onFoodClickListener);
    }


    //    @Override
//    public SearchAdapterFood.SearchViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.search_food_items,parent,false);
//
//        return new SearchViewHolder(view);
//    }


    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {

        holder.nume_mancare.setText(lista_search_mancare.get(position).getAliment());
        holder.numar_calorii.setText(lista_search_mancare.get(position).getCalorii() + " Calorii");
        holder.numar_proteine.setText(lista_search_mancare.get(position).getProteine() + " Proteine");
        holder.numar_carbohidrati.setText(lista_search_mancare.get(position).getCarbohidrati()+ " Carbohidrati");
        holder.numar_lipide.setText(lista_search_mancare.get(position).getLipide() + " Lipide");
    }


    @Override
    public int getItemCount() {
        return lista_search_mancare.size();
    }


    public interface OnFoodClickListener {
        void onFoodItemClick(int position);
    }
}
