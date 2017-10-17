package com.jaj.aho.osframe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import com.blankj.utilcode.util.NetworkUtils;
import com.jaj.aho.osframe.frame.AppManager;
import com.tapadoo.alerter.Alerter;

/**
 * 网络状态监听
 */

public class NetWorkStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!NetworkUtils.getDataEnabled()) {
            Alerter.create(AppManager.create().topActivity()).setTitle("网络状态：").setText("移动数据断开").setBackgroundColorInt(Color.RED).setDuration(2000).show();
        } else if (!NetworkUtils.isWifiAvailable()) {
            Alerter.create(AppManager.create().topActivity()).setTitle("网络状态：").setText("WIFI网络断开").setBackgroundColorInt(Color.RED).setDuration(2000).show();
        } else if (!NetworkUtils.isConnected()) {
            Alerter.create(AppManager.create().topActivity()).setTitle("网络状态：").setText("无网络连接").setBackgroundColorInt(Color.RED).setDuration(2000).show();
        }
    }
}
