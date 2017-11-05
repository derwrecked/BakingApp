package com.derekudacityclassprojects.bakingapp;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.derekudacityclassprojects.bakingapp.FragmentMediaAndInstruction.MediaInstructionFragment;
import com.derekudacityclassprojects.bakingapp.FragmentRecipeList.Recipe;
import com.derekudacityclassprojects.bakingapp.FragmentRecipeList.RecipeStep;
import com.derekudacityclassprojects.bakingapp.FragmentRecipeStepList.RecipeStepListFragment;

import java.util.List;

public class RecipeSingleStepDisplayActivity extends AppCompatActivity implements MediaInstructionFragment.OnFragmentInteractionListener {
    private Recipe recipe;
    private RecipeStep recipeStep;
    private MediaInstructionFragment mediaInstructionFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_single_step_display);
        int recipeid = getIntent().getIntExtra(RecipeStepListActivity.EXTRA_RECIPE_ID, -1);
        int recipestepid = getIntent().getIntExtra(RecipeStepListActivity.EXTRA_RECIPE_STEP_ID, -1);
        if(recipeid == -1 || recipestepid == -1){
            finish();
        }
        mediaInstructionFragment = (MediaInstructionFragment) getSupportFragmentManager().findFragmentById(R.id.step_media_instruction_fragment);
        Recipe[] recipes = JSONUtils.getAllRecipes(JSONUtils.BAKING_JSON_FILE_NAME, getApplicationContext());
        for(Recipe recipe : recipes){
            if(recipe.getId() == recipeid){
                this.recipe = recipe;
                List<RecipeStep> recipeSteps = recipe.getSteps();
                if(recipeSteps != null){
                    for(RecipeStep step : recipeSteps){
                        if(step.getId() == recipestepid){
                            this.recipeStep = step;
                            break;
                        }
                    }
                }
                break;
            }
        }
        if(recipeStep != null && mediaInstructionFragment != null){
            mediaInstructionFragment.setInstruction(recipeStep.getDescription());
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // TODO: forward and back buttons come back through here?
    }
}
