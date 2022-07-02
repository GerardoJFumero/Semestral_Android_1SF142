package com.example.semestral_android_1sf142;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class PhotoRecyclerViewAdapter extends RecyclerView.Adapter<PhotoRecyclerViewAdapter.CustomViewHolder>
{
    private final List<String> mFileList;
    private final Activity mActivity;

    private int selectedImage;

    public PhotoRecyclerViewAdapter(List<String> mFileList, Activity mActivity)
    {
        this.mFileList = mFileList;
        this.mActivity = mActivity;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item, parent, false);
        return new PhotoRecyclerViewAdapter.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position)
    {
        File file = new File(mFileList.get(position));
        Uri uri = Uri.fromFile(file);
        holder.imageResource.setImageURI(uri);

        final int itemPosition = holder.getAdapterPosition();
        holder.imageResource.setOnClickListener(v ->
                {
                    Toast.makeText(mActivity, mFileList.get(itemPosition), Toast.LENGTH_SHORT).show();
                    this.selectedImage = itemPosition;
                }
        );
    }

    @Override
    public int getItemCount()
    {
        return mFileList.size();
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder
    {
        final ImageView imageResource;

        public CustomViewHolder(@NonNull View itemView)
        {
            super(itemView);
            this.imageResource = itemView.findViewById(R.id.image_resource);
        }
    }
}
