package com.derekudacityclassprojects.bakingapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.matcher.BundleMatchers;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.derekudacityclassprojects.bakingapp.FragmentRecipeList.Recipe;
import com.derekudacityclassprojects.bakingapp.TestingTools.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtras;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class Test_RecipeListFragment_Recycler_View_Item_OnClick_Intent {
    @Rule
    public IntentsTestRule<MainActivity> mActivityRule = new IntentsTestRule<>(
            MainActivity.class);
    public IdlingResource mIdlingResource;
    @Before
    public void setupBefore(){
        // catch intent so it does not open Device control activity
        Intent intent = new Intent();
        Instrumentation.ActivityResult intentResult = new Instrumentation.ActivityResult(Activity.RESULT_OK,intent);
        intending(hasComponent(RecipeStepListActivity.class.getName())).respondWith(intentResult);

        mIdlingResource = mActivityRule.getActivity().setIdlingResource();
        // To prove that the test fails, omit this call:
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    /**
     * Click spawns intent with recipe id and that name is displayed correctly.
     */
    @Test
    public void verify_recipe_list_fragment_on_click_induces_intent() {
        // get all recipes
        Recipe[] recipes = JSONUtils.getAllRecipesFromAsset("baking_test", mActivityRule.getActivity());

        // loop through all recipes
        for(int i = 0; i < recipes.length; i++){
            onView(withId(R.id.recipe_list_fragment_recycler_view)).check(matches(isDisplayed()));
            // scroll to position
            onView(withId(R.id.recipe_list_fragment_recycler_view)).perform(RecyclerViewActions.scrollToPosition(i));

            // check to make sure name is displayed correctly
            onView(TestUtils.withRecyclerView(R.id.recipe_list_fragment_recycler_view)
                    .atPositionOnView(i, R.id.recipe_name))
                    .check(matches(withText(recipes[i].getName())));
            // click
            onView(withId(R.id.recipe_list_fragment_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(i, click()));

            //
            intended(allOf(hasComponent(RecipeStepListActivity.class.getName()),
                    hasExtras(BundleMatchers.hasEntry(MainActivity.EXTRA_RECIPE_ID_SELECTION, recipes[i].getId()))));
        }
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
}
