package com.example.customviewsstudy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 1. 构造方法实力化类
 * 2.测量
 */

public class MyToggleButton extends View implements View.OnClickListener {

    private Bitmap backgroundBitmap;
    private Bitmap slideingBitmap;
    private int slideLeftMax;//距离左边的最大距离
    private int slideLeft;//距离左边的距离
    private Paint paint ;

    public MyToggleButton(Context context) {
        super(context);
        initView();
    }
    public MyToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }
    private void initView() {
        paint = new Paint();
        paint.setAntiAlias(true);//设置锯齿
        backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.switch_background);
        slideingBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.slide_button);
        slideLeftMax = backgroundBitmap.getWidth()-slideingBitmap.getWidth();//为遮挡住的长度
        setOnClickListener(this);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(backgroundBitmap.getWidth(),backgroundBitmap.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(backgroundBitmap,0,0,paint);

        canvas.drawBitmap(slideingBitmap,slideLeft,0,paint);//初始默认是0
    }

    private boolean isOpen = false;
    @Override
    public void onClick(View v) {
        isOpen =!isOpen ;
        if (isOpen){
            slideLeft = slideLeftMax ;
        }else {
            slideLeft = 0 ;
        }
        invalidate();
    }

    public boolean getStaue(){
        return isOpen ;
    }
}
