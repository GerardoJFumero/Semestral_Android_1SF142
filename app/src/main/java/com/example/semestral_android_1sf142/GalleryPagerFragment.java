package com.example.semestral_android_1sf142;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import java.io.File;
import java.util.List;

public class GalleryPagerFragment extends Fragment
{

    // Views
    private ViewPager2 galleryPager;

    private ImageButton backBtn;
    private ImageButton deleteBtn;

    private int selectedImage;

    // Photos
    List<String> mFilesList;

    public GalleryPagerFragment(int selectedImage, List<String> mFilesList)
    {
        super(R.layout.fragment_gallery_pager);
        this.selectedImage = selectedImage;
        this.mFilesList = mFilesList;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        this.galleryPager = requireView().findViewById(R.id.gallery_pager);

        //this.mFilesList = MediaUtils.findMediaFiles(requireActivity());

        this.galleryPager.setAdapter(new GalleryPagerAdapter(this.mFilesList, requireActivity()));
        this.galleryPager.setCurrentItem(selectedImage, false);

        this.backBtn = requireView().findViewById(R.id.photo_back_btn);
        this.deleteBtn = requireView().findViewById(R.id.photo_delete_button);

        this.backBtn.setOnClickListener(v -> handleBackBtn());
        this.deleteBtn.setOnClickListener(v -> handleDeleteBtn());
    }

    /*
        Button Handlers
     */

    private void handleBackBtn()
    {
        popBackStack();
    }

    private void handleDeleteBtn()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());


        builder.setPositiveButton("Si", (dialog, which) ->
        {
            int selectedItem = this.galleryPager.getCurrentItem();
            String path = this.mFilesList.get(selectedItem);
            deleteFile(path);

            this.mFilesList.remove(selectedItem);
            popBackStack();
        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        builder.setMessage("Desea borrar esta foto?");
        builder.setTitle("Borrar Foto");

        AlertDialog d = builder.create();
        d.show();

    }

    private void deleteFile(String path)
    {
        File file = new File(path);
        if (file.exists())
        {
            String[] projection = {MediaStore.Images.Media._ID};

            // Match file path
            String selection = MediaStore.Images.Media.DATA + " = ?";
            String[] selectionArgs = new String[]{file.getAbsolutePath()};

            // Query id file path match
            Uri queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver contentResolver = requireActivity().getContentResolver();
            Cursor c = contentResolver.query(queryUri, projection, selection, selectionArgs, null);
            if (c.moveToFirst())
            {
                // found id
                long id = c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                Uri deleteUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                contentResolver.delete(deleteUri, null, null);
                Toast.makeText(requireActivity(), "File Deleted:" + path, Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(requireActivity(), "ERROR File not Deleted:" + path, Toast.LENGTH_SHORT).show();
            }
            c.close();
        }
    }

    private void popBackStack()
    {
        FragmentManager manager = this.getParentFragmentManager();
        manager.popBackStack();
    }
}
