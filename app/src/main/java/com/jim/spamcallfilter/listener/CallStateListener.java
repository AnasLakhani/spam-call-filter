// File: app/src/main/java/com/jim/spamcallfilter/CallStateListener.java
package com.jim.spamcallfilter.listener;

import static android.content.Context.AUDIO_SERVICE;

import android.content.Context;
import android.media.AudioManager;
import android.telecom.CallScreeningService;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.jim.spamcallfilter.utilites.SpamLogger;

import java.util.Arrays;
import java.util.List;

public class CallStateListener extends PhoneStateListener {
    private final Context context;

    private static final List<String> MA_AREA_CODES = Arrays.asList(
            "339", "351", "413", "508", "617", "774", "781", "857", "978",
            "1339", "1351", "1413", "1508", "1617", "1774", "1781", "1857", "1978"
    );


    public CallStateListener(Context context) {
        this.context = context;
    }

    @Override
    public void onCallStateChanged(int state, String phoneNumber) {
        AudioManager audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
        if (audioManager == null) return;

        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                System.out.println("Incoming call from: " + phoneNumber);

                if (isMassachusettsSpam(phoneNumber)) {
                    // Log the spam call
                    SpamLogger.logSpamCall(context, phoneNumber);

                    // Reject the spam call
                    CallScreeningService.CallResponse.Builder response = new CallScreeningService.CallResponse.Builder();
                    response.setRejectCall(true);

                    // Mute the ringtone
                    audioManager.setStreamVolume(AudioManager.STREAM_RING, 0, 0);
                    System.out.println("Muted ringtone for spam call: " + phoneNumber);
                } else {
                    // Set ringtone to maximum volume for non-MA numbers
                    int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
                    audioManager.setStreamVolume(AudioManager.STREAM_RING, maxVolume, 0);
                    System.out.println("Set ringtone to max volume for non-spam call: " + phoneNumber);
                }
                break;

            case TelephonyManager.CALL_STATE_IDLE:
                // Restore the volume to its original state when the call ends
                int defaultVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING) / 2; // Example: Set to 50% volume
                audioManager.setStreamVolume(AudioManager.STREAM_RING, defaultVolume, 0);
                System.out.println("Restored ringtone volume to default.");
                break;

            case TelephonyManager.CALL_STATE_OFFHOOK:
                // Optional: Handle off-hook state if needed
                break;
        }
    }

    private boolean isMassachusettsSpam(String number) {
        if (number == null) return false;
        number = number.replaceAll("[^0-9]", "");
        for (String code : MA_AREA_CODES) {
            if (number.startsWith(code)) return true;
        }
        return false;
    }
}