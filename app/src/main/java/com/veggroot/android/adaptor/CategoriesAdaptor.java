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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.veggroot.android.R;
import com.veggroot.android.model.Vegetable;
import com.veggroot.android.utils.OnCart;

import java.util.ArrayList;
import java.util.List;

public class CategoriesAdaptor extends RecyclerView.Adapter<CategoriesAdaptor.MyViewHolder> implements Filterable {
    private Context ctx;
    private List<Vegetable> categoriesList;
    private List<Vegetable> mFilteredList;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

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
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Toast.makeText(ctx, mFilteredList.get(0).getItemName(), Toast.LENGTH_SHORT).show();
        Glide.with(ctx)
                .load(mFilteredList.get(position).getItemImage())
                .into(holder.categoriesImage);
        holder.categoriesTitle.setText(mFilteredList.get(position).getItemName());
        holder.itemRate.setText(mFilteredList.get(position).getCostPerKg());
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("user").child(mAuth.getUid()).child("cart").child(String.valueOf(mFilteredList.get(position).getItemId())).exists()) {
                    holder.subtract.setText("Added to cart");
                    holder.subtract.setEnabled(false);
                }else {
                    holder.subtract.setText("add");
                    holder.subtract.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
                mFilteredList = (List<Vegetable>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CategoriesAdaptor adapter;
        TextView categoriesTitle, itemRate, noOfItems;
        ImageView categoriesImage;
        Button subtract;


        MyViewHolder(final View itemView) {
            super(itemView);
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mAuth = FirebaseAuth.getInstance();
            categoriesTitle = itemView.findViewById(R.id.categories_title_text_view);
            categoriesImage = itemView.findViewById(R.id.categories_image_view);
            itemRate = itemView.findViewById(R.id.item_rate);


            subtract = itemView.findViewById(R.id.addToCart);

            subtract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.child("user").child(mAuth.getUid()).child("cart").child(String.valueOf(mFilteredList.get(getAdapterPosition()).getItemId())).exists()) {
                                OnCart.AddToCartList(mFilteredList.get(getAdapterPosition()).getItemId(),
                                        mFilteredList.get(getAdapterPosition()).getItemName(),
                                        mFilteredList.get(getAdapterPosition()).getItemImage(),
                                        mFilteredList.get(getAdapterPosition()).getCostPerKg(),
                                        1);
                                Toast.makeText(ctx, mFilteredList.get(getAdapterPosition()).getItemName() + " Added to cart", Toast.LENGTH_SHORT).show();

                            } else
                                Toast.makeText(ctx, mFilteredList.get(getAdapterPosition()).getItemName() + " Already Added ", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(ctx, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(ctx, databaseError.getDetails(), Toast.LENGTH_SHORT).show();

                        }
                    });


                }
            });

        }


    }

    void setList(int position) {


        notifyDataSetChanged();
    }


}

