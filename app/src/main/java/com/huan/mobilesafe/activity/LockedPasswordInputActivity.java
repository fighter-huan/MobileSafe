package com.huan.mobilesafe.activity;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.huan.mobilesafe.R;

import java.util.List;

public class LockedPasswordInputActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LockedPasswordInputActivityInfo";

    //输入的密码字符串
    private String passwordInput = "";

    private ImageView ivRevert;
    private EditText etPassword;
    private Button btnOK;
    private Button btn_1;
    private Button btn_2;
    private Button btn_3;
    private Button btn_4;
    private Button btn_5;
    private Button btn_6;
    private Button btn_7;
    private Button btn_8;
    private Button btn_9;
    private Button btn_0;
    private Button btnClean;
    private ImageButton btnDelete;

    //用户偏好
    private SharedPreferences msharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_input);

        initUI();

        //检索配置文件configuration
        msharedPreferences = getSharedPreferences("configuration", MODE_PRIVATE);
    }

    private void initUI() {
        ivRevert = (ImageView) findViewById(R.id.iv_revert);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnOK = (Button) findViewById(R.id.btn_ok);
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_3 = (Button) findViewById(R.id.btn_3);
        btn_4 = (Button) findViewById(R.id.btn_4);
        btn_5 = (Button) findViewById(R.id.btn_5);
        btn_6 = (Button) findViewById(R.id.btn_6);
        btn_7 = (Button) findViewById(R.id.btn_7);
        btn_8 = (Button) findViewById(R.id.btn_8);
        btn_9 = (Button) findViewById(R.id.btn_9);
        btn_0 = (Button) findViewById(R.id.btn_0);
        btnClean = (Button) findViewById(R.id.btn_clean);
        btnDelete = (ImageButton) findViewById(R.id.btn_delete);

        ivRevert.setOnClickListener(this);
        btnOK.setOnClickListener(this);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);
        btn_6.setOnClickListener(this);
        btn_7.setOnClickListener(this);
        btn_8.setOnClickListener(this);
        btn_9.setOnClickListener(this);
        btn_0.setOnClickListener(this);
        btnClean.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_0:
                passwordInput = passwordInput + 0;
                etPassword.setText(passwordInput);
                break;

            case R.id.btn_1:
                passwordInput = passwordInput + 1;
                etPassword.setText(passwordInput);
                break;

            case R.id.btn_2:
                passwordInput = passwordInput + 2;
                etPassword.setText(passwordInput);
                break;

            case R.id.btn_3:
                passwordInput = passwordInput + 3;
                etPassword.setText(passwordInput);
                break;

            case R.id.btn_4:
                passwordInput = passwordInput + 4;
                etPassword.setText(passwordInput);
                break;

            case R.id.btn_5:
                passwordInput = passwordInput + 5;
                etPassword.setText(passwordInput);
                break;

            case R.id.btn_6:
                passwordInput = passwordInput + 6;
                etPassword.setText(passwordInput);
                break;

            case R.id.btn_7:
                passwordInput = passwordInput + 7;
                etPassword.setText(passwordInput);
                break;

            case R.id.btn_8:
                passwordInput = passwordInput + 8;
                etPassword.setText(passwordInput);
                break;

            case R.id.btn_9:
                passwordInput = passwordInput + 9;
                etPassword.setText(passwordInput);
                break;

            case R.id.btn_delete:
                int len = passwordInput.length();
                if (len > 0) {
                    passwordInput = passwordInput.substring(0, len - 1);
                }
                etPassword.setText(passwordInput);
                break;

            case R.id.btn_clean:
                passwordInput = "";
                etPassword.setText(passwordInput);
                break;

            case R.id.btn_ok:
                checkPassword();
                break;

            case R.id.iv_revert:
                //返回 (销毁当前活动)
//                finish();
                Intent intent = new Intent();
                intent.setAction("android.intent.action.MAIN");
                intent.addCategory("android.intent.category.HOME");
                intent.addCategory("android.intent.category.DEFAULT");
                startActivity(intent);
                break;
        }
    }

    private void checkPassword() {

        //获取配置文件信息（默认为null）
        String appLockPassword = msharedPreferences.getString("app_lock_password", null);

        //如果用户输入了有效字符，就进行处理
        if (!TextUtils.isEmpty(passwordInput)) {

            if (passwordInput.equals(appLockPassword)) {

                //表明已经设置过密码，并且输入密码正确，销毁当前活动，发送停止保护广播
                Intent intentFir = new Intent();
                intentFir.setAction("com.huan.mobilesafe.tempstopprotect");
                sendBroadcast(intentFir);
                finish();

            } else {
                //表明已经设置过密码，但是输入密码不正确，给予友好提示
                Toast.makeText(LockedPasswordInputActivity.this, "密码错误，请重新输入",
                        Toast.LENGTH_SHORT).show();
            }

        } else {
            //友好提示
            Toast.makeText(LockedPasswordInputActivity.this, "输入框内容不能为空",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
