package com.mx.filepicker.filter.callback;

import com.mx.filepicker.filter.entity.BaseFile;
import com.mx.filepicker.filter.entity.Directory;

import java.util.List;

public interface FilterResultCallback<T extends BaseFile> {
    void onResult(List<Directory<T>> directories);
}
