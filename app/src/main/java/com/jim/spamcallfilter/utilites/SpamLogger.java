package com.jim.spamcallfilter.utilites;

import android.content.Context;
import android.os.Environment;

import com.jim.spamcallfilter.listener.LogUpdateListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpamLogger {

    private static LogUpdateListener logUpdateListener;

    public static void setLogUpdateListener(LogUpdateListener listener) {
        logUpdateListener = listener;
    }


    public static void logSpamCall(Context context, String phoneNumber) {
        File downloadsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        File logFile = new File(downloadsDir, "Spam_Calls.txt");
        try (FileWriter writer = new FileWriter(logFile, true)) {
            writer.append("Spam Call: ").append(phoneNumber).append("\n");
            if (logUpdateListener != null) {
                logUpdateListener.onLogsUpdated();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getLogs(Context context) {
        List<String> logs = new ArrayList<>();
        File downloadsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        File logFile = new File(downloadsDir, "Spam_Calls.txt");

        if (logFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    logs.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            logs.add("No logs available.");
        }

        return logs;
    }


}
