package com.adel.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adel.bakingapp.recipe_model.Recipe;

import java.util.List;

import static android.content.ContentValues.TAG;
import static com.adel.bakingapp.MainActivity.stepPos;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private Context context;
    private List<Recipe> mRecipes;

    RecipeAdapter(List<Recipe> recipes) {
        mRecipes = recipes;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();

        View view = View.inflate(context, R.layout.list_item, null);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return (mRecipes!=null? mRecipes.size():0);
    }


    class RecipeViewHolder extends RecyclerView.ViewHolder {

        LinearLayout container;
        TextView name;

        RecipeViewHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.item_container);
            name = itemView.findViewById(R.id.tv_name);
        }

        void bind(final int listIndex) {

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stepPos = getAdapterPosition();
                    Intent intent = new Intent(context, IngredientsWidget.class);
                    intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                    int[] ids = AppWidgetManager.getInstance(context.getApplicationContext())
                            .getAppWidgetIds(new ComponentName(context.getApplicationContext(), IngredientsWidget.class));
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                    context.sendBroadcast(intent);
                    launchDetailActivity();
                }
            });

            name.setText(mRecipes.get(listIndex).getmName());
        }
    }

    public void delete(int position) {
        mRecipes.remove(position);
        notifyItemRemoved(position);
    }

    private void launchDetailActivity() {
        Intent intent = new Intent(context, RecipeDetails.class);
        /*intent.putExtra(RecipeDetails.EXTRA_POSITION, position);
        intent.putExtra("title", title);*/

        context.startActivity(intent);
    }
}
