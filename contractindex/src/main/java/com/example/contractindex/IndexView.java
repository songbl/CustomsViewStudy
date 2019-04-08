package com.example.contractindex;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class IndexView extends View {

    /**
     * 每条的宽和高
     * 包含所在的索引和名字条目
     * 如： A
     *     阿里
     */
    private int itemWidth ;
    private int itemHeight ;

    private String[] words = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};

    private Paint paint ;
    public IndexView(Context context,  AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setTypeface(Typeface.DEFAULT_BOLD);//设置粗体
    }

    /**
     *  测量item 的宽和高
     *
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        itemWidth = getMeasuredWidth() ; //和控件设置的宽一致
        itemHeight = getMeasuredHeight() / words.length ;//高就是控件的高除以所以字母
        //控件的宽高没有改变，所以不需要调用setMeasureDimension()
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i=0;i<words.length;i++){

            if(touchIndex ==i){//是按下的哪个索引，画笔变色
                //设置灰色
                paint.setColor(Color.GRAY);
            }else{
                //设置白色
                paint.setColor(Color.WHITE);
            }


            String word = words[i];//取出所有的字母
            Rect rect = new Rect();
            paint.getTextBounds(word,0,1,rect);//后去最小的范围，在rect中
            //取出字母的宽和高
            int wordWidth = rect.width() ;
            int wordHeight = rect.height();
            //计算每个字母的坐标的位置（大致是左下角坐标）
            float wordX = itemWidth/2 -wordWidth/2 ;
            float wordY =  itemHeight / 2 + wordHeight / 2 + i * itemHeight;
            //绘制
            canvas.drawText(word,wordX,wordY,paint);

        }
    }


    /**
     * 实现触摸改变字母颜色
     */
    // 字母的下标位置
    private int touchIndex = -1;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float touchHeight = event.getY();
                int index = (int) (touchHeight/itemHeight);//字母的索引
                if(index !=touchIndex){
                    touchIndex = index ;
                    invalidate();//重新绘制
                    if(onIndexChangeListener != null&& touchIndex < words.length){
                        onIndexChangeListener.onIndexChange(words[touchIndex]);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                touchIndex = -1;
                invalidate();
                break;
        }
        return true;
    }

    /**
     * 字母下标索引变化的监听器
     */
    public interface OnIndexChangeListener{

        /**
         * 当字母下标位置发生变化的时候回调
         * @param word 字母（A~Z）
         */
        void onIndexChange(String word);
    }

    private OnIndexChangeListener onIndexChangeListener;

    /**
     * 设置字母下标索引变化的监听
     * @param onIndexChangeListener
     */
    public void setOnIndexChangeListener(OnIndexChangeListener onIndexChangeListener) {
        this.onIndexChangeListener = onIndexChangeListener;
    }
}
