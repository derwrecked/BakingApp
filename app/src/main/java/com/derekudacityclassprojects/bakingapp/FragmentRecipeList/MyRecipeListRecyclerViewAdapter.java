package com.derekudacityclassprojects.bakingapp.FragmentRecipeList;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.derekudacityclassprojects.bakingapp.FragmentRecipeList.RecipeListFragment.OnListFragmentInteractionListener;
import com.derekudacityclassprojects.bakingapp.R;

import java.util.ArrayList;
public class MyRecipeListRecyclerViewAdapter extends RecyclerView.Adapter<MyRecipeListRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Recipe> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context context;
    public MyRecipeListRecyclerViewAdapter(Recipe[] items,
                                           Context context,
                                           OnListFragmentInteractionListener listener) {
        this.context = context;
        mValues = new ArrayList<>();
        if(items != null){
            for(Recipe recipe : items){
                mValues.add(recipe);
            }
        }
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_recipeitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.recipeName.setText(mValues.get(position).getName());
        holder.recipeShortDescription.setText("---Temporary Description---");
        holder.recipeThumbnail.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.icons8hamburger));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView recipeName;
        public final TextView recipeShortDescription;
        public final ImageView recipeThumbnail;
        public Recipe mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            recipeName = (TextView) view.findViewById(R.id.recipe_name);
            recipeShortDescription = (TextView) view.findViewById(R.id.recipe_description);
            recipeThumbnail = (ImageView) view.findViewById(R.id.recipe_thumbnail);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + recipeName.getText() + "'";
        }
    }
}
