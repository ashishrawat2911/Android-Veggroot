package com.veggroot.android.adaptor;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.veggroot.android.R;
import com.veggroot.android.model.Cart;

import java.util.List;


/**
 * created by Ashish Rawat
 */

public class CartAdaptor extends RecyclerView.Adapter<CartAdaptor.MyViewHolder> {
    private Context ctx;
    private List<Cart> categoriesList;

    private DatabaseReference mDatabase;
    private DatabaseReference updateValueDatabase;
    private FirebaseAuth mAuth;

    public CartAdaptor(Context ctx, List<Cart> categoriesList) {
        this.ctx = ctx;
        this.categoriesList = categoriesList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;
        v = LayoutInflater.from(ctx).inflate(R.layout.cart_tem, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {

        myViewHolder.categoriesTitle.setText(categoriesList.get(position).getItemName());
        Glide.with(ctx)
                .load(categoriesList.get(position).getItemImage())
                .into(myViewHolder.categoriesImage);
        myViewHolder.itemRate.setText("Rs " + categoriesList.get(position).getCost() + " / " + categoriesList.get(position).getUnit());
        myViewHolder.noOfItems.setText("" + categoriesList.get(position).getTotalNumber());
        myViewHolder.marketPrice.setText("Rs " + categoriesList.get(position).getMarketPrice() + " / " + categoriesList.get(position).getUnit());
        myViewHolder.marketPrice.setPaintFlags(myViewHolder.marketPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    private void updateList(int position, boolean b) {
        updateValueDatabase = FirebaseDatabase.getInstance().getReference().child("user")
                .child(mAuth.getUid())
                .child("cart")
                .child(categoriesList.get(position).getItemName())
                .child("totalNumber");
        try {

            if (b) {
                updateValueDatabase.setValue(categoriesList.get(position).getTotalNumber() + 1);
                categoriesList.get(position).setTotalNumber(categoriesList.get(position).getTotalNumber() + 1);
            } else {
                updateValueDatabase.setValue(categoriesList.get(position).getTotalNumber() - 1);
                categoriesList.get(position).setTotalNumber(categoriesList.get(position).getTotalNumber() - 1);

            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        notifyItemChanged(position);
    }

    public void removeFromList(int position) {
        String itemName = categoriesList.get(position).getItemName();
        Toast.makeText(ctx, itemName + " removed from cart", Toast.LENGTH_SHORT).show();
        mDatabase.child("user")
                .child(mAuth.getUid())
                .child("cart")
                .child(itemName)
                .setValue(null);
        categoriesList.remove(position);
        notifyItemRemoved(position);
    }

 /*   public List<Cart> getCategoriesList() {
        return categoriesList;
    }*/

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView categoriesTitle, itemRate, noOfItems, marketPrice;
        ImageView categoriesImage;
        Button add, subtract;


        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mDatabase = FirebaseDatabase.getInstance().getReference();
            updateValueDatabase = FirebaseDatabase.getInstance().getReference();
            mAuth = FirebaseAuth.getInstance();
            categoriesTitle = itemView.findViewById(R.id.cart_title_text_view);
            categoriesImage = itemView.findViewById(R.id.cart_image_view);
            itemRate = itemView.findViewById(R.id.cart_item_rate);
            noOfItems = itemView.findViewById(R.id.cartNumber_of_items);
            add = itemView.findViewById(R.id.cartAdd_value);
            marketPrice = itemView.findViewById(R.id.cart_market_price);
            subtract = itemView.findViewById(R.id.cartSubtractValue);

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateList(getAdapterPosition(), true);
                }
            });
            subtract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int totalNumber = categoriesList.get(getAdapterPosition()).getTotalNumber();
                    if (totalNumber <= 1) {
                        removeFromList(getAdapterPosition());
                    } else {
                        updateList(getAdapterPosition(), false);
                    }
                }
            });
        }
    }

}
