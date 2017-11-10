package com.derekudacityclassprojects.bakingapp.RecipeWidget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.derekudacityclassprojects.bakingapp.FragmentRecipeList.Recipe;
import com.derekudacityclassprojects.bakingapp.JSONUtils;

/**
 * Created by Derek on 11/9/2017.
 */

public class RecipeWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        // get all recipes
        Recipe[] recipes = JSONUtils.getAllRecipes("baking", this.getApplicationContext());
        return(new ListViewRemoteViewsFactory(this.getApplicationContext(), recipes));
    }
}