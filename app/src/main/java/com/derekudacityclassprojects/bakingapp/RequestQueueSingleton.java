package com.derekudacityclassprojects.bakingapp;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Derek on 9/2/2017.
 *
 * This class is a singleton for volley as we only need one request queue due to
 * relatively low volley activity.
 */

public class RequestQueueSingleton {
    private static RequestQueueSingleton mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private RequestQueueSingleton(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();

    }

    /**
     * Get the single instance of the singleton class or create if it doesn't already exist.
     * *NOTE: synchronized for multi threaded use (probably not needed but yolo)
     * @param context
     * @return
     */
    public static synchronized RequestQueueSingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RequestQueueSingleton(context);
        }
        return mInstance;
    }


    /**
     * Get the single instance of the request queue or create if it doesn't already exist.
     * @return
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    /**
     * Add a volley request.
     * @param req
     * @param <T>
     */
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}