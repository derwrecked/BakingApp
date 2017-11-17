package com.derekudacityclassprojects.bakingapp.FragmentRecipeList;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.derekudacityclassprojects.bakingapp.JSONUtils;
import com.derekudacityclassprojects.bakingapp.NetworkChangeReceiver;
import com.derekudacityclassprojects.bakingapp.R;
import com.derekudacityclassprojects.bakingapp.RecipeRepository;
import com.derekudacityclassprojects.bakingapp.SimpleIdlingResource;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class RecipeListFragment extends Fragment {

    public static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 3;
    private OnListFragmentInteractionListener mListener;
    // receiver to detect network changes
    private NetworkChangeReceiver networkChangeReceiver;
    // intent filter for br
    private IntentFilter intentFilter;
    private Recipe[] recipes;
    private String jsonString;
    private String EXTRA_JSON_STRING = "EXTRA_JSON_STRING";
    private ProgressBar progressBar;
    private MyRecipeListRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private TextView couldNotFetchTextView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeListFragment() {
    }

    public static RecipeListFragment newInstance(int columnCount) {
        RecipeListFragment fragment = new RecipeListFragment();
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

        // initialize intent filter for network broadcast receiver
        intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(EXTRA_JSON_STRING, jsonString);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipeitem_list, container, false);
        if (savedInstanceState != null) {
            jsonString = savedInstanceState.getString(EXTRA_JSON_STRING);
        }
        Context context = view.getContext();
        recyclerView = view.findViewById(R.id.recipe_list_fragment_recycler_view);
        progressBar = view.findViewById(R.id.recipe_list_fragment_progress_bar);
        couldNotFetchTextView = view.findViewById(R.id.recipe_list_fragment_could_not_fetch);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float scaleFactor = metrics.density;
        float widthDp = metrics.widthPixels / scaleFactor;

        if (widthDp <= 600) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        if (jsonString != null) {
            recipes = JSONUtils.getAllRecipesFromString(jsonString);
        }
        recyclerView.setAdapter(new MyRecipeListRecyclerViewAdapter(recipes, getContext(), mListener));

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

    @Override
    public void onResume() {
        // setup broadcast receiver for online connection monitor
        networkChangeReceiver = new NetworkChangeReceiver();
        getActivity().registerReceiver(networkChangeReceiver, intentFilter);
        super.onResume();
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(networkChangeReceiver);
        super.onPause();
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
        void onListFragmentInteraction(Recipe item);
    }

    public void setCouldNotFetch() {
        adapter = (MyRecipeListRecyclerViewAdapter) recyclerView.getAdapter();
        if(adapter.getmValues() == null || adapter.getmValues().size() == 0){
            progressBar.setVisibility(View.GONE);
            couldNotFetchTextView.setVisibility(View.VISIBLE);
        }else{
            // this will occur when a previous value is used.
            progressBar.setVisibility(View.GONE);
            couldNotFetchTextView.setVisibility(View.GONE);
        }
    }

    public void setFetchingProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
        couldNotFetchTextView.setVisibility(View.GONE);
    }

    public void setRecipeList(String jsonString, Recipe[] recipes) {
        progressBar.setVisibility(View.GONE);
        couldNotFetchTextView.setVisibility(View.INVISIBLE);
        this.jsonString = jsonString;
        this.recipes = recipes;
        RecipeRepository.createInstance(recipes);
        adapter = (MyRecipeListRecyclerViewAdapter) recyclerView.getAdapter();
        adapter.setmValues(recipes);
    }
}
