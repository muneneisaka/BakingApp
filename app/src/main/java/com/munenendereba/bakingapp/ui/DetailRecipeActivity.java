package com.munenendereba.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.munenendereba.bakingapp.R;
import com.munenendereba.bakingapp.models.Recipe;
import com.munenendereba.bakingapp.models.RecipeStep;

import java.util.ArrayList;

public class DetailRecipeActivity extends AppCompatActivity implements DetailRecipeFragment.OnRecipeStepInteractionListener, DetailRecipeStepFragment.OnDetailRecipeStepInteractionListener {
    boolean isThisATablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recipe);

        Intent intent = getIntent();
        Recipe recipe = intent.getExtras().getParcelable("recipe"); //get the parceled recipe

        //display different layout for tablet vs for phone
        isThisATablet = getResources().getBoolean(R.bool.isTablet);
        Bundle b = new Bundle();
        b.putParcelable("recipe", recipe); //add the parcelable to a bundle and transmit to the fragment

        if (isThisATablet) {
            if (savedInstanceState == null) {
                DetailRecipeFragment detailRecipeFragment = new DetailRecipeFragment(); //initialize fragment
                detailRecipeFragment.setArguments(b);//transmit the data to the fragment

                FragmentManager fragmentManager = getSupportFragmentManager(); //android v4 fm
                fragmentManager.beginTransaction()
                        .add(R.id.leftDetailFrameLayout, detailRecipeFragment)
                        .commit();
            }
        }
        //if it's phone
        else {
            if (savedInstanceState == null) {
                DetailRecipeFragment detailRecipeFragment = new DetailRecipeFragment(); //initialize fragment
                detailRecipeFragment.setArguments(b);//transmit the data to the fragment

                FragmentManager fragmentManager = getSupportFragmentManager(); //android v4 fm
                fragmentManager.beginTransaction()
                        .add(R.id.detailFrameLayout, detailRecipeFragment)
                        .commit();
            }
        }
    }

    @Override
    public void onRecipeStepInteraction(ArrayList<RecipeStep> recipeSteps, int position) {
        if (isThisATablet) {
            Bundle b = new Bundle();
            b.putParcelable("recipeStep", recipeSteps.get(position)); //add the parcelable to a bundle and transmit to the fragment

            DetailRecipeStepFragment detailRecipeFragment = new DetailRecipeStepFragment(); //initialize fragment
            detailRecipeFragment.setArguments(b);//transmit the data to the fragment

            FragmentManager fragmentManager = getSupportFragmentManager(); //android v4 fm
            fragmentManager.beginTransaction()
                    .add(R.id.rightDetailFrameLayout, detailRecipeFragment)
                    .commit();

        } else {
            Intent intent = new Intent(this, DetailRecipeStepActivity.class);
            intent.putExtra("recipeSteps", recipeSteps);
            intent.putExtra("positionOfStep", position);

            startActivity(intent);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onFragmentInteraction(int nextPosition) {

    }
}
