package com.adel.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.adel.bakingapp.RecipeModel.RecipeIngredients;
import static com.adel.bakingapp.MainActivity.listRecipes;
import static com.adel.bakingapp.MainActivity.stepPos;

import java.util.ArrayList;
import java.util.List;

public class ListViewWidgetService extends RemoteViewsService {

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        stopSelf();
    }

    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListViewRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

class ListViewRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<RecipeIngredients> records;

    public ListViewRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
    }

    public void onCreate() {
        records = new ArrayList<>();
    }

    public RemoteViews getViewAt(int position) {

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_content);

        RecipeIngredients data = records.get(position);

        String Ingredient = String.format("Quantity: %S\nMeasure: %S\nIngredient: %S",
                data.getmQuantity(), data.getmMeasure(), data.getmIngredient());

        rv.setTextViewText(R.id.item, Ingredient);

        Bundle extras = new Bundle();

        extras.putInt(IngredientsWidget.EXTRA_ITEM, position);

        Intent fillInIntent = new Intent();

        fillInIntent.putExtra("homescreen_ingredients", Ingredient);

        fillInIntent.putExtras(extras);

        //rv.setOnClickFillInIntent(R.id.item, fillInIntent);

        return rv;
    }

    public int getCount(){
        Log.e("size=",records.size()+"");
        return records.size();
    }

    public void onDataSetChanged(){
        records = listRecipes.get(stepPos).getmRecipeIngredients();
    }

    public int getViewTypeCount(){
        return 1;
    }

    public long getItemId(int position) {
        return position;
    }

    public void onDestroy(){
        records.clear();
    }

    public boolean hasStableIds() {
        return true;
    }

    public RemoteViews getLoadingView() {
        return null;
    }
}
