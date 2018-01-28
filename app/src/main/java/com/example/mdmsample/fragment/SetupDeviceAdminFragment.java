package com.example.mdmsample.fragment;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mdmsample.R;
import com.example.mdmsample.receiver.MDMDeviceAdminReceiver;

public class SetupDeviceAdminFragment extends Fragment {

    //request code for startActivityForResult
    private static final int ACTIVATION_REQUEST = 1;


    private ComponentName demoDeviceAdmin;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setup_profile, container, false);

        //set component name by using static method in our receiver class
        demoDeviceAdmin = new ComponentName(getContext(), MDMDeviceAdminReceiver.class);

        //ask user to give admin permission
        enableAdmin();

        return view;
    }

    private void enableAdmin() {
        //create new intent to ask user for admin permission
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);

        //put our component name to the intent
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, demoDeviceAdmin);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, getString(R.string.app_description));

        startActivityForResult(intent, ACTIVATION_REQUEST);

        //finish current activity, if user accepts to give admin permission the activity will be relaunched
        getActivity().finish();
    }
}
