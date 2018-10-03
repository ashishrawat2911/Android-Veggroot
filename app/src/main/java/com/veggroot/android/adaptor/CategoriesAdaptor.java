package com.veggroot.android.adaptor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;


import com.veggroot.android.R;
import com.veggroot.android.model.Vegetable;

import java.util.ArrayList;
import java.util.List;

public class CategoriesAdaptor extends RecyclerView.Adapter<CategoriesAdaptor.MyViewHolder> implements Filterable {
    private Context ctx;
    private List<Vegetable> categoriesList;
    private List<Vegetable> mFilteredList;
    private OnItemClickListener onItemClickListener;

    public CategoriesAdaptor(Context ctx, List<Vegetable> categoriesList) {
        this.ctx = ctx;
        this.categoriesList = categoriesList;
        this.mFilteredList = categoriesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(ctx).inflate(R.layout.categories_item, parent, false);
        return new MyViewHolder(v,this);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.categoriesImage.setImageResource(mFilteredList.get(position).getImage());


        holder.categoriesTitle.setText(mFilteredList.get(position).getName());
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

                    List<Vegetable> filteredList = new ArrayList<>();

                    for (Vegetable vegetable : categoriesList) {

                        if (vegetable.getName().toLowerCase().contains(charString) || vegetable.getName().toUpperCase().contains(charString)) {

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
                mFilteredList = (List<Vegetable>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(MyViewHolder item, int position);
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CategoriesAdaptor adapter;
        TextView categoriesTitle;
        ImageView categoriesImage;
        CardView cardView;
        // CardView cardView;

        MyViewHolder(View itemView, CategoriesAdaptor parent) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.adapter = parent;
            categoriesTitle = itemView.findViewById(R.id.categories_title_text_view);
            categoriesImage = itemView.findViewById(R.id.categories_image_view);
            cardView = itemView.findViewById(R.id.card_view_show_card);

            }

        @Override
        public void onClick(View view) {
            final OnItemClickListener listener = adapter.getOnItemClickListener();
            if (listener != null) {
                listener.onItemClick(this, getAdapterPosition());
            }
        }
    }
    }

