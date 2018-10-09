package com.veggroot.android.adaptor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.veggroot.android.Database.ItemContract;
import com.veggroot.android.R;
import com.veggroot.android.model.Item;
import com.veggroot.android.model.Vegetable;
import com.veggroot.android.utils.OnCart;

import java.util.ArrayList;
import java.util.List;

public class CategoriesAdaptor extends RecyclerView.Adapter<CategoriesAdaptor.MyViewHolder> implements Filterable {
    private Context ctx;
    private List<Item> categoriesList;
    private List<Item> mFilteredList;


    public CategoriesAdaptor(Context ctx, List<Item> categoriesList) {
        this.ctx = ctx;
        this.categoriesList = categoriesList;
        this.mFilteredList = categoriesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(ctx).inflate(R.layout.categories_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Glide.with(ctx)
                .load(mFilteredList.get(position).getItemImage())
                .into(holder.categoriesImage);
        holder.categoriesTitle.setText(mFilteredList.get(position).getItemName());
        holder.itemRate.setText(mFilteredList.get(position).getCostPerKg());
        holder.noOfItems.setText(mFilteredList.get(position).totalNumber + "");

    }


    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = categoriesList;
                } else {

                    List<Item> filteredList = new ArrayList<>();

                    for (Item vegetable : categoriesList) {

                        if (vegetable.getItemName().toLowerCase().contains(charString) || vegetable.getItemName().toUpperCase().contains(charString)) {

                            filteredList.add(vegetable);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (List<Item>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CategoriesAdaptor adapter;
        TextView categoriesTitle, itemRate, noOfItems;
        ImageView categoriesImage;
        Button add, subtract;


        MyViewHolder(final View itemView) {
            super(itemView);

            categoriesTitle = itemView.findViewById(R.id.categories_title_text_view);
            categoriesImage = itemView.findViewById(R.id.categories_image_view);
            itemRate = itemView.findViewById(R.id.item_rate);
            noOfItems = itemView.findViewById(R.id.number_of_items);
            add = itemView.findViewById(R.id.add_value);
            subtract = itemView.findViewById(R.id.subtractValue);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!OnCart.isItemAdded(ctx, mFilteredList.get(getAdapterPosition()).getItemId())) {
                        OnCart.AddToCartList(ctx,
                                mFilteredList.get(getAdapterPosition()).getItemId(),
                                mFilteredList.get(getAdapterPosition()).getItemName(),
                                mFilteredList.get(getAdapterPosition()).getItemImage(),
                                mFilteredList.get(getAdapterPosition()).getCostPerKg(), 1);
                        setList(getAdapterPosition());
                    } else {
                        OnCart.addValuetoTotal(ctx, mFilteredList.get(getAdapterPosition()).getItemId());
                        setList(getAdapterPosition());
                    }
                }
            });
            subtract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnCart.subValueFromTotal(ctx, mFilteredList.get(getAdapterPosition()).getItemId());
                    setList(getAdapterPosition());
                }
            });


        }


    }

    void setList(int position) {

        try{
            mFilteredList.get(position).setTotalNumber(OnCart.getTotalNumber(ctx, mFilteredList.get(position).getItemId()));

        }catch (Exception e){
            mFilteredList.get(position).setTotalNumber(0);
        }


        notifyDataSetChanged();
    }


}

