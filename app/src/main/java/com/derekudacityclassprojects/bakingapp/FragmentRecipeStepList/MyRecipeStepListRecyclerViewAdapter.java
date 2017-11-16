package com.derekudacityclassprojects.bakingapp.FragmentRecipeStepList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.derekudacityclassprojects.bakingapp.FragmentRecipeList.RecipeStep;
import com.derekudacityclassprojects.bakingapp.FragmentRecipeStepList.RecipeStepListFragment.OnListFragmentInteractionListener;
import com.derekudacityclassprojects.bakingapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyRecipeStepListRecyclerViewAdapter extends RecyclerView.Adapter<MyRecipeStepListRecyclerViewAdapter.ViewHolder> {

    private final List<RecipeStep> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final Context context;

    public MyRecipeStepListRecyclerViewAdapter(Context context, List<RecipeStep> items, OnListFragmentInteractionListener listener) {
        this.context = context;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_recipestepitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(Integer.toString(mValues.get(position).getId()));
        holder.mContentView.setText(mValues.get(position).getDescription());
        if(holder.mItem.getThumbnailURL() != null && !holder.mItem.getThumbnailURL().isEmpty()){
            Picasso.with(context)
                    .load(holder.mItem.getThumbnailURL())
                    .into(holder.imageView);
        }
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
        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageView imageView;
        public RecipeStep mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            imageView = (ImageView) view.findViewById(R.id.recipe_thumbnail_step);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    public void setList(List<RecipeStep> recipeStepList){
        mValues.clear();
        mValues.addAll(recipeStepList);
        notifyDataSetChanged();
    }


}
