package com.huan.mobilesafe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huan.mobilesafe.R;

/**
 * 手机防盗页面
 */
public class GuardActivity extends AppCompatActivity {

    private static final String TAG = "GuardActivityInfo";

    private SharedPreferences mSharedPreferences;

    private TextView tvSafePhone;
    private ImageView ivProtectStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //获取配置文件信息 (是否需要进入向导页面，true-->进入向导)
        mSharedPreferences = getSharedPreferences("configuration", MODE_PRIVATE);
        boolean wizard = mSharedPreferences.getBoolean("wizard", true);

        //判断是否进入设置向导
        if (wizard) {
            //跳转到设置向导页面
            finish();
            startActivity(new Intent(GuardActivity.this, Wizard1Activity.class));
        } else {
            //不进入向导页面
            setContentView(R.layout.activity_guard);

            tvSafePhone = (TextView) findViewById(R.id.tv_safe_phone);
            ivProtectStatus = (ImageView) findViewById(R.id.iv_protect_status);

            //根据sharePreference更新安全号码
            String safePhone = mSharedPreferences.getString("safe_phone", "");
            tvSafePhone.setText(safePhone);

            //根据sharePreference更新防盗状态锁图片
            boolean protectStatus = mSharedPreferences.getBoolean("protect_status", false);
            if (protectStatus) {
                ivProtectStatus.setImageResource(R.mipmap.lock);
            } else {
                ivProtectStatus.setImageResource(R.mipmap.unlock);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    //重新进入设置向导
    public void reEnterWizard(View view) {
        finish();
        startActivity(new Intent(GuardActivity.this, Wizard1Activity.class));
    }
}
