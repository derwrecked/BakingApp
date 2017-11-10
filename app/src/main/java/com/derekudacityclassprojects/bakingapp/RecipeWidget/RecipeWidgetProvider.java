package com.derekudacityclassprojects.bakingapp.RecipeWidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.derekudacityclassprojects.bakingapp.R;
import com.derekudacityclassprojects.bakingapp.RecipeStepListActivity;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        //Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        //// Construct the RemoteViews object
        //RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
        //// get all recipes
        //Recipe[] recipes = JSONUtils.getAllRecipes("baking", context);
        //// update widget based on height
        //updateWidgetViews(context, views, recipes, options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT));
        RemoteViews views = getRecipeListViewRemoteView(context);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    // Called when a new widget is created as well as every update interval
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
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
        updateAppWidget(context, appWidgetManager, appWidgetId);
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

    private static RemoteViews getRecipeListViewRemoteView(Context context){
        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_list_view);
        // service acts as adapter
        Intent intent = new Intent(context, RecipeWidgetService.class);
        view.setRemoteAdapter(R.id.widget_list_view, intent);
        // set
        // pending intent
        Intent appIntent = new Intent(context, RecipeStepListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setPendingIntentTemplate(R.id.widget_list_view, pendingIntent);
        return view;
    }

}

