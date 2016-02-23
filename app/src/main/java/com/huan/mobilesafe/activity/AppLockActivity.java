package com.huan.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.huan.mobilesafe.R;
import com.huan.mobilesafe.fragment.LockFragment;
import com.huan.mobilesafe.fragment.UnlockFragment;
import com.huan.mobilesafe.service.TaskService;

/**
 * 程序锁页面
 */
public class AppLockActivity extends FragmentActivity implements View.OnClickListener {

    private FrameLayout frameLayoutAppList;
    private TextView tvLock;
    private TextView tvUnlock;
    private FragmentManager fragmentManager;
    private UnlockFragment unlockFragment;
    private LockFragment lockFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();
    }

    private void initUI() {
        setContentView(R.layout.activity_app_lock);

        frameLayoutAppList = (FrameLayout) findViewById(R.id.FrameLayout_app_lock_unlock);
        tvLock = (TextView) findViewById(R.id.tv_lock);
        tvUnlock = (TextView) findViewById(R.id.tv_unlock);

        //给两个TextView注册监听器
        tvLock.setOnClickListener(this);
        tvUnlock.setOnClickListener(this);

        //实例化Fragment
        unlockFragment = new UnlockFragment();
        lockFragment = new LockFragment();

        //获取fragment的管理者
        fragmentManager = getSupportFragmentManager();

        //开启事物
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        //向容器中添加默认的fragment (unlockFragment)
        transaction.add(R.id.FrameLayout_app_lock_unlock, unlockFragment);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {

        //开启事物
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        switch (v.getId()) {
            case R.id.tv_unlock:
                //切换背景
                tvUnlock.setBackgroundResource(R.mipmap.tab_left_pressed);
                tvLock.setBackgroundResource(R.mipmap.tab_right_default);

                //切换fragment
                transaction.replace(R.id.FrameLayout_app_lock_unlock, unlockFragment);
                break;

            case R.id.tv_lock:
                //切换背景
                tvUnlock.setBackgroundResource(R.mipmap.tab_left_default);
                tvLock.setBackgroundResource(R.mipmap.tab_right_pressed);

                //切换fragment
                transaction.replace(R.id.FrameLayout_app_lock_unlock, lockFragment);
                break;
        }

        //提交
        transaction.commit();
    }
}
