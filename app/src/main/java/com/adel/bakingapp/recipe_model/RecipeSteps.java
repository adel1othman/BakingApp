package com.adel.bakingapp.recipe_model;

public class RecipeSteps {

    private int mRecipeStepID;
    private int mStepID;
    private String mShortDescription;
    private String mDescription;
    private String mVideoURL;
    private String mThumbnailURL;

    public RecipeSteps(int recipeStepID, int stepID, String shortDescription, String description, String videoURL, String thumbnailURL) {
        mRecipeStepID = recipeStepID;
        mStepID = stepID;
        mShortDescription = shortDescription;
        mDescription = description;
        mVideoURL = videoURL;
        mThumbnailURL = thumbnailURL;
    }

    public int getmRecipeStepID() {
        return mRecipeStepID;
    }

    public int getmStepID() {
        return mStepID;
    }

    public String getmShortDescription() {
        return mShortDescription;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmVideoURL() {
        return mVideoURL;
    }

    public String getmThumbnailURL() {
        return mThumbnailURL;
    }

    public void setmRecipeStepID(int mRecipeStepID) {
        this.mRecipeStepID = mRecipeStepID;
    }

    public void setmStepID(int mStepID) {
        this.mStepID = mStepID;
    }

    public void setmShortDescription(String mShortDescription) {
        this.mShortDescription = mShortDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public void setmVideoURL(String mVideoURL) {
        this.mVideoURL = mVideoURL;
    }

    public void setmThumbnailURL(String mThumbnailURL) {
        this.mThumbnailURL = mThumbnailURL;
    }
}
