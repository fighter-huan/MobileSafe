package com.huan.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.huan.mobilesafe.R;

/**
 * 第二个设置向导页面
 */
public class Wizard4Activity extends WizardBaseActivity {

    private CheckBox cbProtectStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard4);

        cbProtectStatus = (CheckBox) findViewById(R.id.cb_protect_status);

        //判断防盗保护状态
        boolean protect_status = mSharedPreferences.getBoolean("protect_status", false);
        if (protect_status) {
            cbProtectStatus.setChecked(true);
            cbProtectStatus.setText("防盗保护已经开启");
        } else {
            cbProtectStatus.setChecked(false);
            cbProtectStatus.setText("防盗保护没有开启");
        }

        cbProtectStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbProtectStatus.setText("防盗保护已经开启");
                    mSharedPreferences.edit().putBoolean("protect_status", true).commit();
                } else {
                    cbProtectStatus.setText("防盗保护没有开启");
                    mSharedPreferences.edit().putBoolean("protect_status", false).commit();
                }
            }
        });
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
