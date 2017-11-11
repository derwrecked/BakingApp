package com.derekudacityclassprojects.bakingapp;

import android.Manifest;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.derekudacityclassprojects.bakingapp.FragmentMediaAndInstruction.MediaInstructionFragment;
import com.derekudacityclassprojects.bakingapp.FragmentRecipeList.Recipe;
import com.derekudacityclassprojects.bakingapp.FragmentRecipeList.RecipeStep;
import com.derekudacityclassprojects.bakingapp.FragmentRecipeStepList.RecipeStepListFragment;
import com.derekudacityclassprojects.bakingapp.RecipeWidget.RecipeWidgetProvider;

import java.util.List;

public class RecipeStepListActivity extends AppCompatActivity implements RecipeStepListFragment.OnListFragmentInteractionListener,
        MediaInstructionFragment.OnFragmentInteractionListener {
    public static final String EXTRA_RECIPE_ID = "recipeid";
    public static final String EXTRA_RECIPE_STEP_ID = "recipestepid";
    public static final int SINGLE_STEP_DISPLAY_REQUEST_CODE = 78;
    private List<RecipeStep> recipeStepList;
    private Recipe recipe;
    private RecipeStep recipeStep;
    private RecipeStep previousStep;
    private MediaInstructionFragment mediaInstructionFragment;
    public static final String MEDIA_INSTRUCTION_FRAGMENT_TAG = "MEDIA_INSTRUCTION_FRAGMENT_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int selection = getIntent().getIntExtra(MainActivity.EXTRA_RECIPE_ID_SELECTION, -1);
        if (selection == -1) {
            finish();
        }
        setContentView(R.layout.activity_recipe_step);
        Recipe[] recipes = JSONUtils.getAllRecipes(JSONUtils.BAKING_JSON_FILE_NAME, getApplicationContext());
        for (Recipe recipe : recipes) {
            if (recipe.getId() == selection) {
                this.recipe = recipe;
                this.recipeStepList = recipe.getSteps();
                break;
            }
        }
        RecipeStepListFragment stepListFragment = (RecipeStepListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.recipeStepListFragment);
        if (recipeStepList != null) {
            stepListFragment.setListRecipeList(recipeStepList);
        }
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
        int recipeId;
        if(recipe == null){
            recipeId = -1;
        }else{
            recipeId = recipe.getId();
        }
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);
        RecipeWidgetProvider.updateAllRecipeAppWidgets(this, appWidgetManager, appWidgetIds, recipeId);

    }


    @Override
    public void onListFragmentInteraction(RecipeStep item) {
        // set current step
        recipeStep = item;

        FrameLayout frameLayout = findViewById(R.id.step_media_instruction_fragment_holder);
        if (frameLayout == null) {
            // DisplayFragment (Fragment B) is not in the layout (handset layout),
            // so start DisplayActivity (Activity B)
            // and pass it the info about the selected item
            Intent intent = new Intent(this, RecipeSingleStepDisplayActivity.class);
            intent.putExtra(EXTRA_RECIPE_ID, recipe.getId());
            intent.putExtra(EXTRA_RECIPE_STEP_ID, item.getId());
            startActivityForResult(intent, SINGLE_STEP_DISPLAY_REQUEST_CODE);
        } else {
            mediaInstructionFragment = new MediaInstructionFragment();
            // DisplayFragment (Fragment B) is in the layout (tablet layout),
            // so tell the fragment to update
            mediaInstructionFragment.setInstruction(item.getDescription());
            if (item.getVideoURL() == null
                    || item.getVideoURL().isEmpty()
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                    != PackageManager.PERMISSION_GRANTED) {
                mediaInstructionFragment.setVideoUri(null);
            } else {
                mediaInstructionFragment.setVideoUri(Uri.parse(item.getVideoURL()));
            }
            // add fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.step_media_instruction_fragment_holder,
                    mediaInstructionFragment,
                    MEDIA_INSTRUCTION_FRAGMENT_TAG);
            fragmentTransaction.commit();
        }
    }

    /**
     * If at the start and previous is pressed do nothing, if at the end and next
     * is pressed do nothing.
     * @param isNext
     */
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
            fragmentTransaction.replace(R.id.step_media_instruction_fragment_holder, mediaInstructionFragment, MEDIA_INSTRUCTION_FRAGMENT_TAG);
            fragmentTransaction.commit();
        }
    }

    @Override
    public int getCurrentRecipeId() {
        return recipe.getId();
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
}
