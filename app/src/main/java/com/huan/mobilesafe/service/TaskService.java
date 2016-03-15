package com.huan.mobilesafe.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.huan.mobilesafe.activity.LockedPasswordInputActivity;
import com.huan.mobilesafe.dao.AppLockDAO;

import java.util.List;

public class TaskService extends Service {

    private static final String TAG = "TaskServiceInfo";

    private final AppLockDAO appLockDAO;
    private List<String> appLockedList;
    private ActivityManager activityManager;

    //当前任务栈栈顶应用程序的包名
    private String topPackageName;

    //监听状态指令
    private boolean flagDetect;
    private boolean flagDeal;
    private StopReceiver stopReceiver;


    /**
     * 接收临时停止指令的广播
     */
    private class StopReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //停止处理线程
            flagDeal = false;
        }
    }


    public TaskService() {
        appLockDAO = new AppLockDAO(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //注册接收停止指令的广播
        stopReceiver = new StopReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.huan.mobilesafe.tempstopprotect");
        registerReceiver(stopReceiver, intentFilter);

        //获取程序锁数据的所有包名
        appLockedList = appLockDAO.findAll();

        //获取进程管理器
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        startListener();
    }

    /**
     * 开启进程监听
     */
    private void startListener() {

        /**
         * 处理线程
         */
        class ThreadDeal extends Thread {
            @Override
            public void run() {
                flagDeal = true;
                while (flagDeal) {
                    //获取到当前正在运行的任务栈（只需要获取一个）
                    List<ActivityManager.RunningTaskInfo> runningTask =
                            activityManager.getRunningTasks(1);
                    //获取此任务栈栈顶的进程
                    ActivityManager.RunningTaskInfo taskInfo = runningTask.get(0);
                    //获取最顶段应用程序的包名
                    String packageName = taskInfo.topActivity.getPackageName();

                    Log.i(TAG, "packageName = " + packageName);

                    //判断当前的获取的程序包名是否在程序锁数据库中
                    if (appLockedList.contains(packageName)) {
                        //进行保护 (跳转到PasswordInputActivity)
                        Intent intent = new Intent(TaskService.this, LockedPasswordInputActivity.class);

                        //从服务向活动跳转需要设置flag
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent);
                    }
                }
            }
        }

        //开启线程
        new ThreadDeal().start();


        /**
         * 检测线程
         */
        new Thread() {
            public void run() {
                flagDetect = true;

                while (flagDetect) {
                    //获取到当前正在运行的任务栈（只需要获取一个）
                    List<ActivityManager.RunningTaskInfo> runningTask =
                            activityManager.getRunningTasks(1);
                    //获取此任务栈栈顶的进程
                    ActivityManager.RunningTaskInfo taskInfo = runningTask.get(0);
                    //获取最顶段应用程序的包名
                    topPackageName = taskInfo.topActivity.getPackageName();

                    Log.i(TAG, "topPackageName = " + topPackageName);

                    //回到桌面后开启保护
                    if (topPackageName.equals("com.android.launcher")) {
                        //开启线程
                        new ThreadDeal().start();
                    }
                }
            }
        }.start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //停止线程任务
        flagDeal = false;
        flagDetect = false;

        //注销广播
        unregisterReceiver(stopReceiver);
    }
}
