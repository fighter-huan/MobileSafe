package com.huan.mobilesafe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.huan.mobilesafe.R;

/**
 * 第一个设置向导页面
 */
public class Wizard1Activity extends WizardBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard1);
    }

    @Override
    protected void showPreviousPage() {
    }

    @Override
    protected void showNextPage() {
        finish();
        startActivity(new Intent(this, Wizard2Activity.class));
        //两个界面切换的动画效果
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }

}
