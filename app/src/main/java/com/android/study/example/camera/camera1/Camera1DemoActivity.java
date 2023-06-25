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
import android.util.Log;
import android.util.Size;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.android.study.example.R;
import com.android.study.example.camera.Camera2DemoActivity;
import com.android.study.example.camera.CameraBaseActivity;
import com.android.study.example.camera.CustomTextureView;
import com.libyuv.util.YuvUtil;

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

    private int mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private Camera mCamera;
    private Camera.Size mCameraSize;

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
        mCamera = Camera.open(mCameraId);

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

            YuvUtil.init(1280, 720, 1280, 720);

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

    private int[] getCompatibleSize(Camera.Parameters parameters) {

        Size screenSize = getScreenSize();
        int[] resultSize = new int[2];

        int minDistX = Integer.MAX_VALUE;
        int minDistY = minDistX;
        String supportedSizesStr = "Supported resolutions: ";
        // 相机支持的图像格式列表
        List<Camera.Size> supportedSizes = parameters.getSupportedPreviewSizes();
        // 通过传入的quality大小，选择一个最接近相机支持的图像格式
        for (Iterator<Camera.Size> it = supportedSizes.iterator(); it.hasNext();) {
            Camera.Size size = it.next();
            supportedSizesStr += size.width+"x"+size.height+(it.hasNext()?", ":"");
            int distX = Math.abs(screenSize.getWidth() - size.width);
            int distY = Math.abs(screenSize.getHeight() - size.height);
            // 优先比较宽度
            if (distX < minDistX) {
                minDistX = distX;
                resultSize[0] = size.width;
                resultSize[1] = size.height;
                continue;
            }

            // 宽度相同，再优先选择高度最小值
            if (distX == minDistX && distY < minDistY) {
                minDistY = distY;
                resultSize[0] = size.width;
                resultSize[1] = size.height;
                continue;
            }
        }

        Log.v(TAG, supportedSizesStr);
        Log.v(TAG, "Resolution modified: " + screenSize.getWidth() + "x" + screenSize.getHeight() + "->" + resultSize[0] + "x" + resultSize[1]);
        return resultSize;
    }

    public Size getScreenSize() {
        WindowManager wm = (WindowManager) getSystemService(
                Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Size size = new Size(display.getWidth(), display.getHeight());
        return size;
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

    public Bitmap getPriviewPic(byte[] data, Camera camera) {
        //这里传入的data参数就是onpreviewFrame中需要传入的byte[]型数据
        Camera.Size previewSize = camera.getParameters().getPreviewSize();//获取尺寸,格式转换的时候要用到

        Camera.CameraInfo camInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(mCameraId, camInfo);
        int cameraRotationOffset = camInfo.orientation;
        int mHeight = previewSize.height, mWidth = previewSize.width;
        Log.i(TAG, "lvjielvjie cameraRotationOffset: " + cameraRotationOffset
                + ", mWidth: " + mWidth+ ", mHeight: " + mHeight
                + ", data.length: " + data.length
                + ", mHeight*mWidth: " + (mHeight * mWidth*3/2));

        if (cameraRotationOffset == 270) {
            long time = System.currentTimeMillis();
            byte[] dst = new byte[data.length];
            YuvUtil.mirrorYUV(data, 1280, 720, dst, 1280, 720);
            byte[] nv21Data = new byte[data.length];
            YuvUtil.yuvI420ToNV21(dst, nv21Data, 1280, 720);
            YuvUtil.rotateNV21(nv21Data, data, 1280, 720, 90);
            Log.i("lvjielvjie", "time cost: " + (System.currentTimeMillis() - time));
        } else if (cameraRotationOffset == 90) {
            long time = System.currentTimeMillis();
            byte[] dst = new byte[data.length];
            YuvUtil.rotateNV21(data, dst, 1280, 720, 90);
            Log.i("lvjielvjie", "time cost: " + (System.currentTimeMillis() - time));
            data = dst;
        }

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
}