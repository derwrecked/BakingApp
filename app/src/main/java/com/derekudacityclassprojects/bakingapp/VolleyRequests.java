package com.derekudacityclassprojects.bakingapp;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.derekudacityclassprojects.bakingapp.FragmentRecipeList.Recipe;
import org.json.JSONObject;


/**
 * Created by Derek on 11/15/2017.
 */

public class VolleyRequests {
    public static String VOLLEY_KEY_TAG = "VOLLEY_KEY_TAG";

    public static void getBakingJSONString(String url,
                                           final MainActivity mainActivity){
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response != null) {
                                String json = response.toString();
                                Recipe[] recipes = JSONUtils.getAllRecipesFromString(json);
                                mainActivity.getRecipeListFragment().setRecipeList(json, recipes);
                            }else{
                                mainActivity.getRecipeListFragment().setCouldNotFetch();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mainActivity.getRecipeListFragment().setCouldNotFetch();
                        }
                        // only for testing
                        if (mainActivity.getIdlingResource() != null) {
                            mainActivity.getIdlingResource().setIdleState(true);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                mainActivity.getRecipeListFragment().setCouldNotFetch();

                // only for testing
                if (mainActivity.getIdlingResource() != null) {
                    mainActivity.getIdlingResource().setIdleState(true);
                }
            }
        });

        // set tag so we can cancel if need be
        stringRequest.setTag(VOLLEY_KEY_TAG);

        // Access the RequestQueue through your singleton class.
        RequestQueueSingleton.getInstance(mainActivity.getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
