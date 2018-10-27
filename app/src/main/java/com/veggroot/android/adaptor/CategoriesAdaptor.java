package com.veggroot.android.adaptor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
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
import com.veggroot.android.model.Cart;
import com.veggroot.android.model.Items;

import java.util.ArrayList;
import java.util.List;

public class CategoriesAdaptor extends RecyclerView.Adapter<CategoriesAdaptor.MyViewHolder> implements Filterable {
    private Context ctx;
    private List<Items> categoriesList;
    private List<Items> mFilteredList;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    public CategoriesAdaptor(Context ctx, List<Items> categoriesList) {
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
        Glide.with(ctx)
                .load(mFilteredList.get(position).getItemImage())
                .into(holder.categoriesImage);
        holder.categoriesTitle.setText(mFilteredList.get(position).getItemName());
        holder.marketPrice.setText("Rs " + mFilteredList.get(position).getMarketPrice() + " / " + mFilteredList.get(position).getUnit());
        holder.itemRate.setText("Rs " + mFilteredList.get(position).getCost() + " / " + mFilteredList.get(position).getUnit());
        holder.marketPrice.setPaintFlags(holder.marketPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("user").child(mAuth.getUid()).child("cart").child(mFilteredList.get(position).getItemName()).exists()) {
                    holder.addToCartButton.setText(R.string.added_to_cart);
                    holder.addToCartButton.setEnabled(false);
                } else {
                    holder.addToCartButton.setText(R.string.add);
                    holder.addToCartButton.setEnabled(true);
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

                    List<Items> filteredList = new ArrayList<>();

                    for (Items items : categoriesList) {

                        if (items.getItemName().toLowerCase().contains(charString) || items.getItemName().toUpperCase().contains(charString)) {

                            filteredList.add(items);
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
                mFilteredList = (List<Items>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView categoriesTitle, itemRate, marketPrice;
        ImageView categoriesImage;
        Button addToCartButton;

        MyViewHolder(final View itemView) {
            super(itemView);
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mAuth = FirebaseAuth.getInstance();
            categoriesTitle = itemView.findViewById(R.id.categories_title_text_view);
            categoriesImage = itemView.findViewById(R.id.categories_image_view);
            marketPrice = itemView.findViewById(R.id.categories_market_price);
            itemRate = itemView.findViewById(R.id.item_rate);
            addToCartButton = itemView.findViewById(R.id.addToCart);
            addToCartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.child("user").child(mAuth.getUid()).child("cart").child(mFilteredList.get(getAdapterPosition()).getItemName()).exists()) {
                                Cart cart = new Cart(mFilteredList.get(getAdapterPosition()).getCost(),
                                        mFilteredList.get(getAdapterPosition()).getItemId(),
                                        mFilteredList.get(getAdapterPosition()).getItemName(),
                                        mFilteredList.get(getAdapterPosition()).getItemImage()
                                        , mFilteredList.get(getAdapterPosition()).getMarketPrice(),
                                        1,
                                        mFilteredList.get(getAdapterPosition()).getUnit());
                                mDatabase.child("user")
                                        .child(mAuth.getUid())
                                        .child("cart")
                                        .child(mFilteredList.get(getAdapterPosition()).getItemName())
                                        .setValue(cart);
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
}

