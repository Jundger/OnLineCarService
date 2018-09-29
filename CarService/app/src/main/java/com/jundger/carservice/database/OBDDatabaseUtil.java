package com.jundger.carservice.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jundger.carservice.constant.APPConsts;
import com.jundger.carservice.bean.json.FaultCode;

/**
 * Created by Jundger on 2018/1/6.
 */

public class OBDDatabaseUtil {
    private MyDatabaseHelper helper = null;

    public OBDDatabaseUtil(Context context) {
        this.helper = new MyDatabaseHelper(context);
    }

    /**
     * 插入数据
     * @param faultInfo
     * @return
     */
    public Boolean insert(FaultCode faultInfo) {
        boolean state;
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("code", faultInfo.getCode());
        values.put("compont_system", faultInfo.getSystem());
        values.put("scope", faultInfo.getScope());
        values.put("describe", faultInfo.getDescribe());
        db.beginTransaction();
        try {
            db.insert(APPConsts.DB_TABLE_OBD, null, values);
            db.setTransactionSuccessful();
            state = true;
        } catch (Exception e) {
            e.printStackTrace();
            state = false;
        } finally {
            db.endTransaction();
            db.close();
        }
        return state;
    }

    /**
     * 根据故障码code更新故障描述describe
     *
     * @param faultInfo
     * @return
     */
    public boolean update(FaultCode faultInfo) {
        SQLiteDatabase db = helper.getWritableDatabase();
        boolean state;
        ContentValues values = new ContentValues();
        values.put("describe", faultInfo.getDescribe());
        db.beginTransaction();
        try {
            db.update(APPConsts.DB_TABLE_OBD, values, "code=?", new String[]{faultInfo.getCode()});
            db.setTransactionSuccessful();
            state = true;
        } catch (Exception e) {
            e.printStackTrace();
            state = false;
        } finally {
            db.endTransaction();
            db.close();
        }
        return state;
    }

    /**
     * 根据故障码code查询故障信息
     * @param code
     * @return
     */
    public FaultCode query(String code) {
        FaultCode faultInfo = new FaultCode();
        SQLiteDatabase db = helper.getReadableDatabase();
        String statement = "SELECT * FROM " + APPConsts.DB_TABLE_OBD + " WHERE code=?";
        Cursor cursor = db.rawQuery(statement, new String[]{code});
        if (cursor.moveToFirst()) {
            do {
                faultInfo.setCode(cursor.getString(cursor.getColumnIndex("code")));
                faultInfo.setSystem(cursor.getString(cursor.getColumnIndex("compont_system")));
                faultInfo.setScope(cursor.getString(cursor.getColumnIndex("scope")));
                faultInfo.setDescribe(cursor.getString(cursor.getColumnIndex("describe")));
            } while (cursor.moveToNext());
        } else {
            faultInfo = null;
        }
        cursor.close();
        db.close();
        return faultInfo;
    }

    /**
     * 删除表中所有数据
     * @return
     */
    public boolean deleteAll() {
        SQLiteDatabase db = helper.getWritableDatabase();
        boolean state;
        db.beginTransaction();
        try {
            db.execSQL("delete from " + APPConsts.DB_TABLE_OBD);
            db.setTransactionSuccessful();
            state = true;
        } catch (Exception e) {
            e.printStackTrace();
            state = false;
        } finally {
            db.endTransaction();
            db.close();
        }
        return state;
    }
}
