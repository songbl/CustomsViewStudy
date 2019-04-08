package com.example.myviewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;
import android.widget.Toast;

public class MyViewPager extends ViewGroup {

    /**
     * 手势识别器
     * 1. 定义出来
     * 2. 实例化-把想要的方法给重写
     * 3. 在onTouchEvent()把时间传递给手势识别器
     */
    private GestureDetector detector ;
    private int currentIndex = 0;
    private Scroller scroller ;


    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller( context);
        initView(context);
    }

    private void initView(final Context context) {
        //2. 第二步，可以对应实现自己需要的方法
        //复杂的手势可以用这个实现双击，长点击等..
        detector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
           //长按
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                Toast.makeText(context,"长按",Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Toast.makeText(context,"双击",Toast.LENGTH_SHORT).show();
                return super.onDoubleTap(e);
            }

            /**
             * 滑动
             * @param e1
             * @param e2
             * @param distanceX x轴滑动的距离
             * @param distanceY y轴滑动的距离
             * @return
             */
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

                //滑动的过程
                Log.e("songbl","手势===distanceX"+distanceX);
                scrollBy((int) distanceX,0);
                return true;//自己处理喽
            }
        });

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i=0;i<getChildCount();i++){
            View childView = getChildAt(i);
            childView.layout(i*getWidth(),0,(i+1)*getWidth(),getHeight());
        }
    }

    private float startX;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
         super.onTouchEvent(event);
        //3.第三步，将事件传递给手势识别器
        detector.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //1.记录坐标
                startX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            //用以完成回弹或者弹到下个页面的效果
            case MotionEvent.ACTION_UP:
              //滑动后手指抬起时的坐标
              float endX = event.getX();
              //下标的位置
              int tempIndex = currentIndex ;
              if ((startX-endX)>getWidth()/2){
                  //向左移动
                  tempIndex++;
              } else if ((endX-startX)>getWidth()/2){
                  //向右移动
                  tempIndex--;
              }
              //根据下标移动到指定的页面
                scrollToPager(tempIndex);
              break;
          }
        return true;
    }

    public void scrollToPager(int tempIndex) {
        //可能滑动会越界，滑动的页面超出
        if (tempIndex<0){
            tempIndex = 0;
        }
        if (tempIndex>getChildCount()-1){
            tempIndex = getChildCount()-1;
        }
        //最终要滑动到的位置
        currentIndex = tempIndex ;
        if(onPagerChangeListener != null){
//
            onPagerChangeListener.onScrollToPager(currentIndex);
        }
        //getScrollX()已经滑动的距离
        int distanceX = currentIndex*getWidth() - getScrollX();
        Log.e("songbl","===getScrollX"+getScrollX()+"==当前 页"+currentIndex+"抬起"+distanceX);
        //移动到指定的位置，瞬间完成
//        scrollTo(currentIndex*getWidth(),getScrollY());

       // scroller.startScroll(getScrollX(),getScrollY(),distanceX,0);
        scroller.startScroll(getScrollX(),getScrollY(),distanceX,0,500);//添加上了滑动的时间

        invalidate();;//onDraw()
    }

    //通过draw来调用
    @Override
    public void computeScroll() {
//        super.computeScroll();
        //缓慢滑动的过程是否完成
        if( scroller.computeScrollOffset()){

            //得到新的坐标，需要滑动到
            float currX = scroller.getCurrX();
            Log.e("songbl","是否完成===getScrollX"+getScrollX()+"==scroller.getCurrX()"+scroller.getCurrX());

            scrollTo((int) currX,0);

            invalidate();
        };
    }

    /**
     *
     * 自己定义监听页面的改变
     */
    public interface  OnPagerChangeListener{
        /**
         * 当页面改变的时候回调这个方法
         * @param position
         */
        void onScrollToPager(int position);
    }
    private OnPagerChangeListener onPagerChangeListener ;
    public void setOnPagerChangeListener(OnPagerChangeListener onPagerChangeListener ){
        this.onPagerChangeListener = onPagerChangeListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //这个是绘制当前父类的孩子了
        for(int i=0;i<getChildCount();i++){
            View child = getChildAt(i);
            child.measure(widthMeasureSpec,heightMeasureSpec);
        }
    }
}
