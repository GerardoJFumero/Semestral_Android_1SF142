package com.example.semestral_android_1sf142;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.video.OutputOptions;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.common.util.concurrent.ListenableFuture;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CameraFragment extends Fragment
{
    private static final String FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";

    // Views
    private PreviewView viewFinder;

    private ImageButton settingsBtn;
    private ImageButton takePhotoBtn;
    private ImageButton listPhotoBtn;

    private ExecutorService cameraExecutor;

    // Camera

    ImageCapture imageCapture;

    public CameraFragment()
    {
        super(R.layout.fragment_camera);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.viewFinder = requireView().findViewById(R.id.view_finder);

        this.settingsBtn = requireView().findViewById(R.id.settings_btn);
        this.takePhotoBtn = requireView().findViewById(R.id.take_photo_btn);
        this.listPhotoBtn = requireView().findViewById(R.id.list_photo_btn);

        this.settingsBtn.setOnClickListener(v -> handleSettingsBtn());
        this.takePhotoBtn.setOnClickListener(v -> handleTakePhotoBtn());
        this.listPhotoBtn.setOnClickListener(v -> handleListPhotoBtn());

        this.cameraExecutor = Executors.newSingleThreadExecutor();

        startCamera();
    }

    /*
        Button Handlers
     */

    // TODO
    private void handleSettingsBtn()
    {

    }

    private void handleTakePhotoBtn()
    {
        if (this.imageCapture == null)
        {
            return;
        }

        takePhoto();
    }

    // TODO
    private void handleListPhotoBtn()
    {

    }

    /*
        Implementations
     */

    private void startCamera()
    {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(requireActivity());

        cameraProviderFuture.addListener(() ->
        {
            try
            {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(this.viewFinder.getSurfaceProvider());

                this.imageCapture = new ImageCapture.Builder().build();

                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, this.imageCapture);
            }
            catch (ExecutionException | InterruptedException e)
            {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(requireActivity()));
    }

    private void takePhoto()
    {
        SimpleDateFormat format = new SimpleDateFormat(FILENAME_FORMAT, Locale.US);
        String name = format.format(System.currentTimeMillis());

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P)
        {
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Images");
        }

        ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions.Builder(
                requireActivity().getContentResolver(),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues).build();

        this.imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(requireActivity()),
                new ImageCapture.OnImageSavedCallback()
                {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults)
                    {
                        Toast.makeText(requireActivity(), "Saved as: Pictures/CameraX-Images/" + name + ".jpg" , Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException e)
                    {
                        Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                });
    }

    /*
        Clean up
     */

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        this.cameraExecutor.shutdown();
    }
}
