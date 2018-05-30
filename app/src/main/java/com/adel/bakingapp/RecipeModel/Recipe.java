package com.adel.bakingapp.RecipeModel;

import java.util.List;

public class Recipe {

    private long mRecipeID;
    private String mName;
    private List<RecipeIngredients> mRecipeIngredients;
    private List<RecipeSteps> mRecipeSteps;
    private int mServings;
    private String mImage;

    public Recipe(long recipeID, String name, List<RecipeIngredients> recipeIngredients, List<RecipeSteps> recipeSteps, int servings, String image) {
        mRecipeID = recipeID;
        mName = name;
        mRecipeIngredients = recipeIngredients;
        mRecipeSteps = recipeSteps;
        mServings = servings;
        mImage = image;
    }

    public long getmRecipeID() {
        return mRecipeID;
    }

    public String getmName() {
        return mName;
    }

    public List<RecipeIngredients> getmRecipeIngredients() {
        return mRecipeIngredients;
    }

    public List<RecipeSteps> getmRecipeSteps() {
        return mRecipeSteps;
    }

    public int getmServings() {
        return mServings;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmRecipeID(long recipeID) {
        this.mRecipeID = recipeID;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmRecipeIngredients(List<RecipeIngredients> mRecipeIngredients) {
        this.mRecipeIngredients = mRecipeIngredients;
    }

    public void setmRecipeSteps(List<RecipeSteps> mRecipeSteps) {
        this.mRecipeSteps = mRecipeSteps;
    }

    public void setmServings(int mServings) {
        this.mServings = mServings;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }
}