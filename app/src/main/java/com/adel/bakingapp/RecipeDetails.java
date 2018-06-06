package com.adel.bakingapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.OrientationEventListener;
import android.widget.TextView;
import android.widget.Toast;

import static com.adel.bakingapp.MainActivity.listRecipes;
import static com.adel.bakingapp.MainActivity.stepPos;

public class RecipeDetails extends AppCompatActivity {

    private static final int Recipe_STEPS_LOADER_ID = 1;
    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    private String TITLE_KEY;
    private String RECIPE_KEY;
    private String title = "";
    static boolean isFirstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        final FragmentManager fm = getSupportFragmentManager();
        if (isFirstTime){
            RecipeStepsFragment stepsFragment = new RecipeStepsFragment();
            fm.beginTransaction().add(R.id.content, stepsFragment, "0").commit();

            if (RecipeStepsFragment.isTablet(this)){
                fm.beginTransaction().add(R.id.content1, RecipeDetailsDesFragment.newInstance(0), "1").commit();
            }

            isFirstTime = false;
        }

        TextView tvIngredient = findViewById(R.id.tv_ingredient);

        /*Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }else {
            position = intent.getIntExtra("extra_position", DEFAULT_POSITION);
            title = intent.getStringExtra("title");
        }*/

        this.setTitle(listRecipes.get(stepPos).getmName());

        StringBuilder Ingredients = new StringBuilder();
        for (int i = 0; i < listRecipes.get(stepPos).getmRecipeIngredients().size(); i++){
            Ingredients.append(String.format("\nQuantity: %s\nMeasure: %s\nIngredient: %s\n*******\n",
                    listRecipes.get(stepPos).getmRecipeIngredients().get(i).getmQuantity(),
                    listRecipes.get(stepPos).getmRecipeIngredients().get(i).getmMeasure(),
                    listRecipes.get(stepPos).getmRecipeIngredients().get(i).getmIngredient()));
        }

        tvIngredient.setText(Ingredients);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
