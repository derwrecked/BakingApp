package com.derekudacityclassprojects.bakingapp.RecipeWidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.derekudacityclassprojects.bakingapp.FragmentRecipeList.Recipe;
import com.derekudacityclassprojects.bakingapp.JSONUtils;
import com.derekudacityclassprojects.bakingapp.R;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {
    public static int currentRecipeId = 1; // default to 1
    public static Recipe[] recipes;

    public static void updateRecipeAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, int recipeId, Recipe[] recipesAll) {
        currentRecipeId = recipeId;
        recipes = recipesAll;
        RemoteViews views = getRecipeListViewRemoteView(context, currentRecipeId, recipes);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateAllRecipeAppWidgets(Context context, AppWidgetManager appWidgetManager,
                                             int[] appWidgetIds, int recipeId, Recipe[] recipesAll) {
        currentRecipeId = recipeId;
        recipes = recipesAll;
        RemoteViews views = getRecipeListViewRemoteView(context, currentRecipeId, recipes);
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    // Called when a new widget is created as well as every update interval
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateRecipeAppWidget(context, appWidgetManager, appWidgetId, currentRecipeId, recipes);
        }
    }

    /**
     * Called in response to the ACTION_APPWIDGET_OPTIONS_CHANGED broadcast when this
     * widget has been layed out at a new size.
     * @param context
     * @param appWidgetManager
     * @param appWidgetId
     * @param newOptions
     */
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        updateRecipeAppWidget(context, appWidgetManager, appWidgetId, currentRecipeId, recipes);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private static RemoteViews getRecipeListViewRemoteView(Context context,
                                                           int recipeId,
                                                           Recipe[] recipes){
        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_list_view);
        String title = "";
        if(recipes == null){
            title = context.getString(R.string.widget_title_no_recipe_selected);
        }else{
            try {
                for (Recipe item : recipes) {
                    if (item.getId() == recipeId) {
                        title = item.getName();
                        break;
                    }
                }
            } catch (Exception e) {
                title = context.getString(R.string.widget_title_no_recipe_selected);
            }
        }

        // service acts as adapter
        Intent intent = new Intent(context, RecipeWidgetService.class);
        view.setRemoteAdapter(R.id.widget_list_view, intent);
        view.setTextViewText(R.id.widget_recipe_title, title);
        // set
        // pending intent
        //Intent appIntent = new Intent(context, RecipeStepListActivity.class);
        //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //view.setPendingIntentTemplate(R.id.widget_list_view, pendingIntent);
        return view;
    }

}

