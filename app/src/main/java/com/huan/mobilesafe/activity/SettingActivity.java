package com.huan.mobilesafe.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;

import com.huan.mobilesafe.R;
import com.huan.mobilesafe.view.SettingItemView;

/**
 * 设置中心页面
 */
public class SettingActivity extends AppCompatActivity {

    private static final String TAG = "SettingActivityInfo";

    //设置中心自定义控件
    private SettingItemView settingItemView;

    //用户偏好数据
    private SharedPreferences msharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //获取设置中心自定义控件对象
        settingItemView = (SettingItemView) findViewById(R.id.setting_item_update);

        //实例化SharedPreferences
        msharedPreferences = getSharedPreferences("configuration", MODE_PRIVATE);

        //获取配置文件configuration中的信息 (默认值为true，即用户选择之前)
        boolean autoUpdateStatus = msharedPreferences.getBoolean("auto_update_status", true);

        //进入此活动就加载自定义控件，因为标题是固定的所以在初始化时就指定了值
        //根据自动更新设置，加载控件
        settingItemView.setChecked(autoUpdateStatus);

        //CheckBox设置监听器
        settingItemView.getCbStatus().setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        //修改显示
                        settingItemView.setChecked(isChecked);

                        //保存设置
                        SharedPreferences.Editor editor = msharedPreferences.edit();
                        editor.putBoolean("auto_update_status", isChecked);
                        editor.commit();
                    }
                });
    }
}
