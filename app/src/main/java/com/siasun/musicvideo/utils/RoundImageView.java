package com.siasun.musicvideo.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RoundImageView  extends AppCompatImageView {

    private Bitmap bitmapFrame;
    private float radius;
    // 四个角的x,y半径
    private float[] radiusArray = { 0f,0f,0f,0f,0f,0f,0f,0f };
    private Paint bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    public RoundImageView(Context context) {
        this(context,null);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    {
        this.radius = Resources.getSystem().getDisplayMetrics().density * 10;
        radiusArray[0]=radius;
        radiusArray[1]=radius;
        radiusArray[2]=radius;
        radiusArray[3]=radius;
    }
    @Override
    protected void onDraw(Canvas canvas) {

        // 得到原始的图片
        final int w = getWidth();
        final int h = getHeight();
        Bitmap bitmapOriginal = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmapOriginal);

        super.onDraw(c);

          if (bitmapFrame == null) {
            bitmapFrame = makeRoundRectFrame(w, h);
        }

        int sc = canvas.saveLayer(0, 0, w, h, null, Canvas.ALL_SAVE_FLAG);
        canvas.drawBitmap(bitmapFrame, 0, 0, bitmapPaint);
        // 利用Xfermode取交集（利用bitmapFrame作为画框来裁剪bitmapOriginal）
        bitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmapOriginal, 0, 0, bitmapPaint);

        bitmapPaint.setXfermode(null);
        canvas.restoreToCount(sc);

    }


    private Bitmap makeRoundRectFrame(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Path path = new Path();
        path.addRoundRect(new RectF(0, 0, w, h), radiusArray, Path.Direction.CW);
        Paint bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitmapPaint.setColor(Color.GREEN); // 颜色随意，不要有透明度。
        c.drawPath(path, bitmapPaint);
        return bm;
    }
}
