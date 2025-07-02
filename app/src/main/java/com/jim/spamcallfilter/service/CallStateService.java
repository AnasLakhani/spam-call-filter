// File: app/src/main/java/com/jim/spamcallfilter/CallStateService.java
package com.jim.spamcallfilter.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.jim.spamcallfilter.listener.CallStateListener;
import com.jim.spamcallfilter.R;

public class CallStateService extends Service {
    private static final String CHANNEL_ID = "CallStateServiceChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();

        // Start the service in the foreground
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Spam rCall Filter")
                .setContentText("Monitoring calls for spam detection")
                .setSmallIcon(R.mipmap.ic_launcher_foreground) // Ensure this icon exists
                .setPriority(NotificationCompat.PRIORITY_HIGH) // Set priority for visibility
                .build();
        startForeground(1, notification);

        // Register the CallStateListener
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            CallStateListener callStateListener = new CallStateListener(this);
            telephonyManager.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE);
            System.out.println("CallStateService: CallStateListener registered");
        } else {
            System.out.println("CallStateService: TelephonyManager is null");
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Call State Service Channel",
                    NotificationManager.IMPORTANCE_HIGH // Set importance for visibility
            );
            serviceChannel.setDescription("This channel is used by the Spam Call Filter service.");
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }
}