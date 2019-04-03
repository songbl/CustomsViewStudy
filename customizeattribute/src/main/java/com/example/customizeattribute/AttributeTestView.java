package com.example.customizeattribute;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class AttributeTestView extends View {

    //用于获取xml中定义的属性值->赋值
    private int myAge ;
    private String myName;
    private Bitmap myBg;
    private Paint paint ;

    /**
     *
     * @param context
     * @param attrs  反射获取XML中的设置
     */
    public AttributeTestView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        paint.setAntiAlias(true);
        //获取自定义属性有三种方式
        //1. 通过命名空间获取
        String age = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto","my_age");
        //2.通过遍历属性集合
        for(int i =0;i<attrs.getAttributeCount();i++){
            Log.e("songbl","值="+attrs.getAttributeName(i));
        }

        //第三种通过系统工具（一般都使用这种）
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.AttributeTestView);
        for (int i=0;i<typedArray.getIndexCount();i++){
            int index = typedArray.getIndex(i);
            switch (index){//变量
                case R.styleable.AttributeTestView_my_age:
                    myAge = typedArray.getInt(index,0);
                    break;
                case R.styleable.AttributeTestView_my_name:
                    myName = typedArray.getString(index);
                    break;
                case R.styleable.AttributeTestView_my_bg:
                    Drawable drawable = typedArray.getDrawable(index);
                    BitmapDrawable drawable1 = (BitmapDrawable) drawable;
                    myBg = drawable1.getBitmap();
                    break;
            }
        }
        // 记得回收
        typedArray.recycle();

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(myName+myAge,50,50,paint);
        canvas.drawBitmap(myBg,50,80,paint);
    }
}
