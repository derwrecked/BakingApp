package com.derekudacityclassprojects.bakingapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.derekudacityclassprojects.bakingapp.FragmentRecipeList.Recipe;
import com.derekudacityclassprojects.bakingapp.FragmentRecipeList.RecipeListFragment;
import com.derekudacityclassprojects.bakingapp.FragmentRecipeStepList.RecipeStepListFragment;

public class MainActivity extends AppCompatActivity implements RecipeListFragment.OnListFragmentInteractionListener {
    public static final String EXTRA_RECIPE_ID_SELECTION = "recipe_id";
    public static final int RECIPE_REQUEST_CODE = 45;
    private final String TAG = MainActivity.class.getSimpleName();
    private final int MY_PERMISSIONS_REQUEST_INTERNET = 28;
    private RecipeListFragment recipeListFragment;

    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recipeListFragment = (RecipeListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.recipeListFragment);
    }

    @Override
    public void onListFragmentInteraction(Recipe item) {
        Intent intent = new Intent(this, RecipeStepListActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID_SELECTION, item.getId());
        startActivityForResult(intent, RECIPE_REQUEST_CODE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET},
                        MY_PERMISSIONS_REQUEST_INTERNET);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == MY_PERMISSIONS_REQUEST_INTERNET){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                return;
            }else{
                finish();
            }
        }
    }
    public RecipeListFragment getRecipeListFragment(){
        return recipeListFragment;
    }

    @Nullable
    public SimpleIdlingResource getIdlingResource() {
        return mIdlingResource;
    }
    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource setIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
            mIdlingResource.setIdleState(false);
        }
        return mIdlingResource;
    }
}
