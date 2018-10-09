package com.veggroot.android.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.veggroot.android.Database.ItemContract.ItemEntry;

import static com.veggroot.android.Database.ItemContract.ItemEntry.ITEM_ID;
import static com.veggroot.android.Database.ItemContract.ItemEntry._ID;

public class ItemDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "item.db";

    public ItemDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_TABLE = "CREATE TABLE " +
                ItemEntry.TABLE_NAME + " (" +
                ItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ItemEntry.ITEM_ID + " INTEGER NOT NULL, " +
                ItemEntry.ITEM_NAME + " TEXT NOT NULL, " +
                ItemEntry.ITEM_IMAGE + " TEXT NOT NULL, " +
                ItemEntry.COST_PER_KG + " TEXT NOT NULL, " +
                ItemEntry.TOTAL_NUMBER + " Integer NOT NULL);";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ItemEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
