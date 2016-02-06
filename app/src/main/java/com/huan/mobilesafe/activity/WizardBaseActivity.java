package com.huan.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.huan.mobilesafe.R;

/**
 * WizardBaseActivity 手机防盗设置引导页面的都需要继承此抽象类
 * <p/>
 * 不需要在配置文件中注册，因为它不需要显示
 *
 * @author: 欢
 * @time: 2016/2/6 15:52
 */
public abstract class WizardBaseActivity extends Activity {

    protected GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //监听手势滑动事件
        mGestureDetector = new GestureDetector(this,
                new GestureDetector.SimpleOnGestureListener() {

                    /**
                     * @param e1  滑动的起点
                     * @param e2  滑动的终点
                     * @param velocityX  X轴速度
                     * @param velocityY  Y轴速度
                     * @return
                     */
                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2,
                                           float velocityX, float velocityY) {

                        //向右滑动，跳转到上一页
                        if (e2.getRawX() - e1.getRawX() > 100) {
                            showPreviousPage();
                        }

                        //向左滑动，跳转到下一页
                        if (e1.getRawX() - e2.getRawX() > 100) {
                            showNextPage();
                        }

                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });
    }

    /**
     * 显示上一页
     */
    protected abstract void showPreviousPage();

    /**
     * 显示下一页
     */
    protected abstract void showNextPage();

    /**
     * 按钮点击跳转到上一页
     */
    public void btnOnClickPrevious(View view) {
        showPreviousPage();
    }

    /**
     * 按钮点击跳转到下一页
     */
    public void btnOnClickNext(View view) {
        showNextPage();
    }

    /**
     * Activity获取屏幕触摸事件
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //将得到的事件委托给GestureDetector
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
