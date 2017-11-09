package com.derekudacityclassprojects.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

import com.derekudacityclassprojects.bakingapp.FragmentRecipeList.Recipe;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {
    private final static int[] TEXT_VIEW_RECIPE_NAME_HOLDERS = {
            R.id.widget_recipe_holder_text_view_1,
            R.id.widget_recipe_holder_text_view_2,
            R.id.widget_recipe_holder_text_view_3,
            R.id.widget_recipe_holder_text_view_4,
    };
    private final static int[] IMAGE_VIEW_RECIPE_NAME_HOLDERS = {
            R.id.widget_food_image_view_1,
            R.id.widget_food_image_view_2,
            R.id.widget_food_image_view_3,
            R.id.widget_food_image_view_4
    };
    private final static int[] LAYOUT_RECIPE_NAME_HOLDERS = {
            R.id.widget_recipe_holder_1,
            R.id.widget_recipe_holder_2,
            R.id.widget_recipe_holder_3,
            R.id.widget_recipe_holder_4
    };
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
        // get all recipes
        Recipe[] recipes = JSONUtils.getAllRecipes("baking", context);
        // update widget based on height
        updateWidgetViews(context, views, recipes, options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT));
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

    private static void updateWidgetViews(final Context context,
                                          final RemoteViews views,
                                          final Recipe[] recipes,
                                          int widgetHeight){
        // 50 was chosen because it is the min height of the image.
        // Due to the layouts being weighted they will fill any extra space so
        // this value is ok.
        int rows = widgetHeight / 50; // truncate to nearest int
        if(rows == 0){ // take into account height is less than 50
            rows = 1;
        }
        // based on height show number of views that fit, hide the rest
        for(int i = 0; i < TEXT_VIEW_RECIPE_NAME_HOLDERS.length; i++){
            if(i < rows){
                CharSequence charSequence = recipes[i].getName();
                views.setTextViewText(TEXT_VIEW_RECIPE_NAME_HOLDERS[i], charSequence);
                views.setViewVisibility(LAYOUT_RECIPE_NAME_HOLDERS[i], View.VISIBLE);

                // pending intent
                Intent intent = new Intent(context, RecipeStepListActivity.class);
                intent.putExtra(MainActivity.EXTRA_RECIPE_ID_SELECTION, recipes[i].getId());
                PendingIntent pendingIntent = PendingIntent.getActivity(context, i, intent, 0);

                views.setOnClickPendingIntent(LAYOUT_RECIPE_NAME_HOLDERS[i], pendingIntent);
            }else{
                views.setViewVisibility(LAYOUT_RECIPE_NAME_HOLDERS[i], View.GONE);
            }
        }

    }
}

