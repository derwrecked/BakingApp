package com.derekudacityclassprojects.bakingapp.RecipeWidget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Derek on 11/9/2017.
 */

public class RecipeWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        return(new ListViewRemoteViewsFactory(this.getApplicationContext()));
    }
}