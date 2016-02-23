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
import com.huan.mobilesafe.activity.AppLockActivity;
import com.huan.mobilesafe.bean.AppInfos;
import com.huan.mobilesafe.dao.AppLockDAO;
import com.huan.mobilesafe.service.TaskService;
import com.huan.mobilesafe.utils.AppManager;
import com.huan.mobilesafe.utils.ServiceStatusUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * LockFragment
 *
 * @author: 欢
 * @time: 2016/2/18 21:19
 */
public class LockFragment extends Fragment {

    private List<AppInfos> appInfos;
    private ListView lvlock;
    private TextView tvlock;
    private AppLockDAO appLockDAO;
    private List<AppInfos> lockAppInfos;
    private LockAdapter lockAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_lock_list, null);

        //查找控件
        lvlock = (ListView) view.findViewById(R.id.lv_lock_list);
        tvlock = (TextView) view.findViewById(R.id.tv_lock);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        //创建List集合 (用于存放已加锁的APP)
        lockAppInfos = new ArrayList<>();

        //开启子线程获取APP
        new Thread() {
            public void run() {
                //获取手机上安装的全部APP
                appInfos = AppManager.getAppInfos(getContext());

                //创建数据库操作类
                appLockDAO = new AppLockDAO(getContext());

                //遍历appInfo, 并筛选出AppLockDAO数据库中存在的进行加载
                for (AppInfos appInfo : appInfos) {
                    //获取包名
                    String packageName = appInfo.getPackageName();
                    //如果数据库中存在，则存入lockAppInfos集合中
                    if (appLockDAO.find(packageName)) {
                        lockAppInfos.add(appInfo);
                    }
                }

                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            //加载适配器
            lockAdapter = new LockAdapter();
            lvlock.setAdapter(lockAdapter);
        }
    };

    private class LockAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            tvlock.setText("已加锁软件(" + lockAppInfos.size() + ")");
            return lockAppInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return lockAppInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getContext(), R.layout.item_lock, null);

            //查找控件
            final ImageView ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
            final TextView tvAppName = (TextView) view.findViewById(R.id.tv_app_name);
            final ImageView ivLockUnlock = (ImageView) view.findViewById(R.id.iv_lock_unlock);

            //设置控件内容
            final AppInfos appInfo = lockAppInfos.get(position);
            ivIcon.setImageDrawable(appInfo.getIcon());
            tvAppName.setText(appInfo.getApkName());

            //给ivLockUnlock设置监听
            ivLockUnlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //获取该项的包名，并将其从程序锁数据库中删除
                    String packageName = lockAppInfos.get(position).getPackageName();
                    appLockDAO.delete(packageName);

                    //将该项的软件从lockAppInfos集合中删除
                    lockAppInfos.remove(position);

                    //刷新界面
                    lockAdapter.notifyDataSetChanged();

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
