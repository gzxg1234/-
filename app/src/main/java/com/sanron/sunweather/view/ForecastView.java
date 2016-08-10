package com.sanron.sunweather.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.sanron.sunweather.entity.Weather;

import java.util.List;

/**
 * Created by Administrator on 2015/11/30.
 */
public class ForecastView extends View {


    private Paint mPaint;
    private List<Weather> mData;

    private int mBgColor;//背景色
    private int mDateColor;//日期颜色
    private int mLowLineColor;//低温颜色
    private int mHightLineColor;//高温颜色


    public ForecastView(Context context) {
        super(context);
    }

    public ForecastView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {

    }

}
