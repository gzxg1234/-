package com.sanron.sunweather.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.v4.graphics.BitmapCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sanron.sunweather.R;
import com.sanron.sunweather.utils.DimenUtils;

public class PetalView extends View {

    private Drawable mBackground;

    public static int DEF_TEXT_SIZE = 12;//默认字体大小SP
    public static int DAY_PADDING_TOP = 10;//星期和温度之间的间距DP

    private Rect mIconBounds;
    private Rect mTempBounds;
    private Rect mDayBounds;

    private String mWeekDay;
    private String mTemperature;
    private Drawable mWeatherIcon;

    private int mMeasureHeight;
    private int mMeasureWidth;

    private Paint mPaint;

    private FontMetricsInt fontMetricsInt;

    public PetalView(Context context) {
        this(context, null);
    }

    public PetalView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mBackground = getResources().getDrawable(R.drawable.bg_petal);
        setBackground(mBackground);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, DEF_TEXT_SIZE, getResources().getDisplayMetrics()));
        fontMetricsInt = mPaint.getFontMetricsInt();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //强制长宽比
        int dWidth = mBackground.getIntrinsicWidth();
        int dHeight = mBackground.getIntrinsicHeight();

        int height = getMeasuredHeight();
        int width = dWidth * height / dHeight;
        setMeasuredDimension(width, height);
        mMeasureHeight = getMeasuredHeight();
        mMeasureWidth = getMeasuredWidth();
    }

    //计算各部分位置
    private void caculateBounds() {

        int textHeight = fontMetricsInt.bottom - fontMetricsInt.top;
        int textWidth = 0;

        mTempBounds = new Rect();
        textWidth = (int) mPaint.measureText(mTemperature == null ? "" : mTemperature);
        mTempBounds.left = (mMeasureWidth - textWidth) / 2;
        mTempBounds.right = mTempBounds.left + textWidth;
        mTempBounds.top = (mMeasureHeight - textHeight) / 2;
        mTempBounds.bottom = mTempBounds.top + textHeight;

        mIconBounds = new Rect();
        mIconBounds.set(0, 0, mMeasureWidth, mTempBounds.top);

        mDayBounds = new Rect();
        textWidth = (int) mPaint.measureText(mWeekDay == null ? "" : mWeekDay);
        final int paddingTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                DAY_PADDING_TOP, getResources().getDisplayMetrics());
        mDayBounds.left = (mMeasureWidth - textWidth) / 2;
        mDayBounds.right = mDayBounds.left + textWidth;
        mDayBounds.top = mTempBounds.bottom + paddingTop;
        mDayBounds.bottom = mDayBounds.top + textHeight;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Bitmap bmp = ((BitmapDrawable) mBackground.getCurrent()).getBitmap();
            int bmpWidth = bmp.getWidth();
            int bmpHeight = bmp.getHeight();
            int bmpX = x * bmpWidth / mMeasureWidth;
            int bmpY = y * bmpHeight / mMeasureHeight;
            if ((bmpX > 0 && bmpX < bmpWidth && bmpY > 0 && bmpY < bmpHeight)
                    && bmp.getPixel(bmpX, bmpY) == Color.TRANSPARENT) {
                //限定透明区域触摸无效，模拟不规则控件
                return false;
            }
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        caculateBounds();

        if (mWeatherIcon != null) {
            //
            int iconSize = (int) (mIconBounds.width() * 0.4f);
            mWeatherIcon.setBounds(mIconBounds.left, mIconBounds.top,
                    mIconBounds.left + iconSize, mIconBounds.top + iconSize);
            mWeatherIcon.getBounds().offset((mIconBounds.width() - iconSize) / 2,
                    (mIconBounds.height() - iconSize) / 2);

            mWeatherIcon.draw(canvas);
        }

        if (mTemperature != null) {
            canvas.drawText(mTemperature, mTempBounds.left,
                    mTempBounds.top - fontMetricsInt.top, mPaint);
        }

        if (mWeekDay != null) {
            canvas.drawText(mWeekDay, mDayBounds.left,
                    mDayBounds.top - fontMetricsInt.top, mPaint);
        }
    }

    public void setWeekDay(String weekDay) {
        this.mWeekDay = weekDay;
        invalidate();
    }

    public void setTemperature(String temperature) {
        this.mTemperature = temperature;
        invalidate();
    }

    public void setWeatherIcon(int resId) {
        this.mWeatherIcon = getResources().getDrawable(resId);
        invalidate();
    }
}
