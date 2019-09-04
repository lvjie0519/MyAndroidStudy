package com.android.study.example.thirdlib;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.android.study.example.R;

public class RenderScriptBlurDemoActivity extends AppCompatActivity {

    private EditText etBlurDegree;
    private Button btnBlurImageView;
    private ImageView ivBlurPerson1;
    private Button btnBlurView;
    private View viewBlur1;

    private Dialog blurDialog;
    private View dialogRootView;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, RenderScriptBlurDemoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_render_script_blur_demo);

        initView();
        bindListener();
    }

    private void initView(){
        etBlurDegree = (EditText) findViewById(R.id.et_blur_degree);
        ivBlurPerson1 = (ImageView) findViewById(R.id.iv_blur_person_1);
        viewBlur1 = findViewById(R.id.view_blur_1);

        btnBlurImageView = (Button) findViewById(R.id.btn_blur_imageview);
        btnBlurView = (Button) findViewById(R.id.btn_blur_view);
    }

    private void bindListener(){
        btnBlurImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int blurDegree = 10;
                if(!TextUtils.isEmpty(etBlurDegree.getText().toString().trim())){
                    try {
                        blurDegree = Integer.parseInt(etBlurDegree.getText().toString().trim());
                    } catch (NumberFormatException e) {

                    }
                }
                Log.i("lvjie", "blurDegree: "+blurDegree);
//                blurByRenderScript(drawableToBitmap(ivBlurPerson1.getDrawable()), blurDegree, ivBlurPerson1, RenderScriptBlurDemoActivity.this);
                ivBlurPerson1.setImageBitmap(blurBitmap(drawableToBitmap(ivBlurPerson1.getDrawable()), blurDegree));
            }
        });

        btnBlurView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int blurDegree = 10;
                if(!TextUtils.isEmpty(etBlurDegree.getText().toString().trim())){
                    try {
                        blurDegree = Integer.parseInt(etBlurDegree.getText().toString().trim());
                    } catch (NumberFormatException e) {

                    }
                }
                Log.i("lvjie", "blurDegree: "+blurDegree);
                blurByRenderScript(drawableToBitmap(viewBlur1.getBackground()), blurDegree, viewBlur1, RenderScriptBlurDemoActivity.this);
            }
        });

        findViewById(R.id.btn_blur_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBlurDialog();
            }
        });
    }


    public Bitmap drawableToBitmap(Drawable drawable) {

        if(drawable instanceof BitmapDrawable){
            return ((BitmapDrawable) drawable).getBitmap();
        }

        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    public void blurByRenderScript(Bitmap bitmap, int radius, View view, Context context) {

        // 是用.create()方法创建实例，有好几个重载的方法可以选。同一时间点最好只创建一个RenderScript实例。
        RenderScript rs = RenderScript.create(context);

        // 创建一个或多个Allocation类实例：Allocation类用于存储需要进行处理的对象数据。
        // 包含很多静态创建方法，比较常用的是createFromBitmap(…)和createTyped(…)
        Allocation allocation = Allocation.createFromBitmap(rs, bitmap);

        // 创建需要使用的脚本，这里脚本需要分成两类：
        // ScriptC   这是我们自己编写的.rs文件。编译器会为我们自动映射一个java类，名字是ScriptC_文件名。
        // 假如我们写的文件是abc.rs,映射的类名就是ScriptC_abc,实例化操作如： ScriptC_abc abc = new ScriptC_abc(rs);
        // ScriptIntrinsic   这是RenderScript内置的已经帮我们写好了的常用脚本，比如有高斯模糊、图像融合等等，
        // 它们都继承抽象类ScriptIntrinsic。
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, allocation.getElement());

        // 设置必要的脚本变量：
        blur.setInput(allocation);
        blur.setRadius(radius);

        // 执行脚本：
        blur.forEach(allocation);

        // 从Allocation复制数据：将计算完成的数据从Allocation中复制出来，调用copyto(…)方法。 （例如复制到一个bitmap）
        allocation.copyTo(bitmap);
        if(view instanceof ImageView){
            ((ImageView)view).setImageBitmap(bitmap);
        }else{
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);
        }


        // 销毁RenderScript实例：最后，当然不要忘记销毁RenderScript实例，调用它的destroy()方法销毁。
        rs.destroy();
    }

    public Bitmap blurBitmap(Bitmap bitmap, int radius){

        //Let's create an empty bitmap with the same size of the bitmap we want to blur
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        //Instantiate a new Renderscript
        RenderScript rs = RenderScript.create(getApplicationContext());
        //Create an Intrinsic Blur Script using the Renderscript
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        //Create the Allocations (in/out) with the Renderscript and the in/out bitmaps
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

        //Set the radius of the blur: 0 < radius <= 25
        blurScript.setRadius(radius);

        //Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);

        //Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);

        //recycle the original bitmap
        bitmap.recycle();

        //After finishing everything, we destroy the Renderscript.
        rs.destroy();

        return outBitmap;

    }

    private void showBlurDialog(){

        if(blurDialog == null){
            blurDialog = new Dialog(RenderScriptBlurDemoActivity.this, R.style.MyDialogStyle);
            View layout = View.inflate(RenderScriptBlurDemoActivity.this, R.layout.blur_dialog, null);
            dialogRootView = layout.findViewById(R.id.layout_dialog);
            layout.findViewById(R.id.btn_blur_dialog_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    blurDialog.dismiss();
                }
            });

            layout.findViewById(R.id.btn_blur_dialog_view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    blurDialogRootView();
                }
            });

            blurDialog.setContentView(layout);
            Window window = blurDialog.getWindow();
            if (window != null) {
                window.setGravity(Gravity.BOTTOM);
            }
        }
        blurDialog.show();

    }

    /**
     * 图片缩放比例
     */
    private static float BITMAP_SCALE = 1f;
    /**
     * 最大模糊度(在0.0到25.0之间)
     */
    private static float BLUR_RADIUS = 15f;

    private void blurDialogRootView(){

        int blurDegree = 10;
        if(!TextUtils.isEmpty(etBlurDegree.getText().toString().trim())){
            try {
                blurDegree = Integer.parseInt(etBlurDegree.getText().toString().trim());
            } catch (NumberFormatException e) {

            }
        }

        // 获取activity的背景
        View activityView = getWindow().getDecorView();
        activityView.setDrawingCacheEnabled(true);
        activityView.destroyDrawingCache();
        activityView.buildDrawingCache();
        Bitmap image = activityView.getDrawingCache();

        // 计算图片缩小后的长宽
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        // 将缩小后的图片做为预渲染的图片。
        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        // 创建一张渲染后的输出图片。
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        // 创建RenderScript内核对象
        RenderScript rs = RenderScript.create(this);
        // 创建一个模糊效果的RenderScript的工具对象
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间。
        // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去。
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);

        // 设置渲染的模糊程度, 25f是最大模糊度
        blurScript.setRadius(blurDegree);
        // 设置blurScript对象的输入内存
        blurScript.setInput(tmpIn);
        // 将输出数据保存到输出内存中
        blurScript.forEach(tmpOut);

        // 将数据填充到Allocation中
        tmpOut.copyTo(outputBitmap);

//        blurDialog.getWindow().setBackgroundDrawable(new BitmapDrawable(getResources(), outputBitmap));
//        dialogRootView.draw(new Canvas(outputBitmap));
//        dialogRootView.invalidate();
        dialogRootView.setBackground(new BitmapDrawable(getResources(), outputBitmap));
        image.recycle();
    }


}
