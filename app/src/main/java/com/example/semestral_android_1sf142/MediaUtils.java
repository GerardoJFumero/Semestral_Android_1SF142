package com.example.semestral_android_1sf142;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
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

        final String[] projection = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATE_TAKEN};
        Uri imagesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;

        checkMediaFolder();

        //Stores all the images from the gallery in Cursor
        Cursor cursor = context.getContentResolver().query(
                imagesUri,
                projection,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " = ?",
                new String[]{FOLDER_NAME},
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

    public static boolean deleteFile(String path, Context context)
    {
        boolean valid = false;

        File file = new File(path);
        if (file.exists())
        {
            String[] projection = {MediaStore.Images.Media._ID};

            // Match file path
            String selection = MediaStore.Images.Media.DATA + " = ?";
            String[] selectionArgs = new String[]{file.getAbsolutePath()};

            // Query id file path match
            Uri queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver contentResolver = context.getContentResolver();
            Cursor c = contentResolver.query(queryUri, projection, selection, selectionArgs, null);
            if (c.moveToFirst())
            {
                // found id
                long id = c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                Uri deleteUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                contentResolver.delete(deleteUri, null, null);
                valid = true;
            }

            c.close();
        }
        return valid;
    }

    public static boolean renameFile(String path, String newName, Context context)
    {
        boolean valid = false;

        File file = new File(path);

        String[] projection = {MediaStore.Images.Media._ID};

        // Match file path
        String selection = MediaStore.Images.Media.DATA + " = ?";
        String[] selectionArgs = new String[]{file.getAbsolutePath()};

        // Query id file path match
        Uri queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = context.getContentResolver();
        Cursor c = contentResolver.query(queryUri, projection, selection, selectionArgs, null);
        if (c.moveToFirst())
        {
            // found id
            long id = c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
            Uri editUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

            ContentValues contentValues = new ContentValues();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            {
                contentValues.put(MediaStore.Files.FileColumns.IS_PENDING, 1);
                contentResolver.update(editUri, contentValues, null, null);
            }

            contentValues.clear();
            contentValues.put(MediaStore.Files.FileColumns.DISPLAY_NAME, newName);

            if (Build.VERSION.SDK_INT >= 29)
            {

                contentValues.put(MediaStore.Images.Media.RELATIVE_PATH,
                        Environment.DIRECTORY_PICTURES + "/" + MediaUtils.FOLDER_NAME);

            }
            else
            {
                String newPath = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES) + "/" + MediaUtils.FOLDER_NAME + "/" + newName + ".jpg";

                contentValues.put(MediaStore.Images.Media.DATA, newPath);

            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            {
                contentValues.put(MediaStore.Files.FileColumns.IS_PENDING, 0);
            }

            contentResolver.update(editUri, contentValues, null, null);

            valid = true;
        }

        c.close();

        return valid;
    }
}
