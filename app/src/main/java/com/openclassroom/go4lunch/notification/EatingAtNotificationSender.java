package com.openclassroom.go4lunch.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;

import com.openclassroom.go4lunch.R;
import com.openclassroom.go4lunch.models.User;
import com.openclassroom.go4lunch.repository.Repository;
import com.openclassroom.go4lunch.views.activity.MainActivity;

class EatingAtNotificationSender {

    private final Context mContext;
    private static final String NOTIFICATION_CHANNEL_ID = "10001";

    EatingAtNotificationSender(Context context) {
        mContext = context;
    }

    void sendNotification() {
        Repository.getRepository().callUserList((currentUser, userList) -> {
            Intent intent = new Intent(mContext, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext,
                    0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID);
            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
            StringBuilder content = new StringBuilder(mContext.getString(R.string.with));
            for (User user : userList) {
                if (user.getEatingAt().equals(currentUser.getEatingAt()) && !user.getUid().equals(currentUser.getUid())) {
                    content.append(user).append("\n");
                }
            }
            if (content.toString().equals(mContext.getString(R.string.with))) {
                content = new StringBuilder(mContext.getString(R.string.alone));
            }


            mBuilder.setContentTitle(mContext.getString(R.string.you_are_eating_at) + currentUser.getEatingAtName())
                    .setContentText(content)
                    .setAutoCancel(false)
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.enableVibration(true);
                notificationChannel.setVibrationPattern(new long[]{100, 0, 100, 0, 200, 0, 200, 0, 500, 0, 500, 0});
                assert mNotificationManager != null;
                mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
                mNotificationManager.createNotificationChannel(notificationChannel);
            }
            assert mNotificationManager != null;
            mNotificationManager.notify(0, mBuilder.build());
        });
    }
}