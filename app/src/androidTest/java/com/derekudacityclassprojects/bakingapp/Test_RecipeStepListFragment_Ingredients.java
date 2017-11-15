package com.derekudacityclassprojects.bakingapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.matcher.BundleMatchers;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.derekudacityclassprojects.bakingapp.FragmentRecipeList.Ingredient;
import com.derekudacityclassprojects.bakingapp.FragmentRecipeList.Recipe;
import com.derekudacityclassprojects.bakingapp.FragmentRecipeStepList.RecipeStepListFragment;
import com.derekudacityclassprojects.bakingapp.TestingTools.TestUtils;

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
public class Test_RecipeStepListFragment_Ingredients {

    @Rule
    public IntentsTestRule<MainActivity> ActivityTestRule = new IntentsTestRule<>(
            MainActivity.class);

    /**
     * Verify the ingredients button launches IngredientsActivity with extra recipe data.
     */
    @Test
    public void verify_ingredients_button_launches_intent_ingredients() {
        // get all recipes
        Recipe[] recipes = JSONUtils.getAllRecipes("baking_test", ActivityTestRule.getActivity());

        // loop through all recipes
        for(int i = 0; i < recipes.length; i++){
            // scroll to position
            onView(withId(R.id.recipeListFragment)).perform(RecyclerViewActions.scrollToPosition(i));
            // click
            onView(withId(R.id.recipeListFragment)).perform(RecyclerViewActions.actionOnItemAtPosition(i, click()));

            // find ingredients button
            onView(withId(R.id.recipe_step_list_ingredients_button)).check(matches(isDisplayed()));
            // click
            onView(withId(R.id.recipe_step_list_ingredients_button)).perform(click());
            // check intent was sent
            intended(allOf(hasComponent(IngredientsActivity.class.getName()),
                    hasExtras(BundleMatchers.hasEntry(RecipeStepListFragment.EXTRA_RECIPE_ID, recipes[i].getId()))));
            String allIngredients = "";
            // Check ingredients are displayed
            for(Ingredient item : recipes[i].getIngredients()){
                allIngredients += item.getQuantity() + " " + item.getMeasure() + " - " + item.getIngredient() + "\n\n";
            }
            // verify ingredients are displayed correctly
            onView(withId(R.id.ingredients_activity_text_view)).check(matches(withText(allIngredients)));
            Espresso.pressBack();
            Espresso.pressBack();
        }
    }
}
