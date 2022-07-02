package com.example.semestral_android_1sf142;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment
{
    // Views
    private RecyclerView recyclerView;

    private ImageButton backBtn;
    private ImageButton deleteBtn;

    public GalleryFragment()
    {
        super(R.layout.fragment_gallery);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        this.recyclerView = requireView().findViewById(R.id.recycler_view);
        this.recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 3));
        this.recyclerView.setHasFixedSize(true);

        List<String> mFiles = findMediaFiles(requireActivity());

        this.recyclerView.setAdapter(new PhotoRecyclerViewAdapter(mFiles, requireActivity()));

        this.backBtn = requireView().findViewById(R.id.gallery_back_btn);
        this.deleteBtn = requireView().findViewById(R.id.delete_btn);

        this.backBtn.setOnClickListener(v -> handleBackBtn());
        this.deleteBtn.setOnClickListener(v -> handleDeleteBtn());
    }

    private List<String> findMediaFiles(Context context) {
        List<String> fileList = new ArrayList<>();

        final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
        final String orderBy = MediaStore.Images.Media._ID;

        //Stores all the images from the gallery in Cursor
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);

        if (cursor != null) {
            //Total number of images
            int count = cursor.getCount();

            //Create an array to store path to all the images
            String[] arrPath = new String[count];

            for (int i = 0; i < count; i++) {
                cursor.moveToPosition(i);
                int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);

                arrPath[i] = cursor.getString(dataColumnIndex);
                fileList.add(arrPath[i]);
            }
            cursor.close();
        }
        return fileList;
    }

    /*
        Button Handlers
     */

    private void handleBackBtn()
    {
        FragmentManager manager = this.getParentFragmentManager();
        manager.popBackStack();
    }

    private void handleDeleteBtn()
    {
    }
}
