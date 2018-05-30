package com.adel.bakingapp.RecipeModel;

public class RecipeIngredients {

    private double mQuantity;
    private String mMeasure;
    private String mIngredient;

    public RecipeIngredients(double quantity, String measure, String ingredient) {
        mQuantity = quantity;
        mMeasure = measure;
        mIngredient = ingredient;
    }

    public double getmQuantity() {
        return mQuantity;
    }

    public String getmMeasure() {
        return mMeasure;
    }

    public String getmIngredient() {
        return mIngredient;
    }

    public void setmQuantity(double mQuantity) {
        this.mQuantity = mQuantity;
    }

    public void setmMeasure(String mMeasure) {
        this.mMeasure = mMeasure;
    }

    public void setmIngredient(String mIngredient) {
        this.mIngredient = mIngredient;
    }
}
