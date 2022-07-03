package com.example.semestral_android_1sf142;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class GalleryFragment extends Fragment
{
    // Views
    private RecyclerView recyclerView;

    private ImageButton backBtn;
    private ProgressBar progressBar;

    // Photos
    private List<String> mFilesList;

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

        this.backBtn = requireView().findViewById(R.id.gallery_back_btn);

        this.backBtn.setOnClickListener(v -> handleBackBtn());

        this.progressBar = requireView().findViewById(R.id.progressBar);

        new Thread(() ->
        {
            mFilesList = MediaUtils.findMediaFiles(requireActivity());
            requireActivity().runOnUiThread(() ->
            {
                progressBar.setVisibility(View.INVISIBLE);
                recyclerView.setAdapter(new PhotoRecyclerViewAdapter(mFilesList, requireActivity()));
            });

        }).start();
    }

    /*
        Button Handlers
     */

    private void handleBackBtn()
    {
        FragmentManager manager = this.getParentFragmentManager();
        manager.popBackStack();
    }
}
