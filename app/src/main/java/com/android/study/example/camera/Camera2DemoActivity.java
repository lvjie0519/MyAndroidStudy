package com.android.study.example.camera;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.study.example.R;

public class Camera2DemoActivity extends AppCompatActivity {

    private String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
    };

    private AutoFitTextureView mTextureview;
    private LinearLayout mVerticalLinear;
    private LinearLayout mHorizontalLinear;
    private Button mTakePictureBtn;//拍照
    private Button mVideoRecodeBtn;//开始录像
    private Button mTakePictureBtn2;//拍照 横,竖屏状态分别设置了一个拍照,录像的按钮
    private Button mVideoRecodeBtn2;//开始录像
    private TextView mVHScreenBtn;
    private CameraController mCameraController;
    private boolean mIsRecordingVideo; //开始停止录像
    public static String BASE_PATH = Environment.getExternalStorageDirectory() + "/AAA";

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, Camera2DemoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_camera2_demo);
        CameraController.getInstance().init(this);
        //Activity对象
        PermissionsUtils.getInstance().checkPermissions(this, permissions, permissionsResult);
    }

    /**
     * 创建监听权限的接口对象
     */
    private PermissionsUtils.IPermissionsResult permissionsResult = new PermissionsUtils.IPermissionsResult() {
        @Override
        public void requestPermissions(String[] permissions, int requestCode) {
            ActivityCompat.requestPermissions(Camera2DemoActivity.this, permissions, requestCode);
        }

        @Override
        public void passPermissons() {
            //授权后的操作
            //获取相机管理类的实例
            mCameraController = CameraController.getInstance();
            mCameraController.setFolderPath(BASE_PATH);

            initView();
            //判断当前横竖屏状态
            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                mVHScreenBtn.setText("竖屏");
            } else {
                mVHScreenBtn.setText("横屏");

            }
        }
        @Override
        public void forbitPermissons() {
            //未授权，请手动授权

        }

        @Override
        public void positiveClick(Intent intent) {
            startActivity(intent);
            finish();
        }

    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //就多一个参数this
        PermissionsUtils.getInstance().onRequestPermissionsResult(this, this.getPackageName(), requestCode,
                permissions, grantResults);
    }

    private void initView() {
        mTextureview = (AutoFitTextureView) findViewById(R.id.textureview);
        mVerticalLinear = (LinearLayout) findViewById(R.id.vertical_linear);
        mHorizontalLinear = (LinearLayout) findViewById(R.id.horizontal_linear);

        mTakePictureBtn = (Button) findViewById(R.id.take_picture_btn);
        mVideoRecodeBtn = (Button) findViewById(R.id.video_recode_btn);
        mTakePictureBtn2 = (Button) findViewById(R.id.take_picture_btn2);
        mVideoRecodeBtn2 = (Button) findViewById(R.id.video_recode_btn2);
        mVHScreenBtn = (TextView) findViewById(R.id.v_h_screen_btn);

        //判断当前屏幕方向
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            //竖屏时
            mHorizontalLinear.setVisibility(View.VISIBLE);
            mVerticalLinear.setVisibility(View.GONE);
        } else {
            //横屏时
            mVerticalLinear.setVisibility(View.VISIBLE);
            mHorizontalLinear.setVisibility(View.GONE);
        }

        WindowManager windowManager = getWindowManager();
        int orientation = getResources().getConfiguration().orientation;
        mCameraController.initCamera(mTextureview, windowManager, orientation);

    }

    // 拍照
    public void btnOnClickTakePicture(View view) {
        mCameraController.takePicture();
    }

    // 开始录像
    public void btnOnClickVideoRecode(View view) {
        recordingVideo();
    }

    // 横竖屏切换
    public void btnOnClickScreenConvert(View view) {
        //判断当前屏幕方向
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            //切换竖屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            //切换横屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    /**
     * 视频录像
     */
    private void recordingVideo(){
        if (mIsRecordingVideo) {
            mCameraController.stopRecordingVideo();
            mVideoRecodeBtn.setText("开始录像");
            mVideoRecodeBtn2.setText("开始录像");
            ToastUtils.showToast(this, "录像结束");
        } else {
            mVideoRecodeBtn.setText("停止录像");
            mVideoRecodeBtn2.setText("停止录像");
            mCameraController.startRecordingVideo();
            ToastUtils.showToast(this, "录像开始");
        }
        mIsRecordingVideo = !mIsRecordingVideo;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mCameraController != null) {
            mCameraController.closeCamera();
        }
    }
}