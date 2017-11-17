package com.derekudacityclassprojects.bakingapp;

import com.derekudacityclassprojects.bakingapp.FragmentRecipeList.Recipe;

/**
 * Created by Derek on 11/17/2017.
 */

public class RecipeRepository {
    private Recipe[] recipes = new Recipe[0];
    private static RecipeRepository INSTANCE;
    private RecipeRepository(Recipe[] recipes) {
        this.recipes = recipes;
    }

    public static Recipe[] getRecipeList(){
        return INSTANCE.recipes;
    }

    public static void createInstance(Recipe[] recipes){
        INSTANCE = new RecipeRepository(recipes);
    }
}
