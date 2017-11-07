package com.derekudacityclassprojects.bakingapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.derekudacityclassprojects.bakingapp.FragmentMediaAndInstruction.MediaInstructionFragment;
import com.derekudacityclassprojects.bakingapp.FragmentRecipeList.Recipe;
import com.derekudacityclassprojects.bakingapp.FragmentRecipeList.RecipeStep;
import com.derekudacityclassprojects.bakingapp.FragmentRecipeStepList.RecipeStepListFragment;

import java.util.List;

public class RecipeStepListActivity extends AppCompatActivity implements RecipeStepListFragment.OnListFragmentInteractionListener, MediaInstructionFragment.OnFragmentInteractionListener {
    public static final String EXTRA_RECIPE_ID = "recipeid";
    public static final String EXTRA_RECIPE_STEP_ID = "recipestepid";
    private List<RecipeStep> recipeSteps;
    private Recipe recipe;

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
                this.recipeSteps = recipe.getSteps();
                break;
            }
        }
        RecipeStepListFragment stepListFragment = (RecipeStepListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.recipeStepListFragment);
        if (recipeSteps != null) {
            stepListFragment.setListRecipeList(recipeSteps);
        }

    }


    @Override
    public void onListFragmentInteraction(RecipeStep item) {
        MediaInstructionFragment mediaInstructionFragment = (MediaInstructionFragment) getSupportFragmentManager()
                .findFragmentById(R.id.step_media_instruction_fragment);
        if (mediaInstructionFragment == null) {
            // DisplayFragment (Fragment B) is not in the layout (handset layout),
            // so start DisplayActivity (Activity B)
            // and pass it the info about the selected item
            Intent intent = new Intent(this, RecipeSingleStepDisplayActivity.class);
            intent.putExtra(EXTRA_RECIPE_ID, recipe.getId());
            intent.putExtra(EXTRA_RECIPE_STEP_ID, item.getId());
            startActivity(intent);
        } else {
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
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
