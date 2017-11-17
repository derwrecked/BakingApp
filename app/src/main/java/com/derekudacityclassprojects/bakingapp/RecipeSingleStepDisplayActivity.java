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
    private int recipestepid;
    private int currentStepIndex;
    private RecipeStep recipeStep;
    private List<RecipeStep> recipeStepList;
    private MediaInstructionFragment mediaInstructionFragment;
    public static final String SAVE_STEP_ID = "SAVE_STEP_ID";
    private String FRAGMENT_TAG = "FRAGMENT_TAG_SINGLE";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SAVE_STEP_ID, recipestepid);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_single_step_display);
        int recipeid = getIntent().getIntExtra(RecipeStepListActivity.EXTRA_RECIPE_ID, -1);
        if(savedInstanceState != null){
            recipestepid = savedInstanceState.getInt(SAVE_STEP_ID, -1);
        }else{
            recipestepid = getIntent().getIntExtra(RecipeStepListActivity.EXTRA_RECIPE_STEP_ID, -1);
        }

        if (recipeid == -1 || recipestepid == -1) {
            finish();
        }
        Recipe[] recipes = RecipeRepository.getRecipeList();
        currentStepIndex = 0;
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
                        currentStepIndex++;
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
                currentStepIndex = getNextStep(currentStepIndex, recipeStepList);
            } else {
                currentStepIndex = getPreviousStep(currentStepIndex, recipeStepList);
            }
            recipeStep = recipeStepList.get(currentStepIndex);
            mediaInstructionFragment = createFragment(recipeStep);
            recipestepid = recipeStep.getId();
            // add fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.step_media_instruction_fragment_holder, mediaInstructionFragment);
            fragmentTransaction.commit();
        }
    }

    /**
     * Get next step.
     * @param stepIndex
     * @param recipeStepList
     * @return
     */
    private static int getNextStep(int stepIndex, final List<RecipeStep> recipeStepList) {
        if (recipeStepList.size() == 0
                || stepIndex == (recipeStepList.size() - 1)) {
            return stepIndex;
        } else {
            // increment after calculation
            return ++stepIndex;
        }
    }

    /**
     * Get previous step.
     * @param recipeStepList
     * @param stepIndex
     * @return
     */
    private static int getPreviousStep(int stepIndex, final List<RecipeStep> recipeStepList) {
        if (stepIndex == 0) {
            return stepIndex;
        } else {
            // increment after calculation
            return --stepIndex;
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
