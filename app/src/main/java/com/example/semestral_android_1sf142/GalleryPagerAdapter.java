package com.example.semestral_android_1sf142;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class GalleryPagerAdapter extends RecyclerView.Adapter<GalleryPagerAdapter.CustomViewHolder>
{
    private final List<String> mFileList;
    private final Activity mActivity;

    public GalleryPagerAdapter(List<String> mFileList, Activity mActivity)
    {
        this.mFileList = mFileList;
        this.mActivity = mActivity;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_pager_item, parent, false);
        return new GalleryPagerAdapter.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position)
    {
        File file = new File(mFileList.get(position));
        Uri uri = Uri.fromFile(file);

//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, 200);
//        holder.imageResource.setLayoutParams(params);

        holder.imageResource.setImageURI(uri);
    }

    @Override
    public int getItemCount()
    {
        return this.mFileList.size();
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder
    {
        final ImageView imageResource;

        public CustomViewHolder(@NonNull View itemView)
        {
            super(itemView);
            this.imageResource = itemView.findViewById(R.id.pager_image_resource);
        }
    }
}
