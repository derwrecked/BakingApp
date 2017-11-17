package com.derekudacityclassprojects.bakingapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;

/**
 * Created by Derek on 9/10/2017.
 * <p>
 * This class will trigger on any network change but will only start a volley request when
 * isOnline is active that is there is some type of network connection present (mobile or internet).
 */

public class NetworkChangeReceiver extends BroadcastReceiver {
    private MainActivity mainActivity;
    private String url = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    public NetworkChangeReceiver() {
    }

    /**
     * When connectivity changes. In the end only create volley request when online.
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        mainActivity = (MainActivity) context;
        mainActivity.getRecipeListFragment().setFetchingProgressBar();
        VolleyRequests.getBakingJSONString(url, mainActivity);
    }

    /**
     * Checks if there is a network connection.
     *
     * @param context
     * @return true if connected to network or internet else false
     */
    private static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }
}