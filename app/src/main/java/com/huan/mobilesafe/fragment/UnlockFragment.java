package com.huan.mobilesafe.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.huan.mobilesafe.service.TaskService;
import com.huan.mobilesafe.utils.AppManager;
import com.huan.mobilesafe.utils.ServiceStatusUtils;

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
    private UnlockAdapter unlockAdapter;
    private List<AppInfos> unlockAppInfos;

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

        //创建一个集合unlockAppInfos (未加锁APP)
        unlockAppInfos = new ArrayList<>();

        appLockDAO = new AppLockDAO(getContext());

        //开启子线程
        new Thread() {
            @Override
            public void run() {
                //获取安装在手机上的所有App
                appInfos = AppManager.getAppInfos(getContext());

                //遍历appInfo (筛选数据库中不存在的APP进行加载)
                for (AppInfos appInfo : appInfos) {
                    //获取包名
                    String packageName = appInfo.getPackageName();
                    //如果数据库中不存在，则存入unlockAppInfos集合中
                    if (!(appLockDAO.find(packageName))) {
                        unlockAppInfos.add(appInfo);
                    }
                }

                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            unlockAdapter = new UnlockAdapter();

            //视图加载适配器
            lvUnlock.setAdapter(unlockAdapter);
        }
    };

    //定义适配器
    private class UnlockAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            tvUnlock.setText("未加锁软件(" + unlockAppInfos.size() + ")");
            return unlockAppInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return unlockAppInfos.get(position);
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

            //设置控件内容
            final AppInfos appInfo = unlockAppInfos.get(position);
            ivIcon.setImageDrawable(appInfo.getIcon());
            tvAppName.setText(appInfo.getApkName());

            //给ivLockUnlock设置监听
            ivLockUnlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //获取该项的包名，并将其添加到数据库(app_locked)中
                    String packageName = unlockAppInfos.get(position).getPackageName();
                    appLockDAO.add(packageName);

                    //将该项的软件从appInfos集合中删除
                    unlockAppInfos.remove(position);

                    //刷新界面
                    unlockAdapter.notifyDataSetChanged();

                    //如果程序锁服务(TaskService)已开启，就重新启动服务（达到刷新服务的效果）
                    if (ServiceStatusUtils.isServiceRunning(getContext(),
                            "com.huan.mobilesafe.service.TaskService")) {
                        //刷新服务
                        getContext().stopService(new Intent(getContext(), TaskService.class));
                        getContext().startService(new Intent(getContext(), TaskService.class));
                    }
                }
            });

            return view;
        }
    }
}
