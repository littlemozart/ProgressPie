package com.lee.progresspie;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.lee.lib.R;

public class ProgressPie extends View implements IProgress {

    private int mMin;
    private int mMax;
    private int mProgress;

    private int mBackground;
    private int mProgressTint;
    private int mCheckTint;

    private Paint mPaint;
    private RectF mRectF;
    private Path mPath;

    private static final int DEFAULT_SIZE = 40;

    /**
     * (sqrt(5) - 1) / 2
     */
    private static final float GOLDEN_RATIO = 0.618f;

    /**
     * cos(45)
     */
    private static final float COS_45 = 0.525f;

    public ProgressPie(Context context) {
        this(context, null);
    }

    public ProgressPie(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressPie(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.ProgressPie, defStyleAttr, 0);
        mMin = a.getInt(R.styleable.ProgressPie_pp_min, 0);
        mMax = a.getInt(R.styleable.ProgressPie_pp_max, 100);
        mProgress = a.getInt(R.styleable.ProgressPie_pp_progress, 0);
        mBackground = a.getColor(R.styleable.ProgressPie_pp_background, Color.LTGRAY);
        mProgressTint = a.getColor(R.styleable.ProgressPie_pp_progress_tint, Color.RED);
        mCheckTint = a.getColor(R.styleable.ProgressPie_pp_check_tint, Color.WHITE);
        a.recycle();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectF = new RectF();
        mPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureSize(widthMeasureSpec), measureSize(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingTop - paddingBottom;
        float radius = Math.min(width, height) / 2f;

        // draw background circle
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mBackground);
        canvas.drawCircle(paddingLeft + radius, paddingTop + radius, radius, mPaint);

        // draw progress pie
        float angle = 360f * mProgress / (mMax - mMin);
        mPaint.setColor(mProgressTint);
        mRectF.set(paddingLeft, paddingTop, radius * 2 + paddingLeft, radius * 2 + paddingTop);
        canvas.drawArc(mRectF, -90, angle, true, mPaint);

        // draw check
        if (mProgress >= mMax) {
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(radius / 8);
            mPaint.setColor(mCheckTint);
            float longSide = radius * 2 * GOLDEN_RATIO;
            float shortSide = longSide * GOLDEN_RATIO;
            float x1 = paddingLeft + shortSide * GOLDEN_RATIO;
            float y1 = paddingTop + radius;
            float x2 = x1 + shortSide * COS_45;
            float y2 = y1 + shortSide * COS_45;
            float x3 = x2 + longSide * COS_45;
            float y3 = y2 - longSide * COS_45;
            mPath.moveTo(x1, y1);
            mPath.lineTo(x2, y2);
            mPath.lineTo(x3, y3);
            canvas.drawPath(mPath, mPaint);
        }
    }

    private int measureSize(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int size = DEFAULT_SIZE;
        if (specMode == MeasureSpec.EXACTLY) {
            size = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            size = Math.min(size, specSize);
        }
        return size;
    }

    @Override
    public void setMax(int max) {
        mMax = max < 0 ? Math.max(0, mMin) : Math.max(mMin, max);
        invalidate();
    }

    @Override
    public void setMin(int min) {
        mMin = min < 0 ? 0 : Math.min(min, mMax);
        invalidate();
    }

    @Override
    public void setProgress(int progress) {
        mProgress = progress > mMax ? mMax : Math.max(mMin, progress);
        invalidate();
    }

    @Override
    public int getMax() {
        return mMax;
    }

    @Override
    public int getMin() {
        return mMin;
    }

    @Override
    public int getProgress() {
        return mProgress;
    }
}
