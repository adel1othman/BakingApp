package com.adel.bakingapp;

import android.app.LoaderManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adel.bakingapp.recipe_model.RecipeSteps;

import java.util.List;

import static com.adel.bakingapp.MainActivity.listRecipes;
import static com.adel.bakingapp.MainActivity.stepPos;

public class RecipeStepsFragment extends Fragment {

    View rootView;
    RecyclerView rvRecipeDetail;
    GridLayoutManager layoutManager;
    private Parcelable mListState;
    private String STATE_KEY = "STATE_KEY";
    LoaderManager.LoaderCallbacks<List<RecipeSteps>> myCallbacks;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.recipe_details_fragment, container, false);

        layoutManager = new GridLayoutManager(rootView.getContext(), 1);
        rvRecipeDetail = rootView.findViewById(R.id.rv_recipe_detail);

        rvRecipeDetail.setLayoutManager(layoutManager);

        if (isTablet(rootView.getContext())){
            RecipeStepsTabAdapter mAdapter = new RecipeStepsTabAdapter(listRecipes.get(stepPos).getmRecipeSteps());
            rvRecipeDetail.setAdapter(mAdapter);
        }else {
            RecipeStepsAdapter mAdapter = new RecipeStepsAdapter(listRecipes.get(stepPos).getmRecipeSteps());
            rvRecipeDetail.setAdapter(mAdapter);
        }

        return rootView;
    }

    public static boolean isTablet(Context context) {
        try {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            float screenWidth  = dm.widthPixels / dm.xdpi;
            float screenHeight = dm.heightPixels / dm.ydpi;
            double size = Math.sqrt(Math.pow(screenWidth, 2) +
                    Math.pow(screenHeight, 2));
            // Tablet devices have a screen size greater than 6 inches
            return size >= 6;
        } catch(Throwable t) {
            Log.e("Failed: ", t.toString());
            return false;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        mListState = layoutManager.onSaveInstanceState();
        savedInstanceState.putParcelable(STATE_KEY, mListState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
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