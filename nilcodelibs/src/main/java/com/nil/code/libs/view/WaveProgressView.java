package com.nil.code.libs.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by code_nil on 2017/8/23
 * 关于水波纹view的方法
 * 不支持鸿祥的自动匹配布局
 */
public class WaveProgressView extends View {
    private int mWidth;
    private int mHeight;

    private Bitmap mBackgroundBitmap;

    private Path mPath;
    private Paint mPathPaint;

    private float mWaveHeight = 20f;
    private float mWaveHalfWidth = 100f;
    private String mWaveColor = "#5be4ef";
    private int mWaveSpeed = 30;

    private Paint mTextPaint;
    private String mCurrentText = "";
    private String mTextColor = "#FFFFFF";
    private int mTextSize = 86;

    private int mMaxProgress = 100;
    private int mCurrentProgress = 0;
    private float mCurY;

    private float mDistance = 0;
    private int mRefreshGap = 10;
    private String[] colors = {"#0f2d3c", "#1e7396", "#28a0d2"};
    private int[] colorL = {Color.RED, Color.BLUE, Color.YELLOW};
    private boolean mAllowProgressInBothDirections = false;
    private LinearGradient shader;
    private static final int INVALIDATE = 0X777;
    //屏幕密度，用于控制在不同密度屏幕下面，px出现界面大小不同的情况
    private int density = 3;
    //画布抗锯齿
    PaintFlagsDrawFilter paintFlagsDrawFilter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INVALIDATE:
                    invalidate();
                    sendEmptyMessageDelayed(INVALIDATE, mRefreshGap);
                    break;
            }
        }
    };


    public WaveProgressView(Context context) {
        this(context, null, 0);
    }

    public WaveProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Init();
    }


    private void Init() {

        if (null == getBackground()) {
            throw new IllegalArgumentException(String.format("background is null."));
        } else {
            mBackgroundBitmap = getBitmapFromDrawable(getBackground());
        }

        mPath = new Path();
        mPathPaint = new Paint();
        mPathPaint.setAntiAlias(true);
        mPathPaint.setStyle(Paint.Style.FILL);


        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "pirulen.ttf"));
        paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        handler.sendEmptyMessageDelayed(INVALIDATE, 100);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mCurY = mHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.setDrawFilter(paintFlagsDrawFilter);
        if (mBackgroundBitmap != null) {
            canvas.drawBitmap(createImage(), 0, 0, null);
        }
    }

    private Bitmap createImage() {
        if (shader == null) {
            //渐变线
            shader = new LinearGradient(0, mHeight, 0, 0,
                    new int[]{
                            Color.parseColor("#0f2d3c"),
                            Color.parseColor("#1e7396"),
                            Color.parseColor("#28a0d2")},
                    new float[]{0, 0.5f, 1.0f}, Shader.TileMode.MIRROR);
        }
        mPathPaint.setShader(shader);
        mTextPaint.setColor(Color.parseColor(mTextColor));
        mTextPaint.setTextSize(mTextSize);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap finalBmp = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(finalBmp);

        float CurMidY = mHeight * (mMaxProgress - mCurrentProgress) / mMaxProgress;
        if (mCurY > CurMidY) {
            mCurY = mCurY - (mCurY - CurMidY) / 10;
        }
        mPath.reset();
        mPath.moveTo(0 - mDistance, mCurY);

        int waveNum = mWidth / ((int) mWaveHalfWidth * 4) + 1;
        int multiplier = 0;
        //控制图像的移动
        for (int i = 0; i < waveNum * 3; i++) {
            mPath.quadTo(mWaveHalfWidth * (multiplier + 1) - mDistance, mCurY - mWaveHeight, mWaveHalfWidth * (multiplier + 2) - mDistance, mCurY);
            mPath.quadTo(mWaveHalfWidth * (multiplier + 3) - mDistance, mCurY + mWaveHeight, mWaveHalfWidth * (multiplier + 4) - mDistance, mCurY);
            multiplier += 4;
        }
        mDistance += mWaveHalfWidth / mWaveSpeed;
        mDistance = mDistance % (mWaveHalfWidth * 4);

        mPath.lineTo(mWidth, mHeight);
        mPath.lineTo(0, mHeight);
        mPath.close();
        canvas.drawPath(mPath, mPathPaint);

        int min = Math.min(mWidth, mHeight);
        //设置true 用于抗锯齿
        mBackgroundBitmap = Bitmap.createScaledBitmap(mBackgroundBitmap, min, min, true);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));

        canvas.drawBitmap(mBackgroundBitmap, 0, 0, paint);

        canvas.drawText(mCurrentText, mWidth / 2, mHeight / 2, mTextPaint);
        return finalBmp;
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {

        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    //region set方法
    public void setDensity(int density) {
        this.density = density;
    }

    public void setCurrent(int currentProgress, String currentText) {
        this.mCurrentProgress = currentProgress;
        this.mCurrentText = currentText;
    }

    public void setMaxProgress(int maxProgress) {
        this.mMaxProgress = maxProgress;
    }

    public void setText(String mTextColor, int mTextSize) {
        this.mTextColor = mTextColor;
        this.mTextSize = (int) (mTextSize * density) / 3;
    }

    public void setWave(float mWaveHight, float mWaveWidth) {
        this.mWaveHeight = mWaveHight;
        this.mWaveHalfWidth = mWaveWidth / 2;
    }

    public void setWaveColor(String mWaveColor) {
        this.mWaveColor = mWaveColor;
    }

    public void setWaveSpeed(int mWaveSpeed) {
        this.mWaveSpeed = mWaveSpeed;
    }

    public void allowProgressInBothDirections(boolean allow) {
        this.mAllowProgressInBothDirections = allow;
    }
    //endregion
}
