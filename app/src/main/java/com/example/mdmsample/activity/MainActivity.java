package com.example.mdmsample.activity;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mdmsample.receiver.MDMDeviceAdminReceiver;
import com.example.mdmsample.fragment.MainFragment;
import com.example.mdmsample.R;
import com.example.mdmsample.fragment.SetupDeviceAdminFragment;
import com.example.mdmsample.service.MDMService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //obtain devicePolicyManager from System Service
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);

        //check if the app is admin or not
        if (!devicePolicyManager.isAdminActive(MDMDeviceAdminReceiver.getComponentName(this))) {
            //the app has not admin permission, show related fragment to ask user for admin permission
            showSetupAdmin();
        } else {
            //the app has admin permission, first start service to listen client
            startService(new Intent(this, MDMService.class));

            //after service started, show main fragment
            showMainFragment();
        }
    }

    private void showSetupAdmin() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new SetupDeviceAdminFragment())
                .commit();
    }


    private void showMainFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new MainFragment())
                .commit();
    }
}
