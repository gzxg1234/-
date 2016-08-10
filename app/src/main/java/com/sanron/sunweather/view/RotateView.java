
package com.sanron.sunweather.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RotateDrawable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.sanron.sunweather.R;

/**
 * 旋转的地球视图
 *
 * @author Sanron
 */
public class RotateView extends View {

    private final float ROTATE_SPEED = 1;//每秒转动角度;
    private final float NORMAL_OFF_DEGREES = ROTATE_SPEED / 30;
    private final int ANIMATION_DURATION = 400;//动画时间

    public static int STATE_UP = 1;
    public static int STATE_DOWN = 2;
    public static int STATE_HIDE = 3;

    private Scroller scroller;
    /***
     * 当前旋转角度
     */
    private float currentDegrees = 0;
    private FlingRotateTask flingRotateTask;//快速旋转
    private GestureDetector detector;//手势解析
    private boolean touchRotateable = true;//是否可触摸转动
    private int scrollMin = -1;//scrollY最小值
    private int scrollMax = 0;//scrollY最大值
    private int state = STATE_UP;
    private boolean isDrag = false;
    private float offDegrees = NORMAL_OFF_DEGREES; //角度偏移

    private int mMeasureWidth;
    private int mMeasureHeight;

    private Drawable mDrawable;
    private int mDrawableSize;//球的半径

    private OnSingleTapListener onSingleTapListener;//单击
    private OnScrollListener onScrollListener;
    private OnStateChangeListener onStateChangeListener;


    public RotateView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mDrawable = getResources().getDrawable(R.drawable.earth);
        scroller = new Scroller(getContext());

        detector = new GestureDetector(getContext(), new OnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (state == STATE_UP) {
                    toDown(true);
                } else if (state == STATE_DOWN) {
                    toUp(true);
                }
                if (onSingleTapListener != null) {
                    onSingleTapListener.onSingleTap(RotateView.this);
                }
                return true;
            }

