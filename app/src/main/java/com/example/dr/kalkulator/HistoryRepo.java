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

public class HistoryRepo {
    private DBHelper dbHelper;

    public HistoryRepo(Context context) {
        dbHelper = new DBHelper(context);
    }

    public int insert(History history) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(History.KEY_expression, history.getExpression());
        values.put(History.KEY_result, history.getResult());

        long historyId = db.insert(History.TABLE, null, values);
        db.close();
        return (int) historyId;
    }

    public void delete(Long historyId) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(History.TABLE, History.KEY_ID + "= ?", new String[]{String.valueOf(historyId)});
        db.close();
    }

    public Long count() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Long count = DatabaseUtils.queryNumEntries(db, History.TABLE);
        db.close();
        return count;
    }

    public List<History> getHistoryList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT  " +
                History.KEY_ID + "," +
                History.KEY_expression + "," +
                History.KEY_result +
                " FROM " + History.TABLE +
                " ORDER BY " +
                History.KEY_ID +
                " DESC;";

        List<History> historyList = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                History history = new History();
                history.setId(cursor.getLong(cursor.getColumnIndex(History.KEY_ID)));
                history.setExpression(cursor.getString(cursor.getColumnIndex(History.KEY_expression)));
                history.setResult(cursor.getString(cursor.getColumnIndex(History.KEY_result)));
                historyList.add(history);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return historyList;
    }

    public Long getFirstId() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT  " +
                History.KEY_ID +
                " FROM " + History.TABLE +
                " LIMIT 1;";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            Long id = cursor.getLong(cursor.getColumnIndex(History.KEY_ID));
            cursor.close();
            db.close();
            return id;
        }
        return null;
    }

    public History getLast() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT  " +
                History.KEY_ID + "," +
                History.KEY_expression + "," +
                History.KEY_result +
                " FROM " + History.TABLE +
                " ORDER BY " +
                History.KEY_ID +
                " DESC LIMIT 1;";


        Cursor cursor = db.rawQuery(selectQuery, null);
        History history = new History();
        if (cursor.moveToFirst()) {

            history.setId(cursor.getLong(cursor.getColumnIndex(History.KEY_ID)));
            history.setExpression(cursor.getString(cursor.getColumnIndex(History.KEY_expression)));
            history.setResult(cursor.getString(cursor.getColumnIndex(History.KEY_result)));
            cursor.close();
            db.close();
            return history;
        }
        return null;
    }

    public void insertWithoutSame(History history) {
        History lastHistry = getLast();
        if(lastHistry == null || !(history.equals(lastHistry))){
            insert(history);
        }
        if(count()> 20){
            delete(getFirstId());
        }
    }
}
