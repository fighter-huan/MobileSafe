package com.huan.mobilesafe.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.huan.mobilesafe.R;
import com.huan.mobilesafe.bean.AppInfos;
import com.huan.mobilesafe.logic.AppManager;

import java.util.List;

public class AppManagerActivity extends AppCompatActivity {

    private static final String TAG = "AppManagerActivityInfo";

    private TextView tvRom;
    private TextView tvSd;
    private ListView lvAppList;
    private List<AppInfos> appInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initData();

        //lvAppList注册监听器
        lvAppList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent launchIntentForPackage =
                        getPackageManager().getLaunchIntentForPackage(appInfos.get(position)
                                .getPackageName());
                startActivity(launchIntentForPackage);
            }
        });
    }

    //初始化适配器
    private class AppManagerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return appInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return appInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(AppManagerActivity.this, R.layout.item_app_manager, null);

            //查找控件
            ImageView ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
            TextView tvAppName = (TextView) view.findViewById(R.id.tv_app_name);
            TextView tvAppLocation = (TextView) view.findViewById(R.id.tv_app_location);
            TextView tvAppSize = (TextView) view.findViewById(R.id.tv_app_size);
            Button btnUninstall = (Button) view.findViewById(R.id.btn_uninstall);

            //获取appInfos集合中的每一项
            AppInfos appInfo = appInfos.get(position);
            //获取图标
            Drawable icon = appInfo.getIcon();
            //获取名称
            final String appName = appInfo.getApkName();
            //获取包名
            final String packageName = appInfo.getPackageName();
            //获取安装位置
            boolean isRom = appInfo.isRom();
            //获取大小
            String appSize = appInfo.getApkSize();

            //将获取到的图标，名称，安装位置，大小设置给控件
            ivIcon.setImageDrawable(icon);
            tvAppName.setText(appName);
            tvAppSize.setText(appSize);
            if (isRom) {
                tvAppLocation.setText("手机内存");
            } else {
                tvAppLocation.setText("SD卡");
            }

            //给btnUninstall注册点击事件
            btnUninstall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //卸载
                    Uri packageUri = Uri.parse("package:" + packageName);
                    Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageUri);
                    startActivity(uninstallIntent);

                    //刷新界面
                    initUI();
                    initData();
                }
            });


            return view;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //创建适配器并加载
            AppManagerAdapter adapter = new AppManagerAdapter();
            lvAppList.setAdapter(adapter);
        }
    };

    /**
     * 初始化数据
     */
    private void initData() {
        //获取应用程序属于耗时操作，所以另起线程
        new Thread() {
            @Override
            public void run() {
                //获取到所有安装到手机上的应用程序
                appInfos = AppManager.getAppInfos(AppManagerActivity.this);
                //发送空消息
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        setContentView(R.layout.activity_app_manager);

        tvRom = (TextView) findViewById(R.id.tv_rom);
        tvSd = (TextView) findViewById(R.id.tv_sd);
        lvAppList = (ListView) findViewById(R.id.lv_app_list);

        //获取rom的剩余空间
        long romFreeSpace = Environment.getDataDirectory().getFreeSpace();
        //获取SD卡的剩余空间
        long sdFreeSpace = Environment.getExternalStorageDirectory().getFreeSpace();

        tvRom.setText("内存可用：" + Formatter.formatFileSize(this, romFreeSpace));
        tvSd.setText("SD卡可用：" + Formatter.formatFileSize(this, sdFreeSpace));
    }
}
