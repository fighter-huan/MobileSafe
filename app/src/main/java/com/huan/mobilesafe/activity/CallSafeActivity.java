package com.huan.mobilesafe.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.huan.mobilesafe.R;
import com.huan.mobilesafe.bean.BlackListInfo;
import com.huan.mobilesafe.dao.BlackListDAO;

import java.util.List;

/**
 * 通讯卫士页面
 */
public class CallSafeActivity extends AppCompatActivity {

    private static final String TAG = "CallSafeActivityInfo";

    //拦截模式
    public static final String MODE_PHONE_AND_SM = "电话+短信拦截";
    public static final String MODE_PHONE_ONLY = "电话拦截";
    public static final String MODE_SM_ONLY = "短信拦截";

    private ListView lvBlackList;
    private List<BlackListInfo> listInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_safe);

        initUI();
        initData();

        //ListView注册长按事件 (可修改拦截模式)
        lvBlackList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //获取长点击项的电话号码
                String phone = listInfos.get(position).getPhone();
                //修改拦截模式
                changeInterceptMode(phone);
                return true;
            }
        });

        BlackListDAO blackListDb = new BlackListDAO(this);
    }

    /**
     * 修改拦截模式
     *
     * @param phone 需要修改拦截模式的电话号码
     */
    private void changeInterceptMode(final String phone) {

        //拦截模式
        final String[] mode = new String[1];

        //弹出选择模式的对话框
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //设置标题
        builder.setTitle("请选择拦截模式");

        //单选对话框 (默认模式：电话+短信拦截)
        final String[] modeArray = new String[]{MODE_PHONE_AND_SM, MODE_PHONE_ONLY, MODE_SM_ONLY};
        builder.setSingleChoiceItems(modeArray, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //修改模式
                mode[0] = modeArray[which];
            }
        });

        //确认按钮
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BlackListDAO blackListDb = new BlackListDAO(CallSafeActivity.this);
                blackListDb.changeMode(phone, mode[0]);
                //刷新界面
                initData();
            }
        });

        //取消按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        //显示对话框
        builder.show();
    }

    /**
     * 定义适配器
     */
    private class CallSafeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return listInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return listInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = View.inflate(CallSafeActivity.this, R.layout.item_call_safe, null);

            //查找控件
            TextView tvPhone = (TextView) view.findViewById(R.id.tv_phone);
            TextView tvMode = (TextView) view.findViewById(R.id.tv_mode);
            final ImageView ivDelete = (ImageView) view.findViewById(R.id.iv_delete);

            //给ImageView注册监听事件 (删除黑名单操作)
            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //获取该项的电话号码
                    String phone = listInfos.get(position).getPhone();
                    //删除操作
                    removePhone(phone, ivDelete);
                }
            });

            tvPhone.setText(listInfos.get(position).getPhone());
            tvMode.setText(listInfos.get(position).getMode());

            return view;
        }
    }

    /**
     * 删除黑名单
     *
     * @param phone    电话号码
     * @param ivDelete 删除按钮图标
     */
    private void removePhone(final String phone, final ImageView ivDelete) {

        //显示点击效果
        ivDelete.setImageResource(R.mipmap.delete_btn_focused);

        //弹出确认对话框
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //设置标题
        builder.setTitle("提示：");

        //设置内容
        builder.setMessage("确认删除该条拦截吗？");

        //确认按钮
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //从数据库中删除数据
                BlackListDAO blackListDb = new BlackListDAO(CallSafeActivity.this);
                blackListDb.delete(phone);
                //刷新界面
                initData();
            }
        });

        //取消按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //取消点击效果
                ivDelete.setImageResource(R.mipmap.delete_btn);
            }
        });

        //显示对话框
        builder.show();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //查找黑名单数据库所有数据, 并添加到集合中
        BlackListDAO blackListDb = new BlackListDAO(this);
        listInfos = blackListDb.findAll();

        CallSafeAdapter callSafeAdapter = new CallSafeAdapter();
        //视图加载适配器
        lvBlackList.setAdapter(callSafeAdapter);
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        lvBlackList = (ListView) findViewById(R.id.black_list_view);
    }

    /**
     * 添加黑名单
     *
     * @param view
     */
    public void addBlackPhone(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();

        //把自定义布局设置给dialog
        View viewInputPhone = View.inflate(this, R.layout.dialog_add_black_list, null);
        dialog.setView(viewInputPhone);

        //显示对话框
        dialog.show();

        //获取控件
        Button btnPhoneOk = (Button) viewInputPhone.findViewById(R.id.btn_phone_ok);
        Button btnPhoneCancel = (Button) viewInputPhone.findViewById(R.id.btn_phone_cancel);
        final CheckBox cbPhone = (CheckBox) viewInputPhone.findViewById(R.id.cb_phone);
        final CheckBox cbShortMessage = (CheckBox) viewInputPhone.findViewById(R.id.cb_short_message);
        final EditText etPhoneIntercept = (EditText) viewInputPhone.findViewById(R.id.et_phone_intercept);

        //确定
        btnPhoneOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //获取输入框内容
                String phoneIntercept = etPhoneIntercept.getText().toString().trim();

                if (!TextUtils.isEmpty(phoneIntercept)) {

                    String mode = "";
                    if (cbPhone.isChecked() && cbShortMessage.isChecked()) {
                        //电话+短信拦截
                        mode = MODE_PHONE_AND_SM;
                    } else if (cbPhone.isChecked()) {
                        //电话拦截
                        mode = MODE_PHONE_ONLY;
                    } else if (cbShortMessage.isChecked()) {
                        //短信拦截
                        mode = MODE_SM_ONLY;
                    } else {
                        Toast.makeText(CallSafeActivity.this, "请选择拦截模式",
                                Toast.LENGTH_SHORT).show();
                        //结束此方法
                        return;
                    }

                    //将该电话号码和拦截模式添加到数据库
                    BlackListDAO blackListDb = new BlackListDAO(CallSafeActivity.this);
                    blackListDb.add(phoneIntercept, mode);

                    //刷新界面
                    initData();

                    //取消对话框
                    dialog.dismiss();

                } else {
                    //友好提示
                    Toast.makeText(CallSafeActivity.this, "输入框内容不能为空",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        //取消
        btnPhoneCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消对话框
                dialog.dismiss();
            }
        });
    }
}
