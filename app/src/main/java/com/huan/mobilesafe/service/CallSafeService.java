package com.huan.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.SmsMessage;

import com.huan.mobilesafe.activity.CallSafeActivity;
import com.huan.mobilesafe.dao.BlackListDAO;

public class CallSafeService extends Service {

    private static final String TAG = "CallSafeServiceInfo";

    private BlackListDAO blackListDb;

    public CallSafeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //注册短信广播
        SMeceiver sMeceiver = new SMeceiver();
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(Integer.MAX_VALUE);
        registerReceiver(sMeceiver, intentFilter);

        //初始化黑名单数据库
        blackListDb = new BlackListDAO(this);
    }

    private class SMeceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //获取短信数据
            Object[] puds = (Object[]) intent.getExtras().get("pdus");
            byte[] pud = (byte[]) puds[0];
            //将字节数组封装为SmsMessage
            SmsMessage message = SmsMessage.createFromPdu(pud);
            //获取短信内容
            String content = message.getMessageBody();
            //获取发送方号码
            String senderNumber = message.getOriginatingAddress();

            //通过短信的电话号码查询拦截模式
            String mode = blackListDb.findPhone(senderNumber);
            if (CallSafeActivity.MODE_PHONE_AND_SM.equals(mode)
                    || CallSafeActivity.MODE_SM_ONLY.equals(mode)) {
                //如果该号码的拦截模式是：电话+短信或短信，就拦截
                abortBroadcast();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
