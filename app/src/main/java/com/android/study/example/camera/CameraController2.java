package com.android.study.example.camera;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class CameraController2 {
    private static final String TAG = "CameraController2";

    private Context mContext;
    private static CameraController2 sInstance;

    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler;

    private CameraManager mCameraManager;
    private String mCameraId;

    private CameraDevice mCameraDevice;
    // 从camera 里面读取数据流
    private ImageReader mImageReader;

    // camera 中的数据显示到AutoFitTextureView
    private AutoFitTextureView mAutoFitTextureView;
    private AutoFitTextureView mAutoFitTextureView2;
    private ImageView mImageView;

    private CameraCaptureSession mCameraCaptureSession;

    private CameraController2(){}

    public static CameraController2 getInstance() {
        if (sInstance == null) {
            synchronized (CameraController2.class) {
                if (sInstance == null) {
                    sInstance = new CameraController2();
                }
            }
        }

        return sInstance;
    }

    public void init(Context context){
        Context appContext = context.getApplicationContext();
        mContext = appContext == null ? context : appContext;

        initBackgroundHandler();
        initCameraManager();
    }

    private void initBackgroundHandler() {
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    private void initCameraManager(){
        mCameraManager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
    }

    public void initCamera(AutoFitTextureView textureView){
        mAutoFitTextureView = textureView;
        if (mAutoFitTextureView.isAvailable()) {
            openCamera();
        } else {
            mAutoFitTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
                @Override
                public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
                    openCamera();
                }

                @Override
                public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {

                }

                @Override
                public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
                    return false;
                }

                @Override
                public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {

                }
            });
        }
    }

    private void openCamera() {
        try {

            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "not has camera permission.");
                return;
            }

            mCameraManager.openCamera(getCameraId(), new CameraDevice.StateCallback() {
                @Override
                public void onOpened(@NonNull CameraDevice camera) {
                    Log.i(TAG, "camera onOpened.");
                    mCameraDevice = camera;
                    takePreview();
                }

                @Override
                public void onDisconnected(@NonNull CameraDevice camera) {
                    Log.i(TAG, "camera onDisconnected.");
                    camera.close();
                    mCameraDevice = null;
                }

                @Override
                public void onError(@NonNull CameraDevice camera, int error) {
                    Log.e(TAG, "camera onError, error: " + error);
                    camera.close();
                    mCameraDevice = null;
                    ToastUtils.showToast(mContext, "摄像头开启失败!");
                }
            }, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private String getCameraId() {
        Log.i(TAG, "mCameraId: " + mCameraId);
        if (!TextUtils.isEmpty(mCameraId)) {
            return mCameraId;
        }

        try {
            for (String cameraId : mCameraManager.getCameraIdList()) {

                CameraCharacteristics characteristics = mCameraManager.getCameraCharacteristics(cameraId);
                //不使用前置摄像
                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                    // 不结束循环,只跳出本次循环
                    continue;
                }

                StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                // 摄像头没有分辨率， 也过滤掉
                if (map == null) {
                    continue;
                }

                //对于静态图像拍照, 使用最大的可用尺寸
                Size largest = Collections.max(Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)),new CameraController.CompareSizesByArea());
                initImageReader(largest);

                mCameraId = cameraId;
                break;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        return mCameraId;
    }

    private void initImageReader(Size size){
        Log.i(TAG, "initImageReader call, width: "+size.getWidth()+", height: "+size.getHeight());
        mImageReader = ImageReader.newInstance(1080, 1920, ImageFormat.JPEG, 2);
        mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                // 这里获取摄像头数据
                Log.i(TAG, "onImageAvailable callback.");
                Image image = reader.acquireNextImage();
                ByteBuffer byteBuffer = image.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[byteBuffer.remaining()];
                byteBuffer.get(bytes);
                mBackgroundHandler.post(new CameraController2.ImageSaver(mImageView, bytes));
                image.close();
            }
        }, mBackgroundHandler);
    }

    private void takePreview() {
        final CaptureRequest.Builder previewRequestBuilder;
        try {
            previewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

            SurfaceTexture texture = mAutoFitTextureView.getSurfaceTexture();
            assert texture != null;
            // 预览的输出Surface。
            Surface surface = new Surface(texture);

            previewRequestBuilder.addTarget(surface);
            previewRequestBuilder.addTarget(mImageReader.getSurface());

            mCameraDevice.createCaptureSession(Arrays.asList(surface, mImageReader.getSurface()), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    mCameraCaptureSession = session;

                    previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);

                    CaptureRequest previewRequest = previewRequestBuilder.build();
                    try {
                        mCameraCaptureSession.setRepeatingRequest(previewRequest, null, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    ToastUtils.showToast(mContext, "onConfigureFailed!");
                }
            }, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    public void closeCamera() {
        if (mAutoFitTextureView != null) {
            mAutoFitTextureView.setSurfaceTextureListener(null);
            mAutoFitTextureView = null;
        }
    }

    public void setAutoFitTextureView2(AutoFitTextureView mAutoFitTextureView) {
        this.mAutoFitTextureView2 = mAutoFitTextureView;
    }

    public void setImageView(ImageView mImageView) {
        this.mImageView = mImageView;
    }

    private static class ImageSaver implements Runnable {

        private static Paint mBitPaint;
        private static Matrix mMatrix;

        /**
         * JPEG图像
         */
        private final byte[] mByteBuffer;

        private AutoFitTextureView mAutoFitTextureView;
        private ImageView mImageView;

        private void initPaint(){
            if(mBitPaint != null){
                return;
            }

            mBitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mBitPaint.setFilterBitmap(true);
            mBitPaint.setDither(true);
        }

        private void initMatrix(){
            mMatrix = new Matrix();
            mMatrix.postRotate(90);
        }

        public ImageSaver(ImageView imageView, byte[] bytes) {
            mByteBuffer = bytes;
            mImageView = imageView;
            initPaint();
            initMatrix();
        }

        @Override
        public void run() {
            Log.i(TAG, "bytes.length: "+mByteBuffer.length);
            ToastUtils.mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap = bytes2Bitmap(mByteBuffer, null);
//                    bitmap = Bitmap.createBitmap(bitmap, 0, 0,bitmap.getWidth(),bitmap.getHeight(), mMatrix, true);
                    mImageView.setImageBitmap(bitmap);
                }
            });
        }

        public static Bitmap bytes2Bitmap(byte[] bytes, BitmapFactory.Options opts){
            if ( null != opts ){
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,opts);
            }else{
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }
        }

    }

}
