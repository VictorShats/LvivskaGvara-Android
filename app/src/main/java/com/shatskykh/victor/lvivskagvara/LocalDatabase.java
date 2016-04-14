package com.shatskykh.victor.lvivskagvara;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;
import java.util.Vector;

public class LocalDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "myitems";
    private static final String ITEM_TABLE = "items";
    private static final String ITEM_NAME_COL = "item_name";
    private static final String ITEMS_TABLE_CREATE_SQL = "CREATE TABLE " + ITEM_TABLE + " ( " + ITEM_NAME_COL + " TEXT )";

    LocalDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ITEMS_TABLE_CREATE_SQL);
    }

    public void fillDb(List<String> words) {
        for (String word : words) {
            SQLiteDatabase db = getWritableDatabase();
            addItem(word, db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + ITEM_TABLE);
        db.execSQL(ITEMS_TABLE_CREATE_SQL);
    }

    public long addItem(String item_name, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(ITEM_NAME_COL, item_name);
        if (db != null) {
            return db.insert(ITEM_TABLE, null, values);
        }
        return getWritableDatabase().insert(ITEM_TABLE, null, values);
    }

    public Vector<String> getItems() {
        Vector<String> ret = new Vector<String>();

        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + ITEM_TABLE, null);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ret.add(cursor.getString(cursor.getColumnIndex(ITEM_NAME_COL)));
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }
        return ret;
    }

    public void deleteItem(String item_name) {
        getWritableDatabase().delete(ITEM_TABLE, ITEM_NAME_COL + " = ?", new String[]{item_name});
    }
}