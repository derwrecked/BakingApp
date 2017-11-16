package com.derekudacityclassprojects.bakingapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.derekudacityclassprojects.bakingapp.FragmentMediaAndInstruction.MediaInstructionFragment;
import com.derekudacityclassprojects.bakingapp.FragmentRecipeList.Recipe;
import com.derekudacityclassprojects.bakingapp.FragmentRecipeList.RecipeStep;

import java.util.List;

public class RecipeSingleStepDisplayActivity extends AppCompatActivity implements MediaInstructionFragment.OnFragmentInteractionListener {
    private Recipe recipe;
    private RecipeStep recipeStep;
    private List<RecipeStep> recipeStepList;
    private MediaInstructionFragment mediaInstructionFragment;
    private String FRAGMENT_TAG = "FRAGMENT_TAG_SINGLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_single_step_display);
        int recipeid = getIntent().getIntExtra(RecipeStepListActivity.EXTRA_RECIPE_ID, -1);
        int recipestepid = getIntent().getIntExtra(RecipeStepListActivity.EXTRA_RECIPE_STEP_ID, -1);
        if (recipeid == -1 || recipestepid == -1) {
            finish();
        }
        Recipe[] recipes = JSONUtils.getAllRecipesFromAsset(JSONUtils.BAKING_JSON_FILE_NAME, getApplicationContext());
        for (Recipe recipe : recipes) {
            if (recipe.getId() == recipeid) {
                this.recipe = recipe;
                recipeStepList = recipe.getSteps();
                if (recipeStepList != null) {
                    for (RecipeStep step : recipeStepList) {
                        if (step.getId() == recipestepid) {
                            this.recipeStep = step;
                            break;
                        }
                    }
                }
                break;
            }
        }

        // has the same fragment already replaced the container and assumed its id?
        MediaInstructionFragment existingFragment =
                (MediaInstructionFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        if (existingFragment != null)
        {
            mediaInstructionFragment = existingFragment;
        }else{
            mediaInstructionFragment = createFragment(recipeStep);
            // add fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.step_media_instruction_fragment_holder, mediaInstructionFragment, FRAGMENT_TAG);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onFragmentInteraction(boolean isNext) {
        if((isNext &&  (recipeStep.getId() == recipeStepList.get(recipeStepList.size() - 1).getId()))
                || (!isNext && recipeStep.getId() == 0)) {
            // do nothing
        }else{
            if(mediaInstructionFragment != null){
                mediaInstructionFragment.releasePlayer();
            }
            if (isNext) {
                recipeStep = getNextStep(recipeStep, recipeStepList);
            } else {
                recipeStep = getPreviousStep(recipeStep, recipeStepList);
            }
            mediaInstructionFragment = createFragment(recipeStep);
            // add fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.step_media_instruction_fragment_holder, mediaInstructionFragment);
            fragmentTransaction.commit();
        }
    }

    /**
     * Get next step.
     * @param currentStep
     * @param recipeStepList
     * @return
     */
    private static RecipeStep getNextStep(final RecipeStep currentStep, final List<RecipeStep> recipeStepList) {
        if (recipeStepList.size() == 0
                || currentStep.getId() == recipeStepList.get(recipeStepList.size() - 1).getId()) {
            return currentStep;
        } else {
            return recipeStepList.get(currentStep.getId() + 1);
        }
    }

    /**
     * Get previous step.
     * @param currentStep
     * @param recipeStepList
     * @return
     */
    private static RecipeStep getPreviousStep(final RecipeStep currentStep, final List<RecipeStep> recipeStepList) {
        if (currentStep.getId() == 0) {
            return currentStep;
        } else {
            return recipeStepList.get(currentStep.getId() - 1);
        }
    }

    /**
     * Create fragment with media and step info.
     * @param recipeStep
     * @return
     */
    private MediaInstructionFragment createFragment(RecipeStep recipeStep) {
        MediaInstructionFragment mediaInstructionFragment = new MediaInstructionFragment();
        // DisplayFragment (Fragment B) is in the layout (tablet layout),
        // so tell the fragment to update
        mediaInstructionFragment.setInstruction(recipeStep.getDescription());
        if (recipeStep.getVideoURL() == null
                || recipeStep.getVideoURL().isEmpty()
                || ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            mediaInstructionFragment.setVideoUri(null);
        } else {
            mediaInstructionFragment.setVideoUri(Uri.parse(recipeStep.getVideoURL()));
        }
        return mediaInstructionFragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
