package com.android.study.example.filepicker.filter;

import android.support.v4.app.FragmentActivity;

import com.android.study.example.filepicker.filter.callback.FileLoaderCallbacks;
import com.android.study.example.filepicker.filter.callback.FilterResultCallback;
import com.android.study.example.filepicker.filter.entity.VideoFile;

public class FileFilter {

    public static void getVideos(FragmentActivity activity, FilterResultCallback<VideoFile> callback){
        activity.getSupportLoaderManager().initLoader(1, null,
                new FileLoaderCallbacks(activity, callback, FileLoaderCallbacks.TYPE_VIDEO));
    }
}
