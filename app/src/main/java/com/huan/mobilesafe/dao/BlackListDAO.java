package com.huan.mobilesafe.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.huan.mobilesafe.bean.BlackListInfo;
import com.huan.mobilesafe.db.BlackListOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * BlackListDAO 黑名单数据库类
 *
 * @author: 欢
 * @time: 2016/2/14 9:32
 */
public class BlackListDAO {

    private BlackListOpenHelper helper;

    public BlackListDAO(Context context) {
        helper = new BlackListOpenHelper(context);
    }

    /**
     * 添加黑名单号码
     *
     * @param phone 黑名单号码
     * @param mode  拦截模式
     * @return
     */
    public boolean add(String phone, String mode) {
        //创建数据库
        SQLiteDatabase database = helper.getWritableDatabase();
        //添加黑名单号码和拦截模式
        ContentValues contentValues = new ContentValues();
        contentValues.put("phone", phone);
        contentValues.put("mode", mode);
        long rowId = database.insert("blackList", null, contentValues);
        database.close();
        if (rowId == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 删除黑名单号码
     *
     * @param phone 黑名单号码
     * @return
     */
    public boolean delete(String phone) {
        SQLiteDatabase database = helper.getWritableDatabase();
        int rowNumber = database.delete("blackList", "phone = ?", new String[]{phone});
        database.close();
        if (rowNumber == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 通过电话号码修改拦截模式
     *
     * @param phone 电话号码
     * @param mode  拦截模式
     * @return
     */
    public boolean changeMode(String phone, String mode) {
        SQLiteDatabase database = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("mode", mode);
        int rowNumber = database.update("blackList", contentValues, "phone = ?", new String[]{phone});
        database.close();
        if (rowNumber == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 查询电话号码的拦截模式
     *
     * @param phone 电话号码
     * @return 返回一个黑名单号码拦截模式
     */
    public String findPhone(String phone) {
        String mode = "";
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.query("blackList",
                new String[]{"mode"}, "phone = ?", new String[]{phone}, null, null, null);
        if (cursor.moveToNext()) {
            mode = cursor.getString(0);
        }
        cursor.close();
        database.close();
        return mode;
    }

    /**
     * 查询所有的黑名单
     *
     * @return
     */
    public List<BlackListInfo> findAll() {
        SQLiteDatabase database = helper.getReadableDatabase();
        //创建集合 (用于保存BlackListInfo对象)
        List<BlackListInfo> blackListInfos = new ArrayList<>();
        Cursor cursor = database.query("blackList",
                new String[]{"phone", "mode"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            //创建BlackListInfo对象
            BlackListInfo blackListInfo = new BlackListInfo();
            blackListInfo.setPhone(cursor.getString(0));
            blackListInfo.setMode(cursor.getString(1));
            blackListInfos.add(blackListInfo);
        }
        cursor.close();
        database.close();
        return blackListInfos;
    }
}
