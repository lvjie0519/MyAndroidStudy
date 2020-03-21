package com.android.study.example.uidemo.screenshot;

import android.app.Activity;
import android.view.View;

import java.io.File;

public class ScreenShotUtil {

    public static void fullScreenShot(Activity activity, String filePathName){

        File outputFile = new File(filePathName);
        if(outputFile.exists()){
            outputFile.delete();
        }

        int tag = -1;
        String extension = "jpg";
        int format = ViewShot.Formats.JPEG;
        double quality = 0.8;
        Integer width = null;
        Integer height = null;
        File output = outputFile;
        String result = ViewShot.Results.TEMP_FILE;
        Boolean snapshotContentContainer = false;
        View currentView = activity.getWindow().getDecorView().findViewById(android.R.id.content);

        new ViewShot(tag, extension, format, quality, width, height, output, result, snapshotContentContainer, currentView).execute();
    }

}
