package com.WideMouth.bluetooth20.WMView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.WideMouth.bluetooth20.R;
import com.WideMouth.bluetooth20.Util.WMUtil;

import java.util.ArrayList;
import java.util.List;

public class WMScanFrameLayout extends FrameLayout {

    private static final String TAG = "WMScanFrameLayout";

    private FloatingActionView floatingActionView;

    private OnCenterCircleClickListener onCenterCircleClickListener;

    private Context mContext;

    private String text = START;

    public static final String START = "开 始";
    public static final String STOP = "停 止";
    public static final String DISCONNECT = "断 开";

    private boolean showRipple = false;

    // 画笔对象

    private Paint mPaint;


    // View宽

    private float mWidth;


    // View高

    private float mHeight;


    // 声波的圆圈集合

    private List<Circle> mRipple;

    private float minCircleR = 0;

    // 圆圈扩散的速度

    private int mSpeed;


    // 圆圈之间的密度

    private int mDensity;

    // 圆圈的颜色

    private int mColor;

    // 圆圈是否为填充模式

    private boolean isFill;


    // 圆圈是否为渐变模式

    private boolean isAlpha;

    private boolean inscribedCircle = false;

    public WMScanFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public WMScanFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WMScanFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public WMScanFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        // 获取用户配置属性
        TypedArray tya = context.obtainStyledAttributes(attrs, R.styleable.WMRippleView);
        mColor = tya.getColor(R.styleable.WMRippleView_wmColor, 0xFFEE7C3D);
        mSpeed = tya.getInt(R.styleable.WMRippleView_wmSpeed, 3);
        mDensity = tya.getInt(R.styleable.WMRippleView_wmDensity, 50);
        isFill = tya.getBoolean(R.styleable.WMRippleView_wmIsFill, true);
        isAlpha = tya.getBoolean(R.styleable.WMRippleView_wmIsAlpha, true);
        tya.recycle();

        init();
    }

    private void init() {
        mContext = getContext();
        // 设置画笔样式
        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setStrokeWidth(WMUtil.dip2px(mContext, 1));
        if (isFill) {
            mPaint.setStyle(Paint.Style.FILL);
        } else {
            mPaint.setStyle(Paint.Style.STROKE);
        }
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);

        // 添加第一个圆圈
        mRipple = new ArrayList<>();

        mDensity = WMUtil.dip2px(mContext, mDensity);

        // 设置View的圆为半透明
        setBackgroundColor(Color.TRANSPARENT);
        setClickable(true);
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
            mWidth = WMUtil.dip2px(mContext, 120);
        }

        // 获取高度
        if (myHeightSpecMode == MeasureSpec.EXACTLY) {
            mHeight = myHeightSpecSize;
        } else {
            // wrap_content
            mHeight = WMUtil.dip2px(mContext, 120);
        }
        minCircleR = mWidth / 7;
        if (floatingActionView == null) {
            floatingActionView = new FloatingActionView(getContext());
            LayoutParams layoutParams = new LayoutParams((int) (minCircleR * 2 + 2), (int) (minCircleR * 2 + 2));
            layoutParams.gravity = Gravity.CENTER;
            addView(floatingActionView, layoutParams);
            floatingActionView.bringToFront();
        }
        // 设置该view的宽高
        setMeasuredDimension((int) mWidth, (int) mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (showRipple) {
            if (mRipple.size() == 0) {
                mRipple.add(new Circle(minCircleR, 255));
            }
        }
        drawRipple(canvas);
    }

    private void drawRipple(Canvas canvas) {
        canvas.save();
        float maxR = 0;
        if (inscribedCircle) {
            maxR = mWidth / 2;   //内切圆半径
        } else {
            maxR = (int) (Math.sqrt(mWidth * mWidth + mWidth * mWidth) / 2);//外切圆半径
        }

        // 处理每个圆的宽度和透明度
        for (int i = 0; i < mRipple.size(); i++) {
            Circle c = mRipple.get(i);
            mPaint.setAlpha(c.alpha);// （透明）0~255（不透明）
            canvas.drawCircle(mWidth / 2, mHeight / 2, c.r - mPaint.getStrokeWidth(), mPaint);

            // 当圆超出View的宽度后删除
            if (c.r > maxR) {
                mRipple.remove(i);
            } else {
                // 计算不透明的数值，这里有个小知识，就是如果不加上double的话，255除以一个任意比它大的数都将是0
                if (isAlpha) {
                    double alpha = 255 - 255 * (c.r - minCircleR + mSpeed) / ((double) maxR - minCircleR);
                    c.alpha = (int) Math.max(alpha, 0);
                }
                // 修改这个值控制速度
                c.r += mSpeed;
            }
        }

        // 里面添加圆
        if (mRipple.size() > 0) {
            // 控制第二个圆出来的间距
            if (showRipple && mRipple.get(0).r > WMUtil.dip2px(mContext, mDensity) + minCircleR) {
                mRipple.add(0, new Circle(minCircleR, 255));
            }

        }
        invalidate();
        canvas.restore();
    }


    public void startShowRipple() {
        this.showRipple = true;
        invalidate();
    }

    public void cancelShowRipple() {
        this.showRipple = false;
        invalidate();
    }

    public void setOnCenterCircleClickListener(OnCenterCircleClickListener onCenterCircleClickListener) {
        this.onCenterCircleClickListener = onCenterCircleClickListener;
    }

    public void setText(String text) {
        this.text = text;
        floatingActionView.invalidate();
    }

    public String getText() {
        return text;
    }

    public interface OnCenterCircleClickListener {
        void onClick(boolean showRipple, String text);
    }

    class FloatingActionView extends View {

        Paint p = new Paint();

        public FloatingActionView(Context context) {
            super(context);
            setClickable(true);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            p.setColor(0xAAEE7C3D);
            p.setAntiAlias(true);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, minCircleR, p);
            p.setTextSize(50);
            p.setColor(0xFFFFFFFF);
            p.setTextAlign(Paint.Align.CENTER);
            Paint.FontMetrics fontMetrics = p.getFontMetrics();
            float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
            float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
            int baseLineY = (int) (getHeight() / 2 - top / 2 - bottom / 2);//基线中间点的y轴计算公式
            canvas.drawText(text, getWidth() / 2, baseLineY, p);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = x;
                    downY = y;
                    break;
                case MotionEvent.ACTION_UP:
                    if (x == downX && y == downY) {
                        double r = Math.sqrt((x - getWidth() / 2) * (x - getWidth() / 2) + (y - getHeight() / 2) * (y - getHeight() / 2));
                        if (r < minCircleR) {
                            if (onCenterCircleClickListener != null) {
                                onCenterCircleClickListener.onClick(showRipple, text);
                            }
                        }
                    }
            }
            return super.onTouchEvent(event);
        }
    }

    class Circle {
        float r;
        int alpha;

        Circle(float r, int alpha) {
            this.r = r;
            this.alpha = alpha;
        }

    }

    float downX = 0;
    float downY = 0;

}