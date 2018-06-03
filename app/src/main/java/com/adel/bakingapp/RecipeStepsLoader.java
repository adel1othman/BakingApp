package com.adel.bakingapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.adel.bakingapp.recipe_model.RecipeSteps;

import java.util.List;

public class RecipeStepsLoader extends AsyncTaskLoader<List<RecipeSteps>> {

    private List<RecipeSteps> mRecipeSteps;

    RecipeStepsLoader(Context context, List<RecipeSteps> recipeSteps) {
        super(context);
        mRecipeSteps = recipeSteps;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<RecipeSteps> loadInBackground() {
        return mRecipeSteps;
    }
}
