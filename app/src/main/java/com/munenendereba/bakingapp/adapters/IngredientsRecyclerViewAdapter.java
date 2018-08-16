package com.munenendereba.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.munenendereba.bakingapp.R;
import com.munenendereba.bakingapp.models.RecipeIngredient;

import java.util.ArrayList;

public class IngredientsRecyclerViewAdapter extends RecyclerView.Adapter<IngredientsRecyclerViewAdapter.ViewHolder> {
    private ArrayList<RecipeIngredient> recipeData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public IngredientsRecyclerViewAdapter(Context context, ArrayList<RecipeIngredient> data) {
        this.mInflater = LayoutInflater.from(context);
        this.recipeData = data;
    }

    @Override
    public IngredientsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_view_ingredients, parent, false);
        return new ViewHolder(view);
    }

    //binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Log.d("WHERE IS DATA:", movieData.get(position) + "");
        RecipeIngredient r = recipeData.get(position);
        String measure = r.getMeasure();
        switch (measure) {
            case "TBLSP":
                measure = " tablespoon of ";
                break;
            case "TSP":
                measure = " teaspoon of ";
                break;
            case "OZ":
                measure = " ounces of ";
                break;
            case "UNIT":
                measure = " ";
                break;
            case "G":
                measure = "g of ";
                break;
            case "K":
                measure = "kg of ";
                break;
            case "CUP":
                measure = " cups of ";
                break;
        }

        holder.textViewIngredients.setText(r.getQuantity() + measure + r.getIngredientName());
    }

    //get total number of cells
    @Override
    public int getItemCount() {
        return recipeData.size();
    }

    //allows click events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    //parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position, ArrayList<RecipeIngredient> recipes);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewIngredients;

        ViewHolder(View itemView) {
            super(itemView);
            textViewIngredients = itemView.findViewById(R.id.textViewIngredients);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.onItemClick(view, getAdapterPosition(), recipeData);
                //Log.d("ITEM-CLICKED POSITION:", getAdapterPosition() + "");
            } else
                Log.d("CLICK-PROBLEM", "NO CLICK DETECTED");
        }
    }
}
