package com.veggroot.android.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.veggroot.android.Database.ItemContract.ItemEntry;
import com.veggroot.android.Database.ItemDbHelper;
import com.veggroot.android.model.Item;

import java.util.ArrayList;
import java.util.List;

public class OnCart {
    public static void AddToCartList(Context context,
                                     Integer itemId,
                                     String itemName,
                                     String itemImage,
                                     String costPerKg,
                                     Integer totalNumber) {

        if (itemId == null) return;
        ItemDbHelper databaseHelper = new ItemDbHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        if (!isItemAdded(context, itemId)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ItemEntry.ITEM_ID, itemId);
            contentValues.put(ItemEntry.ITEM_NAME, itemName);
            contentValues.put(ItemEntry.ITEM_IMAGE, itemImage);
            contentValues.put(ItemEntry.COST_PER_KG, costPerKg);
            contentValues.put(ItemEntry.TOTAL_NUMBER, totalNumber);
            database.insert(ItemEntry.TABLE_NAME, null, contentValues);
        }
        database.close();
    }

    public static boolean isItemAdded(Context context, Integer itemId) {
        if (itemId == null) return false;
        ItemDbHelper databaseHelper = new ItemDbHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        boolean isItemAdded;
        Cursor cursor = database.query(ItemEntry.TABLE_NAME,
                null,
                ItemEntry.ITEM_ID + " = " + itemId,
                null,
                null,
                null,
                null);
        if (cursor.getCount() == 1)
            isItemAdded = true;
        else
            isItemAdded = false;

        cursor.close();
        database.close();
        return isItemAdded;
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

    public static void removeListFromCart(Context context, Integer itemId) {
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
        ItemDbHelper databaseHelper = new ItemDbHelper(context);
        SQLiteDatabase databaseW = databaseHelper.getWritableDatabase();
        int totalNumber = getTotalNumber(context, itemId);
        ContentValues contentValues = new ContentValues();
        contentValues.put(ItemEntry.TOTAL_NUMBER, totalNumber + 1);
        databaseW.update(
                ItemEntry.TABLE_NAME,
                contentValues,
                ItemEntry.ITEM_ID + " = " + itemId,
                null);

        databaseW.close();

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
}