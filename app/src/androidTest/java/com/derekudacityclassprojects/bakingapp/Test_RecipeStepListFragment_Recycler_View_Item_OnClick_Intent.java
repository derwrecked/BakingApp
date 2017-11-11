package com.derekudacityclassprojects.bakingapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.matcher.BundleMatchers;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.widget.FrameLayout;

import com.derekudacityclassprojects.bakingapp.FragmentMediaAndInstruction.MediaInstructionFragment;
import com.derekudacityclassprojects.bakingapp.FragmentRecipeList.Recipe;
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
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class Test_RecipeStepListFragment_Recycler_View_Item_OnClick_Intent {
    @Rule
    public IntentsTestRule<RecipeStepListActivity> mActivityRule = new IntentsTestRule<>(
            RecipeStepListActivity.class, false,false);

    @Before
    public void setupBefore() {
        // need to fill recipe id intent send from main activity.
        Intent intentExtra = new Intent();
        intentExtra.putExtra(MainActivity.EXTRA_RECIPE_ID_SELECTION, 1);
        mActivityRule.launchActivity(intentExtra);
        // for catching intent
        Intent intent = new Intent();
        Instrumentation.ActivityResult intentResult = new Instrumentation.ActivityResult(Activity.RESULT_OK, intent);
        intending(hasComponent(RecipeSingleStepDisplayActivity.class.getName())).respondWith(intentResult);
    }

    /**
     * Click spawns intent with recipe id and that name is displayed correctly.
     */
    @Test
    public void verify_recipe_list_fragment_on_click_induces_intent() {
        // get all recipes
        Recipe[] recipes = JSONUtils.getAllRecipes("baking", mActivityRule.getActivity());

        FrameLayout frameLayout = mActivityRule.getActivity().findViewById(R.id.step_media_instruction_fragment_holder);
        // loop through all recipes
        for (int i = 0; i < recipes[0].getSteps().size(); i++) {
            // scroll to position
            onView(withId(R.id.recipe_step_list_recycler_view)).perform(RecyclerViewActions.scrollToPosition(i));
            // click
            onView(withId(R.id.recipe_step_list_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(i, click()));

            // if frame layout does not exist then it will spawn an intent, otherwise it will fill
            // fragment in framelayout
            if (frameLayout == null) {
                // verify recipe id
                intended(allOf(hasComponent(RecipeSingleStepDisplayActivity.class.getName()),
                        hasExtras(
                                allOf(
                                        BundleMatchers.hasEntry(RecipeStepListActivity.EXTRA_RECIPE_ID, recipes[0].getId()),
                                        BundleMatchers.hasEntry(RecipeStepListActivity.EXTRA_RECIPE_STEP_ID, recipes[0].getSteps().get(i).getId())))));
            } else {
                MediaInstructionFragment mediaInstructionFragment = (MediaInstructionFragment) mActivityRule.getActivity().getSupportFragmentManager()
                        .findFragmentByTag(RecipeStepListActivity.MEDIA_INSTRUCTION_FRAGMENT_TAG);
                String mediaruri = recipes[0].getSteps().get(i).getVideoURL();
                if(mediaruri == null){
                    mediaruri = "";
                }
                Uri uri = mediaInstructionFragment.getMediaUrl();
                String storedUri;
                if(uri == null){
                    storedUri = "";
                }else{
                    storedUri = uri.toString();
                }
                assertTrue(mediaruri.equals(storedUri));
                // scroll to position
                onView(withId(R.id.media_instruction_step_description_text_view)).check(matches(withText(recipes[0].getSteps().get(i).getDescription())));
            }

        }
    }
}
