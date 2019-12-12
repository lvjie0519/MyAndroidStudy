package com.android.study.example.uidemo.mapsweeper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class ZoomMapSweeperView extends RelativeLayout {


    /**
     * Zooming view listener interface.
     *
     * @author karooolek
     *
     */
    public interface ZoomViewListener {

        void onZoomStarted(float zoom, float zoomx, float zoomy);

        void onZooming(float zoom, float zoomx, float zoomy);

        void onZoomEnded(float zoom, float zoomx, float zoomy);
    }

    // zooming
    private static final float MIN_ZOOM = 1.0f;
    private static final float MAX_ZOOM = 10.0f;
    private static final float SMOOTH_ZOOM_DEFAULT = 1.0f;
    float zoom = MIN_ZOOM;              //
    float maxZoom = MAX_ZOOM;
    float smoothZoom = SMOOTH_ZOOM_DEFAULT;
    float zoomX, zoomY;
    float smoothZoomX, smoothZoomY;
    private boolean scrolling;

    // touching variables
    private long lastTapTime;
    private float touchStartX, touchStartY;
    private float touchLastX, touchLastY;
    private float startd;
    private boolean pinching;
    private float lastd;
    private float lastdx1, lastdy1;
    private float lastdx2, lastdy2;

    // drawing
    private final Matrix m = new Matrix();
    private final Paint p = new Paint();

    // listener
    ZoomViewListener listener;

    private Bitmap ch;

    // 地图扫地机
    private MapSweeperView mMapSweeperView;
    private int mScreenWidth;       // 屏幕宽度

    public ZoomMapSweeperView(final Context context) {
        this(context, null);
    }

    public ZoomMapSweeperView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomMapSweeperView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initView(context);
    }

    private void  initView(Context context){
        this.mMapSweeperView = new MapSweeperView(context);
        LayoutParams sweeperViewParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        sweeperViewParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
        addView(this.mMapSweeperView, sweeperViewParams);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {


        int mapSweeperViewHeight = getMapSweeperViewHeight(widthMeasureSpec, heightMeasureSpec);
        this.mMapSweeperView.setHeight(mapSweeperViewHeight);
        this.mMapSweeperView.setWidth(mapSweeperViewHeight);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int getMapSweeperViewHeight(int widthMeasureSpec, int heightMeasureSpec){
        int result = mScreenWidth;

        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        if(heightMode == MeasureSpec.EXACTLY && (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST)){
            result = (height <= width) ? height : width;
            return result;
        }

        return result;
    }

    public MapSweeperView getMapSweeperView() {
        return mMapSweeperView;
    }

    public void setScreenWidth(int screenWidth) {
        this.mScreenWidth = screenWidth;
    }

    public float getZoom() {
        return zoom;
    }

    public float getMaxZoom() {
        return maxZoom;
    }

    public void setMaxZoom(final float maxZoom) {
        if (maxZoom < MIN_ZOOM || maxZoom>MAX_ZOOM) {
            this.maxZoom = MAX_ZOOM;
        }else {
            this.maxZoom = maxZoom;
        }
    }

    public void zoomTo(final float zoom, final float x, final float y) {
        this.zoom = Math.min(zoom, maxZoom);
        zoomX = x;
        zoomY = y;
        smoothZoomTo(this.zoom, x, y);
    }

    public void smoothZoomTo(final float zoom, final float x, final float y) {
        smoothZoom = clamp(MIN_ZOOM, zoom, maxZoom);
        smoothZoomX = x;
        smoothZoomY = y;
        if (listener != null) {
            listener.onZoomStarted(smoothZoom, x, y);
        }
    }

    public ZoomViewListener getListener() {
        return listener;
    }

    public void setListner(final ZoomViewListener listener) {
        this.listener = listener;
    }

    public float getZoomFocusX() {
        return zoomX * zoom;
    }

    public float getZoomFocusY() {
        return zoomY * zoom;
    }

    @Override
    public boolean dispatchTouchEvent(final MotionEvent ev) {

        // single touch
        if (ev.getPointerCount() == 1) {
            processSingleTouchEvent(ev);
        }

        // // double touch
        if (ev.getPointerCount() == 2) {
            processDoubleTouchEvent(ev);
        }

        // redraw
        getRootView().invalidate();
        invalidate();

        return true;
    }

    private void processSingleTouchEvent(final MotionEvent ev) {
        processSingleTouchOutsideMinimap(ev);
    }

    private void processSingleTouchOutsideMinimap(final MotionEvent ev) {
        final float x = ev.getX();
        final float y = ev.getY();
        float lx = x - touchStartX;
        float ly = y - touchStartY;
        final float l = (float) Math.hypot(lx, ly);     // 平方和的平方根
        float dx = x - touchLastX;
        float dy = y - touchLastY;
        touchLastX = x;
        touchLastY = y;

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStartX = x;
                touchStartY = y;
                touchLastX = x;
                touchLastY = y;
                dx = 0;
                dy = 0;
                lx = 0;
                ly = 0;
                scrolling = false;
                break;

            case MotionEvent.ACTION_MOVE:
                if (scrolling || (l > 30.0f)) {
                    if (!scrolling) {
                        scrolling = true;
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                        super.dispatchTouchEvent(ev);
                    }
                    smoothZoomX -= dx / zoom;
                    smoothZoomY -= dy / zoom;

                    return;
                }
                break;

            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_UP:

                // tap
                if (l < 30.0f) {
                    // 检查连续两次点击，自动放大或缩小, 暂时先注释
//                    if (System.currentTimeMillis() - lastTapTime < 500) {
//                        if (smoothZoom == 1.0f) {
//                            smoothZoomTo(maxZoom, x, y);
//                        } else {
//                            smoothZoomTo(1.0f, getWidth() / 2.0f,
//                                    getHeight() / 2.0f);
//                        }
//                        lastTapTime = 0;
//                        ev.setAction(MotionEvent.ACTION_CANCEL);
//                        super.dispatchTouchEvent(ev);
//                        return;
//                    }
//                    lastTapTime = System.currentTimeMillis();

                    performClick();
                }
                break;

            default:
                break;
        }

        ev.setLocation(zoomX + (x - 0.5f * getWidth()) / zoom, zoomY
                + (y - 0.5f * getHeight()) / zoom);

        super.dispatchTouchEvent(ev);
    }

    private void processDoubleTouchEvent(final MotionEvent ev) {
        // 第一个手指的触摸点处理
        final float x1 = ev.getX(0);
        final float dx1 = x1 - lastdx1;
        lastdx1 = x1;
        final float y1 = ev.getY(0);
        final float dy1 = y1 - lastdy1;
        lastdy1 = y1;

        // 第二个手指的触摸点处理
        final float x2 = ev.getX(1);
        final float dx2 = x2 - lastdx2;
        lastdx2 = x2;
        final float y2 = ev.getY(1);
        final float dy2 = y2 - lastdy2;
        lastdy2 = y2;

        // pointers distance
        final float d = (float) Math.hypot(x2 - x1, y2 - y1);  // 返回它的所有参数的平方和的平方根
        final float dd = d - lastd;
        lastd = d;
        final float ld = Math.abs(d - startd);      // 绝对值

//        Math.atan2(y2 - y1, x2 - x1);       // 好像没什么用
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startd = d;
                pinching = false;
                break;

            case MotionEvent.ACTION_MOVE:
                if (pinching || ld > 30.0f) {
                    pinching = true;
                    final float dxk = 0.5f * (dx1 + dx2);
                    final float dyk = 0.5f * (dy1 + dy2);
                    smoothZoomTo(Math.max(MIN_ZOOM, zoom * d / (d - dd)), zoomX - dxk
                            / zoom, zoomY - dyk / zoom);
                }

                break;
            case MotionEvent.ACTION_POINTER_2_UP:
            case MotionEvent.ACTION_POINTER_1_UP:
                Log.i("lvjie", "double Touch up..."+ev.getAction());
                this.mMapSweeperView.startAnimSweeperView();
                break;
            case MotionEvent.ACTION_UP:
                Log.i("lvjie", "double Touch up..."+ev.getAction());
            default:
                Log.i("lvjie", "double Touch up..."+ev.getAction());
                pinching = false;
                break;
        }

        ev.setAction(MotionEvent.ACTION_CANCEL);
        super.dispatchTouchEvent(ev);
    }

    private float clamp(final float min, final float value, final float max) {
        return Math.max(min, Math.min(value, max));
    }

    private float lerp(final float a, final float b, final float k) {
        return a + (b - a) * k;
    }

    private float bias(final float a, final float b, final float k) {
        return Math.abs(b - a) >= k ? a + k * Math.signum(b - a) : b;
    }

    @Override
    protected void dispatchDraw(final Canvas canvas) {

        // nothing to draw
        if (getChildCount() == 0) {
            return;
        }

        // do zoom
        zoom = lerp(bias(zoom, smoothZoom, 0.05f), smoothZoom, 0.2f);
        smoothZoomX = clamp(0.5f * getWidth() / smoothZoom, smoothZoomX,
                getWidth() - 0.5f * getWidth() / smoothZoom);
        smoothZoomY = clamp(0.5f * getHeight() / smoothZoom, smoothZoomY,
                getHeight() - 0.5f * getHeight() / smoothZoom);

        zoomX = lerp(bias(zoomX, smoothZoomX, 0.1f), smoothZoomX, 0.35f);
        zoomY = lerp(bias(zoomY, smoothZoomY, 0.1f), smoothZoomY, 0.35f);
        if (zoom != smoothZoom && listener != null) {
            listener.onZooming(zoom, zoomX, zoomY);
        }

        final boolean animating = Math.abs(zoom - smoothZoom) > 0.0000001f
                || Math.abs(zoomX - smoothZoomX) > 0.0000001f
                || Math.abs(zoomY - smoothZoomY) > 0.0000001f;

        // prepare matrix
        m.setTranslate(0.5f * getWidth(), 0.5f * getHeight());
        m.preScale(zoom, zoom);         // x  y  的缩放比例
        m.preTranslate(
                -clamp(0.5f * getWidth() / zoom, zoomX, getWidth() - 0.5f
                        * getWidth() / zoom),
                -clamp(0.5f * getHeight() / zoom, zoomY, getHeight() - 0.5f
                        * getHeight() / zoom));

        // get view
        final View v = getChildAt(0);
        m.preTranslate(v.getLeft(), v.getTop());

        // get drawing cache if available
        if (animating && ch == null && isAnimationCacheEnabled()) {
            v.setDrawingCacheEnabled(true);
            ch = v.getDrawingCache();
        }

        // draw using cache while animating
        if (animating && isAnimationCacheEnabled() && ch != null) {
            p.setColor(0xffffffff);
            canvas.drawBitmap(ch, m, p);
        } else { // zoomed or cache unavailable
            ch = null;
            canvas.save();
            canvas.concat(m);       // 使用矩阵放大或缩小及平移
            v.draw(canvas);
            canvas.restore();
        }

        // zoom改变,按需修改图片及相关大小
        if(this.mMapSweeperView != null){
            this.mMapSweeperView.updateViewByZoom(this.zoom);
        }

        // redraw
        // if (animating) {
        getRootView().invalidate();
        invalidate();
        // }
    }
}