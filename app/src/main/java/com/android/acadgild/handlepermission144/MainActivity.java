package com.android.acadgild.handlepermission144;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.CAMERA;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    // Any valid random integer number as PERMISSION_REQUEST_CODE
    private static final int PERMISSION_REQUEST_CODE = 200;
    // View object to be used when on button click listener
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar object
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //Get Buttons from UI
        Button check_permission = (Button) findViewById(R.id.check_permission);
        Button request_permission = (Button) findViewById(R.id.request_permission);
        // Set on click listener of buttons
        check_permission.setOnClickListener(this);
        request_permission.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //Assigning View object
        view = v;
        // get Id of view
        int id = v.getId();
        //Switch to check which view button clicked
        switch (id) {
            //When check permission button is clicked
            case R.id.check_permission:
                //Call check permission method to check Permission is granted or not
                if (checkPermission()) {

                    Snackbar.make(view, "Permission already granted.", Snackbar.LENGTH_LONG).show();

                } else {

                    Snackbar.make(view, "Please request permission.", Snackbar.LENGTH_LONG).show();
                }
                break;
            //When Request Permission button is clicked
            case R.id.request_permission:
                //Call check permission method to check Permission is granted or not
                if (!checkPermission()) {
                    //If not then request permission
                    requestPermission();

                } else {
                    //Else display this message.
                    Snackbar.make(view, "Permission already granted.", Snackbar.LENGTH_LONG).show();

                }
                break;
        }

    }

    //checkPermission method
    private boolean checkPermission() {
        //Take result for each permision by calling ContextCompat.checkSelfPermission method
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE);
        //Return result
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED;
    }

    //requestPermission method
    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, CAMERA, CALL_PHONE}, PERMISSION_REQUEST_CODE);

    }

    //onRequestPermissionsResult method
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        // Check for permission request code
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                //Check if length of grantResults array greater than 0
                if (grantResults.length > 0) {
                    //Take result of all 3 requests
                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean wifiAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    // If all 3 requests are granted then display this message
                    if (locationAccepted && cameraAccepted && wifiAccepted)
                        Snackbar.make(view, "Permission Granted, Now you can access location data and camera.", Snackbar.LENGTH_LONG).show();
                    else {
                        //Else dispay denied permission message.
                        Snackbar.make(view, "Permission Denied, You cannot access location data and camera.", Snackbar.LENGTH_LONG).show();
                        // Check if version is greater than Marshmallow or not
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                //call showMessageOKCancel method if all permissions are not granted
                                showMessageOKCancel("You need to allow access to all the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    //Again call request permission
                                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION, CAMERA, CALL_PHONE},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }
    //Method to show message in dialog
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}
