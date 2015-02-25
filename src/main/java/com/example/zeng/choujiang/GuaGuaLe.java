package com.example.zeng.choujiang;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zeng on 2015/2/9.
 */
public class GuaGuaLe extends View {
    private Bitmap mBitmap;
    private String mText = "恭喜发财";

    private Rect mTextRect;

    private int textSize;

    private int coverColor = 0x333333;

    private int viewWidth;
    private int viewHeight;

    private Paint mPaint;

    private float textX;

    private float textY;

    private Paint mPathPaint;

    private Path mPath;

    private Canvas mCanvas;

    private Paint mBackPaint;

    private int lastX;

    private int lastY;
    private boolean mComplete;

    private CompleteListener listener;

    public GuaGuaLe(Context context) {
        this(context, null);
    }

    public GuaGuaLe(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuaGuaLe(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(30);

        mTextRect = new Rect();
        mPaint.getTextBounds(mText, 0, mText.length(), mTextRect);

        mBackPaint = new Paint();
        mBackPaint.setAntiAlias(true);
        mBackPaint.setColor(Color.RED);


        mPathPaint = new Paint();
        mPath = new Path();

        mPathPaint.setColor(Color.parseColor("#c0c0c0"));
        mPathPaint.setAntiAlias(true);
        mPathPaint.setDither(true);
        mPathPaint.setStrokeJoin(Paint.Join.ROUND);
        mPathPaint.setStrokeCap(Paint.Cap.ROUND);
        mPathPaint.setStyle(Paint.Style.FILL);
        mPathPaint.setStrokeWidth(25);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        viewWidth = getMeasuredWidth();

        viewHeight = getMeasuredHeight();

        textX = viewWidth / 2 - mTextRect.width() / 2;

        textY = viewHeight / 2 - mTextRect.height() / 2 + mTextRect.height();


        mBitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);

        mCanvas = new Canvas(mBitmap);

        mCanvas.drawRect(new RectF(0, 0, viewWidth, viewHeight),
                mPathPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawRect(0, 0, getRight(), getBottom(), mBackPaint);

        canvas.drawText(mText, textX, textY, mPaint);

        mPathPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        if(!mComplete) {
            mCanvas.drawPath(mPath, mPathPaint);
            canvas.drawBitmap(mBitmap, 0, 0, null);
        } else {
            if(listener != null) {
                listener.complete();
            }
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                mPath.moveTo(lastX, lastY);
                break;

            case MotionEvent.ACTION_MOVE:
                if (Math.abs(lastX - x) > 3 || Math.abs(lastY - y) > 3) {

                    lastX = x;
                    lastY = y;
                    mPath.lineTo(lastX, lastY);
                }
                break;

            case MotionEvent.ACTION_UP:
                if(!mComplete) {
                    new Thread(new CalculateRun()).start();
                }
                break;


        }

        if(!mComplete) {
            invalidate();
        }

        return true;
    }

    class CalculateRun implements  Runnable {

        @Override
        public void run() {
            int w = getWidth();
            int h = getHeight();

            float wipeArea = 0;
            float allArea = w * h;
            Bitmap bitmap = mBitmap;
            int[] mPx = new int[w * h];

            bitmap.getPixels(mPx,0,w,0,0,w,h);

            for (int i = 0; i < w; i++) {
                for (int j = 0; j < h; j++) {
                    int index = i + j * w;
                    if (mPx[index] == 0)
                    {
                        wipeArea++;
                    }
                }
            }


            if (wipeArea > 0 && allArea > 0)
            {
                int percent = (int) (wipeArea * 100 / allArea);


                if (percent > 60)
                {
                    // 清除掉图层区域
                    mComplete = true;
                    postInvalidate();

                }

            }

        }
    }

    public void setCompleteListener(CompleteListener listener) {
        this.listener = listener;
    }

    public interface CompleteListener{
        public void complete();
    }
}
