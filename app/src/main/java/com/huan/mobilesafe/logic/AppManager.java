package com.huan.mobilesafe.logic;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.text.format.Formatter;
import android.util.Log;

import com.huan.mobilesafe.bean.AppInfos;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * AppManager 所有App的管理类
 *
 * @author: 欢
 * @time: 2016/2/15 18:17
 */
public class AppManager {

    private static final String TAG = "AppManagerInfo";

    public static List<AppInfos> getAppInfos(Context context) {

        //创建一个装载AppInfos的List集合
        List<AppInfos> list = new ArrayList<>();

        //获取包的管理者
        PackageManager packageManager = context.getPackageManager();

        //获取rom中的安装包
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);

        //遍历installedPackages集合
        for (PackageInfo installedPackage : installedPackages) {

            //创建AppInfo对象
            AppInfos appInfos = new AppInfos();

            //获取到应用程序的图标
            Drawable icon = installedPackage.applicationInfo.loadIcon(packageManager);
            appInfos.setIcon(icon);

            //获取应用程序的名字
            String apkName = installedPackage.applicationInfo.loadLabel(packageManager).toString();
            appInfos.setApkName(apkName);

            //获取应用程序的包名
            String packageName = installedPackage.packageName;
            appInfos.setPackageName(packageName);

            //获取apk资源的路径
            String sourceDir = installedPackage.applicationInfo.sourceDir;

            File file = new File(sourceDir);
            //获取apk的大小
            long apkSize = file.length();
            //格式化大小
            String fileSize = Formatter.formatFileSize(context, apkSize);
            appInfos.setApkSize(fileSize);

            //获取应用程序的标记
            int flags = installedPackage.applicationInfo.flags;
            if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                //系统APP
                appInfos.setIsUserApp(false);
            } else {
                //第三方APP
                appInfos.setIsUserApp(true);
            }

            //判断安装位置
            if ((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
                //SD卡
                appInfos.setIsRom(false);
            } else {
                //rom
                appInfos.setIsRom(true);
            }

            //将appInfos添加到list集合中
            list.add(appInfos);
        }

        return list;
    }
}
