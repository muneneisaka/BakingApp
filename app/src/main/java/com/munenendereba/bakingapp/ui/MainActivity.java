package com.munenendereba.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.munenendereba.bakingapp.R;
import com.munenendereba.bakingapp.models.Recipe;

public class MainActivity extends AppCompatActivity implements RecipeCardsFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check for internet connection
        if (checkInternetConnection()) {
            //create fragments only when no fragments exist
            if (savedInstanceState == null) {
                RecipeCardsFragment recipeCardsFragment = new RecipeCardsFragment(); //initialize fragment
                FragmentManager fragmentManager = getSupportFragmentManager(); //android v4 fm
                fragmentManager.beginTransaction()
                        .add(R.id.mainFrameLayout, recipeCardsFragment)
                        .commit();
            }
        } else {
            InternetErrorFragment internetErrorFragment = new InternetErrorFragment(); //initialize fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.mainFrameLayout, internetErrorFragment)
                    .commit();
        }
    }

    //method to check for internet connection and present appropriate UI
    public boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }

    //implement the callback
    @Override
    public void onFragmentInteraction(Recipe recipe) {
        Intent intent = new Intent(this, DetailRecipeActivity.class);
        intent.putExtra("recipe", recipe);

        startActivity(intent);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
