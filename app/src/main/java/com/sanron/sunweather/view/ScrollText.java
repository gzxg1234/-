package com.sanron.sunweather.view;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

import com.sanron.sunweather.R;

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
import android.view.View;
import android.widget.Scroller;

/**
 * 滚动text
 *
 * @author Sanron
 */
public class ScrollText extends View {

    public static final int DURATION = 500;
    public static final int QUCIK_DURATION = 200;

    public static final int DEF_TEXT_SIZE = 12;//默认字体大小SP

    private String outText;
    private String inText;
    private Paint paint;
    private FontMetricsInt fontMetrics;
    private Queue<String> textQueue;
    private float fraction;
    private Scroller scroller;

    public ScrollText(Context context, AttributeSet attrs) {
        super(context, attrs);

        Resources resources = getResources();
        final int defTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, DEF_TEXT_SIZE, resources.getDisplayMetrics());
        final int defColor = Color.WHITE;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ScrollText);
        int textSize = ta.getDimensionPixelSize(R.styleable.ScrollText_textSize, defTextSize);
        int textColor = ta.getDimensionPixelSize(R.styleable.ScrollText_textColor, defColor);
        ta.recycle();

        textQueue = new LinkedBlockingDeque<String>();
        textQueue.add("");
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        fontMetrics = paint.getFontMetricsInt();
        scroller = new Scroller(context);
    }


    public void addText(String text) {
        textQueue.add(text);
        if (scroller.isFinished()) {
            startScroll();
        }
    }

    public void startScroll() {
        outText = textQueue.poll();
        inText = textQueue.peek();
        fraction = 0;
        //当队列数量大于2时，快速滚动
        scroller.startScroll(0, 0, 0, 100, textQueue.size() > 1 ? QUCIK_DURATION : DURATION);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            //移动比例
            fraction = scroller.getCurrY() / (float) scroller.getFinalY();
            if (fraction > 1) fraction = 1;
            invalidate();
        } else {
            if (textQueue.size() > 1) {
                //延迟100毫秒滚动下一次，停留效果
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startScroll();
                    }
                }, 100);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int alpha = (int) (fraction * 0xff);

        if (inText != null) {
            paint.setAlpha(alpha);
            Rect inBounds = caculateInBounds();
            canvas.drawText(inText, inBounds.left, inBounds.top - fontMetrics.top, paint);
        }
        if (outText != null) {
            paint.setAlpha(255 - alpha);
            Rect outBounds = caculateOutBounds();
            canvas.drawText(outText, outBounds.left, outBounds.top - fontMetrics.top, paint);
        }
    }


    private Rect caculateInBounds() {
        Rect inBounds = caculateText(inText);
        final int height = inBounds.height();
        final int startTop = getMeasuredHeight();
        final int endTop = (getMeasuredHeight() - height) / 2;
        inBounds.top = (int) (startTop + fraction * (endTop - startTop));
        inBounds.bottom = inBounds.top + height;
        return inBounds;
    }

    private Rect caculateOutBounds() {
        Rect outBounds = caculateText(outText);
        final int height = outBounds.height();
        final int startBottom = (getMeasuredHeight() + height) / 2;
        ;
        final int endBottom = 0;
        outBounds.bottom = (int) (startBottom + fraction * (endBottom - startBottom));
        outBounds.top = outBounds.bottom - height;
        return outBounds;
    }


    private Rect caculateText(String text) {
        Rect bounds = new Rect();
        bounds.bottom = fontMetrics.bottom - fontMetrics.top;
        bounds.right = (int) paint.measureText(text);

        int width = bounds.width();
        bounds.left = getMeasuredWidth() / 2 - bounds.width() / 2;
        bounds.right = bounds.left + width;
        return bounds;
    }
}