            @Override
            public void onShowPress(MotionEvent e) {
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                                    float distanceY) {
                isDrag = true;
                if (Math.abs(distanceX) > Math.abs(distanceY)) {
                    if (touchRotateable) {
                        offDegrees = (float) ((-distanceX) / 10);
                        postInvalidate();
                    }
                } else {
                    int toY = (int) (getScrollY() + distanceY);
                    if (toY < scrollMin) {
                        scrollTo(0, scrollMin);
                    } else if (toY > scrollMax) {
                        scrollTo(0, scrollMax);
                    } else {
                        scrollTo(0, toY);
                    }
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, final float velocityX,
                                   float velocityY) {
                //飞速旋转
                if (touchRotateable) {
                    flingRotateTask = new FlingRotateTask();
                    flingRotateTask.start(velocityX / 2500);
                }
                return true;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                //判断是否在地球内触摸
                isDrag = false;
                int x = (int) e.getX();
                int y = (int) e.getY();
                int dx = x - getWidth() / 2;
                int dy = (y + getScrollY()) - getHeight();

                if (Math.sqrt(dx * dx + dy * dy) < mDrawableSize * 0.4) {
                    //按下时停止所有旋转
                    offDegrees = 0;
                    return true;
                }
                return false;
            }

        });
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        //计算地球图片的直径
        mMeasureWidth = getMeasuredWidth();
        mMeasureHeight = getMeasuredHeight();

        mDrawableSize = mMeasureHeight * 2;
        Rect bounds = new Rect((mMeasureWidth - mDrawableSize) / 2, 0,
                (mMeasureWidth + mDrawableSize) / 2, mDrawableSize);
        mDrawable.setBounds(bounds);

        scrollMin = (int) (-mMeasureHeight * 0.5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(currentDegrees, mMeasureWidth / 2, mMeasureHeight);
        mDrawable.draw(canvas);
        currentDegrees += offDegrees;
        if (currentDegrees > 360 || currentDegrees < -360) {
            currentDegrees = currentDegrees * 1000 % 360000 / 1000f;
        }
        if (offDegrees == NORMAL_OFF_DEGREES) {
            postInvalidateDelayed(33);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP
                || event.getAction() == MotionEvent.ACTION_CANCEL) {

            if (isDrag) {
                if (getScrollY() < (scrollMin) / 2) {
                    toDown(true);
                } else {
                    toUp(true);
                }
            }
            offDegrees = NORMAL_OFF_DEGREES;
            postInvalidate();
        }
        return detector.onTouchEvent(event);
    }


    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            int toY = scroller.getCurrY();
            scrollTo(0, toY);
            invalidate();
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollListener != null) {
            onScrollListener.onScroll(t - oldt);
        }
    }

    private void toUp(boolean isActively) {
        scroller.startScroll(0, getScrollY(), 0, -getScrollY(), ANIMATION_DURATION);
        if (state != STATE_UP) {
            state = STATE_UP;
            if (isActively && onStateChangeListener != null) {
                onStateChangeListener.onStateChange(state);
            }
        }
        setTouchRotateable(true);
        invalidate();
    }

    /**
     * 向下弹出
     *
     * @param isActively是否触发状态改变事件
     */
    private void toDown(boolean isActively) {
        scroller.startScroll(0, getScrollY(), 0, scrollMin - getScrollY(), ANIMATION_DURATION);
        if (state != STATE_DOWN) {
            state = STATE_DOWN;
            if (isActively && onStateChangeListener != null) {
                onStateChangeListener.onStateChange(state);
            }
        }
        setTouchRotateable(false);
        invalidate();
    }

    /**
     * 向下隐藏
     */
    private void toHide(boolean isActively) {
        scroller.startScroll(0, getScrollY(), 0, -getMeasuredHeight() - getScrollY(), ANIMATION_DURATION);
        if (state != STATE_HIDE) {
            state = STATE_HIDE;
            if (isActively && onStateChangeListener != null) {
                onStateChangeListener.onStateChange(state);
            }
        }
        setTouchRotateable(false);
        invalidate();
    }

    public void toUp() {
        toUp(false);
    }

    public void toDown() {
        toDown(false);
    }

    public void toHide() {
        toHide(false);
    }


    /**
     * 单击事件监听（按下与抬起的位置相投，即手指未滑动）
     *
     * @author Administrator
     */
    public interface OnSingleTapListener {
        public void onSingleTap(View v);
    }

    public void setOnSingleTapListener(OnSingleTapListener onSingleTapListener) {
        this.onSingleTapListener = onSingleTapListener;
    }

    public boolean isTouchRotateable() {
        return touchRotateable;
    }

    public void setTouchRotateable(boolean touchRotateable) {
        this.touchRotateable = touchRotateable;
    }


    public OnStateChangeListener getOnStateChangeListener() {
        return onStateChangeListener;
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }

    public OnScrollListener getOnScrollListener() {
        return onScrollListener;
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }


    /**
     * 快速旋转
     *
     * @author Administrator
     */
    public class FlingRotateTask extends Thread {

        public void start(float velocityX) {
            offDegrees = velocityX;
            super.start();
        }

        public void stopFling() {
            offDegrees = 0;
        }

        @Override
        public void run() {
            //加速度
            float factor = 0.002f;
            if (offDegrees > 0) {
                while (offDegrees > 0) {
                    offDegrees -= factor;
                    postInvalidate();
                    SystemClock.sleep(1);
                }
            } else if (offDegrees < 0) {
                while (offDegrees < 0) {
                    offDegrees += factor;
                    postInvalidate();
                    SystemClock.sleep(1);
                }
            }
            offDegrees = NORMAL_OFF_DEGREES;
        }
    }


    public interface OnScrollListener {
        /**
         * @param offY y轴位移
         */
        public void onScroll(int offY);
    }

    public interface OnStateChangeListener {
        public void onStateChange(int state);
    }
}

