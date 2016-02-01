package com.huan.mobilesafe.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.huan.mobilesafe.R;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivityInfo";

    private TextView tvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tvVersion = (TextView) findViewById(R.id.tv_version);

        //设置版本名称
        tvVersion.setText("当前版本:" + getVersionName());
    }

    /**
     * 获取版本名称
     *
     * @return 当前版本名称
     */
    private String getVersionName() {
        //声明版本名称
        String versionName = "";

        //获取包管理器
        PackageManager packageManager = getPackageManager();

        try {
            //获取包的信息
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);

            //获取版本号和版本名称
            int versionCode = packageInfo.versionCode;
            versionName = packageInfo.versionName;

            Log.i(TAG, "versionCode : " + versionCode + "  versionName : " + versionName);

            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            //没有找到包名会抛出此异常
            e.printStackTrace();
        }

        return versionName;
    }
}
