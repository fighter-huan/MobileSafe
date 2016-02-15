package com.huan.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.huan.mobilesafe.db.AddressDb;

public class OutCallReceiver extends BroadcastReceiver {
    public OutCallReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        //获取呼出的电话号码
        String number = getResultData();

        //获取归属地
        String address = AddressDb.getAddress(number);

        Toast.makeText(context, address, Toast.LENGTH_LONG).show();
    }
}
