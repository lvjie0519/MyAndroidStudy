package com.android.study.example.camera.camera1;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.android.study.example.R;
import com.android.study.example.camera.Camera2DemoActivity;
import com.android.study.example.camera.CameraBaseActivity;

import java.util.Iterator;
import java.util.List;

/**
 * https://blog.csdn.net/XSF50717/article/details/120340789
 */
public class Camera1DemoActivity extends CameraBaseActivity {
    private static final String TAG = "Camera1DemoActivity";

    private SurfaceView mCameraSurfaceView;
    private SurfaceHolder mSurfaceHolder;

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
        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);

        // 监听预览的数据
        mCamera.setPreviewCallback(new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                showLog(TAG, "onPreviewFrame call, data.length: "+data.length);
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
            parameters.setFocusMode(Camera.Parameters.FLASH_MODE_AUTO); // 自动对焦
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

}