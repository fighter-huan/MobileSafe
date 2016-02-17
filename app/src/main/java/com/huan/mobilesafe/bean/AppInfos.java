package com.huan.mobilesafe.bean;

import android.graphics.drawable.Drawable;

/**
 * AppInfos
 *
 * @author: 欢
 * @time: 2016/2/15 18:20
 */
public class AppInfos {
    //应用程序的图标
    private Drawable icon;

    //应用程序的名字
    private String apkName;

    //程序的大小 (格式化之后)
    private String apkSize;

    //表示用户App和系统App
    //用户App ——> true
    //系统App ——> false
    private boolean isUserApp;

    //放置的位置
    private boolean isRom;

    //包名
    private String packageName;

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public String getApkSize() {
        return apkSize;
    }

    public void setApkSize(String apkSize) {
        this.apkSize = apkSize;
    }

    public boolean isUserApp() {
        return isUserApp;
    }

    public void setIsUserApp(boolean isUserApp) {
        this.isUserApp = isUserApp;
    }

    public boolean isRom() {
        return isRom;
    }

    public void setIsRom(boolean isRom) {
        this.isRom = isRom;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public String toString() {
        return "AppInfos{" +
                "apkName='" + apkName + '\'' +
                ", apkSize=" + apkSize +
                ", isUserApp=" + isUserApp +
                ", isRom=" + isRom +
                ", packageName='" + packageName + '\'' +
                '}';
    }
}
