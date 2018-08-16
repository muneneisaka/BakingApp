package com.munenendereba.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.munenendereba.bakingapp.R;
import com.munenendereba.bakingapp.models.RecipeStep;

import java.util.ArrayList;

public class DetailRecipeStepActivity extends AppCompatActivity implements DetailRecipeStepFragment.OnDetailRecipeStepInteractionListener {
    private int positionOfStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recipe_step);

        Intent intent = getIntent();
        ArrayList<RecipeStep> recipeSteps = intent.getExtras().getParcelableArrayList("recipeSteps"); //get the parceled recipeSteps
        positionOfStep = intent.getIntExtra("positionOfStep", 0);

        Bundle b = new Bundle();
        b.putParcelableArrayList("recipeSteps", recipeSteps); //add the parcelable to a bundle and transmit to the fragment
        b.putInt("positionOfStep", positionOfStep);

        if (savedInstanceState == null) {
            DetailRecipeStepFragment detailRecipeStepFragment = new DetailRecipeStepFragment(); //initialize fragment
            detailRecipeStepFragment.setArguments(b);//transmit the data to the fragment

            FragmentManager fragmentManager = getSupportFragmentManager(); //android v4 fm
            fragmentManager.beginTransaction()
                    .add(R.id.detailRecipeStepFrameLayout, detailRecipeStepFragment)
                    .commit();
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    //if the next button is pressed, replace the screen with next step
    @Override
    public void onFragmentInteraction(int nextPosition) {
        Intent intent = getIntent();
        ArrayList<RecipeStep> recipeSteps = intent.getExtras().getParcelableArrayList("recipeSteps");

        DetailRecipeStepFragment detailRecipeStepFragment = new DetailRecipeStepFragment(); //initialize fragment
        Bundle b = new Bundle();
        b.putParcelableArrayList("recipeSteps", recipeSteps); //add the parcelable to a bundle and transmit to the fragment
        b.putInt("positionOfStep", nextPosition);
        detailRecipeStepFragment.setArguments(b);//transmit the data to the fragment

        FragmentManager fragmentManager = getSupportFragmentManager(); //android v4 fm
        fragmentManager.beginTransaction()
                .replace(R.id.detailRecipeStepFrameLayout, detailRecipeStepFragment)
                .commit();

    }
}
