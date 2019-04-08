package com.example.myviewpager;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends Activity {

    private MyViewPager myviewpager;
    private RadioGroup radioGroup ;
    private int[] ids = {R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4, R.drawable.a5, R.drawable.a6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myviewpager = (MyViewPager) findViewById(R.id.myviewpager);
        radioGroup = (RadioGroup) findViewById(R.id.rg);

        //添加页面
        for (int i = 0; i < ids.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(ids[i]);

            //添加到MyViewPager这个View中
            myviewpager.addView(imageView);
        }
        //添加RadioButton
        for(int i=0;i<myviewpager.getChildCount();i++){
            RadioButton button = new RadioButton(this);
            button.setId(i);//0~5的id

            if(i==0){
                button.setChecked(true);
            }

            //添加到RadioGroup
            radioGroup.addView(button);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                myviewpager.scrollToPager(checkedId);
            }
        });

        //设置页面改变的监听
        myviewpager.setOnPagerChangeListener(new MyViewPager.OnPagerChangeListener() {
            @Override
            public void onScrollToPager(int position) {

                radioGroup.check(position);
            }
        });
    }
}
