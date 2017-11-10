package com.derekudacityclassprojects.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.derekudacityclassprojects.bakingapp.FragmentRecipeList.Ingredient;
import com.derekudacityclassprojects.bakingapp.FragmentRecipeList.Recipe;
import com.derekudacityclassprojects.bakingapp.FragmentRecipeStepList.RecipeStepListFragment;

import java.util.ArrayList;
import java.util.List;

public class IngredientsActivity extends AppCompatActivity {
    private Recipe recipe;
    private List<Ingredient> ingredients;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        ingredients = new ArrayList<>();
        Intent intent = getIntent();
        int recipe = intent.getIntExtra(RecipeStepListFragment.EXTRA_RECIPE_ID, -1);
        if(recipe != -1){
            Recipe[] recipes = JSONUtils.getAllRecipes(JSONUtils.BAKING_JSON_FILE_NAME, getApplicationContext());
            for (Recipe item : recipes) {
                if (item.getId() == recipe) {
                    this.recipe = item;
                    this.ingredients = item.getIngredients();
                    break;
                }
            }
        }
        TextView textView = findViewById(R.id.ingredients_activity_text_view);
        String allIngredients = getString(R.string.ingredients_activity_no_ingredients_found);
        if(ingredients != null){
            if(ingredients.size() != 0){
                allIngredients = "";
                for(Ingredient item : ingredients){
                    allIngredients += item.getQuantity() + " " + item.getMeasure() + " - " + item.getIngredient() + "\n\n";
                }
            }
        }
        textView.setText(allIngredients);
    }
}
