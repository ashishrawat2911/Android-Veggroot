package com.veggroot.android.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.veggroot.android.adaptor.CategoriesAdaptor;
import com.veggroot.android.R;
import com.veggroot.android.model.Item;
import com.veggroot.android.model.Vegetable;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FruitsCategoryFragment extends Fragment {

    DatabaseReference myRef;

    ProgressBar progressBar;

    RecyclerView categoriesRecyclerView;
    CategoriesAdaptor categoriesAdaptor;


    public FruitsCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //instantiate RecyclerView
        categoriesRecyclerView = view.findViewById(R.id.categories_recycler_view);
        categoriesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        progressBar = view.findViewById(R.id.itemCategoryProgressBar);
        myRef = FirebaseDatabase.getInstance().getReference().child("item").child("fruits");
        myRef.keepSynced(true);

        loadData();
        //creating a new list

        return view;
    }

    private void loadData() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.VISIBLE);
                List<Vegetable> categoriesList = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Vegetable vegetable = dataSnapshot1.getValue(Vegetable.class);
                    Log.e("Fire base data change ", "onDataChange: " + vegetable.getItemName());
                    categoriesList.add(vegetable);
                }
                progressBar.setVisibility(View.GONE);
                categoriesAdaptor = new CategoriesAdaptor(getContext(), categoriesList);
                categoriesRecyclerView.setAdapter(categoriesAdaptor);
                categoriesAdaptor.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}