package com.huan.mobilesafe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;

import com.huan.mobilesafe.R;
import com.huan.mobilesafe.service.AddressService;
import com.huan.mobilesafe.service.CallSafeService;
import com.huan.mobilesafe.service.TaskService;
import com.huan.mobilesafe.utils.ServiceStatusUtils;
import com.huan.mobilesafe.view.SettingItemView;

/**
 * 设置中心页面
 */
public class SettingActivity extends AppCompatActivity {

    private static final String TAG = "SettingActivityInfo";

    //设置自动更新
    private SettingItemView settingItemUpdate;
    //设置来电归属地显示
    private SettingItemView settingItemShowAddress;
    //设置骚扰拦截
    private SettingItemView settingItemBlackList;
    //设置程序锁
    private SettingItemView settingItemAppLock;

    //用户偏好数据
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //实例化SharedPreferences
        mSharedPreferences = getSharedPreferences("configuration", MODE_PRIVATE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //初始化自动更新开关
        initAutoUpdate();

        //初始化归属地开关
        initAddressView();

        //初始化骚扰拦截开关
        initBlackList();

        //初始化程序锁开关
        initAppLock();
    }

    /**
     * 初始化自动更新开关
     */
    private void initAutoUpdate() {
        //获取设置中心自定义控件对象
        settingItemUpdate = (SettingItemView) findViewById(R.id.setting_item_update);

        //获取配置文件configuration中的信息 (默认值为true，即用户选择之前)
        boolean autoUpdateStatus = mSharedPreferences.getBoolean("auto_update_status", true);

        //进入此活动就加载自定义控件，因为标题是固定的所以在初始化时就指定了值
        //根据自动更新设置，加载控件
        settingItemUpdate.setChecked(autoUpdateStatus);

        //CheckBox设置监听器
        settingItemUpdate.getCbStatus().setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        //修改显示
                        settingItemUpdate.setChecked(isChecked);

                        //保存设置
                        SharedPreferences.Editor editor = mSharedPreferences.edit();
                        editor.putBoolean("auto_update_status", isChecked);
                        editor.commit();
                    }
                });


    }


    /**
     * 初始化归属地开关
     */
    private void initAddressView() {

        //查找控件
        settingItemShowAddress = (SettingItemView) findViewById(R.id.setting_item_show_address);

        //获取归属地设置状态 (默认不显示)
        boolean showAddressStatus = mSharedPreferences.getBoolean("show_address_status", false);

        //检查服务是否在后台运行，保证与设置状态同步
        boolean serviceRunning =
                ServiceStatusUtils.isServiceRunning(this, "com.huan.mobilesafe.service.AddressService");

        //修改归属地设置状态和配置信息
        showAddressStatus = serviceRunning;
        mSharedPreferences.edit().putBoolean("show_address_status", showAddressStatus).commit();

        //根据设置加载控件
        settingItemShowAddress.setChecked(showAddressStatus);

        //CheckBox设置监听器
        settingItemShowAddress.getCbStatus().setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        //修改显示
                        settingItemShowAddress.setChecked(isChecked);

                        if (isChecked) {
                            //开启来电归属地显示服务
                            startService(new Intent(SettingActivity.this, AddressService.class));
                        } else {
                            //关闭来电归属地显示服务
                            stopService(new Intent(SettingActivity.this, AddressService.class));
                        }

                        //保存设置
                        mSharedPreferences.edit().putBoolean("show_address_status", isChecked).commit();
                    }
                });
    }

    /**
     * 初始化骚扰拦截开关
     */
    private void initBlackList() {

        //查找控件
        settingItemBlackList = (SettingItemView) findViewById(R.id.Setting_item_black_list);

        //获取骚扰拦截设置状态 (默认不显示)
        boolean blackListStatus = mSharedPreferences.getBoolean("black_list_status", false);

        //检查服务是否在后台运行，保证与设置状态同步
        boolean serviceRunning =
                ServiceStatusUtils.isServiceRunning(this, "com.huan.mobilesafe.service.CallSafeService");

        //修改骚扰拦截设置状态和配置信息
        blackListStatus = serviceRunning;
        mSharedPreferences.edit().putBoolean("black_list_status", blackListStatus).commit();

        //根据设置加载控件
        settingItemBlackList.setChecked(blackListStatus);

        //CheckBox设置监听器
        settingItemBlackList.getCbStatus().setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        //修改显示
                        settingItemBlackList.setChecked(isChecked);

                        if (isChecked) {
                            //开启骚扰拦截服务
                            startService(new Intent(SettingActivity.this, CallSafeService.class));
                        } else {
                            //关闭骚扰拦截服务
                            stopService(new Intent(SettingActivity.this, CallSafeService.class));
                        }

                        //保存设置
                        mSharedPreferences.edit().putBoolean("black_list_status", isChecked).commit();
                    }
                });
    }

    /**
     * 初始化程序锁开关
     */
    private void initAppLock() {

        //查找控件
        settingItemAppLock = (SettingItemView) findViewById(R.id.Setting_item_app_lock);

        //获取程序锁设置状态 (默认不显示)
        boolean appLockStatus = mSharedPreferences.getBoolean("app_lock_status", false);

        //检查服务是否在后台运行，保证与设置状态同步
        boolean serviceRunning =
                ServiceStatusUtils.isServiceRunning(this, "com.huan.mobilesafe.service.TaskService");

        //修改程序锁设置状态和配置信息
        appLockStatus = serviceRunning;
        mSharedPreferences.edit().putBoolean("app_lock_status", appLockStatus).commit();

        //根据设置加载控件
        settingItemAppLock.setChecked(appLockStatus);

        //CheckBox设置监听器
        settingItemAppLock.getCbStatus().setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        //修改显示
                        settingItemAppLock.setChecked(isChecked);

                        if (isChecked) {
                            //开启程序锁服务
                            startService(new Intent(SettingActivity.this, TaskService.class));
                        } else {
                            //关闭程序锁服务
                            stopService(new Intent(SettingActivity.this, TaskService.class));
                        }

                        //保存设置
                        mSharedPreferences.edit().putBoolean("app_lock_status", isChecked).commit();
                    }
                });
    }
}
