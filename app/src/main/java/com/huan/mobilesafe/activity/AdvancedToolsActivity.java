package com.huan.mobilesafe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.huan.mobilesafe.R;

/**
 * 高级工具页面
 */
public class AdvancedToolsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_tools);
    }

    /**
     * 电话归属地查询
     * @param view
     */
    public void getPhoneArea(View view) {
        startActivity(new Intent(this, PhoneAreaActivity.class));
    }

    /**
     * 常用号码查询
     * @param view
     */
    public void queryCommonPhone(View view) {
        Toast.makeText(this, "此功能未实现", Toast.LENGTH_SHORT).show();
    }

    /**
     * 短信备份
     * @param view
     */
    public void backupSM(View view) {
        Toast.makeText(this, "此功能未实现", Toast.LENGTH_SHORT).show();
    }

    /**
     * 程序锁
     * @param view
     */
    public void appLock(View view) {
        //跳转到加锁列表界面
        startActivity(new Intent(this, AppLockActivity.class));
    }
}
