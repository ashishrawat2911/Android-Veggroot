package com.veggroot.android.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.veggroot.android.adaptor.CategoriesAdaptor;
import com.veggroot.android.R;
import com.veggroot.android.model.Item;
import com.veggroot.android.model.Vegetable;
import com.veggroot.android.utils.Constants;
import com.veggroot.android.utils.OnCart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class VegetableCategoryFragment extends Fragment {
    List<Item> categoriesList = new ArrayList<>();
    RecyclerView categoriesRecyclerView;
    CategoriesAdaptor categoriesAdaptor;


    public VegetableCategoryFragment() {
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


        //creating a new list

        loadData();


        //setting the layout of RecyclerView as Grid
        categoriesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        return view;
    }

    private void loadData() {

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.CATEGORIES_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray array = object.getJSONArray("vegetables");
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject jo = array.getJSONObject(i);

                                Item questionsAnswers = new Item(
                                        jo.getInt("item_id"),
                                        jo.getString("item_name"),
                                        jo.getString("item_image"),
                                        jo.getString("cost_per_kg"),
                                        getList(getContext(), jo.getInt("item_id")));
                                Log.e("RESPONSE", "onResponse: " + jo.getInt("item_id") +
                                        jo.getString("item_name") +
                                        jo.getString("cost_per_kg") +
                                        jo.getString("item_image") +
                                        0);
                                categoriesList.add(questionsAnswers);
                                categoriesAdaptor = new CategoriesAdaptor(getContext(), categoriesList);
                                categoriesRecyclerView.setAdapter(categoriesAdaptor);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error" + error.toString(), Toast.LENGTH_SHORT).show();


            }
        });
        RequestQueue queue = Volley.newRequestQueue(getContext());
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    /*   @Override
       public void search(String ppkt) {
           if (categoriesAdaptor != null)
               categoriesAdaptor.getFilter().filter(ppkt.toLowerCase());

       }
   */
    Integer getList(Context context, Integer itemId) {
        if (OnCart.isItemAdded(context, itemId)) {
            Log.e("list", "getList: " + OnCart.getTotalNumber(context, itemId));
            return OnCart.getTotalNumber(context, itemId);
        } else {
            Log.e("list", "getList: " + 0);
            return 0;
        }
    }
}
