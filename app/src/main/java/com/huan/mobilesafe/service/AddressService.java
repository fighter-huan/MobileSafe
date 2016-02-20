package com.huan.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.huan.mobilesafe.dao.AddressDAO;
import com.huan.mobilesafe.receiver.OutCallReceiver;

/**
 * 来电提醒服务
 */
public class AddressService extends Service {

    private static final String TAG = "AddressServiceInfo";

    private TelephonyManager telephonyManager = null;
    private MyListener listener = null;
    OutCallReceiver outCallReceiver = null;

    public AddressService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        listener = new MyListener();
        //监听来电的状态
        telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

        //注册监听去电广播
        outCallReceiver = new OutCallReceiver();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
        registerReceiver(outCallReceiver, intentFilter);
    }

    //电话状态监听器
    class MyListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);

            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    //根据来电号码查询归属地
                    String address = AddressDAO.getAddress(incomingNumber);
                    Toast.makeText(AddressService.this, address, Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //停止后台服务
        telephonyManager.listen(listener, PhoneStateListener.LISTEN_NONE);

        //注销监听去电广播
        unregisterReceiver(outCallReceiver);
    }
}
