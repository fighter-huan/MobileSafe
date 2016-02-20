package com.huan.mobilesafe.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huan.mobilesafe.R;
import com.huan.mobilesafe.utils.MD5Utils;

/**
 * 主页面
 */
public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivityInfo";

    private GridView gvHome;

    private SharedPreferences mSharedPreferences;

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

        //用户偏好数据文件
        mSharedPreferences = getSharedPreferences("configuration", MODE_PRIVATE);

        //视图加载适配器
        gvHome.setAdapter(new HomeAdapter());

        //监听每一项的点击事件
        gvHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        //手机防盗
                        showPasswordDialog();
                        break;

                    case 1:
                        //通讯卫士
                        startActivity(new Intent(HomeActivity.this, CallSafeActivity.class));
                        break;

                    case 2:
                        //软件管理
                        startActivity(new Intent(HomeActivity.this, AppManagerActivity.class));
                        break;

                    case 3:
                        //进程管理
                        startActivity(new Intent(HomeActivity.this, TaskManagerActivity.class));
                        break;

                    case 4:
                        //流量统计
                        Toast.makeText(HomeActivity.this, "此功能未实现", Toast.LENGTH_SHORT).show();
                        break;

                    case 5:
                        //手机杀毒
                        break;

                    case 6:
                        //缓存清理
                        Toast.makeText(HomeActivity.this, "此功能未实现", Toast.LENGTH_SHORT).show();
                        break;

                    case 7:
                        //高级工具
                        startActivity(new Intent(HomeActivity.this, AdvancedToolsActivity.class));
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
     * 弹出输入密码对话框
     */
    private void showPasswordDialog() {
        //判断是否已经设置过密码
        String savedPassword = mSharedPreferences.getString("password", null);
        if (!TextUtils.isEmpty(savedPassword)) {
            //弹出输入密码对话框
            showPasswordInputDialog();
        } else {
            //弹出设置密码对话框
            showPasswordSetDialog();
        }
    }

    /**
     * 输入密码对话框
     */
    private void showPasswordInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        //将自定义布局设置给dialog
        View view = View.inflate(this, R.layout.dialog_input_password, null);
        dialog.setView(view);

        //获取控件
        final EditText etPasswordFirst = (EditText) view.findViewById(R.id.et_password);
        Button btnPasswordOK = (Button) view.findViewById(R.id.btn_password_ok);
        Button btnPasswordCancel = (Button) view.findViewById(R.id.btn_password_cancel);

        //显示对话框
        dialog.show();

        //注册按钮监听器
        //确定
        btnPasswordOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取输入的数据
                String password = etPasswordFirst.getText().toString();
                if (!TextUtils.isEmpty(password)) {
                    //获取用户已设置的密码
                    String savedPassword = mSharedPreferences.getString("password", null);
                    if (MD5Utils.encode(password).equals(savedPassword)) {
                        Toast.makeText(HomeActivity.this, "登陆成功",
                                Toast.LENGTH_SHORT).show();

                        //退出对话框
                        dialog.dismiss();

                        //跳转到手机防盗页面
                        startActivity(new Intent(HomeActivity.this, GuardActivity.class));
                    } else {
                        Toast.makeText(HomeActivity.this, "密码错误，请重新输入",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(HomeActivity.this, "输入框内容不能为空",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        //取消
        btnPasswordCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 设置密码对话框
     */
    private void showPasswordSetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        //将自定义布局设置给dialog
        View view = View.inflate(this, R.layout.dialog_set_password, null);
        dialog.setView(view);

        //获取控件
        final EditText etPasswordFirst = (EditText) view.findViewById(R.id.et_password_first);
        final EditText etPasswordSecond = (EditText) view.findViewById(R.id.et_password_second);
        Button btnPasswordOK = (Button) view.findViewById(R.id.btn_password_ok);
        Button btnPasswordCancel = (Button) view.findViewById(R.id.btn_password_cancel);

        //显示对话框
        dialog.show();

        //注册按钮监听器
        //确定
        btnPasswordOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取输入内容
                String passwordFirst = etPasswordFirst.getText().toString();
                String passwordSecond = etPasswordSecond.getText().toString();

                //输入内容有效性检验
                if (!TextUtils.isEmpty(passwordFirst) && !TextUtils.isEmpty(passwordSecond)) {
                    if (passwordFirst.equals(passwordSecond)) {

                        //保存密码 (进行MD5加密)
                        mSharedPreferences.edit().putString("password",
                                MD5Utils.encode(passwordFirst)).commit();

                        Toast.makeText(HomeActivity.this, "登陆成功",
                                Toast.LENGTH_SHORT).show();

                        //退出对话框
                        dialog.dismiss();

                        //跳转到手机防盗页面
                        startActivity(new Intent(HomeActivity.this, GuardActivity.class));
                    } else {
                        Toast.makeText(HomeActivity.this, "两次密码不一致",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(HomeActivity.this, "输入框内容不能为空",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        //取消
        btnPasswordCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
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
