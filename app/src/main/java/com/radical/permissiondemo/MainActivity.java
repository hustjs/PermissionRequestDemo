package com.radical.permissiondemo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.radical.permissiondemo.camera.CameraPreviewFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 0;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 2;
    private Button btn_callPhone,btn_showCamera,btn_writeSdcard;
    private View mLayout;
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLayout = findViewById(R.id.main_layout);
        btn_callPhone = (Button) findViewById(R.id.id_btn_callPhone);
        btn_showCamera = (Button) findViewById(R.id.id_btn_showCamera);
        btn_writeSdcard = (Button) findViewById(R.id.id_btn_sdcard);
        btn_callPhone.setOnClickListener(this);
        btn_writeSdcard.setOnClickListener(this);
        btn_showCamera.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_btn_callPhone:
                testCall();
                break;
            case R.id.id_btn_sdcard:
                testSdcard();
                break;
            case R.id.id_btn_showCamera:
                showCamera();
                break;
            default:
                break;
        }
    }

    private void showCamera() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                // Provide an additional rationale to the user if the permission was not granted
                // and the user would benefit from additional context for the use of the permission.
                // Display a SnackBar with a button to request the missing permission.
                Snackbar.make(mLayout, "Camera access is required to display the camera preview.",
                        Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Request the permission
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);
                    }
                }).show();

            } else {
                Snackbar.make(mLayout,
                        "Permission is not available. Requesting camera permission.",
                        Snackbar.LENGTH_SHORT).show();
                // Request the permission. The result will be received in onRequestPermissionResult().
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
            }
        }else {
            Log.i(TAG, "showCamera: "+"was granted");
            // Permission is already available, start camera preview
            Snackbar.make(mLayout,
                    "Camera permission is available. Starting preview.",
                    Snackbar.LENGTH_SHORT).show();
            startCamera();
        }
    }

    private void testCall() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
            /*Snackbar.make(btn_callPhone, "Camera access is required to display the camera preview.",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Request the permission
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE);
                }
            }).show();*/
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        } else {
            Log.i(TAG, "testCall: "+"was granted");
            callPhone();
        }
    }

    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "10086"));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callPhone();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_CAMERA:{
                // Request for camera permission.
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission has been granted. Start camera preview Activity.
                    Snackbar.make(mLayout, "Call Phone permission was granted. Starting preview.",
                            Snackbar.LENGTH_SHORT)
                            .show();
                    startCamera();
                } else {
                    // Permission request was denied.
                    Snackbar.make(mLayout, "Call phone permission request was denied.",
                            Snackbar.LENGTH_SHORT)
                            .show();
                }
            }
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // Request for camera permission.
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission has been granted. Start camera preview Activity.
                    Snackbar.make(mLayout, "Write to Sdcard permission was granted. Starting.",
                            Snackbar.LENGTH_SHORT)
                            .show();
                    writeSdcard();
                } else {
                    // Permission request was denied.
                    Snackbar.make(mLayout, "Write external storage permission request was denied.",
                            Snackbar.LENGTH_SHORT)
                            .show();
                }
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void startCamera() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.sample_content_fragment, CameraPreviewFragment.newInstance())
                .addToBackStack("contacts")
                .commit();
    }

    private void testSdcard() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Snackbar.make(mLayout, "Sdcard access is required to write to external storage.",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Request the permission
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                }
            }).show();
        }else {
            Log.i(TAG, "testSdcard: "+"was granted");
            writeSdcard();
        }
    }

    private void writeSdcard() {
        // TODO: 16-9-4
        Toast.makeText(MainActivity.this, "write sdcard ", Toast.LENGTH_SHORT).show();
    }
    
    public void onBackClick(View view){
        if(!isAdded()) return;
        getActivity().getSupportFragmentManager().beginTransaction()
                .hide(CameraPreviewFragment.this)
                .commit();
    }
}
