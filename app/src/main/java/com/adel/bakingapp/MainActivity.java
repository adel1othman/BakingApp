package com.adel.bakingapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.IdlingResource;

import com.adel.bakingapp.recipe_model.Recipe;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_RECIPE_NAME = "com.adel.bakingapp.EXTRA_RECIPE_NAME";
    private static final int DELAY_MILLIS = 3000;

    static List<Recipe> listRecipes = new ArrayList<>();

    private String STATE_KEY;
    private String REQUEST_URL;

    private static final int Recipe_LOADER_ID = 1;

    private RecipeAdapter mAdapter;
    static int opstionsID;

    RecyclerView recipeRecyclerView;
    GridLayoutManager layoutManager;
    LoaderManager.LoaderCallbacks<List<Recipe>> myCallbacks;
    ProgressBar loadingIndicator;
    private Parcelable mListState;
    static int stepPos = -1;

    @Nullable
    private SimpleIdlingRes mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingRes();
        }
        return (IdlingResource) mIdlingResource;
    }

    @Override
    protected void onStart() {
        super.onStart();

        GetRecipeList(this, myCallbacks, mIdlingResource);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecipeDetails.isFirstTime = true;

        loadingIndicator = findViewById(R.id.loading_indicator);
        recipeRecyclerView = findViewById(R.id.rv_recipes);
        layoutManager = new GridLayoutManager(this, calculateNoOfColumns(this));

        /*if (RecipeDetails.position != -1){
            layoutManager.scrollToPosition(RecipeDetails.position);
            RecipeDetails.position = -1;
        }*/

        REQUEST_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

        myCallbacks = new LoaderManager.LoaderCallbacks<List<Recipe>>() {
            @Override
            public Loader<List<Recipe>> onCreateLoader(int id, Bundle args) {
                loadingIndicator.setVisibility(View.VISIBLE);
                return new RecipeLoader(getBaseContext(), REQUEST_URL);
            }

            @Override
            public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> recipes) {
                loadingIndicator.setVisibility(View.GONE);

                if (recipes != null && !recipes.isEmpty()) {
                    listRecipes = recipes;
                    recipeRecyclerView.setLayoutManager(layoutManager);
                    mAdapter = new RecipeAdapter(recipes);
                    recipeRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onLoaderReset(Loader<List<Recipe>> loader) {
                loadingIndicator.setVisibility(View.GONE);
            }
        };

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.restartLoader(Recipe_LOADER_ID, null, myCallbacks);
        } else {
            loadingIndicator.setVisibility(View.GONE);
        }

        getIdlingResource();

        if (mIdlingResource != null) {
            if (mIdlingResource.isIdleNow()) {
                Log.e(TAG, "isIdleNow: True");
            }else {
                Log.e(TAG, "isIdleNow: False");
            }
        }
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        int noOfColumns = 1;
        if (width > 1080 || height > 1920){
            float dpWidth = metrics.widthPixels / metrics.density;
            int scalingFactor = 180;
            noOfColumns = (int) (dpWidth / scalingFactor);
            if(noOfColumns < 2)
                noOfColumns = 2;
        }else{
            //noOfColumns = 1;
        }
        return noOfColumns;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mListState = layoutManager.onSaveInstanceState();
        outState.putParcelable(STATE_KEY, mListState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null){
            mListState = savedInstanceState.getParcelable(STATE_KEY);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mListState != null) {
            layoutManager.onRestoreInstanceState(mListState);
        }
    }

    void GetRecipeList(Context context, final LoaderManager.LoaderCallbacks<List<Recipe>> callback,
                       @Nullable final SimpleIdlingRes idlingResource) {
        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onLoadFinished(getLoaderManager().<List<Recipe>>getLoader(Recipe_LOADER_ID), listRecipes);
                    if (idlingResource != null) {
                        idlingResource.setIdleState(true);
                    }
                }
            }
        }, DELAY_MILLIS);
    }
}
