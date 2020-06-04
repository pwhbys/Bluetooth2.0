package com.WideMouth.bluetooth20.WMView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.WideMouth.bluetooth20.Util.WMUtil;

import java.util.ArrayList;
import java.util.List;

public class WMNumberPicker extends View {

    int minNum = 0;
    int maxNum = 9;

    int minTextSize;
    int maxTextSize;

    // View宽
    private float mWidth;

    // View高
    private float mHeight;

    private Context context;

    List<Text> textList = new ArrayList<>();

    public WMNumberPicker(Context context) {
        this(context, null);
    }

    public WMNumberPicker(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WMNumberPicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public WMNumberPicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        this.minTextSize = WMUtil.dip2px(context, 14);
        this.maxTextSize = WMUtil.dip2px(context, 18);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int myWidthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int myWidthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int myHeightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int myHeightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        // 获取宽度
        if (myWidthSpecMode == MeasureSpec.EXACTLY) {
            // match_parent
            mWidth = myWidthSpecSize;
        } else {
            // wrap_content
            mWidth = WMUtil.dip2px(context, 120);
        }

        // 获取高度
        if (myHeightSpecMode == MeasureSpec.EXACTLY) {
            mHeight = myHeightSpecSize;
        } else {
            // wrap_content
            mHeight = WMUtil.dip2px(context, 50);
        }

        Text text0 = new Text(maxNum, minTextSize, 155, mWidth / 6);
        Text text = new Text(minNum, maxTextSize, 255, mWidth / 2);
        Text text1 = new Text(minNum + 1, minTextSize, 155, mWidth * 5 / 6);
        textList.add(text0);
        textList.add(text);
        textList.add(text1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(Text text:textList){
            Paint p = new Paint();
            p.setColor(0xFF333333);
            p.setAntiAlias(true);
            p.setTextSize(text.size);
            p.setTextAlign(Paint.Align.CENTER);
            Paint.FontMetrics fontMetrics = p.getFontMetrics();
            float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
            float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
            int baseLineY = (int) (getHeight() / 2 - top / 2 - bottom / 2);//基线中间点的y轴计算公式
            canvas.drawText(String.valueOf(text.num), text.x, baseLineY, p);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    class Text {
        int num;
        int size;
        int alpha;
        float x;

        public Text(int num, int size, int alpha, float x) {
            this.num = num;
            this.size = size;
            this.alpha = alpha;
            this.x = x;
        }
    }

}