package com.huan.mobilesafe.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.huan.mobilesafe.R;
import com.huan.mobilesafe.utils.StreamUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivityInfo";

    private static final int CODE_UPDATE_DIALOG = 0;
    private static final int CODE_URL_ERROR = 1;
    private static final int CODE_NET_ERROR = 2;
    private static final int CODE_JSON_ERROR = 3;
    private static final int CODE_ENTER_HOME = 4;
    private static final int CODE_DOWNLOADING = 5;
    private static final int CODE_DOWNLOAD_FINISH = 6;

    private TextView tvVersion;
    private TextView tvDownloadProgress;

    //从服务器上获取的信息
    //版本名v
    private String mVersionName;
    //版本号
    private int mVersionCode;
    //版本描述
    private String mDescription;
    //下载地址
    private String mDownloadUrl;

    //关联初始化数据的线程 (检查版本更新，网络，数据解析)
    private Handler mHandlerSplash = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //用switch语句判断消息码
            switch (msg.what) {
                case CODE_UPDATE_DIALOG:
                    showUpdateDialog();
                    break;

                case CODE_URL_ERROR:
                    Toast.makeText(SplashActivity.this, "地址错误", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;

                case CODE_NET_ERROR:
                    Toast.makeText(SplashActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;

                case CODE_JSON_ERROR:
                    Toast.makeText(SplashActivity.this, "数据解析错误", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;

                case CODE_ENTER_HOME:
                    enterHome();
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tvVersion = (TextView) findViewById(R.id.tv_version);
        tvDownloadProgress = (TextView) findViewById(R.id.tv_download_progress);

        //设置版本名
        tvVersion.setText("当前版本:" + getVersionName());

        //检查版本
        checkVersion();
    }

    /**
     * 获取本地版本名
     *
     * @return 返回本地版本名，若出现异常，返回 null
     */
    private String getVersionName() {

        //声明版本名
        String versionName = "";

        //获取包管理器
        PackageManager packageManager = getPackageManager();

        try {
            //获取包的信息
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);

            //获取版本名
            versionName = packageInfo.versionName;
            return versionName;

        } catch (PackageManager.NameNotFoundException e) {
            //没有找到包名会抛出此异常
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取本地版本号
     *
     * @return 返回本地版本号，若出现异常，返回 -1
     */
    private int getVersionCode() {

        //声明版本号
        int versionCode = 0;

        //获取包管理器
        PackageManager packageManager = getPackageManager();

        try {
            //获取包的信息
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);

            //获取版本号
            versionCode = packageInfo.versionCode;
            return versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            //没有找到包名会抛出此异常
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * 从服务器获取版本信息进行校验
     */
    private void checkVersion() {

        //获取检查版本开始的时间 (在内部类中访问局部变量，该局部变量需要用final修饰)
        final long startTime = System.currentTimeMillis();

        //启动子线程异步加载数据
        new Thread() {
            @Override
            public void run() {
                //获取消息实例
                Message msg = Message.obtain();

                HttpURLConnection conn = null;
                try {
                    //填写本机地址
                    URL url = new URL("http://192.168.1.102:8080/update.json");
                    conn = (HttpURLConnection) url.openConnection();
                    //设置请求方法
                    conn.setRequestMethod("GET");
                    //设置连接超时(5s)
                    conn.setConnectTimeout(5 * 1000);
                    //设置响应超时(5s)，响应超时是指：已连接上服务器，但服务器迟迟不给响应
                    conn.setReadTimeout(5 * 1000);

                    //连接服务器
                    conn.connect();

                    //获取响应码
                    int responseCode = conn.getResponseCode();
                    if (responseCode == 200) {
                        InputStream inputStream = conn.getInputStream();
                        String result = StreamUtils.readFromStream(inputStream);

                        //解析JSON数据
                        JSONObject jsonObject = new JSONObject(result);
                        mVersionName = jsonObject.getString("versionName");
                        mVersionCode = jsonObject.getInt("versionCode");
                        mDescription = jsonObject.getString("description");
                        mDownloadUrl = jsonObject.getString("downloadUrl");

                        //判断是否有更新
                        if (mVersionCode > getVersionCode()) {
                            //服务器版本号大于本地版本号,说明有更新，弹出升级对话框
                            msg.what = CODE_UPDATE_DIALOG;
                        } else {
                            //没有版本更新
                            msg.what = CODE_ENTER_HOME;
                        }
                    }
                } catch (MalformedURLException e) {
                    //url错误异常
                    msg.what = CODE_URL_ERROR;
                    e.printStackTrace();
                } catch (IOException e) {
                    //网络错误异常
                    msg.what = CODE_NET_ERROR;
                    e.printStackTrace();
                } catch (JSONException e) {
                    //JSON解析失败
                    msg.what = CODE_JSON_ERROR;
                    e.printStackTrace();
                } finally {
                    //获取检查版本结束的时间
                    long endTime = System.currentTimeMillis();

                    //计算访问网络花费的时间
                    long timeUsed = endTime - startTime;

                    if (timeUsed < (2 * 1000)) {
                        //强制睡眠一段时间，保证闪屏页面展示2s
                        try {
                            Thread.sleep(2000 - timeUsed);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    //发送消息
                    mHandlerSplash.sendMessage(msg);
                    if (conn != null) {
                        //关闭网络连接
                        conn.disconnect();
                    }
                }
            }
        }.start();
    }

    /**
     * 弹出升级对话框
     */
    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置标题
        builder.setTitle("最新版本：" + mVersionName);
        //设置内容
        builder.setMessage(mDescription);

        //更新按钮
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                download();
            }
        });

        //取消按钮
        builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //跳转到主页面
                enterHome();
            }
        });

        //显示对话框
        builder.show();
    }

    /**
     * 下载Apk文件
     */
    private void download() {

        //关联下载Apk子线程 (给UI主线程实时反馈下载进度)
        final Handler mHandlerDownload = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case CODE_DOWNLOADING:
                        //计算下载量的百分比，显示进度
                        int progress = (msg.arg1 * 100) / msg.arg2;
                        tvDownloadProgress.setVisibility(View.VISIBLE);
                        tvDownloadProgress.setText("下载进度: " + progress + " %");
                        break;

                    case CODE_DOWNLOAD_FINISH:
                        tvDownloadProgress.setText("下载完成");
                        break;

                    default:
                        break;
                }
            }
        };

        //启动子线程下载文件
        new Thread() {
            @Override
            public void run() {

                InputStream inputStream = null;
                FileOutputStream fileOutputStream = null;
                try {
                    URL url = new URL(mDownloadUrl);
                    URLConnection conn = url.openConnection();
                    inputStream = conn.getInputStream();

                    //获取Apk文件大小 (单位:byte)
                    int fileLength = conn.getContentLength();
                    //初始化下载前文件大小为0
                    int downedFileLength = 0;

                    //获取Apk名称
                    String filename = mDownloadUrl.substring(mDownloadUrl.lastIndexOf("/"));

                    //判断SD卡是否正常挂载
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        //获取SD卡规范路径，设置下载路径
                        String filePath = Environment.getExternalStorageDirectory() + filename;

                        fileOutputStream = new FileOutputStream(filePath);
                        byte[] buffer = new byte[1024];
                        int len = 0;
                        //读取数据，并写入SD卡
                        while ((len = inputStream.read(buffer)) != -1) {
                            //获取消息
                            Message msg = new Message();

                            fileOutputStream.write(buffer, 0, len);
                            //下载累加
                            downedFileLength += len;

                            if (downedFileLength < fileLength) {
                                //下载中
                                msg.what = CODE_DOWNLOADING;
                                //传递已下载大小和总大小
                                msg.arg1 = downedFileLength;
                                msg.arg2 = fileLength;
                            }

                            if (downedFileLength == fileLength) {
                                //下载完成
                                msg.what = CODE_DOWNLOAD_FINISH;
                            }

                            //发送消息
                            mHandlerDownload.sendMessage(msg);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        //关闭流
                        inputStream.close();
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    /**
     * 进入主页面
     */
    private void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        //销毁SplashActivity
        finish();
    }
}
