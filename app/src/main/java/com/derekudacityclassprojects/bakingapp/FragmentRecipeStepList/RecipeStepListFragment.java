package com.derekudacityclassprojects.bakingapp.FragmentRecipeStepList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.derekudacityclassprojects.bakingapp.FragmentRecipeList.RecipeStep;
import com.derekudacityclassprojects.bakingapp.IngredientsActivity;
import com.derekudacityclassprojects.bakingapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class RecipeStepListFragment extends Fragment {
    public static final String EXTRA_RECIPE_ID = "EXTRA_RECIPE_ID";
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private Button ingredientsButton;
    private RecyclerView recyclerView;
    private final int INGREDIENTS_ACTIVITY_REQUEST_CODE = 500;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeStepListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static RecipeStepListFragment newInstance(int columnCount) {
        RecipeStepListFragment fragment = new RecipeStepListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipestepitem_list, container, false);
        ingredientsButton = view.findViewById(R.id.recipe_step_list_ingredients_button);
        ingredientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), IngredientsActivity.class);
                intent.putExtra(EXTRA_RECIPE_ID, mListener.getCurrentRecipeId());
                startActivityForResult(intent, INGREDIENTS_ACTIVITY_REQUEST_CODE);
            }
        });

        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.recipe_step_list_recycler_view);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        recyclerView.setAdapter(new MyRecipeStepListRecyclerViewAdapter(getContext(), new ArrayList<RecipeStep>(), mListener));

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(RecipeStep item);

        int getCurrentRecipeId();
    }

    public void setListRecipeList(List<RecipeStep> recipeStepList) {
        MyRecipeStepListRecyclerViewAdapter adapter = (MyRecipeStepListRecyclerViewAdapter) recyclerView.getAdapter();
        adapter.setList(recipeStepList);
    }
}
