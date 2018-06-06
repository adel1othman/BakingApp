package com.adel.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adel.bakingapp.recipe_model.RecipeSteps;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.ContentValues.TAG;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.RecipeStepsViewHolder> {

    private Context context;
    private List<RecipeSteps> mRecipeSteps;

    RecipeStepsAdapter(List<RecipeSteps> recipeSteps) {
        mRecipeSteps = recipeSteps;
    }

    @NonNull
    @Override
    public RecipeStepsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();

        View view = View.inflate(context, R.layout.list_item_details, null);

        return new RecipeStepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepsViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return (mRecipeSteps!=null? mRecipeSteps.size():0);
    }


    class RecipeStepsViewHolder extends RecyclerView.ViewHolder {

        LinearLayout container;
        TextView step;
        ImageView thumbnail;
        ImageView vidAvailability;

        RecipeStepsViewHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.item_detail_container);
            step = itemView.findViewById(R.id.tv_step);
            thumbnail = itemView.findViewById(R.id.iv_thumbnail);
            vidAvailability = itemView.findViewById(R.id.iv_vid_state);
        }

        void bind(final int listIndex) {

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RecipeDetails.isFirstTime = true;
                    launchStepsActivity(mRecipeSteps.get(listIndex).getmRecipeStepID(), getAdapterPosition());
                }
            });

            step.setText(mRecipeSteps.get(listIndex).getmShortDescription());

            if (!mRecipeSteps.get(listIndex).getmThumbnailURL().equals("")){
                try {
                    Picasso.with(context)
                            .load(mRecipeSteps.get(listIndex).getmThumbnailURL())
                            .into(thumbnail);
                }catch (Exception ex){
                    Log.d("Invalid image url: ", ex.getMessage());
                }
            }

            if (!mRecipeSteps.get(listIndex).getmVideoURL().equals("")){
                vidAvailability.setImageResource(R.drawable.video);
            }else {
                vidAvailability.setImageResource(R.drawable.no_video);
            }
        }
    }

    public void delete(int position) {
        mRecipeSteps.remove(position);
        notifyItemRemoved(position);
    }

    private void launchStepsActivity(int recipePosition, int stepPosition) {
        Intent intent = new Intent(context, StepsActivity.class);
        intent.putExtra("extra_position_recipe", recipePosition);
        intent.putExtra("extra_position_step", stepPosition);

        context.startActivity(intent);
    }
}
