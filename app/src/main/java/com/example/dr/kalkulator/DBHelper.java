package com.example.dr.kalkulator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by DR on 11.12.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 6;

    private static final String DATABASE_NAME = "db.db";

    String CREATE_TABLE_RESULT = "CREATE TABLE " + Result.TABLE  + "("
            + Result.KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + Result.KEY_name + " TEXT, "
            + Result.KEY_value + " TEXT )";

    String CREATE_TABLE_HISTORY = "CREATE TABLE " + History.TABLE  + "("
            + History.KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + History.KEY_expression + " TEXT, "
            + History.KEY_result + " TEXT )";

    public DBHelper(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_RESULT);
        db.execSQL(CREATE_TABLE_HISTORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Result.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + History.TABLE);
        onCreate(db);
    }
}
