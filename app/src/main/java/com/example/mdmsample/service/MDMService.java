package com.example.mdmsample.service;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.mdmsample.receiver.MDMDeviceAdminReceiver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by ibrahimsalgin on 28.01.2018.
 */

public class MDMService extends Service {

    private DevicePolicyManager devicePolicyManager;


    private Intent youtubeIntent;
    private boolean disableCamera;

    @Override
    public void onCreate() {
        super.onCreate();
        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder();
    }

    public boolean isCameraDisabled() {
        return devicePolicyManager.getCameraDisabled(MDMDeviceAdminReceiver.getComponentName(this));
    }

    public void updateCameraState(boolean disable) {
        this.disableCamera = disable;

        cameraRunnable.run();
    }

    public class LocalBinder extends Binder {
        public MDMService getService() {
            return MDMService.this;
        }
    }

    public void openYoutube(String videoId) {

        //Create an intent to start youtube app
        youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
        youtubeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //check if youtube app is installed on device
        if (this.getPackageManager().queryIntentActivities(youtubeIntent, PackageManager.MATCH_DEFAULT_ONLY).size() == 0) {
            Toast.makeText(this, "Please install Youtube App first", Toast.LENGTH_SHORT);
        } else {
            youtubeRunnable.run();
        }
    }

    //runnable for enable/disable camera after 3 seconds wait
    private Runnable cameraRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.currentThread().sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                devicePolicyManager.setCameraDisabled(MDMDeviceAdminReceiver.getComponentName(MDMService.this), disableCamera);
                Toast.makeText(MDMService.this, "Camera state has been changed!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    //runnable for opening youtube app after 3 seconds wait
    private Runnable youtubeRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.currentThread().sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                startActivity(youtubeIntent);
            }
        }
    };

}
