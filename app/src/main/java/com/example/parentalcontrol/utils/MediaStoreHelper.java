package com.example.parentalcontrol.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import com.example.parentalcontrol.model.DocumentModel;
import com.example.parentalcontrol.model.ImageModel;
import com.example.parentalcontrol.model.VideoModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MediaStoreHelper {
    public static List<ImageModel> getAllImages(Context context) {
        String[] projection = {
                MediaStore.Images.Media.TITLE,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_ADDED
        };
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Images.Media.DATE_ADDED + " DESC"
        );
        List<ImageModel> imageModels = new ArrayList<>();
        if (cursor != null) {

            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.TITLE));
                    @SuppressLint("Range") String size = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
                    @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media._ID));
                    @SuppressLint("Range") String folderName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                    @SuppressLint("Range") String folderId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID));
                    @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

                    try {
                        File file = new File(path);
                        Uri uri = Uri.fromFile(file);
                        ImageModel imageModel = new ImageModel(id, title, folderName, size, path, uri);
                        imageModels.add(imageModel);
                        Log.d("ImageInfo", "Image uri: " + uri);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } while (cursor.moveToNext());

            }
            cursor.close();
        }
        return imageModels;
    }


    public static List<VideoModel> getAllVideos(Context context) {
        String[] projection = {
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media.BUCKET_ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DATE_ADDED
        };

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Video.Media.DATE_ADDED + " DESC"
        );

        List<VideoModel> videoModels = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                    @SuppressLint("Range") String size = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                    @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                    @SuppressLint("Range") String folderName = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME));
                    @SuppressLint("Range") String folderId = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID));
                    @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));

                    try {
                        File file = new File(path);
                        Uri uri = Uri.fromFile(file);
                        VideoModel videoModel = new VideoModel(id, title, folderName, size, path, uri);
                        videoModels.add(videoModel);
                        Log.d("VideoInfo", "Video uri: " + uri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return videoModels;
    }


    public static List<DocumentModel> getAllDocuments(Context context) {
        String[] projection = {
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.TITLE,
                MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.DATE_ADDED
        };

        String selection = MediaStore.Files.FileColumns.MIME_TYPE + " IN ('application/pdf', 'text/plain', 'application/msword', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document')";

        String sortOrder = MediaStore.Files.FileColumns.DATE_ADDED + " DESC";

        Uri collection = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ?
                MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL) :
                MediaStore.Files.getContentUri("external");

        Cursor cursor = context.getContentResolver().query(
                collection,
                projection,
                selection,
                null,
                sortOrder
        );

        List<DocumentModel> documentModels = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE));
                @SuppressLint("Range") String folderName = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME));
                @SuppressLint("Range") String size = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE));
                @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));

                try {
                    // Create a DocumentModel instance and add it to the list
                    DocumentModel documentModel = new DocumentModel(id, title, folderName, size, path);
                    documentModels.add(documentModel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            cursor.close();
        }
        return documentModels;
    }


}
