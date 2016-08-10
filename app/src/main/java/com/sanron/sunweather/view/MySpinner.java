package com.sanron.sunweather.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Scroller;

import com.sanron.sunweather.R;


/**
 * 自定义下拉列表
 *
 * @author Sanron
 */
public class MySpinner extends View {

    /**
     * 下拉动画时间
     */
    public static final int DROP_DURATION = 300;

    public static final int DEF_TEXT_SIZE = 12;
    public static final int DEF_PADDING_LEFT = 10;

    /**
     * 列表数据
     */
    private List<String> mData;

    /**
     * 每个item的范围
     */
    private List<Rect> mBounds;
    /**
     * 列表是否展开
     */
    private boolean mIsDropDown;
    /**
     * 当前选中
     */
    private int mCurPosition;

    private Paint mPaint;

    private int mMeasureHeight;
    private int mMeasureWidth;

    private Scroller mScroller;

    private float mOffsetY;

    private int mTextColor;
    private int mSelectedColor;
    private int mTextSize;

    private int mPaddingLeft;

    private FontMetricsInt fontMetrics;

    private OnItemSelectedListener onItemSelectedListener;

    public interface OnItemSelectedListener {
        void onItemSelected(View parent, int position);
    }

    public MySpinner(Context context, AttributeSet attrs) {
        super(context, attrs);

        final Resources resources = getResources();
        final int defTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, DEF_TEXT_SIZE, resources.getDisplayMetrics());
        final int defPaddingLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEF_PADDING_LEFT, resources.getDisplayMetrics());
        final int defTextColor = Color.BLACK;
        final int defSelectedColor = Color.WHITE;

        TypedArray ta = resources.obtainAttributes(attrs, R.styleable.MySpinner);
        mTextSize = ta.getDimensionPixelSize(R.styleable.MySpinner_textSize, defTextSize);
        mTextColor = ta.getColor(R.styleable.MySpinner_textColor, defTextColor);
        mSelectedColor = ta.getColor(R.styleable.MySpinner_textSelectedColor, defSelectedColor);
        mPaddingLeft = ta.getDimensionPixelSize(R.styleable.MySpinner_paddingLeft, defPaddingLeft);
        CharSequence[] texts = ta.getTextArray(R.styleable.MySpinner_entries);

        mScroller = new Scroller(getContext());
        mBounds = new ArrayList<Rect>();
        mPaint = new Paint();
        mPaint.setColor(mTextColor);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(mTextSize);
        fontMetrics = mPaint.getFontMetricsInt();

        if (texts != null) {
            mData = new ArrayList<String>();
            for (CharSequence cs : texts) {
                mData.add(cs.toString());
            }
            resetBounds();
        }

        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mMeasureHeight = getMeasuredHeight();
                mMeasureWidth = getMeasuredWidth();
            }
        });
    }

    public void setData(List<String> data) {
        if (this.mData == data) {
            return;
        }

        this.mData = data;
        resetBounds();
        invalidate();
    }

    private void resetBounds() {
        mBounds.clear();

        if (hasData()) {
            mCurPosition = 0;
            for (int i = 0; i < mData.size(); i++) {
                mBounds.add(new Rect());
            }
        }
    }

    private void computeBounds() {
        int offY = (int) (mCurPosition * mMeasureHeight * mOffsetY);
        for (int i = 0; i < mData.size(); i++) {
            final Rect bounds = mBounds.get(i);
            bounds.left = 0;
            bounds.right = mMeasureWidth;
            bounds.top = mMeasureHeight * (i - mCurPosition) + offY;
            bounds.bottom = mMeasureHeight * (i - mCurPosition + 1) + offY;
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            //移动列表
            mOffsetY = mScroller.getCurrY() / 100f;
            invalidate();

            //改变高度
            final int height = (int) (mMeasureHeight + (mMeasureHeight * (mData.size() - 1) * mOffsetY));
            changeHeight(height);
        }
    }

    //改变高度
    private void changeHeight(int height) {
        LayoutParams lp = getLayoutParams();
        lp.height = height;
        setLayoutParams(lp);
    }

    //拉出收回 下拉列表
    public void toggle() {

        if (mIsDropDown) {
            //当前展开状态，要收回
            setPressed(false);
            expandViewTouch(false);
            mScroller.startScroll(0, 100, 0, -100, DROP_DURATION);
        } else {
            expandViewTouch(true);
            mScroller.startScroll(0, 0, 0, 100, DROP_DURATION);
        }
        mIsDropDown = !mIsDropDown;
        invalidate();
    }

    private boolean hasData() {
        return mData != null && mData.size() != 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (!hasData()) return;

        computeBounds();

        for (int i = 0; i < mData.size(); i++) {
            final String text = mData.get(i);
            final Rect bounds = mBounds.get(i);
            final int baseLine = caculateTextBaseLine(bounds, text);
            if (i == mCurPosition && mIsDropDown) {
                mPaint.setColor(mSelectedColor);
                canvas.drawText(text, bounds.left + mPaddingLeft, baseLine, mPaint);
                mPaint.setColor(mTextColor);
                continue;
            }

            canvas.drawText(text, bounds.left + mPaddingLeft, baseLine, mPaint);
        }
    }

    //计算文字基线，让文字垂直居中
    private int caculateTextBaseLine(Rect bounds, String text) {
        int baseLine = 0;
        int textHeight = fontMetrics.bottom - fontMetrics.top;
        baseLine = bounds.top + (bounds.height() - textHeight) / 2 - fontMetrics.top;
        return baseLine;
    }

    public void setPosition(int position) {
        if (mCurPosition == position) {
            return;
        }

        if (mCurPosition < 0 || mCurPosition > mData.size() - 1) {
            throw new IndexOutOfBoundsException();
        }

        mCurPosition = position;
        if (onItemSelectedListener != null) {
            onItemSelectedListener.onItemSelected(this, mCurPosition);
        }
        invalidate();
    }


    private boolean isTouchInView(int x, int y) {
        return x > 0 && x < getMeasuredWidth()
                && y > 0 && y < getMeasuredHeight();
    }

    //设置触摸委托，扩大触摸范围，实现点击spinner外收起列表
    private void expandViewTouch(boolean open) {
        if (getParent() instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) getParent();
            if (open) {
                Rect parentRect = new Rect();
                parent.getHitRect(parentRect);
                parent.setTouchDelegate(new TouchDelegate(parentRect, this) {
                    public boolean onTouchEvent(MotionEvent event) {
                        toggle();
                        return true;
                    }
                });
            } else {
                parent.setTouchDelegate(null);
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN: {
                setPressed(true);
            }
            break;

            case MotionEvent.ACTION_MOVE: {
                if (!isTouchInView(x, y)
                        && !mIsDropDown) {
                    setPressed(false);
                }
            }
            break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                if (isPressed()) {
                    if (!hasData() || mData.size() < 2) {
                        setPressed(false);
                    } else if (!mIsDropDown) {
                        toggle();
                    } else {
                        for (int i = 0; i < mBounds.size(); i++) {
                            if (mBounds.get(i).contains(x, y)) {
                                setPosition(i);
                                toggle();
                                break;
                            }
                        }
                    }
                }

            }
            break;

        }
        return true;
    }

    public OnItemSelectedListener getOnItemSelectedListener() {
        return onItemSelectedListener;
    }

    public void setOnItemSelectedListener(
            OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }
}
