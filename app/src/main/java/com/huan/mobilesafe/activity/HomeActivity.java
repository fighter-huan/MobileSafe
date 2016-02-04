package com.huan.mobilesafe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.huan.mobilesafe.R;

/**
 * 主页面
 */
public class HomeActivity extends AppCompatActivity {

    private GridView gvHome;

    //定义每一项功能的名称和图标
    private String[] mName = new String[]{"手机防盗", "通讯卫士", "软件管理",
            "进程管理", "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心"};
    private int[] mIcon = new int[]{R.mipmap.home_safe,
            R.mipmap.home_callmsgsafe,
            R.mipmap.home_apps,
            R.mipmap.home_taskmanager,
            R.mipmap.home_netmanager,
            R.mipmap.home_trojans,
            R.mipmap.home_sysoptimize,
            R.mipmap.home_tools,
            R.mipmap.home_settings};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        gvHome = (GridView) findViewById(R.id.gv_home);

        //视图加载适配器
        gvHome.setAdapter(new HomeAdapter());

        //监听每一项的点击事件
        gvHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        break;

                    case 1:
                        break;

                    case 2:
                        break;

                    case 3:
                        break;

                    case 4:
                        break;

                    case 5:
                        break;

                    case 6:
                        break;

                    case 7:
                        break;

                    case 8:
                        //设置中心
                        startActivity(new Intent(HomeActivity.this, SettingActivity.class));
                        break;

                    default:
                        break;
                }
            }
        });
    }

    /**
     * 定义适配器
     */
    class HomeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mName.length;
        }

        @Override
        public Object getItem(int position) {
            return mName[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * 此方法别调用 9 次
         *
         * @param position
         * @param convertView
         * @param parent
         * @return
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //获取home_list_item布局对象
            View view = View.inflate(HomeActivity.this, R.layout.home_list_item, null);

            //获取每一项的图标和名称
            ImageView ivIcon = (ImageView) view.findViewById(R.id.iv_Icon);
            TextView tvName = (TextView) view.findViewById(R.id.tv_name);

            //初始化每一项的图标和名称
            ivIcon.setImageResource(mIcon[position]);
            tvName.setText(mName[position]);

            //返回视图
            return view;
        }
    }
}
