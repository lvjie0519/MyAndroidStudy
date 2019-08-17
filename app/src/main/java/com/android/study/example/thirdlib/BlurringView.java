package com.android.study.example.thirdlib;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.AttributeSet;
import android.view.View;


/**
 * A custom view for presenting a dynamically blurred version of another view's content.
 * <p/>
 * Use {@link #setBlurredView(View)} to set up the reference to the view to be blurred.
 * After that, call {@link #invalidate()} to trigger blurring whenever necessary.
 */
public class BlurringView extends View {

    private static final int BLUR_RADIUS_DEFAULT = 15;
    private static final int DOWNSAMPLE_FACTOR_DEFAULT = 8;
    private static final int OVERLAY_COLOR_DEFAULT = Color.parseColor("#AAFFFFFF");

    public BlurringView(Context context) {
        this(context, null);
    }

    public BlurringView(Context context, AttributeSet attrs) {
        super(context, attrs);

        final Resources res = getResources();
        final int defaultBlurRadius = BLUR_RADIUS_DEFAULT;
        final int defaultDownsampleFactor = DOWNSAMPLE_FACTOR_DEFAULT;
        final int defaultOverlayColor = OVERLAY_COLOR_DEFAULT;

        initializeRenderScript(context);

        setBlurRadius(defaultBlurRadius);
        setDownsampleFactor(defaultDownsampleFactor);
        setOverlayColor(defaultOverlayColor);
    }

    public void setBlurredView(View blurredView) {
        mBlurredView = blurredView;
        checkForCircularReference();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mBlurredView != null) {
            if (prepare()) {
                // If the background of the blurred view is a color drawable, we use it to clear
                // the blurring canvas, which ensures that edges of the child views are blurred
                // as well; otherwise we clear the blurring canvas with a transparent color.
                if (mBlurredView.getBackground() != null && mBlurredView.getBackground() instanceof ColorDrawable) {
                    mBitmapToBlur.eraseColor(((ColorDrawable) mBlurredView.getBackground()).getColor());
                } else {
                    mBitmapToBlur.eraseColor(Color.TRANSPARENT);
                }

                mBlurredView.draw(mBlurringCanvas);
                blur();

                //锁画布(为了保存之前的画布状态)
                canvas.save();
                //把当前画布的原点移到(x,y),后面的操作都以(x,y)作为参照点，默认原点为(0,0)
                canvas.translate(mBlurredView.getX() - getX(), mBlurredView.getY() - getY());
                // 作用：sx、sy 是 x、y 方向上缩放的倍数,画布缩放后，再画出的图片相应的坐标都会进行缩放
                canvas.scale(mDownsampleFactor, mDownsampleFactor);
                canvas.drawBitmap(mBlurredBitmap, 0, 0, null);
                //把当前画布返回（调整）到上一个save()状态之前
                canvas.restore();
            }
            canvas.drawColor(mOverlayColor);
        }
    }

    public void setBlurRadius(int radius) {
        mBlurScript.setRadius(radius);
        invalidate();
    }

    public void setDownsampleFactor(int factor) {
        if (factor <= 0) {
            throw new IllegalArgumentException("Downsample factor must be greater than 0.");
        }

        if (mDownsampleFactor != factor) {
            mDownsampleFactor = factor;
            mDownsampleFactorChanged = true;
            invalidate();
        }
    }

    public void setOverlayColor(int color) {
        if (mOverlayColor != color) {
            mOverlayColor = color;
            invalidate();
        }
    }

    private void initializeRenderScript(Context context) {
        mRenderScript = RenderScript.create(context);
        mBlurScript = ScriptIntrinsicBlur.create(mRenderScript, Element.U8_4(mRenderScript));
    }

    protected boolean prepare() {
        final int width = mBlurredView.getWidth();
        final int height = mBlurredView.getHeight();

        if (mBlurringCanvas == null || mDownsampleFactorChanged
                || mBlurredViewWidth != width || mBlurredViewHeight != height) {
            mDownsampleFactorChanged = false;

            mBlurredViewWidth = width;
            mBlurredViewHeight = height;

            int scaledWidth = width / mDownsampleFactor;
            int scaledHeight = height / mDownsampleFactor;

            // This is no longer necessary on newer versions of RenderScript (23+)
            // -------------------------------------------------------------------
            // The following manipulation is to avoid some RenderScript artifacts at the edge.
            //scaledWidth = scaledWidth - scaledWidth % 4 + 4;
            //scaledHeight = scaledHeight - scaledHeight % 4 + 4;

            if (mBlurredBitmap == null
                    || mBlurredBitmap.getWidth() != scaledWidth
                    || mBlurredBitmap.getHeight() != scaledHeight) {
                mBitmapToBlur = Bitmap.createBitmap(scaledWidth, scaledHeight,
                        Bitmap.Config.ARGB_8888);
                if (mBitmapToBlur == null) {
                    return false;
                }

                mBlurredBitmap = Bitmap.createBitmap(scaledWidth, scaledHeight,
                        Bitmap.Config.ARGB_8888);
                if (mBlurredBitmap == null) {
                    return false;
                }
            }

            mBlurringCanvas = new Canvas(mBitmapToBlur);
            mBlurringCanvas.scale(1f / mDownsampleFactor, 1f / mDownsampleFactor);
            mBlurInput = Allocation.createFromBitmap(mRenderScript, mBitmapToBlur,
                    Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
            mBlurOutput = Allocation.createTyped(mRenderScript, mBlurInput.getType());
        }
        return true;
    }

    protected void blur() {
        mBlurInput.copyFrom(mBitmapToBlur);
        mBlurScript.setInput(mBlurInput);
        mBlurScript.forEach(mBlurOutput);
        mBlurOutput.copyTo(mBlurredBitmap);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        invalidate();
        checkForCircularReference();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mRenderScript != null) {
            mRenderScript.destroy();
        }
    }

    private void checkForCircularReference() {
        // Need to wait until blurredView is set and the view is attached to window.
        if (mBlurredView == null || getParent() == null) return;

        Boolean circularReference = (mBlurredView.findViewById(getId()) != null);
        if (circularReference) {
            setBlurredView(null);
            invalidate();
            return;
        }
    }

    private int mDownsampleFactor;
    private int mOverlayColor;

    protected View mBlurredView;
    private int mBlurredViewWidth, mBlurredViewHeight;

    private boolean mDownsampleFactorChanged;
    private Bitmap mBitmapToBlur, mBlurredBitmap;
    private Canvas mBlurringCanvas;
    private RenderScript mRenderScript;
    private ScriptIntrinsicBlur mBlurScript;
    private Allocation mBlurInput, mBlurOutput;
}
