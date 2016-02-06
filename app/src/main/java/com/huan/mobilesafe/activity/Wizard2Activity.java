package com.huan.mobilesafe.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;

import com.huan.mobilesafe.R;
import com.huan.mobilesafe.view.SettingItemView;

/**
 * 第二个设置向导页面
 */
public class Wizard2Activity extends WizardBaseActivity {

    private static final String TAG = "Wizard2ActivityInfo";

    private SharedPreferences mSharedPreferences;

    private SettingItemView settingItemView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard2);

        settingItemView = (SettingItemView) findViewById(R.id.sim_bind_status);

        //获取用户偏好配置文件
        mSharedPreferences = getSharedPreferences("configuration", MODE_PRIVATE);

        //判断用户是否绑定SIM卡
        String simSerialNumber = mSharedPreferences.getString("simSerialNumber", null);
        if (simSerialNumber == null) {
            //未绑定
            settingItemView.setChecked(false);
        } else {
            //已绑定
            settingItemView.setChecked(true);
        }

        //监听SIM卡复选框
        settingItemView.getCbStatus().setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        //更改显示neirong
                        settingItemView.setChecked(isChecked);

                        if (isChecked) {
                            //获取SIM卡序列号
                            TelephonyManager telephonyManager =
                                    (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                            String simSerialNumber = telephonyManager.getSimSerialNumber();

                            //保存该序列号
                            mSharedPreferences.edit().putString("simSerialNumber", simSerialNumber)
                                    .commit();
                        } else {
                            //删除已保存的序列号
                            mSharedPreferences.edit().remove("simSerialNumber").commit();
                        }

                    }
                });
    }

    @Override
    protected void showPreviousPage() {
        finish();
        startActivity(new Intent(this, Wizard1Activity.class));
        overridePendingTransition(R.anim.tran_previous_in, R.anim.tran_previous_out);
    }

    @Override
    protected void showNextPage() {
        finish();
        startActivity(new Intent(this, Wizard3Activity.class));
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }
}
