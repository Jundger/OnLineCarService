package com.jundger.carservice.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jundger.carservice.constant.APPConsts;

/**
 * Created by Jundger on 2018/1/6.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String statement = "CREATE TABLE " + APPConsts.DB_TABLE_OBD +
            "(id integer primary key autoincrement, code text, compont_system text, scope text, describe text)";

    public MyDatabaseHelper(Context context) {
        super(context, APPConsts.DATABASE_NAME, null, APPConsts.DATABASE_VERSION);
    }

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(statement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + APPConsts.DB_TABLE_OBD);
        onCreate(sqLiteDatabase);
    }
}
