package com.jim.spamcallfilter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.jim.spamcallfilter.listener.LogUpdateListener;
import com.jim.spamcallfilter.service.CallStateService;
import com.jim.spamcallfilter.utilites.SpamLogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LogUpdateListener {
    private static final int REQUEST_ALL_PERMISSIONS = 1;
    private static final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.RECEIVE_BOOT_COMPLETED,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.READ_PHONE_NUMBERS,
            Manifest.permission.FOREGROUND_SERVICE
    };

    private ListView logListView;
    private TextView logTextView;
    private Button btnRequestPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView footerTextView = findViewById(R.id.tv_footer);

        Spanned htmlText;
        String htmlString = "Created by <a href='https://upwork.com/freelancers/anaslakhani'>Jim Jewett ❤️</a>";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            htmlText = Html.fromHtml(htmlString, Html.FROM_HTML_MODE_LEGACY);
        } else {
            htmlText = Html.fromHtml(htmlString);
        }

        footerTextView.setText(htmlText);
        footerTextView.setMovementMethod(LinkMovementMethod.getInstance());
        footerTextView.setLinksClickable(true);
        footerTextView.setLinkTextColor(Color.BLUE);
        footerTextView.setClickable(true);
        footerTextView.setFocusable(true);
        btnRequestPermissions = findViewById(R.id.btn_request_permissions);
        logListView = findViewById(R.id.log_list_view);

        logTextView = findViewById(R.id.tv_call_log_title);


        btnRequestPermissions.setOnClickListener(v -> {
            if (areAllPermissionsGranted()) {
                btnRequestPermissions.setVisibility(View.GONE);
                startApp();
            } else {
                btnRequestPermissions.setVisibility(View.VISIBLE);
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_ALL_PERMISSIONS);
            }
        });

        btnRequestPermissions.performClick();
        populateLogListView();
    }

    private boolean areAllPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void startApp() {
        Intent serviceIntent = new Intent(this, CallStateService.class);
        startService(serviceIntent);
    }

    private void populateLogListView() {
        List<String> logs = SpamLogger.getLogs(this);


        if (logs.isEmpty()) {
            logTextView.setText(R.string.no_logs_available);
        } else {
            logTextView.setText(R.string.spam_call_logs);
        }

        // Reverse the logs to show newer entries first
        Collections.reverse(logs);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, logs);

        logListView.setAdapter(adapter);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ALL_PERMISSIONS) {
            if (areAllPermissionsGranted()) {
                btnRequestPermissions.performClick();

            } else {
                for (String permission : REQUIRED_PERMISSIONS) {
                    if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission required: " + permission, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void onLogsUpdated() {
        // Refresh the log list view when logs are updated
        populateLogListView();
    }
}