package com.huan.mobilesafe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.huan.mobilesafe.R;

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

    }

    /**
     * 短信备份
     * @param view
     */
    public void backupSM(View view) {

    }

    /**
     * 程序锁
     * @param view
     */
    public void programLock(View view) {

    }
}
