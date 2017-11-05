package com.derekudacityclassprojects.bakingapp;

import android.content.Context;

import com.derekudacityclassprojects.bakingapp.FragmentRecipeList.Recipe;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Derek on 11/5/2017.
 */

public class JSONUtils {
    public static final String BAKING_JSON_FILE_NAME = "baking";
    // store it here so we dont have to go through json every time.
    private static Recipe[] recipes;
    /**
     * Returns a list of recipes; an empty list if nothing is found or an error occurs.
     * @param filename
     * @return
     */
    public static Recipe[] getAllRecipes(String filename, Context context){
        if(recipes != null && recipes.length != 0){
            return recipes;
        }
        Gson gson = new Gson();
        Recipe[] recipes;
        try {
            String json = loadJSONFromAsset(filename, context);
            recipes = gson.fromJson(json, Recipe[].class);
            if(recipes == null){
                recipes = new Recipe[0];
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Recipe[0];
        }
        return recipes;
    }

    /**
     * Load json to string.
     * @param filename
     * @param context
     * @return
     */
    private static String loadJSONFromAsset(String filename, Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(filename + ".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
