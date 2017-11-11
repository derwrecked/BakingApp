package com.derekudacityclassprojects.bakingapp.RecipeWidget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.derekudacityclassprojects.bakingapp.FragmentRecipeList.Ingredient;
import com.derekudacityclassprojects.bakingapp.FragmentRecipeList.Recipe;
import com.derekudacityclassprojects.bakingapp.JSONUtils;
import com.derekudacityclassprojects.bakingapp.MainActivity;
import com.derekudacityclassprojects.bakingapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Derek on 11/9/2017.
 */

public class ListViewRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private List<Ingredient> ingredientList;

    public ListViewRemoteViewsFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    // on create and whenever it is notified to update its data.
    @Override
    public void onDataSetChanged() {
        Recipe[] recipes = JSONUtils.getAllRecipes("baking", context);
        ingredientList = new ArrayList<>();
        try {
            for (Recipe item : recipes) {
                if (item.getId() == RecipeWidgetProvider.currentRecipeId) {
                    ingredientList = item.getIngredients();
                    break;
                }
            }
        } catch (Exception e) {
            ingredientList = new ArrayList<>();
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return ingredientList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews row = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_list_view_row);
        String ingredientString = ingredientList.get(position).getQuantity() + " " + ingredientList.get(position).getMeasure() + " - " + ingredientList.get(position).getIngredient();
        row.setTextViewText(R.id.widget_recipe_holder_text_view, ingredientString);

        //// template pending intent
        //Intent fillInIntent = new Intent();
        //fillInIntent.putExtra(MainActivity.EXTRA_RECIPE_ID_SELECTION, recipes[position].getId());
        //row.setOnClickFillInIntent(R.id.widget_food_image_view, fillInIntent);
        return row;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
