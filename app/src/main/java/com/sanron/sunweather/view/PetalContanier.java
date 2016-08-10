package com.sanron.sunweather.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * 花瓣容器
 *
 * @author 三荣
 */
public class PetalContanier extends ViewGroup {

    private OnItemClickListener onItemClickListener;

    public PetalContanier(Context context, AttributeSet attrs) {
        super(context, attrs);
        for (int i = 0; i < 5; i++) {
            PetalView petalView = new PetalView(context);
            addView(petalView);
            final int position = i;
            petalView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, position);
                    }
                }
            });
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		int childHeight = Math.min(getMeasuredHeight(), getMeasuredWidth()/2);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth() / 2);
        for (int i = 0; i < getChildCount(); i++) {
            //限定花瓣的高度为容器宽的一半
            int childHeightSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY);
            getChildAt(i).measure(widthMeasureSpec, childHeightSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //设置倾斜度
        for (int i = 0; i < 5; i++) {
            View child = getChildAt(i);
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            child.setPivotX(width / 2);
            child.setPivotY(height);
            child.setRotation(-72 + i * 36);
            int left = getMeasuredWidth() / 2 - width / 2;
            child.layout(left, getMeasuredHeight() - height, left + width, getMeasuredHeight());
        }
    }


    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 花瓣点击监听
     *
     * @author Administrator
     */
    public static interface OnItemClickListener {
        public void onItemClick(View v, int position);
    }
}
