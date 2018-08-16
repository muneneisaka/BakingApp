package com.munenendereba.bakingapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.munenendereba.bakingapp.R;
import com.munenendereba.bakingapp.adapters.RecipeRecyclerViewAdapter;
import com.munenendereba.bakingapp.models.Recipe;
import com.munenendereba.bakingapp.models.RecipeIngredient;
import com.munenendereba.bakingapp.models.RecipeStep;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecipeCardsFragment extends Fragment implements RecipeRecyclerViewAdapter.ItemClickListener {

    RecyclerView recyclerViewRecipeCards;
    RecipeRecyclerViewAdapter recipeRecyclerViewAdapter;
    //MainActivity mainActivity = new MainActivity();
    private OnFragmentInteractionListener mListener;


    public RecipeCardsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_cards, container, false);

        recyclerViewRecipeCards = view.findViewById(R.id.rvRecipeCards);
        getData();

        recyclerViewRecipeCards.setLayoutManager(new LinearLayoutManager(recyclerViewRecipeCards.getContext()));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerViewRecipeCards = getView().findViewById(R.id.rvRecipeCards);
        getData();
        //recipeRecyclerViewAdapter = new RecipeRecyclerViewAdapter(getContext());
        //recipeRecyclerViewAdapter.setClickListener(this);
        //recyclerViewRecipeCards.setAdapter(recipeRecyclerViewAdapter);
        recyclerViewRecipeCards.setLayoutManager(new LinearLayoutManager(recyclerViewRecipeCards.getContext()));
    }

    private void getData() {
        String url = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        ArrayList<String> r = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                processRecipeData(response);
                //Log.d("DATA-HERE", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR-GETTING-DATA", "Error " + error.getMessage());
            }

        });

        requestQueue.add(stringRequest);
    }

    private void processRecipeData(String response) {
        ArrayList<Recipe> recipes = new ArrayList<>();// = new Recipe();
        //ArrayList<String> x = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(response);
            int num = jsonArray.length();
            JSONObject jsonObject;// = new JSONObject();
            for (int l = 0; l < num; l++) {
                jsonObject = jsonArray.getJSONObject(l);
                int recipeId = jsonObject.getInt("id");
                String recipeName = jsonObject.getString("name");
                int servesNumPeople = jsonObject.getInt("servings");
                //x.add(recipeName);

                //get ingredients
                ArrayList<RecipeIngredient> recipeIngredients = new ArrayList<>();
                try {
                    JSONArray arrayIngredients = jsonObject.getJSONArray("ingredients");
                    int numIngredients = arrayIngredients.length();
                    JSONObject objectIngredient;
                    for (int ii = 0; ii < numIngredients; ii++) {
                        objectIngredient = arrayIngredients.getJSONObject(ii);
                        String ingredientName = objectIngredient.getString("ingredient");
                        String measure = objectIngredient.getString("measure");
                        String quantity = objectIngredient.getString("quantity");
                        recipeIngredients.add(new RecipeIngredient(ingredientName, measure, quantity));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //end get ingredients


                //get recipe step
                ArrayList<RecipeStep> recipeSteps = new ArrayList<>();
                try {
                    JSONArray arraySteps = jsonObject.getJSONArray("steps");
                    int numSteps = arraySteps.length();
                    JSONObject objectStep;
                    for (int kk = 0; kk < numSteps; kk++) {
                        objectStep = arraySteps.getJSONObject(kk);
                        int stepId = objectStep.getInt("id");
                        String shortDescription = objectStep.getString("shortDescription");
                        String description = objectStep.getString("description");
                        String videoURL = objectStep.getString("videoURL");
                        String thumbnailURL = objectStep.getString("thumbnailURL");
                        recipeSteps.add(new RecipeStep(stepId, shortDescription, description, videoURL, thumbnailURL));
                    }

                    //recipeStep = new
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //end get recipe step

                //Log.d("RECIPE-NAME", nameOfRecipe);
                recipes.add(new Recipe(recipeId, recipeName, servesNumPeople, recipeIngredients, recipeSteps));
            }

            recipeRecyclerViewAdapter = new RecipeRecyclerViewAdapter(getContext(), recipes);
            recipeRecyclerViewAdapter.setClickListener(this);
            recyclerViewRecipeCards.setAdapter(recipeRecyclerViewAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(View view, int position, ArrayList<Recipe> recipes) {
        if (mListener != null)
            mListener.onFragmentInteraction(recipes.get(position));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //interface for handling click interactions
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Recipe recipe);
    }
}
