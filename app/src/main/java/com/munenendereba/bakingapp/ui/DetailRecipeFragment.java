package com.munenendereba.bakingapp.ui;


import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.munenendereba.bakingapp.BakingAppWidget;
import com.munenendereba.bakingapp.R;
import com.munenendereba.bakingapp.adapters.IngredientsRecyclerViewAdapter;
import com.munenendereba.bakingapp.adapters.StepsRecyclerViewAdapter;
import com.munenendereba.bakingapp.models.Recipe;
import com.munenendereba.bakingapp.models.RecipeIngredient;
import com.munenendereba.bakingapp.models.RecipeStep;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailRecipeFragment extends Fragment implements StepsRecyclerViewAdapter.ItemClickListener {
    RecyclerView recyclerViewIngredients, recyclerViewSteps;
    IngredientsRecyclerViewAdapter ingredientsRecyclerViewAdapter;
    StepsRecyclerViewAdapter stepsRecyclerViewAdapter;
    SharedPreferences sharedPreferences;
    Recipe recipe;
    int savedId;
    private OnRecipeStepInteractionListener mListener;

    public DetailRecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); //register that fragment has menu
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sharedPreferences = getContext().getSharedPreferences("com.munenendereba.bakingapp", getContext().MODE_PRIVATE);
        try {
            savedId = sharedPreferences.getInt("favoriteRecipeId", 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        recipe = getArguments().getParcelable("recipe");//get the parceled bundle

        return inflater.inflate(R.layout.fragment_detail_recipe, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerViewIngredients = getView().findViewById(R.id.recyclerViewIngredients);
        recyclerViewIngredients.setLayoutManager(new LinearLayoutManager(recyclerViewIngredients.getContext()));
        recyclerViewSteps = getView().findViewById(R.id.recyclerViewSteps);
        recyclerViewSteps.setLayoutManager(new LinearLayoutManager(recyclerViewSteps.getContext()));

        ingredientsRecyclerViewAdapter = new IngredientsRecyclerViewAdapter(getContext(), recipe.getRecipeIngredient());
        recyclerViewIngredients.setAdapter(ingredientsRecyclerViewAdapter);
        stepsRecyclerViewAdapter = new StepsRecyclerViewAdapter(getContext(), recipe.getRecipeStep());
        stepsRecyclerViewAdapter.setClickListener(this);
        recyclerViewSteps.setAdapter(stepsRecyclerViewAdapter);
        getActivity().setTitle("How to make " + recipe.getRecipeName() + " for " + recipe.getServesNumPeople());
    }

    @Override
    public void onItemClick(View view, int position, ArrayList<RecipeStep> recipeSteps) {
        if (mListener != null)
            mListener.onRecipeStepInteraction(recipeSteps, position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRecipeStepInteractionListener) {
            mListener = (OnRecipeStepInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRecipeStepInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.update_shared_pref_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem item = menu.findItem(R.id.menu_item_add_pref);
        //if the saved preference is the currently viewed recipe, show the icon as the colored star
        if (savedId == recipe.getRecipeId())
            item.setIcon(R.drawable.star);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_item_add_pref:
                SharedPreferences.Editor editor = sharedPreferences.edit();
                savedId = sharedPreferences.getInt("favoriteRecipeId", 0);
                if (savedId != recipe.getRecipeId()) {

                    String recipeIngredients = getRecipeIngredients();
                    String recipeName = "Ingredients to make " + recipe.getRecipeName();

                    editor.putInt("favoriteRecipeId", recipe.getRecipeId());
                    editor.putString("favoriteRecipeName", recipeName);
                    editor.putString("favoriteRecipeIngredient", recipeIngredients);

                    editor.apply();
                    updateWidget(recipeName, recipeIngredients); //call the update widget method

                    item.setIcon(R.drawable.star); //update the icon to show that the item is the current favorite

                    Toast.makeText(getActivity(), "Favorite Recipe Updated to " + recipe.getRecipeName(), Toast.LENGTH_SHORT).show();
                } else {
                    editor.putInt("favoriteRecipeId", 0);
                    editor.putString("favoriteRecipeName", "No Favorite Recipe");
                    editor.putString("favoriteRecipeIngredient", "No Ingredients");

                    editor.apply();
                    updateWidget("No Favorite Recipe", "No Ingredients"); //call the update widget method

                    item.setIcon(R.drawable.star_grey); //update the icon to show that the item is the current favorite

                    Toast.makeText(getActivity(), "Recipe Removed from Favorites ", Toast.LENGTH_SHORT).show();
                }
                return true;
            case android.R.id.home:
                //source: https://developer.android.com/training/implementing-navigation/ancestral#java
                Intent upIntent = NavUtils.getParentActivityIntent(getActivity());
                if (NavUtils.shouldUpRecreateTask(getActivity(), upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(getContext())
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                            // Navigate up to the closest parent
                            .startActivities();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(getActivity(), upIntent);
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //update the widget with the ingredients and recipe
    private void updateWidget(String recipeName, String ingredients) {
        Context context = getContext();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        ComponentName thisWidget = new ComponentName(context, BakingAppWidget.class);
        remoteViews.setTextViewText(R.id.recipeIngredients_text, ingredients);
        remoteViews.setTextViewText(R.id.recipeName_text, recipeName);

        appWidgetManager.updateAppWidget(thisWidget, remoteViews);
    }

    //this method constructs the recipe ingredients to be sent to the widget
    private String getRecipeIngredients() {
        StringBuilder l = new StringBuilder();

        ArrayList<RecipeIngredient> r = recipe.getRecipeIngredient();
        String measure;
        for (int jj = 0; jj < r.size(); jj++) {
            RecipeIngredient lll = r.get(jj);
            measure = lll.getMeasure();
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
            l.append(lll.getQuantity() + measure + lll.getIngredientName() + "\n");
        }
        //l.add()
        return l.toString();
    }

    //interface for handling click interactions
    public interface OnRecipeStepInteractionListener {
        void onRecipeStepInteraction(ArrayList<RecipeStep> recipeSteps, int position);
    }
}
