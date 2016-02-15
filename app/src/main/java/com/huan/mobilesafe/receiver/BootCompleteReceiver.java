package com.huan.mobilesafe.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/**
 * 监听手机开机的广播
 */
public class BootCompleteReceiver extends BroadcastReceiver {

    private static final String TAG = "BootCompleteReceiverInfo";

    private SharedPreferences mSharedPreferences;

    public BootCompleteReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        //如果用户已经绑定SIM卡,就获取SIM卡序列号
        mSharedPreferences = context.getSharedPreferences("configuration",
                Context.MODE_PRIVATE);
        String simSerialNumber = mSharedPreferences.getString("simSerialNumber", null);
        if (simSerialNumber != null) {
            //获取开机后当前的SIM卡序列号
            TelephonyManager telephonyManager = (TelephonyManager)
                    context.getSystemService(Context.TELEPHONY_SERVICE);
            String currentSimSerialNumber = telephonyManager.getSimSerialNumber();

            if (currentSimSerialNumber != null) {
                //表示有SIM卡，然后进行对比
                if (currentSimSerialNumber.equals(simSerialNumber)) {
                    Toast.makeText(context, "SIM卡未更换", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "SIM卡已更换", Toast.LENGTH_LONG).show();
                    //读取安全号码
                    String safePhone = mSharedPreferences.getString("safe_phone", "");
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(safePhone, null, "SIM卡已改变", null, null);
                }
            }
        }
    }
}
