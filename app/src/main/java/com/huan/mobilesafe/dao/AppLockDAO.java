package com.huan.mobilesafe.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.huan.mobilesafe.db.AppLockOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * AppLockDAO
 *
 * @author: 欢
 * @time: 2016/2/19 20:48
 */
public class AppLockDAO {

    private final AppLockOpenHelper helper;

    public AppLockDAO(Context context) {
        helper = new AppLockOpenHelper(context);
    }

    /**
     * 添加已加锁APP
     *
     * @param packageName 包名
     */
    public void add(String packageName) {
        //创建数据库
        SQLiteDatabase database = helper.getWritableDatabase();

        //添加包名到contentValues
        ContentValues contentValues = new ContentValues();
        contentValues.put("package_name", packageName);

        database.insert("app_locked", "package_name", contentValues);

        database.close();
    }

    /**
     * 删除已加锁APP
     *
     * @param packageName 包名
     */
    public void delete(String packageName) {
        //创建数据库
        SQLiteDatabase database = helper.getWritableDatabase();

        database.delete("app_locked", "package_name = ?", new String[]{packageName});

        database.close();
    }

    /**
     * 查询数据库中是否有指定的包名
     *
     * @param packageName 包名
     * @return 存在返回true, 否则返回false
     */
    public boolean find(String packageName) {

        boolean result = false;

        SQLiteDatabase database = helper.getReadableDatabase();

        Cursor cursor = database.query("app_locked", null, "package_name = ?",
                new String[]{packageName}, null, null, null);

        if (cursor.moveToNext()) {
            //查询到，将result置为true;
            result = true;
        }

        cursor.close();
        database.close();

        return result;
    }

    /**
     * 查询数据库中所有包名
     *
     * @return allLockedApp
     */
    public List<String> findAll() {
        List<String> allLockedApp = new ArrayList<>();

        SQLiteDatabase database = helper.getReadableDatabase();

        //查询pack_name键的所有行
        Cursor cursor = database.query("app_locked", new String[]{"package_name"},
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            allLockedApp.add(cursor.getString(0));
        }

        cursor.close();
        database.close();

        return allLockedApp;
    }
}
