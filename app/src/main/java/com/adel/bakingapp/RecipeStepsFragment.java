package com.adel.bakingapp;

import android.app.LoaderManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adel.bakingapp.RecipeModel.RecipeSteps;

import java.util.List;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static com.adel.bakingapp.MainActivity.listRecipes;
import static com.adel.bakingapp.MainActivity.stepPos;

public class RecipeStepsFragment extends Fragment {

    View rootView;
    RecyclerView rvRecipeDetail;
    GridLayoutManager layoutManager;
    private Parcelable mListState;
    private String STATE_KEY;
    LoaderManager.LoaderCallbacks<List<RecipeSteps>> myCallbacks;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.recipe_details_fragment, container, false);

        layoutManager = new GridLayoutManager(rootView.getContext(), 1);
        rvRecipeDetail = rootView.findViewById(R.id.rv_recipe_detail);

        rvRecipeDetail.setLayoutManager(layoutManager);

        if (IsTablet(rootView.getContext())){
            RecipeStepsTabAdapter mAdapter = new RecipeStepsTabAdapter(listRecipes.get(stepPos).getmRecipeSteps());
            rvRecipeDetail.setAdapter(mAdapter);
        }else {
            RecipeStepsAdapter mAdapter = new RecipeStepsAdapter(listRecipes.get(stepPos).getmRecipeSteps());
            rvRecipeDetail.setAdapter(mAdapter);
        }

        if (RecipeDetails.scrollPos != -1){
            layoutManager.scrollToPosition(RecipeDetails.scrollPos);
        }

        return rootView;
    }

    public static boolean IsTablet(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        if (context.getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT){
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;

            return width > 1080 || height > 1920;
        }else if (context.getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE){
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;

            return width > 1920 || height > 1080;
        }

        return false;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        mListState = layoutManager.onSaveInstanceState();
        savedInstanceState.putParcelable(STATE_KEY, mListState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState != null){
            mListState = savedInstanceState.getParcelable(STATE_KEY);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mListState != null) {
            layoutManager.onRestoreInstanceState(mListState);
        }
    }
}