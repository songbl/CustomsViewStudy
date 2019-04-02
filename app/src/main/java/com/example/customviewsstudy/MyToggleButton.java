package com.example.customviewsstudy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 1. 构造方法实力化类
 * 2.测量
 */

public class MyToggleButton extends View implements View.OnClickListener {

    private Bitmap backgroundBitmap;
    private Bitmap slidingBitmap;
    /**
     * 距离左边最大距离
     */
    private int slidLeftMax;
    private Paint paint;
    private int slideLeft;

    private float startX;
    private float lastX;

    private boolean isOpen = false;

    /**
     * 如果我们在布局文件使用该类，将会用这个构造方法实例该类，如果没有就崩溃
     * @param context
     * @param attrs
     */
    public MyToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        paint = new Paint();
        paint.setAntiAlias(true);//设置抗锯齿
        backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.switch_background);
        slidingBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.slide_button);
        slidLeftMax = backgroundBitmap.getWidth() - slidingBitmap.getWidth();

        setOnClickListener(this);
    }

    /**
     * 视图的测量
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(backgroundBitmap.getWidth(), backgroundBitmap.getHeight());
    }

    /**
     * 绘制
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        canvas.drawBitmap(backgroundBitmap,0,0,paint);
        canvas.drawBitmap(slidingBitmap, slideLeft, 0, paint);
    }


    /**
     * true:点击事件生效，滑动事件不生效
     * false:点击事件不生效，滑动事件生效
     */
    private boolean isEnableClick = true;
    @Override
    public void onClick(View v) {
        if(isEnableClick){
            isOpen = !isOpen;

            flushView();
        }

    }

    private void flushView() {
        if(isOpen){
            slideLeft = slidLeftMax;
        }else{
            slideLeft = 0;
        }

        invalidate();//会导致onDraw()执行
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);//执行父类的方法
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //1.记录按下的坐标
                lastX = startX = event.getX();
                isEnableClick = true;
                break;
            case MotionEvent.ACTION_MOVE:
                //2.计算结束值
                float endX = event.getX();
                //3.计算偏移量
                float distanceX = endX - startX;

//                slideLeft = (int) (slideLeft + distanceX);
                slideLeft += distanceX;
                //4.屏蔽非法值
                if(slideLeft <0){
                    slideLeft = 0;
                }else if(slideLeft>slidLeftMax){
                    slideLeft = slidLeftMax;
                }
                //5.刷新
                invalidate();//这个时候是，拖动的过程进行绘制

                Log.e("====","调用了绘制的方法"+slideLeft);

                Log.e("====","还原后之前"+startX+"结束的值"+endX);
                //6.数据还原；继续滑动的时候，这个值需要一直改变，这个方法一直调用，在未完成前，相当于内部的上一次结果（一个滑动过程）
                startX = event.getX();

                if(Math.abs(endX - lastX) > 5){
                    //滑动
                    isEnableClick = false;
                }

                Log.e("====","还原后的初始值"+startX);
                break;
            case MotionEvent.ACTION_UP:
                Log.e("====","抬起"+isEnableClick);

                if(!isEnableClick){
                    if(slideLeft > slidLeftMax/2){
                        Log.e("====","超过一半"+slideLeft +"最大"+slidLeftMax);
                        //显示按钮开
                        isOpen = true;
                    }else{

                        isOpen = false;

                    }
                    flushView();
                }



                break;
        }
        return true;
    }
}
