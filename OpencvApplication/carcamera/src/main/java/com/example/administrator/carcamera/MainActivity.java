package com.example.administrator.carcamera;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity {
    private static  final String TAG="MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initOpenCV();

    }

    private void initOpenCV() {
        boolean result=OpenCVLoader.initDebug();
        if(result)
        {
            Log.i(TAG,"initOpenCV success");
            //初始化车牌监测器


        }
    }
}
