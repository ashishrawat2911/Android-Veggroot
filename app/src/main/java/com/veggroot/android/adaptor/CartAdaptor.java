package com.veggroot.android.adaptor;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.veggroot.android.Database.ItemContract;
import com.veggroot.android.R;
import com.veggroot.android.model.Cart;
import com.veggroot.android.model.Vegetable;

import java.util.List;


public class CartAdaptor extends RecyclerView.Adapter<CartAdaptor.MyViewHolder> {
    private Context ctx;
    private List<Cart> categoriesList;

    private DatabaseReference mDatabase;
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView categoriesTitle, itemRate, noOfItems, marketPrice;
        ImageView categoriesImage;
        Button add, subtract;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mDatabase = FirebaseDatabase.getInstance().getReference();
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

                    updateList( getAdapterPosition(), true);
                }
            });
            subtract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (categoriesList.get( getAdapterPosition()).getTotalNumber() <= 0) {
                        removeFromList( getAdapterPosition());
                        Toast.makeText(ctx, " Item removed from cart", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    } else if (categoriesList.get( getAdapterPosition()).getTotalNumber() == 1) {
                        removeFromList( getAdapterPosition());
                        Toast.makeText(ctx, " Item removed from cart", Toast.LENGTH_SHORT).show();
                    } else {
                        updateList( getAdapterPosition(), false);
                    }
                }
            });
        }
    }

    public void removeFromList(int position) {
        mDatabase.child("user")
                .child(mAuth.getUid())
                .child("cart")
                .child(categoriesList.get(position).getItemName())
                .setValue(null);
        categoriesList.remove(position);
        notifyItemRemoved(position);
    }

 /*   public List<Cart> getCategoriesList() {
        return categoriesList;
    }*/

    private void updateList(int position, boolean b) {
        if (b) {
            mDatabase.child("user")
                    .child(mAuth.getUid())
                    .child("cart")
                    .child(categoriesList.get(position).getItemName())
                    .child("totalNumber").setValue(categoriesList.get(position).getTotalNumber() + 1);
            categoriesList.get(position).setTotalNumber(categoriesList.get(position).getTotalNumber() + 1);
        } else {
            mDatabase.child("user")
                    .child(mAuth.getUid())
                    .child("cart")
                    .child(categoriesList.get(position).getItemName())
                    .child("totalNumber").setValue(categoriesList.get(position).getTotalNumber() - 1);
            categoriesList.get(position).setTotalNumber(categoriesList.get(position).getTotalNumber() - 1);

        }
        notifyItemChanged(position);
    }

}
