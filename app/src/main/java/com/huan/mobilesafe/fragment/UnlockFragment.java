package com.huan.mobilesafe.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.huan.mobilesafe.R;
import com.huan.mobilesafe.bean.AppInfos;
import com.huan.mobilesafe.dao.AppLockDAO;
import com.huan.mobilesafe.logic.AppManager;

import java.util.ArrayList;
import java.util.List;

/**
 * UnlockFragment
 *
 * @author: 欢
 * @time: 2016/2/18 21:19
 */
public class UnlockFragment extends Fragment {

    private static final String TAG = "UnlockFragmentInfo";

    private ListView lvUnlock;
    private TextView tvUnlock;
    private List<AppInfos> appInfos;
    private AppLockDAO appLockDAO;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_unlock_list, null);

        //查找控件
        lvUnlock = (ListView) view.findViewById(R.id.lv_unlock_list);
        tvUnlock = (TextView) view.findViewById(R.id.tv_unlock);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        appLockDAO = new AppLockDAO(getContext());

        //开启子线程
        new Thread() {
            @Override
            public void run() {
                //获取安装在手机上的所有App
                appInfos = AppManager.getAppInfos(getContext());

                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            UnlockAdapter unlockAdapter = new UnlockAdapter();

            //视图加载适配器
            lvUnlock.setAdapter(unlockAdapter);
        }
    };

    //定义适配器
    private class UnlockAdapter extends BaseAdapter {

        private final List<AppInfos> appUnlocked;

        /**
         * 无参构造器 (获取未加锁APP)
         */
        public UnlockAdapter() {
            //创建存放未加锁APP的集合
            appUnlocked = new ArrayList();

            //遍历appInfos集合 (手机上所有安装的App)
            for (AppInfos appInfo : appInfos) {
                //获取包名
                String packageName = appInfo.getPackageName();
                if (!(appLockDAO.find(packageName))) {
                    appUnlocked.add(appInfo);
                }
            }
        }

        @Override
        public int getCount() {
            return appUnlocked.size();
        }

        @Override
        public Object getItem(int position) {
            return appUnlocked.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getContext(), R.layout.item_unlock, null);

            //查找控件
            final ImageView ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
            final TextView tvAppName = (TextView) view.findViewById(R.id.tv_app_name);
            final ImageView ivLockUnlock = (ImageView) view.findViewById(R.id.iv_lock_unlock);

            tvUnlock.setText("未加锁软件(" + appUnlocked.size() + ")");

            //设置控件内容
            AppInfos appInfo = appUnlocked.get(position);
            ivIcon.setImageDrawable(appInfo.getIcon());
            tvAppName.setText(appInfo.getApkName());

            //给ivLockUnlock设置监听
            ivLockUnlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //获取该项的包名，并将其添加到数据库(app_locked)中
                    String packageName = appUnlocked.get(position).getPackageName();

                    //如果程序锁数据库中不存在，就添加 (不能重复添加)
                    if (!(appLockDAO.find(packageName))) {
                        appLockDAO.add(packageName);
                    }

                    //刷新界面
                    //遍历appInfos集合 (手机上所有安装的App)
                    List<AppInfos> appUnlockAfterClick = new ArrayList();
                    for (AppInfos appInfo : appUnlocked) {
                        //获取包名
                        String packageNameAfterClick = appInfo.getPackageName();
                        if (!(appLockDAO.find(packageNameAfterClick))) {
                            appUnlockAfterClick.add(appInfo);
                        }
                    }
                    tvUnlock.setText("未加锁软件(" + appUnlockAfterClick.size() + ")");

                    //设置控件内容
                    AppInfos appInfo = appUnlockAfterClick.get(position);
                    ivIcon.setImageDrawable(appInfo.getIcon());
                    tvAppName.setText(appInfo.getApkName());
                }
            });

            return view;
        }
    }
}
