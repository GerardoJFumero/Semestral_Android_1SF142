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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

public class GalleryPagerFragment extends Fragment
{

    // Views
    private ViewPager2 galleryPager;

    private ImageButton backBtn;
    private ImageButton editBtn;
    private ImageButton deleteBtn;
    private ProgressBar progressBar;

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

        this.backBtn = requireView().findViewById(R.id.photo_back_btn);
        this.editBtn = requireView().findViewById(R.id.photo_edit_button);
        this.deleteBtn = requireView().findViewById(R.id.photo_delete_button);

        this.editBtn.bringToFront();
        this.deleteBtn.bringToFront();

        this.backBtn.setOnClickListener(v -> handleBackBtn());
        this.editBtn.setOnClickListener(v -> handleEditBtn());
        this.deleteBtn.setOnClickListener(v -> handleDeleteBtn());

        this.progressBar = requireView().findViewById(R.id.progressBar);

        new Thread(() -> requireActivity().runOnUiThread(() ->
        {
            this.galleryPager.setAdapter(new GalleryPagerAdapter(this.mFilesList, requireActivity()));
            this.progressBar.setVisibility(View.INVISIBLE);
            this.galleryPager.setCurrentItem(this.selectedImage, false);
        })).start();
    }

    /*
        Button Handlers
     */

    private void handleBackBtn()
    {
        popBackStack();
    }

    private void handleEditBtn()
    {
        File file = new File(this.mFilesList.get(selectedImage));
        String fileName = file.getName();

        EditText nameEt = new EditText(requireActivity());


        nameEt.setText(fileName.substring(0, fileName.length() - 4));

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        builder
                .setTitle("Cambiar Nombre")
                .setMessage("Ingrese el nuevo nombre")
                .setView(nameEt)
                .setPositiveButton("Cambiar", null)
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        AlertDialog d = builder.create();

        d.setOnShowListener(dialog ->
        {
            Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
            b.setOnClickListener(v ->
            {
                String newName = nameEt.getText().toString();

                if (newName.isEmpty())
                {
                    nameEt.setError("Debe Ingresar un nombre");
                    return;
                }

                File newFile = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES) + "/" + MediaUtils.FOLDER_NAME + "/" + newName + ".jpg");

                if (newFile.exists())
                {
                    nameEt.setError("Archivo ya existe con este nombre");
                    return;
                }

                if (file.renameTo(newFile))
                {
                    if (MediaUtils.renameFile(this.mFilesList.get(selectedImage), newName, requireActivity()))
                    {
                        Toast.makeText(requireActivity(), "Saved as: Pictures/CameraX-Images/" + newName + ".jpg" , Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(requireActivity(), "Error Renaming Media Store File", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(requireActivity(), "Error Renaming File", Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
            });
        });

        d.show();
    }

    private void handleDeleteBtn()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        builder
                .setTitle("Borrar Foto")
                .setMessage("Desea borrar esta foto?")
                .setPositiveButton("Si", (dialog, which) ->
                    {
                        int selectedItem = this.galleryPager.getCurrentItem();
                        String path = this.mFilesList.get(selectedItem);

                        if(MediaUtils.deleteFile(path, requireActivity()))
                        {
                            Toast.makeText(requireActivity(), "File Deleted:" + path, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(requireActivity(), "ERROR File not Deleted:" + path, Toast.LENGTH_SHORT).show();
                        }

                        this.mFilesList.remove(selectedItem);
                        popBackStack();
                    })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        AlertDialog d = builder.create();
        d.show();

    }

    private void popBackStack()
    {
        FragmentManager manager = this.getParentFragmentManager();
        manager.popBackStack();
    }
}
