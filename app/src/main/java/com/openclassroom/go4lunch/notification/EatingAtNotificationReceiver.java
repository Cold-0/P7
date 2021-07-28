package com.openclassroom.go4lunch.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class EatingAtNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        EatingAtNotificationSender notificationHelper = new EatingAtNotificationSender(context);
        notificationHelper.sendNotification();
    }
}