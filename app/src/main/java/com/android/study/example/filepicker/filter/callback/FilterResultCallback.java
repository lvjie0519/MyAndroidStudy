package com.android.study.example.filepicker.filter.callback;

import com.android.study.example.filepicker.filter.entity.BaseFile;
import com.android.study.example.filepicker.filter.entity.Directory;

import java.util.List;

public interface FilterResultCallback<T extends BaseFile> {
    void onResult(List<Directory<T>> directories);
}
