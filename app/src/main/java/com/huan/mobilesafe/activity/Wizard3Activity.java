package com.huan.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.huan.mobilesafe.R;

/**
 * 第二个设置向导页面
 */
public class Wizard3Activity extends WizardBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard3);
    }

    @Override
    protected void showPreviousPage() {
        finish();
        startActivity(new Intent(this, Wizard2Activity.class));
        overridePendingTransition(R.anim.tran_previous_in, R.anim.tran_previous_out);
    }

    @Override
    protected void showNextPage() {
        finish();
        startActivity(new Intent(this, Wizard4Activity.class));
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }
}
