package com.huan.mobilesafe.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * ServiceStatusUtils 服务状态工具类
 *
 * @author: 欢
 * @time: 2016/2/13 12:07
 */
public class ServiceStatusUtils {

    private static final String TAG = "ServiceStatusUtilsInfo";

    /**
     * 检测服务是否正在运行
     *
     * @return
     */
    public static boolean isServiceRunning(Context context, String serviceName) {

        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        //获取100个系统正在运行的服务
        List<ActivityManager.RunningServiceInfo> runningServices =
                activityManager.getRunningServices(100);

        //遍历获取到的服务
        for (ActivityManager.RunningServiceInfo runningServiceInfo : runningServices) {
            //获取服务名称
            String runningServiceName = runningServiceInfo.service.getClassName();

            //如果服务存在，返回true
            if (runningServiceName.equals(serviceName)) {
                return true;
            }
        }

        //如果后台不存在该服务，返回false
        return false;
    }
}
