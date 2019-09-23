package com.android.study.example.uidemo.mapsweeper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.android.study.example.R;
import com.android.study.example.utils.DisplayUtil;

public class SweeperView extends RelativeLayout {

    private static final int SweeperCircularDefaultSize = 30;   // dp
    private static final int SweeperDefaultSize = 20;   // dp

    private Context mContext;

    private View mSweeperCircular;
    private View mSweeper;
    private Animation mSweeperCircularAnimation;


    private int mViewHeight;
    private int mViewWidth;

    private Scroller mScroller;

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
}
