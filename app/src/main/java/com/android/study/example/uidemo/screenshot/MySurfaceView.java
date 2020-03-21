package com.android.study.example.uidemo.screenshot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.SurfaceView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MySurfaceView extends GLSurfaceView {
    boolean shouldTakePic = false;
    File tempDir;
    private MyRenderer renderer;
    public void setShouldTakePic(boolean shouldTakePic) {
        this.shouldTakePic = shouldTakePic;
    }
    public MySurfaceView(Context context) {
        super(context);
        renderer = new MyRenderer();
        setRenderer(renderer);
    }
    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        renderer = new MyRenderer();
        setRenderer(renderer);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
    class MyRenderer implements GLSurfaceView.Renderer {
        int surfaceWidth;
        int surfaceHeight;
        public MyRenderer() {
        }
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        }
        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES20.glViewport(0, 0, width, height);
            surfaceWidth = width;
            surfaceHeight = height;
        }
        @Override
        public void onDrawFrame(GL10 gl) {
            try {
                if (shouldTakePic) {
                    shouldTakePic = false;
                    int w = surfaceWidth;
                    int h = surfaceHeight;
                    int b[] = new int[(int) (w * h)];
                    int bt[] = new int[(int) (w * h)];
                    IntBuffer buffer = IntBuffer.wrap(b);
                    buffer.position(0);
                    GLES20.glReadPixels(0, 0, w, h, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, buffer);
                    for (int i = 0; i < h; i++) {
                        for (int j = 0; j < w; j++) {
                            int pix = b[i * w + j];
                            int pb = (pix >> 16) & 0xff;
                            int pr = (pix << 16) & 0x00ff0000;
                            int pix1 = (pix & 0xff00ff00) | pr | pb;
                            bt[(h - i - 1) * w + j] = pix1;
                        }
                    }
                    Bitmap inBitmap = null;
                    if (inBitmap == null || !inBitmap.isMutable()
                            || inBitmap.getWidth() != w || inBitmap.getHeight() != h) {
                        inBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
                        //为了图像能小一点，使用了RGB_565而不是ARGB_8888
                    }
                    inBitmap.copyPixelsFromBuffer(buffer);
                    inBitmap = Bitmap.createBitmap(bt, w, h, Bitmap.Config.RGB_565);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    inBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);
                    byte[] bitmapData = bos.toByteArray();
                    ByteArrayInputStream fis = new ByteArrayInputStream(bitmapData);
                    String tempPicFile = "temp_" + System.currentTimeMillis() + ".jpeg";
                    tempDir = new File(Environment.getExternalStorageDirectory() + File.separator +
                            "SurfaceScreenShot" + File.separator + "Images");
                    tempDir.mkdirs();
                    try {
                        File tmpFile = new File(tempDir, tempPicFile);
                        FileOutputStream fos = new FileOutputStream(tmpFile);
                        byte[] buf = new byte[1024];
                        int len;
                        while ((len = fis.read(buf)) > 0) {
                            fos.write(buf, 0, len);
                        }
                        fis.close();
                        fos.close();
                        inBitmap.recycle();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
