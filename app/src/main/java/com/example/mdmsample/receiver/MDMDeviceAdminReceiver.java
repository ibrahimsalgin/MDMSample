package com.example.mdmsample.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.mdmsample.R;
import com.example.mdmsample.activity.MainActivity;

/**
 * Created by ibrahimsalgin on 28.01.2018.
 */

public class MDMDeviceAdminReceiver extends DeviceAdminReceiver {

    /**
     * Called when this application is approved to be a device administrator.
     */
    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);

        //inform user to Device administrator is enabled
        Toast.makeText(context, R.string.device_admin_enabled, Toast.LENGTH_SHORT).show();

        //create new intent to launch Main Activity
        Intent launch = new Intent(context, MainActivity.class);
        launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(launch);

    }

    /**
     * Called when this application is no longer the device administrator.
     */
    @Override
    public void onDisabled(Context context, Intent intent) {
        super.onDisabled(context, intent);
        Toast.makeText(context, R.string.device_admin_disabled, Toast.LENGTH_SHORT).show();
    }


    //static method to obtain component name
    public static ComponentName getComponentName(Context context) {
        return new ComponentName(context.getApplicationContext(), MDMDeviceAdminReceiver.class);
    }
}
