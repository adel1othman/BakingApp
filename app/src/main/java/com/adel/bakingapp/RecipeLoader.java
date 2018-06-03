package com.adel.bakingapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.adel.bakingapp.recipe_model.Recipe;

import java.util.List;


public class RecipeLoader extends AsyncTaskLoader<List<Recipe>> {

    private String mUrl;

    public RecipeLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Recipe> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        List<Recipe> recipes = QueryUtils.fetchRecipeData(mUrl);

        return recipes;
    }
}