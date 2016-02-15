package com.huan.mobilesafe.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.huan.mobilesafe.R;
import com.huan.mobilesafe.db.AddressDb;

public class PhoneAreaActivity extends AppCompatActivity {

    private EditText etPhoneQuery;
    private TextView tvResultQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_area);

        etPhoneQuery = (EditText) findViewById(R.id.et_phone_query);
        tvResultQuery = (TextView) findViewById(R.id.tv_result_query);

        //监听EditText的变化
        etPhoneQuery.addTextChangedListener(new TextWatcher() {
            //变化前回调
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            //变化时回调
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String result = AddressDb.getAddress(s.toString());
                //显示查询结果
                tvResultQuery.setText("查询结果：" + result);
            }

            //变化后回调
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 查询归属地
     *
     * @param view
     */
    public void query(View view) {
        //获取号码
        String phoneNumber = etPhoneQuery.getText().toString().trim();
        if (!TextUtils.isEmpty(phoneNumber)) {
            String result = AddressDb.getAddress(phoneNumber);
            //显示查询结果
            tvResultQuery.setText("查询结果：" + result);
        }
    }
}
