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
import com.munenendereba.bakingapp.models.Recipe;

import java.util.ArrayList;

public class RecipeRecyclerViewAdapter extends RecyclerView.Adapter<RecipeRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Recipe> recipeData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public RecipeRecyclerViewAdapter(Context context, ArrayList<Recipe> data) {
        this.mInflater = LayoutInflater.from(context);
        this.recipeData = data;
    }

    @Override
    public RecipeRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_view_recipe_cards, parent, false);
        return new ViewHolder(view);
    }

    //binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Log.d("WHERE IS DATA:", movieData.get(position) + "");
        holder.textViewRecipes.setText(recipeData.get(position).getRecipeName());
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
        void onItemClick(View view, int position, ArrayList<Recipe> recipes);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewRecipes;

        ViewHolder(View itemView) {
            super(itemView);
            textViewRecipes = itemView.findViewById(R.id.txtRecipeCard);
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
