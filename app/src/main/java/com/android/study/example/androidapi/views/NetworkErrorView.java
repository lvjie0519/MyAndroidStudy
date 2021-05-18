package com.android.study.example.androidapi.views;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.study.example.R;
import com.android.study.example.utils.DisplayUtil;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * 网络异常view
 */
public class NetworkErrorView extends LinearLayout {

    private TextView mTvReload;
    private NetworkErrorClickListener mClickListener;

    public NetworkErrorView(Context context) {
        super(context);

        initView(context);
    }

    private void initView(Context context){
        setLayoutParams(new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);

        // 网络出错图片
        ImageView imageView = new ImageView(context);
        LayoutParams imageParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        imageParams.width = DisplayUtil.dip2px(context, 120);
        imageParams.height = DisplayUtil.dip2px(context, 120);
        imageView.setLayoutParams(imageParams);
        imageView.setAdjustViewBounds(false);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//        BitmapDrawable imageBg = new BitmapDrawable(getResources(), UIHelper.loadBitmapFromAssets(context, Values.picture.PIC_NO_NETWORK));
        imageView.setBackground(getResources().getDrawable(R.drawable.ic_default_nfc_lock));
        addView(imageView);

        // 无法打开登录页 请检查网络连接情况
        TextView tvErrorTip = new TextView(context);
        LayoutParams tvErrorTipParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        tvErrorTipParams.setMargins(0,DisplayUtil.dip2px(context, 20), 0,DisplayUtil.dip2px(context, 20));
        tvErrorTip.setLayoutParams(tvErrorTipParams);
        tvErrorTip.setText("Values.strings.NETWORK_ERROR_TIPS");
        addView(tvErrorTip);

        // 重新加载
        mTvReload = new TextView(context);
        LayoutParams tvReloadParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        tvReloadParams.height = DisplayUtil.dip2px(context,44);
        mTvReload.setLayoutParams(tvReloadParams);
        int padding = DisplayUtil.dip2px(context,10);
        mTvReload.setPadding(padding, 0, padding, 0);
        mTvReload.setGravity(Gravity.CENTER);
        mTvReload.setText("Values.strings.RELOAD");
        mTvReload.setOnClickListener(mOnClickListener);
        addView(mTvReload);
    }

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v == mTvReload && mClickListener!=null){
                mClickListener.onClickReload(v);
            }
        }
    };

    public void setClickListener(NetworkErrorClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    public interface NetworkErrorClickListener{
        void onClickReload(View view);
    }
}
