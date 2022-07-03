package com.example.semestral_android_1sf142;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private static final int REQUEST_WRITE_PERMISSION = 400;

    private boolean needsFileListUpdate;
    private List<String> mFilesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.needsFileListUpdate = true;

        // Check realtime permissions if run higher API 23
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            requestCameraPermission();
        }

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            requestWritePermission();
        }

        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, CameraFragment.class,null)
                    .commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION)
        {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getApplicationContext(), "Necesita dar los permisos a la camara", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        else if (requestCode == REQUEST_WRITE_PERMISSION)
        {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getApplicationContext(), "Necesita dar los permisos para leer archivos", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void requestCameraPermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.CAMERA
        }, REQUEST_CAMERA_PERMISSION);
    }

    private void requestWritePermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, REQUEST_WRITE_PERMISSION);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        setNeedsFileListUpdate(true);
    }

    /*
        Getters & Settters
     */

    public boolean isNeedsFileListUpdate()
    {
        return needsFileListUpdate;
    }

    public void setNeedsFileListUpdate(boolean needsFileListUpdate)
    {
        this.needsFileListUpdate = needsFileListUpdate;
    }

    public void loadFileList()
    {
        this.mFilesList = MediaUtils.findMediaFiles(this);
        setNeedsFileListUpdate(false);
    }

    public List<String> getmFilesList()
    {
        return mFilesList;
    }
}