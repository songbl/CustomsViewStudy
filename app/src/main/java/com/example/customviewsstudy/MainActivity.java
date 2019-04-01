package com.example.customviewsstudy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private MyToggleButton myToggleButton ;
    private Button button ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myToggleButton = findViewById(R.id.mytogglebtn);
        button = findViewById(R.id.btnTest);
        Log.e("====",""+myToggleButton.getStaue());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("====",""+myToggleButton.getStaue());

            }
        });
    }
}
