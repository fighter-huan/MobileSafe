package com.huan.mobilesafe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.huan.mobilesafe.R;

/**
 * 第二个设置向导页面
 */
public class Wizard4Activity extends WizardBaseActivity {

    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard4);

        mSharedPreferences = getSharedPreferences("configuration", MODE_PRIVATE);
    }

    @Override
    protected void showPreviousPage() {
        startActivity(new Intent(this, Wizard3Activity.class));
        finish();
        overridePendingTransition(R.anim.tran_previous_in, R.anim.tran_previous_out);
    }

    @Override
    protected void showNextPage() {
        finish();
        startActivity(new Intent(this, GuardActivity.class));

        //更改"wizard"值，表示已经展示过设置向导了，下次进来就不展示了
        mSharedPreferences.edit().putBoolean("wizard", false).commit();

        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }
}
