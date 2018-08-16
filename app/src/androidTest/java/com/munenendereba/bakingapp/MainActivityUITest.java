package com.munenendereba.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.munenendereba.bakingapp.ui.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/*@RunWith(AndroidJUnit4.class)
@RestrictTo(RestrictTo.Scope.TESTS)*/
@RunWith(AndroidJUnit4.class)

public class MainActivityUITest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void TestMainRecipeUI() {
        delayTest();
        //click recipe 2
        onView(withId(R.id.rvRecipeCards)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        delayTest();
        onView(withId(R.id.recyclerViewSteps)).check(matches(hasDescendant(withText("Starting prep"))));
        //check that the detailed recipe step has "Next Step" "Previous Step" buttons
        onView(withId(R.id.recyclerViewSteps)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        delayTest();
        onView(withId(R.id.buttonNext)).check(matches(withText("NEXT STEP")));
        onView(withId(R.id.buttonPrevious)).check(matches(withText("PREVIOUS STEP")));
    }

    @Test
    public void TestClickNextButton() {
        delayTest();
        //click recipe 1
        onView(withId(R.id.rvRecipeCards)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        delayTest();
        onView(withId(R.id.recyclerViewSteps)).check(matches(hasDescendant(withText("Recipe Introduction"))));
        //check that the detailed recipe step has "Next Step" buttons
        onView(withId(R.id.recyclerViewSteps)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        delayTest();
        onView(withId(R.id.buttonNext)).check(matches(withText("NEXT STEP")));

        //click the NEXT button
        onView(withId(R.id.buttonNext)).perform(click());
        delayTest();
        //check that the PREVIOUS button exists
        onView(withId(R.id.buttonPrevious)).check(matches(withText("PREVIOUS STEP")));
    }

    //this method delays the execution time by 5s to enable the screens to load completely
    private void delayTest() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
