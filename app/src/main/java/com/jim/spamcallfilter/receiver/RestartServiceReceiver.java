// File: app/src/main/java/com/jim/spamcallfilter/RestartServiceReceiver.java
package com.jim.spamcallfilter.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jim.spamcallfilter.service.CallStateService;

public class RestartServiceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_MY_PACKAGE_REPLACED)) {
            System.out.println("RestartServiceReceiver: App removed from recents, restarting service.");
            Intent serviceIntent = new Intent(context, CallStateService.class);
            context.startForegroundService(serviceIntent);
        }
    }
}