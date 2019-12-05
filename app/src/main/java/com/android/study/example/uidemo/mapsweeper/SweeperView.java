package com.android.study.example.uidemo.mapsweeper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.android.study.example.R;
import com.android.study.example.utils.DisplayUtil;

/**
 * 封装的扫地机
 */
public class SweeperView extends RelativeLayout {

    private static final int SweeperCircularDefaultSize = 20;   // dp
    private static final int SweeperDefaultSize = 20;   // dp

    private Context mContext;

    private View mSweeperCircular;
    private View mSweeper;
    private Animation mSweeperCircularAnimation;

    // 初始化时的view宽高
    private int mViewHeight;
    private int mViewWidth;

    private Scroller mScroller;

    private float mZoom = 1.0f;      // 放大的倍数

    public SweeperView(@NonNull Context context) {
        this(context, null);
    }

    public SweeperView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SweeperView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.mContext = context;
        initView(context);
        initData(context);
    }


    private void initView(Context context){

        this.mSweeperCircular = new View(context);
        this.mSweeperCircular.setBackgroundResource(R.drawable.icon_sweeper_circular);
        mViewHeight = DisplayUtil.dip2px(context, SweeperCircularDefaultSize);
        mViewWidth = mViewHeight;
        RelativeLayout.LayoutParams sweeperCircularParams = new RelativeLayout.LayoutParams(mViewHeight, mViewWidth);
        sweeperCircularParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
        addView(this.mSweeperCircular, sweeperCircularParams);

        this.mSweeper = new View(context);
        this.mSweeper.setBackgroundResource(R.drawable.icon_sweeper);
        int sweeperViewPx = DisplayUtil.dip2px(context, SweeperDefaultSize);
        RelativeLayout.LayoutParams sweeperViewParams = new RelativeLayout.LayoutParams(sweeperViewPx, sweeperViewPx);
        sweeperViewParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
        addView(this.mSweeper, sweeperViewParams);
    }

    private void initData(Context context){
        mSweeperCircularAnimation = AnimationUtils.loadAnimation(context, R.anim.anim_sweeper_circular);

        mScroller = new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        startSweeperCircularAnimator();
    }

    private void startSweeperCircularAnimator(){
        this.mSweeperCircular.startAnimation(this.mSweeperCircularAnimation);
    }

    @Override
    protected void onDetachedFromWindow() {
        // 停止动画等
        super.onDetachedFromWindow();
    }

    //调用此方法滚动到目标位置
    public void smoothScrollTo(int fx, int fy) {
        int dx = fx - mScroller.getFinalX();
        int dy = fy - mScroller.getFinalY();
        smoothScrollBy(dx, dy);
    }

    //调用此方法设置滚动的相对偏移
    private void smoothScrollBy(int dx, int dy) {

        //设置mScroller的滚动偏移量
        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy);
        invalidate();//这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
    }

    @Override
    public void computeScroll() {
        //先判断mScroller滚动是否完成
        if (mScroller.computeScrollOffset()) {

            //这里调用View的scrollTo()完成实际的滚动
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            //必须调用该方法，否则不一定能看到滚动效果
            postInvalidate();
        }
        super.computeScroll();
    }

    public void setZoom(float zoom) {
        if(this.mZoom != zoom){
            this.mZoom = zoom;
            updateSweeperViewByZoom();
        }
    }

    /**
     * 但放大地图时，对图片进行适当的缩小，使得图片显示不是非常大
     */
    private void updateSweeperViewByZoom(){

        float width = this.mViewWidth / this.mZoom + this.mZoom/2;
        float height = this.mViewHeight / this.mZoom + this.mZoom/2;
        if(this.mSweeper != null){
            LayoutParams params = (LayoutParams) this.mSweeper.getLayoutParams();
            params.width = width < 1 ? 1 : (int) width;
            params.height = height < 1 ? 1 : (int) height;
            this.mSweeper.setLayoutParams(params);
            Log.i("SweeperView", "Sweeper  updateSweeperViewByZoom-->" +
                    "params.width="+params.width+"  params.height="+params.height+
                    "  mZoom="+this.mZoom+
                    "  mViewWidth="+this.mViewWidth+"  mViewHeight="+this.mViewHeight);
        }

        if(this.mSweeperCircular != null){
            LayoutParams params = (LayoutParams) this.mSweeperCircular.getLayoutParams();
            params.width = width < 1 ? 1 : (int) width;
            params.height = height < 1 ? 1 : (int) height;
            this.mSweeperCircular.setLayoutParams(params);
//            RnPluginLog.i("CommonSweeperView SweeperCircular  updateSweeperViewByZoom-->params.width="+params.width+"  params.height="+params.height);
        }
    }
}
