package com.huan.mobilesafe.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * AddressDb
 *
 * @author: 欢
 * @time: 2016/2/11 23:07
 */
public class AddressDb {

    //将数据库拷贝到此路径下
    private static final String PATH = "data/data/com.huan.mobilesafe/files/address.db";

    /**
     * 查询归属地
     *
     * @param number 电话号码
     * @return 归属地
     */
    public static String getAddress(String number) {
        String address = "未知号码";

        //获取数据库对象
        SQLiteDatabase database = SQLiteDatabase.openDatabase(PATH, null,
                SQLiteDatabase.OPEN_READONLY);

        //采用正则表达式匹配电话号码
        if (number.matches("^1[3-8]\\d{9}$")) {
            //匹配手机号码
            Cursor cursor = database.rawQuery(
                    "select location from data2 where id = (select outkey from data1 where id = ?)",
                    new String[]{number.substring(0, 7)});

            if (cursor.moveToNext()) {
                address = cursor.getString(0);
            }
            cursor.close();
        } else if (number.matches("^\\d+$")) {
            //匹配数字
            switch (number.length()) {
                case 3:
                    address = "报警电话";
                    break;

                case 4:
                    address = "模拟器";
                    break;

                case 5:
                    address = "客服电话";
                    break;

                case 7:
                case 8:
                    address = "本地电话";
                    break;

                default:
                    if (number.startsWith("0") && number.length() > 10) {
                        //有可能是长途电话
                        //区号有3位和4位 (都是以0开头的)
                        //先查询4位区号
                        Cursor cursor = database.rawQuery("select location from data2 where area = ?",
                                new String[]{number.substring(1, 4)});
                        if (cursor.moveToNext()) {
                            address = cursor.getString(0);
                            cursor.close();
                        } else {
                            //查询3位区号
                            cursor = database.rawQuery("select location from data2 where area = ?",
                                    new String[]{number.substring(1, 3)});
                            if (cursor.moveToNext()) {
                                address = cursor.getString(0);
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }

        //关闭数据库资源
        database.close();

        return address;
    }
}
