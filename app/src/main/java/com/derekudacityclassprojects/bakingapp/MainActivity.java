package com.derekudacityclassprojects.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.derekudacityclassprojects.bakingapp.FragmentRecipeList.Recipe;
import com.derekudacityclassprojects.bakingapp.FragmentRecipeList.RecipeListFragment;

public class MainActivity extends AppCompatActivity implements RecipeListFragment.OnListFragmentInteractionListener {
    public static final String EXTRA_RECIPE_ID_SELECTION = "recipe_id";
    private final String TAG = MainActivity.class.getSimpleName();
    private RecipeListFragment recipeListFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recipeListFragment = (RecipeListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.recipeListFragment);
    }

    @Override
    public void onListFragmentInteraction(Recipe item) {
        Intent intent = new Intent(this, RecipeStepListActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID_SELECTION, item.getId());
        startActivity(intent);
    }
}
