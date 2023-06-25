package com.android.study.example.camera.camera1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.android.study.example.R;
import com.android.study.example.camera.Camera2DemoActivity;
import com.android.study.example.camera.CameraBaseActivity;
import com.android.study.example.camera.CustomTextureView;

import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.List;

/**
 * https://blog.csdn.net/XSF50717/article/details/120340789
 */
public class Camera1DemoActivity extends CameraBaseActivity {
    private static final String TAG = "Camera1DemoActivity";

    private SurfaceView mCameraSurfaceView;
    private SurfaceHolder mSurfaceHolder;

    private CustomTextureView mCameraSurfaceView2;

    private Camera mCamera;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, Camera1DemoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera1_demo);

        openCamera();
        initView();
    }

    private void initView() {
        mCameraSurfaceView = findViewById(R.id.camera_surfaceview);
        mSurfaceHolder = mCameraSurfaceView.getHolder();

        mCameraSurfaceView2 = findViewById(R.id.camera_surfaceview_2);
//        mCameraSurfaceView2.setRotation(270);

        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                showLog(TAG, "surfaceCreated call.");
                startPreview();
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
                showLog(TAG, "surfaceChanged call, format: " + format + ", width:" + width + ", height:" + height);
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                showLog(TAG, "surfaceDestroyed call.");
                releaseCamera();
            }
        });
    }

    private void openCamera(){
        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);

        // 监听预览的数据
        mCamera.setPreviewCallback(new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                showLog(TAG, "onPreviewFrame call, data.length: "+data.length);
                mCameraSurfaceView2.drawBitMap(getPriviewPic(data, camera));
            }
        });
    }

    private void startPreview() {
        try {
            //设置实时预览
            mCamera.setPreviewDisplay(mSurfaceHolder);
            //Orientation
            mCamera.setDisplayOrientation(90);
            // 设置相机参数
            Camera.Parameters parameters = mCamera.getParameters();
//            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO); // 自动对焦
//            parameters.setFocusMode(Camera.Parameters.FLASH_MODE_AUTO); // 自动对焦
            Camera.Size size = getMaxSize(parameters);
            showLog(TAG, "width: " + size.width + ", height: " + size.height);
            parameters.setPreviewSize(1280, 720);
            mCamera.setParameters(parameters);
            //开始预览
            mCamera.startPreview();

            // 相机自动对焦完成后时通知回调
            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    showLog(TAG, "onAutoFocus call, success: " + success);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Camera.Size getMaxSize(Camera.Parameters parameters) {
        List<Camera.Size> supportedSizes = parameters.getSupportedPreviewSizes();

        Camera.Size result = supportedSizes.get(0);
        for(int i=1; i<supportedSizes.size(); i++){
            Camera.Size tempSize = supportedSizes.get(i);
            if(result.width < tempSize.width || result.height < tempSize.height){
                result = tempSize;
            }
        }

        return result;
    }

    private void releaseCamera() {
        if (null != mCamera) {
            mCamera.stopPreview();
            mCamera.stopFaceDetection();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    public Bitmap getPriviewPic(byte[] data, Camera camera) {//这里传入的data参数就是onpreviewFrame中需要传入的byte[]型数据
        Camera.Size previewSize = camera.getParameters().getPreviewSize();//获取尺寸,格式转换的时候要用到

//        data = rotateYUVDegree270AndMirror(data, previewSize.width, previewSize.height);
        mirror(data, previewSize.width, previewSize.height);

        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        YuvImage yuvimage = new YuvImage(
                data,
                ImageFormat.NV21,
                previewSize.width,
                previewSize.height,
                null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        yuvimage.compressToJpeg(new Rect(0, 0, previewSize.width, previewSize.height), 100, baos);// 80--JPG图片的质量[0-100],100最高
        byte[] rawImage = baos.toByteArray();
        //将rawImage转换成bitmap
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeByteArray(rawImage, 0, rawImage.length, options);
        return bitmap;
    }

    //镜像
    private void mirror(byte[] yuv_temp, int w, int h) {
        int i, j;

        int a, b;
        byte temp;
        //mirror y
        for (i = 0; i < h; i++) {
            a = i * w;
            b = (i + 1) * w - 1;
            while (a < b) {
                temp = yuv_temp[a];
                yuv_temp[a] = yuv_temp[b];
                yuv_temp[b] = temp;
                a++;
                b--;
            }
        }
        //mirror u
        int uindex = w * h;
        for (i = 0; i < h / 2; i++) {
            a = i * w / 2;
            b = (i + 1) * w / 2 - 1;
            while (a < b) {
                temp = yuv_temp[a + uindex];
                yuv_temp[a + uindex] = yuv_temp[b + uindex];
                yuv_temp[b + uindex] = temp;
                a++;
                b--;
            }
        }
        //mirror v
        uindex = w * h / 4 * 5;
        for (i = 0; i < h / 2; i++) {
            a = i * w / 2;
            b = (i + 1) * w / 2 - 1;
            while (a < b) {
                temp = yuv_temp[a + uindex];
                yuv_temp[a + uindex] = yuv_temp[b + uindex];
                yuv_temp[b + uindex] = temp;
                a++;
                b--;
            }
        }
    }

    //顺时针旋转90
    private void YUV420spRotate90Clockwise(byte[] src, byte[] dst, int srcWidth, int srcHeight) {
        int wh = srcWidth * srcHeight;
        int uvHeight = srcHeight >> 1;

        //旋转Y
        int k = 0;
        for (int i = 0; i < srcWidth; i++) {
            int nPos = 0;
            for (int j = 0; j < srcHeight; j++) {
                dst[k] = src[nPos + i];
                k++;
                nPos += srcWidth;
            }
        }

        for (int i = 0; i < srcWidth; i += 2) {
            int nPos = wh;
            for (int j = 0; j < uvHeight; j++) {
                dst[k] = src[nPos + i];
                dst[k + 1] = src[nPos + i + 1];
                k += 2;
                nPos += srcWidth;
            }
        }
    }

    private byte[] rotateYUVDegree270AndMirror(byte[] data, int imageWidth, int imageHeight) {
        byte[] yuv = new byte[imageWidth * imageHeight * 3 / 2];
        // Rotate and mirror the Y luma
        int i = 0;
        int maxY = 0;
        for (int x = imageWidth - 1; x >= 0; x--) {
            maxY = imageWidth * (imageHeight - 1) + x * 2;
            for (int y = 0; y < imageHeight; y++) {
                yuv[i] = data[maxY - (y * imageWidth + x)];
                i++;
            }
        }
        // Rotate and mirror the U and V color components
        int uvSize = imageWidth * imageHeight;
        i = uvSize;
        int maxUV = 0;
        for (int x = imageWidth - 1; x > 0; x = x - 2) {
            maxUV = imageWidth * (imageHeight / 2 - 1) + x * 2 + uvSize;
            for (int y = 0; y < imageHeight / 2; y++) {
                yuv[i] = data[maxUV - 2 - (y * imageWidth + x - 1)];
                i++;
                yuv[i] = data[maxUV - (y * imageWidth + x)];
                i++;
            }
        }
        return yuv;
    }

}