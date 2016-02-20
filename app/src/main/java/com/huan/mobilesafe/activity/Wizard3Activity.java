package com.huan.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.huan.mobilesafe.R;

/**
 * 第三个设置向导页面
 */
public class Wizard3Activity extends WizardBaseActivity {

    private static final String TAG = "Wizard3ActivityInfo";

    private EditText etPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard3);

        etPhone = (EditText) findViewById(R.id.et_phone);

        String phone = mSharedPreferences.getString("safe_phone", "");
        etPhone.setText(phone);
    }

    @Override
    protected void showPreviousPage() {
        finish();
        startActivity(new Intent(this, Wizard2Activity.class));
        overridePendingTransition(R.anim.tran_previous_in, R.anim.tran_previous_out);
    }

    @Override
    protected void showNextPage() {
        //判断是否输入了安全号码(不输入安全号码不允许下一步)
        //过滤空格
        String phone = etPhone.getText().toString().trim();
        if ("".equals(phone)) {
            Toast.makeText(this, "安全号码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        //保存安全号码
        mSharedPreferences.edit().putString("safe_phone", phone).commit();

        finish();
        startActivity(new Intent(this, Wizard4Activity.class));
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String phone = data.getStringExtra("phone");
            etPhone.setText(phone);
        }
    }

    /**
     * 选择联系人
     *
     * @param view
     */
    public void selectContacts(View view) {
        Intent intent = new Intent(this, ContactsActivity.class);
        startActivityForResult(intent, 0);
    }
}
