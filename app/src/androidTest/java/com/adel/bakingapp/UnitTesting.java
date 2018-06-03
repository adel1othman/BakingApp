package com.adel.bakingapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.widget.Button;

import com.adel.bakingapp.recipe_model.Recipe;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class UnitTesting {
    private Context appContext = InstrumentationRegistry.getTargetContext();

    @Test
    public void QueryUtilsTest() {
        List<Recipe> recipes = QueryUtils.fetchRecipeData("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json");
        Assert.assertNotNull("QueryUtils is working properly", recipes);
    }

    @Test
    public void NoOfColTest() {
        int result = MainActivity.calculateNoOfColumns(appContext);
        Assert.assertNotEquals(0, result);
    }
}