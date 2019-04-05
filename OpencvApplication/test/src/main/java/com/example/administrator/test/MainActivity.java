package com.example.administrator.test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity {
    private final static String TAG="CVSAMPLE";
    Button btn;
  /*  Bitmap src;
    Bitmap dst;*/
    ImageView img;
    //加载OpenCV，回调函数
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {

        @Override
        public void onManagerConnected(int status) {
            // TODO Auto-generated method stub
            switch (status){
                case BaseLoaderCallback.SUCCESS:
                    Log.i(TAG, "成功加载");
                    break;
                default:
                    super.onManagerConnected(status);
                    Log.i(TAG, "加载失败");
                    break;
            }

        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //加载opencv
        iniUI();
        Button btn=findViewById(R.id.btn_gray_process);
       btn.setOnClickListener(new ProcessClickListener());
    }

    private void iniUI() {
        btn=(Button)findViewById(R.id.btn_gray_process);
        img=findViewById(R.id.img);
    }
    private class ProcessClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;//最高质量
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tayler, options);

            //矩阵,图像灰度化,src为操作前图像颜色矩阵,dst为灰度化后图像的颜色举证,
            //颜色矩阵和bitmap的转换通过utils工具完成
            Mat src = new Mat();
            Mat dst = new Mat();
            Utils.bitmapToMat(bitmap, src);
            Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGRA2GRAY);
            Utils.matToBitmap(dst, bitmap);
            img = findViewById(R.id.img);
            Log.e(TAG, "initOpenCV success");
        }

    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

   /* public void onClick(View v) {
        if (v.getId() == R.id.btn_gray_process) {
            //加载图片
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;//最高质量
            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.tayler, options);

            //矩阵,图像灰度化,src为操作前图像颜色矩阵,dst为灰度化后图像的颜色举证,
            //颜色矩阵和bitmap的转换通过utils工具完成
            Mat src = new Mat();
            Mat dst = new Mat();
            Utils.bitmapToMat(bitmap, src);
            Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGRA2GRAY);
            Utils.matToBitmap(dst, bitmap);
            ImageView img = findViewById(R.id.img);
            Log.e(TAG, "initOpenCV success");
        }
    }*/
}
