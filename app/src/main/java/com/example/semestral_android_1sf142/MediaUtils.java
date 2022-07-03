package com.example.semestral_android_1sf142;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MediaUtils
{
    public static final String FOLDER_NAME = "CameraX-Images";

    public static List<String> findMediaFiles(Context context)
    {
        List<String> fileList = new ArrayList<>();

        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID, MediaStore.Images.Media.DATE_TAKEN};
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;

        checkMediaFolder();

        //Stores all the images from the gallery in Cursor
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                columns,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " = ?",
                new String[] {FOLDER_NAME},
                orderBy);

        if (cursor != null)
        {
            //Total number of images
            int count = cursor.getCount();

            //Create an array to store path to all the images
            String[] arrPath = new String[count];

            for (int i = 0; i < count; i++)
            {
                cursor.moveToPosition(i);
                int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);

                arrPath[i] = cursor.getString(dataColumnIndex);
                fileList.add(arrPath[i]);
            }
            cursor.close();
        }
        return fileList;
    }

    public static void checkMediaFolder()
    {
        File photoDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), FOLDER_NAME);
        if (!photoDir.exists())
        {
            photoDir.mkdirs();
        }
    }
}
