package com.adel.bakingapp;

import android.text.TextUtils;
import android.util.Log;

import com.adel.bakingapp.RecipeModel.Recipe;
import com.adel.bakingapp.RecipeModel.RecipeIngredients;
import com.adel.bakingapp.RecipeModel.RecipeSteps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<Recipe> fetchRecipeData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        return extractFeatureFromJson(jsonResponse);
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the recipe JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Recipe> extractFeatureFromJson(String recipeJSON) {
        if (TextUtils.isEmpty(recipeJSON)) {
            return null;
        }

        List<Recipe> recipes = new ArrayList<>();
        List<RecipeIngredients> recipeIngredients;
        List<RecipeSteps> recipeSteps;

        try {
            JSONArray baseJsonResponse = new JSONArray(recipeJSON);
            for (int i = 0; i < baseJsonResponse.length(); i++) {
                recipeIngredients = new ArrayList<>();
                recipeSteps = new ArrayList<>();

                JSONObject currentRecipe = baseJsonResponse.getJSONObject(i);
                long id = currentRecipe.getLong("id");
                String name = currentRecipe.getString("name");
                int servings = currentRecipe.getInt("servings");
                String image = currentRecipe.getString("image");

                JSONArray ingredients = currentRecipe.getJSONArray("ingredients");
                for (int j = 0; j < ingredients.length(); j++) {
                    JSONObject currentIngredient = ingredients.getJSONObject(j);
                    double quantity = currentIngredient.getDouble("quantity");
                    String measure = currentIngredient.getString("measure");
                    String ingredient = currentIngredient.getString("ingredient");

                    RecipeIngredients recipeIngredient = new RecipeIngredients(quantity, measure, ingredient);

                    recipeIngredients.add(recipeIngredient);
                }

                JSONArray steps = currentRecipe.getJSONArray("steps");
                for (int k = 0; k < steps.length(); k++) {
                    JSONObject currentStep = steps.getJSONObject(k);
                    int stepID = currentStep.getInt("id");
                    String shortDescription = currentStep.getString("shortDescription");
                    String description = currentStep.getString("description");
                    String videoURL = currentStep.getString("videoURL");
                    String thumbnailURL = currentStep.getString("thumbnailURL");

                    RecipeSteps recipeStep = new RecipeSteps(i, stepID, shortDescription, description, videoURL, thumbnailURL);

                    recipeSteps.add(recipeStep);
                }

                Recipe recipe = new Recipe(id, name, recipeIngredients, recipeSteps, servings, image);

                recipes.add(recipe);
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the recipe JSON results", e);
        }

        return recipes;
    }
}