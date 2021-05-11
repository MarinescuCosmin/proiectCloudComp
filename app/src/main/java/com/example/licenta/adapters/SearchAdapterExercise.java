package com.example.licenta.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licenta.MainActivity;
import com.example.licenta.R;
import com.example.licenta.models.Exercise;

import java.util.ArrayList;

public class SearchAdapterExercise  extends RecyclerView.Adapter<SearchAdapterExercise.SearchViewHolder> {

    Context context;
    ArrayList<Exercise> lista_search_exercitii;
    public OnExerciseClickListener onExerciseClickListener;

    public
    class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nume_exercitiu, numar_calorii;
        Button button_add_exercise;
        SearchAdapterExercise.OnExerciseClickListener onExerciseClickListener;

        public SearchViewHolder(@NonNull View itemView, SearchAdapterExercise.OnExerciseClickListener onExerciseClickListener) {
            super(itemView);
            button_add_exercise = itemView.findViewById(R.id.add_button_exercise);
            this.button_add_exercise.setOnClickListener(this);
            nume_exercitiu = itemView.findViewById(R.id.tVAdapterExerciseNume);
            numar_calorii = itemView.findViewById(R.id.tVAdapterExerciseCalorii);

            this.onExerciseClickListener = onExerciseClickListener;

        }

        @Override
        public void onClick(View v) {
            onExerciseClickListener.onExerciseItemClick(getAdapterPosition());
        }
    }

    public SearchAdapterExercise(Context context, ArrayList<Exercise> lista_search_exercitii, SearchAdapterExercise.OnExerciseClickListener onExerciseClickListener) {
        this.context = context;
        this.onExerciseClickListener = onExerciseClickListener;
        this.lista_search_exercitii = lista_search_exercitii;
    }

    @NonNull
    @Override
    public SearchAdapterExercise.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_exercise_item, parent, false);

        return new SearchAdapterExercise.SearchViewHolder(view, onExerciseClickListener);
    }


    //    @Override
//    public SearchAdapterFood.SearchViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.search_food_items,parent,false);
//
//        return new SearchViewHolder(view);
//    }


    @Override
    public void onBindViewHolder(@NonNull SearchAdapterExercise.SearchViewHolder holder, int position) {

        holder.nume_exercitiu.setText(lista_search_exercitii.get(position).getActivitate());
        holder.numar_calorii.setText(String.format("%.0f",lista_search_exercitii.get(position).getMET()* MainActivity.currentUserDetails.getWeight()) + " Calorii Arse / 60 min");

    }


    @Override
    public int getItemCount() {
        return lista_search_exercitii.size();
    }


    public interface OnExerciseClickListener {
        void onExerciseItemClick(int position);
    }
}
