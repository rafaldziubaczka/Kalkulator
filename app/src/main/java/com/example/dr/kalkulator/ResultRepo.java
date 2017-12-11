package com.example.dr.kalkulator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DR on 11.12.2017.
 */

public class ResultRepo {
    private DBHelper dbHelper;

    public ResultRepo(Context context) {
        dbHelper = new DBHelper(context);
    }

    public int insert(Result result) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Result.KEY_name, result.getName());
        values.put(Result.KEY_value, result.getValue());

        long resultId = db.insert(Result.TABLE, null, values);
        db.close();
        return (int) resultId;
    }

    public void delete(Long resultId) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(Result.TABLE, Result.KEY_ID + "= ?", new String[]{String.valueOf(resultId)});
        db.close();
    }

    public void update(Result result) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Result.KEY_value, result.getValue());
        values.put(Result.KEY_name, result.getName());

        db.update(Result.TABLE, values, Result.KEY_ID + "= ?", new String[]{String.valueOf(result.getId())});
        db.close();
    }

    public Long count(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Long count = DatabaseUtils.queryNumEntries(db, Result.TABLE);
        db.close();
        return count;
    }

    public List<Result> getStudentList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT  " +
                Result.KEY_ID + "," +
                Result.KEY_name + "," +
                Result.KEY_value +
                " FROM " + Result.TABLE;

        List<Result> resultList = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Result result = new Result();
                result.setId(cursor.getLong(cursor.getColumnIndex(Result.KEY_ID)));
                result.setName(cursor.getString(cursor.getColumnIndex(Result.KEY_name)));
                result.setValue(cursor.getDouble(cursor.getColumnIndex(Result.KEY_value)));
                resultList.add(result);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return resultList;
    }
}
