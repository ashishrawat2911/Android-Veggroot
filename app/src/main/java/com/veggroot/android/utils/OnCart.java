package com.veggroot.android.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.veggroot.android.Database.ItemContract.ItemEntry;
import com.veggroot.android.Database.ItemDbHelper;
import com.veggroot.android.activities.AddInfoActivity;
import com.veggroot.android.activities.MainCategoryActivity;
import com.veggroot.android.activities.VerifyPhoneActivity;
import com.veggroot.android.model.Item;

import java.util.ArrayList;
import java.util.List;

public class OnCart {


    public static void AddToCartList(final Integer itemId,
                                     final String itemName,
                                     final String itemImage,
                                     final String costPerKg,
                                     final Integer totalNumber) {
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (!isItemAdded(itemId)) {
            Item item = new Item(itemId, itemName, itemImage, costPerKg, totalNumber);
            mDatabase.child("user").child(mAuth.getUid()).child("cart").child(String.valueOf(itemId)).setValue(item);
        }


    }

    private void checkDataExistsOrNot() {


    }

    public static boolean isItemAdded(final Integer itemId) {
        if (itemId == null) return false;
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        final boolean[] isItemAdded = new boolean[1];
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.child("user").child(mAuth.getUid()).child(String.valueOf(itemId)).exists()) {
                    isItemAdded[0] = false;
                } else isItemAdded[0] = true;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return isItemAdded[0];
    }

    public static void addValuetoTotal(final Integer itemId) {
        if (itemId == null) return;
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final int[] total = new int[1];
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               total[0] = (Integer) dataSnapshot.child("user").child(mAuth.getUid()).child(String.valueOf(itemId)).child("totalNumber").getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if(OnCart.isItemAdded(itemId)){
            mDatabase.child("user")
                    .child(mAuth.getUid())
                    .child("cart")
                    .child(String.valueOf(itemId))
                    .child("totalNumber").setValue(2);

        }
    }

    public static List<Item> getCartAddedList(Context context) {
        ItemDbHelper databaseHelper = new ItemDbHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        List<Item> cartList = new ArrayList<>();
        Cursor cursor = database.query(ItemEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                ItemEntry._ID + " DESC");
        while (cursor.moveToNext()) {
            int movieId = cursor.getInt(cursor.getColumnIndex(ItemEntry.ITEM_ID));
            String itemName = cursor.getString(cursor.getColumnIndex(ItemEntry.ITEM_NAME));
            String itemImage = cursor.getString(cursor.getColumnIndex(ItemEntry.ITEM_IMAGE));
            String costPerKg = cursor.getString(cursor.getColumnIndex(ItemEntry.COST_PER_KG));
            int totalNumber = cursor.getInt(cursor.getColumnIndex(ItemEntry.TOTAL_NUMBER));

            cartList.add(new Item(movieId, itemName, itemImage, costPerKg, totalNumber));
        }
        cursor.close();
        database.close();
        return cartList;
    }

  /*  public static void removeListFromCart(Context context, Integer itemId) {
        if (itemId == null) return;
        ItemDbHelper databaseHelper = new ItemDbHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        if (isItemAdded(context, itemId)) {
            database.delete(ItemEntry.TABLE_NAME, ItemEntry.ITEM_ID + " = " + itemId, null);
        }
        database.close();
    }

    public static void addValuetoTotal(Context context, Integer itemId) {
        if (itemId == null) return;


    }

    public static void subValueFromTotal(Context context, Integer itemId) {
        if (itemId == null) return;
        ItemDbHelper databaseHelper = new ItemDbHelper(context);
        SQLiteDatabase databaseW = databaseHelper.getWritableDatabase();
        if (isItemAdded(context, itemId)) {
            if (getTotalNumber(context, itemId) <= 0) {
                removeListFromCart(context, itemId);
            } else {
                ContentValues contentValues = new ContentValues();
                contentValues.put(ItemEntry.TOTAL_NUMBER, getTotalNumber(context, itemId) - 1);
                databaseW.update(
                        ItemEntry.TABLE_NAME,
                        contentValues,
                        ItemEntry.ITEM_ID + " = " + itemId,
                        null);
                if (getTotalNumber(context, itemId) <= 0) {
                    if (isItemAdded(context, itemId)) {
                        removeListFromCart(context, itemId);
                    }
                }

                databaseW.close();
            }
        } else if (!isItemAdded(context, itemId)) {
            Toast.makeText(context, "Item removed", Toast.LENGTH_SHORT).show();
        }


    }

    public static int getTotalNumber(Context context, Integer itemId) {
        ItemDbHelper databaseHelper = new ItemDbHelper(context);
        SQLiteDatabase databaseR = databaseHelper.getReadableDatabase();
        Cursor cursor = databaseR.query(ItemEntry.TABLE_NAME,
                null,
                ItemEntry.ITEM_ID + " = " + itemId,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex(ItemEntry.TOTAL_NUMBER));
    }
}*/
}