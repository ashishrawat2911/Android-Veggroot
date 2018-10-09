package com.veggroot.android.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.veggroot.android.Database.ItemContract;
import com.veggroot.android.Database.ItemDbHelper;
import com.veggroot.android.R;
import com.veggroot.android.adaptor.CartAdaptor;
import com.veggroot.android.adaptor.CategoriesAdaptor;

public class CartActivity extends AppCompatActivity {
    ItemDbHelper databaseHelper;
    SQLiteDatabase database ;
    Cursor cursor;
    RecyclerView cartRecyclerView;
    CartAdaptor cartAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        databaseHelper = new ItemDbHelper(CartActivity.this);
        database = databaseHelper.getReadableDatabase();

        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        cursor=getCartItems();
        cartAdaptor = new CartAdaptor(this, cursor);
        cartRecyclerView.setAdapter(cartAdaptor);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    Cursor getCartItems() {
      return   cursor = database.query(ItemContract.ItemEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                ItemContract.ItemEntry.ITEM_ID + " DESC");
    }
}
