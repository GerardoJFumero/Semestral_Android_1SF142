package com.example.semestral_android_1sf142;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class GalleryFragment extends Fragment
{
    // Views
    private RecyclerView recyclerView;

    private ImageButton backBtn;
    private ImageButton deleteBtn;

    // Photos
    List<String> mFilesList;

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

        this.mFilesList = MediaUtils.findMediaFiles(requireActivity());

        this.recyclerView.setAdapter(new PhotoRecyclerViewAdapter(this.mFilesList, requireActivity()));

        this.backBtn = requireView().findViewById(R.id.gallery_back_btn);
//        this.deleteBtn = requireView().findViewById(R.id.gallery_delete_btn);

        this.backBtn.setOnClickListener(v -> handleBackBtn());
//        this.deleteBtn.setOnClickListener(v -> handleDeleteBtn());
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
