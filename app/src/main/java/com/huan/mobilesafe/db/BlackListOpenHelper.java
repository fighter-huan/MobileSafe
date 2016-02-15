package com.huan.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * BlackListOpenHelper 黑名单数据库工具类
 *
 * @author: 欢
 * @time: 2016/2/14 9:34
 */
public class BlackListOpenHelper extends SQLiteOpenHelper {

    public BlackListOpenHelper(Context context) {
        super(context, "blackList.db", null, 1);
    }

    /**
     * blackList 表名
     * _id 主键自动增长
     * phone 需要拦截的电话号码
     * mode 拦截模式 (电话拦截, 短信拦截, 电话+短信拦截)
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table blackList" +
                "(_id integer primary key autoincrement, phone varchar(20), mode varchar(2))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
