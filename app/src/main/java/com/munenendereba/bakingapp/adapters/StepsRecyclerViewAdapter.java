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
import com.munenendereba.bakingapp.models.RecipeStep;

import java.util.ArrayList;

public class StepsRecyclerViewAdapter extends RecyclerView.Adapter<StepsRecyclerViewAdapter.ViewHolder> {
    private ArrayList<RecipeStep> recipeData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public StepsRecyclerViewAdapter(Context context, ArrayList<RecipeStep> data) {
        this.mInflater = LayoutInflater.from(context);
        this.recipeData = data;
    }

    @Override
    public StepsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_view_steps, parent, false);
        return new ViewHolder(view);
    }

    //binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Log.d("WHERE IS DATA:", movieData.get(position) + "");
        holder.textViewSteps.setText(recipeData.get(position).getShortDescription());
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
        void onItemClick(View view, int position, ArrayList<RecipeStep> recipes);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewSteps;

        ViewHolder(View itemView) {
            super(itemView);
            textViewSteps = itemView.findViewById(R.id.textViewSteps);
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
