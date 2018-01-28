package com.example.mdmsample.fragment;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.mdmsample.R;
import com.example.mdmsample.service.MDMService;

public class MainFragment extends Fragment implements View.OnClickListener {

    //Buttons
    private Button enableCameraButton;
    private Button disableCameraButton;
    private Button watchYoutubeButton;


    private View rootView;
    private MDMService mMDMService;

    //boolean to hold service connection state
    private boolean isBound = false;

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has
            // been established, giving us the service object we can use
            // to interact with the service.  Because we have bound to a
            // explicit service that we know is running in our own
            // process, we can cast its IBinder to a concrete class and
            // directly access it.
            mMDMService = ((MDMService.LocalBinder) service).getService();
            isBound = true;

            //first, check the camera if it s enabled or disabled
            checkCameraState();
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has
            // been unexpectedly disconnected -- that is, its process
            // crashed. Because it is running in our same process, we
            // should never see this happen.
            mMDMService = null;
        }
    };

    void bindService() {
        // Establish a connection with the service.  We use an explicit
        // class name because we want a specific service implementation
        // that we know will be running in our own process (and thus
        // won't be supporting component replacement by other
        // applications).
        getContext().bindService(new Intent(getContext(), MDMService.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    void unbindService() {
        if (isBound) {
            // Detach our existing connection.
            getContext().unbindService(mConnection);
            isBound = false;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        bindService();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        unbindService();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //define a rootView and inflate it one time
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        enableCameraButton = rootView.findViewById(R.id.button_enable_camera);
        disableCameraButton = rootView.findViewById(R.id.button_disable_camera);
        watchYoutubeButton = rootView.findViewById(R.id.button_watch_youtube);

        //set buttons click listener
        enableCameraButton.setOnClickListener(this);
        disableCameraButton.setOnClickListener(this);
        watchYoutubeButton.setOnClickListener(this);
    }

    private void checkCameraState() {
        //update buttons state
        if (mMDMService.isCameraDisabled()) {
            enableCameraButton.setEnabled(true);
            disableCameraButton.setEnabled(false);
        } else {
            enableCameraButton.setEnabled(false);
            disableCameraButton.setEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        if (!isBound) {
            Toast.makeText(getContext(), "Please wait until service connected!", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (v.getId()) {
            case R.id.button_enable_camera:
                //enable camera
                mMDMService.updateCameraState(false);
                getActivity().finish();
                break;
            case R.id.button_disable_camera:
                //disable camera
                mMDMService.updateCameraState(true);
                getActivity().finish();
                break;
            case R.id.button_watch_youtube:
                mMDMService.openYoutube("eZYWMSsb7s8");
                getActivity().finish();
                break;
        }

    }
}
