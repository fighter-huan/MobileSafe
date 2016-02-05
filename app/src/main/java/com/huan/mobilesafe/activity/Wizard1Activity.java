package com.huan.mobilesafe.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.huan.mobilesafe.R;

/**
 * 第一个设置向导页面
 */
public class Wizard1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard1);
    }

    //跳转到下一页
    public void btnOnClick(View view) {

    }
}
