package me.khrystal.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * usage: 播放暂停按钮
 * author: kHRYSTAL
 * create time: 17/10/23
 * update time:
 * email: 723526676@qq.com
 */

public class PlayPauseView extends View {

    private int mWidth;
    private int mHeight;
    private Paint mPaint;
    private Path mLeftPath; // 暂停样式左侧竖线
    private Path mRightPath; // 暂停时右侧竖线
    private float mGapWidth; // 两个暂停竖线中间的间距 默认为竖线宽度
    private float mProgress; // 动画progress
    private Rect mRect;
    private boolean isPlaying;
    private float mRectWidth; // 圆内矩形宽度
    private float mRectHeight; // 圆内矩形高度
    private int mRectLT; // 矩形左侧上侧坐标
    private int mRadius; // 圆的半径
    private int mBgColor = Color.WHITE;
    private int mBtnColor = Color.BLACK;
    private int mDirection = Direction.POSITIVE.value;
    private float mPadding;
    private int mAnimDuration = 200; // 动画时间长度


    public PlayPauseView(Context context) {
        super(context);
    }

    public PlayPauseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PlayPauseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mLeftPath = new Path();
        mRightPath = new Path();
        mRect = new Rect();
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PlayPauseView);
        mBgColor = ta.getColor(R.styleable.PlayPauseView_bg_color, Color.WHITE);
        mBtnColor = ta.getColor(R.styleable.PlayPauseView_btn_color, Color.BLACK);
        mGapWidth = ta.getFloat(R.styleable.PlayPauseView_gap_width, 0);
        mDirection = ta.getInt(R.styleable.PlayPauseView_anim_direction, Direction.POSITIVE.value);
        mPadding = ta.getFloat(R.styleable.PlayPauseView_space_padding, 0);
        mAnimDuration = ta.getInt(R.styleable.PlayPauseView_anim_direction, mAnimDuration);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                mWidth = mHeight = Math.min(mWidth, mHeight);
                setMeasuredDimension(mWidth, mHeight);
                break;
            case MeasureSpec.AT_MOST:
                float density = getResources().getDisplayMetrics().density;
                mWidth = mHeight = (int) (50 * density); // 默认为50dp
                setMeasuredDimension(mWidth, mHeight);
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = mHeight = w;
        initValue();
    }

    private void initValue() {
        mRadius = mWidth / 2;
        mPadding = getSpacePadding() == 0 ? mRadius / 3f : getSpacePadding();
        if (getSpacePadding() > mRadius / Math.sqrt(2) || mPadding < 0) {
            mPadding = mRadius / 3f; // 默认值
        }

        float space = (float) (mRadius / Math.sqrt(2) - mPadding); // 矩形宽高的一半
        mRectLT = (int) (mRadius - space);
        int rectRB = (int) (mRadius + space);
        mRect.top = mRectLT;
        mRect.bottom = rectRB;
        mRect.left = mRectLT;
        mRect.right = rectRB;

        mRectWidth = 2 * space; // 改为float类型，否则动画有抖动。并增加一像素防止三角形之间有缝隙
        mRectHeight = 2 * space + 1;
        mGapWidth = getGapWidth() != 0 ? getGapWidth() : mRectWidth / 3;
        mProgress = isPlaying ? 0 : 1;
        mAnimDuration = getAnimDuration() < 0 ? 200 : getAnimDuration();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mLeftPath.rewind();
        mRightPath.rewind();

        mPaint.setColor(mBgColor);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius, mPaint);

        float distance = mGapWidth * (1 - mProgress);  //暂停时左右两边矩形距离
        float barWidth = mRectWidth / 2 - distance / 2;     //一个矩形的宽度
        float leftLeftTop = barWidth * mProgress;       //左边矩形左上角

        float rightLeftTop = barWidth + distance;       //右边矩形左上角
        float rightRightTop = 2 * barWidth + distance;  //右边矩形右上角
        float rightRightBottom = rightRightTop - barWidth * mProgress; //右边矩形右下角

        mPaint.setColor(mBtnColor);
        mPaint.setStyle(Paint.Style.FILL);

        if (mDirection == Direction.NEGATIVE.value) {
            mLeftPath.moveTo(mRectLT, mRectLT);
            mLeftPath.lineTo(leftLeftTop + mRectLT, mRectHeight + mRectLT);
            mLeftPath.lineTo(barWidth + mRectLT, mRectHeight + mRectLT);
            mLeftPath.lineTo(barWidth + mRectLT, mRectLT);
            mLeftPath.close();

            mRightPath.moveTo(rightLeftTop + mRectLT, mRectLT);
            mRightPath.lineTo(rightLeftTop + mRectLT, mRectHeight + mRectLT);
            mRightPath.lineTo(rightRightBottom + mRectLT, mRectHeight + mRectLT);
            mRightPath.lineTo(rightRightTop + mRectLT, mRectLT);
            mRightPath.close();
        } else {
            mLeftPath.moveTo(leftLeftTop + mRectLT, mRectLT);
            mLeftPath.lineTo(mRectLT, mRectHeight + mRectLT);
            mLeftPath.lineTo(barWidth + mRectLT, mRectHeight + mRectLT);
            mLeftPath.lineTo(barWidth + mRectLT, mRectLT);
            mLeftPath.close();

            mRightPath.moveTo(rightLeftTop + mRectLT, mRectLT);
            mRightPath.lineTo(rightLeftTop + mRectLT, mRectHeight + mRectLT);
            mRightPath.lineTo(rightLeftTop + mRectLT + barWidth, mRectHeight + mRectLT);
            mRightPath.lineTo(rightRightBottom + mRectLT, mRectLT);
            mRightPath.close();
        }

        canvas.save();

        canvas.translate(mRectHeight / 8f * mProgress, 0);

        float progress = isPlaying ? (1 - mProgress) : mProgress;
        int corner = mDirection == Direction.NEGATIVE.value ? -90 : 90;
        float rotation = isPlaying ? corner * (1 + progress) : corner * progress;
        canvas.rotate(rotation, mWidth / 2f, mHeight / 2f);

        canvas.drawPath(mLeftPath, mPaint);
        canvas.drawPath(mRightPath, mPaint);

        canvas.restore();
    }

    public ValueAnimator getPlayPauseAnim() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(isPlaying ? 1 : 0, isPlaying ? 0 : 1);
        valueAnimator.setDuration(mAnimDuration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mProgress = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        return valueAnimator;
    }

    public void play() {
        if (getPlayPauseAnim() != null) {
            getPlayPauseAnim().cancel();
        }
        setPlaying(true);
        getPlayPauseAnim().start();
    }

    public void pause() {
        if (getPlayPauseAnim() != null) {
            getPlayPauseAnim().cancel();
        }
        setPlaying(false);
        getPlayPauseAnim().start();
    }

    public float getSpacePadding() {
        return mPadding;
    }

    public float getGapWidth() {
        return mGapWidth;
    }

    public int getAnimDuration() {
        return mAnimDuration;
    }

    private PlayPauseListener mPlayPauseListener;

    public void setPlayPauseListener(PlayPauseListener playPauseListener) {
        mPlayPauseListener = playPauseListener;
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying()) {
                    pause();
                    if (null != mPlayPauseListener) {
                        mPlayPauseListener.pause();
                    }
                } else {
                    play();
                    if (null != mPlayPauseListener) {
                        mPlayPauseListener.play();
                    }
                }
            }
        });
    }

    public interface PlayPauseListener {
        void play();

        void pause();
    }

    public enum Direction {
        POSITIVE(1), // 顺时针
        NEGATIVE(2); // 逆时针
        int value;

        Direction(int value) {
            this.value = value;
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public void setGapWidth(int gapWidth) {
        mGapWidth = gapWidth;
    }

    public int getBgColor() {
        return mBgColor;
    }

    public int getBtnColor() {
        return mBtnColor;
    }

    public int getDirection() {
        return mDirection;
    }

    public void setBgColor(int bgColor) {
        mBgColor = bgColor;
    }

    public void setBtnColor(int btnColor) {
        mBtnColor = btnColor;
    }

    public void setDirection(Direction direction) {
        mDirection = direction.value;
    }

    public void setSpacePadding(float padding) {
        mPadding = padding;
    }


    public void setAnimDuration(int animDuration) {
        mAnimDuration = animDuration;
    }
}
