package com.huan.mobilesafe.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;

import com.huan.mobilesafe.R;
import com.huan.mobilesafe.view.SettingItemView;

public class SettingActivity extends AppCompatActivity {

    //设置中心自定义控件
    private SettingItemView settingItemView;

    //用户偏好数据
    private SharedPreferences msharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //获取设置中心自定义控件对象
        settingItemView = (SettingItemView) findViewById(R.id.setting_item_view);
        settingItemView.setTvTitle("自动更新设置");

        //实例化SharedPreferences
        msharedPreferences = getSharedPreferences("configuration", MODE_PRIVATE);

        //获取配置文件configuration中的信息 (默认值为true，即用户选择之前)
        boolean autoUpdateStatus = msharedPreferences.getBoolean("auto_update_status", true);

        if (autoUpdateStatus) {
            settingItemView.setTvDescription("自动更新已开启");
            settingItemView.getCbStatus().setChecked(autoUpdateStatus);
        } else {
            settingItemView.setTvDescription("自动更新已关闭");
            settingItemView.getCbStatus().setChecked(autoUpdateStatus);
        }

        //CheckBox设置监听器
        settingItemView.getCbStatus().setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (!isChecked) {
                            //更改tvDescription为“自动更新已关闭”
                            settingItemView.setTvDescription("自动更新已关闭");

                            //保存设置
                            SharedPreferences.Editor editor = msharedPreferences.edit();
                            editor.putBoolean("auto_update_status", false);
                            editor.commit();
                        } else {
                            //更改tvDescription为“自动更新已开启”
                            settingItemView.setTvDescription("自动更新已开启");

                            //保存设置
                            SharedPreferences.Editor editor = msharedPreferences.edit();
                            editor.putBoolean("auto_update_status", true);
                            editor.commit();
                        }
                    }
                });
    }
}
