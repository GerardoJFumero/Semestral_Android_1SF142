package com.example.semestral_android_1sf142;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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

    public GalleryPagerFragment(int selectedImage)
    {
        super(R.layout.fragment_gallery_pager);
        this.selectedImage = selectedImage;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        this.galleryPager = requireView().findViewById(R.id.gallery_pager);

        this.mFilesList = MediaUtils.findMediaFiles(requireActivity());

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
            String path = mFilesList.get(galleryPager.getCurrentItem());
            deleteFile(path);

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
            if (file.delete())
            {
                Toast.makeText(requireActivity(), "file Deleted :" + path, Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(requireActivity(), "file not Deleted :" + path, Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void popBackStack()
    {
        FragmentManager manager = this.getParentFragmentManager();
        manager.popBackStack();
    }
}
