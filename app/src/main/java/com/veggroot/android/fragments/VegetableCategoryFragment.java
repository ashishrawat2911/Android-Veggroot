package com.veggroot.android.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.veggroot.android.adaptor.CategoriesAdaptor;
import com.veggroot.android.R;
import com.veggroot.android.model.Vegetable;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class VegetableCategoryFragment extends Fragment {
    List<Vegetable> categoriesList;
    RecyclerView categoriesRecyclerView;
    CategoriesAdaptor categoriesAdaptor;


    public VegetableCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fruits_category, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //instantiate RecyclerView
        categoriesRecyclerView = view.findViewById(R.id.categories_recycler_view);


        //creating a new list

        loadCategories();
        categoriesAdaptor = new CategoriesAdaptor(getContext(), categoriesList);
        categoriesRecyclerView.setAdapter(categoriesAdaptor);
        //setting the layout of RecyclerView as Grid
        categoriesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        return view;
    }

    private void loadCategories() {
        categoriesList = new ArrayList<>();
        categoriesList.add(new Vegetable("Brinjal", R.drawable.call_image));
        categoriesList.add(new Vegetable("Broccoli", R.drawable.call_image));
        categoriesList.add(new Vegetable("Cabbage", R.drawable.call_image));
        categoriesList.add(new Vegetable("Cauliflower", R.drawable.call_image));
        categoriesList.add(new Vegetable("Chili", R.drawable.call_image));
        categoriesList.add(new Vegetable("Onion", R.drawable.call_image));
        categoriesList.add(new Vegetable("Potato", R.drawable.call_image));
        categoriesList.add(new Vegetable("Pumpkin", R.drawable.call_image));
        categoriesList.add(new Vegetable("Radish", R.drawable.call_image));
        categoriesList.add(new Vegetable("Tomato", R.drawable.call_image));
        categoriesList.add(new Vegetable("Brinjal", R.drawable.call_image));
        categoriesList.add(new Vegetable("Broccoli", R.drawable.call_image));
        categoriesList.add(new Vegetable("Cabbage", R.drawable.call_image));
        categoriesList.add(new Vegetable("Cauliflower", R.drawable.call_image));
        categoriesList.add(new Vegetable("Chili", R.drawable.call_image));
        categoriesList.add(new Vegetable("Onion", R.drawable.call_image));
        categoriesList.add(new Vegetable("Potato", R.drawable.call_image));
        categoriesList.add(new Vegetable("Pumpkin", R.drawable.call_image));
        categoriesList.add(new Vegetable("Radish", R.drawable.call_image));
        categoriesList.add(new Vegetable("Tomato", R.drawable.call_image));
        categoriesList.add(new Vegetable("Brinjal", R.drawable.call_image));
        categoriesList.add(new Vegetable("Broccoli", R.drawable.call_image));
        categoriesList.add(new Vegetable("Cabbage", R.drawable.call_image));
        categoriesList.add(new Vegetable("Cauliflower", R.drawable.call_image));
        categoriesList.add(new Vegetable("Chili", R.drawable.call_image));
        categoriesList.add(new Vegetable("Onion", R.drawable.call_image));
        categoriesList.add(new Vegetable("Potato", R.drawable.call_image));
        categoriesList.add(new Vegetable("Pumpkin", R.drawable.call_image));
        categoriesList.add(new Vegetable("Radish", R.drawable.call_image));
        categoriesList.add(new Vegetable("Tomato", R.drawable.call_image));
    }

 /*   @Override
    public void search(String ppkt) {
        if (categoriesAdaptor != null)
            categoriesAdaptor.getFilter().filter(ppkt.toLowerCase());

    }
*/
}
