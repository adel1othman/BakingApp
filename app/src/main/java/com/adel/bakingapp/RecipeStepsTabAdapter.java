package com.adel.bakingapp;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.adel.bakingapp.recipe_model.RecipeSteps;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeStepsTabAdapter extends RecyclerView.Adapter<RecipeStepsTabAdapter.RecipeStepsViewHolder> {

    private Context context;
    private List<RecipeSteps> mRecipeSteps;

    RecipeStepsTabAdapter(List<RecipeSteps> recipeSteps) {
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
                    ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content1, RecipeDetailsDesFragment.newInstance(listIndex))
                            .commit();
                }
            });

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

            step.setText(mRecipeSteps.get(listIndex).getmShortDescription());
        }
    }
}
