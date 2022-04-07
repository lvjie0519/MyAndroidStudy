package com.android.study.example.picpro;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PicUtils {

    /**
     * 质量压缩
     */
    public static Bitmap qualityCompressed(Bitmap sourceBmp, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        sourceBmp.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        byte[] bytes = baos.toByteArray();
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * 采样率压缩
     * @return
     */
    public static Bitmap samplingRateCompression(Bitmap sourceBmp, int sampleSize){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        sourceBmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }

    /**
     * 缩放法压缩（martix）
     * @param sourceBmp
     * @param scale
     * @return
     */
    public static Bitmap zoomCompression(Bitmap sourceBmp, float scale){
        // 缩放
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        try {
            Bitmap bitmap = Bitmap.createBitmap(sourceBmp, 0, 0, sourceBmp.getWidth(), sourceBmp.getHeight(), matrix, true);
            return bitmap;
        } catch (Exception e) {

        }

        return null;
    }

}
