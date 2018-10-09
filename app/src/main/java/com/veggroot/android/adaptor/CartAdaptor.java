package com.veggroot.android.adaptor;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.veggroot.android.Database.ItemContract;
import com.veggroot.android.R;


public class CartAdaptor extends RecyclerView.Adapter<CartAdaptor.MyViewHolder> {
    private Context ctx;
    private Cursor cursor;

    public

    CartAdaptor(Context ctx, Cursor cursor) {
        this.ctx = ctx;
        this.cursor = cursor;
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
        if (!cursor.moveToPosition(position))
            return; // bail if returned null
        myViewHolder.categoriesTitle.setText(cursor.getString(cursor.getColumnIndex(ItemContract.ItemEntry.ITEM_NAME)));
        Glide.with(ctx)
                .load(cursor.getString(cursor.getColumnIndex(ItemContract.ItemEntry.ITEM_IMAGE)))
                .into(myViewHolder.categoriesImage);
        myViewHolder.itemRate.setText(cursor.getString(cursor.getColumnIndex(ItemContract.ItemEntry.COST_PER_KG)));
        myViewHolder.noOfItems.setText(cursor.getString(cursor.getColumnIndex(ItemContract.ItemEntry.TOTAL_NUMBER)));
    }

    @Override
    public int getItemCount() {
        if (cursor == null) {
            return 0;
        }
        return cursor.getCount();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView categoriesTitle, itemRate, noOfItems;
        ImageView categoriesImage;
        Button add, subtract;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            categoriesTitle = itemView.findViewById(R.id.cart_title_text_view);
            categoriesImage = itemView.findViewById(R.id.cart_image_view);
            itemRate = itemView.findViewById(R.id.cart_item_rate);
            noOfItems = itemView.findViewById(R.id.cartNumber_of_items);
            add = itemView.findViewById(R.id.cartAdd_value);
            subtract = itemView.findViewById(R.id.cartSubtractValue);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            subtract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    }
}
