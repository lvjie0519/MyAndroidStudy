package com.android.study.example.uidemo.animation;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.android.study.example.R;

public class BeatMouseAnimView extends ImageView {
    
    public BeatMouseAnimView(Context context) {
        this(context, null);
    }

    public BeatMouseAnimView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BeatMouseAnimView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        AnimationDrawable animationDrawable = (AnimationDrawable) getResources().getDrawable(R.drawable.beat_mouse_graph);
        setImageDrawable(animationDrawable);
        animationDrawable.start();

//        setBackgroundResource(R.drawable.beat_mouse_graph_0);
    }
}
