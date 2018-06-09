package com.adel.bakingapp;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.adel.bakingapp.recipe_model.Recipe;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class UnitTesting {
    private Context appContext = InstrumentationRegistry.getTargetContext();
    private static final int ITEM_BELOW_THE_FOLD = 2;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

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

    @Test
    public void scrollToItemBelowFold_checkItsText() {
        onView(ViewMatchers.withId(R.id.rv_recipes))
                .perform(RecyclerViewActions.actionOnItemAtPosition(ITEM_BELOW_THE_FOLD, scrollTo()));

        String itemElementText = "Special Yellow Cake";
        onView(withText(itemElementText)).check(matches(isDisplayed()));
    }

    @Test
    public void itemInMiddleOfList_hasSpecialText() {
        onView(ViewMatchers.withId(R.id.rv_recipes))
                .perform(RecyclerViewActions.scrollToHolder(wantedElement()));

        String middleElementText = "Special Yellow Cake";
        onView(withText(middleElementText)).check(matches(isDisplayed()));
    }

    private static Matcher<RecipeAdapter.RecipeViewHolder> wantedElement() {
        return new TypeSafeMatcher<RecipeAdapter.RecipeViewHolder>() {

            @Override
            protected boolean matchesSafely(RecipeAdapter.RecipeViewHolder customHolder) {
                return customHolder.getWantedElement();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Wanted Item");
            }
        };
    }
}