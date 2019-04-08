package com.example.demo1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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

public class ImageProcessActivity extends AppCompatActivity implements View.OnClickListener{
    Button grayBtn,selectBtn;
    ImageView img;
    private ProcessImageUtils processImageUtils;
    Bitmap dstBitmap;
    Bitmap selectBitmap;
    public static final int CHOOSE_PHOTO=2;
    public int MAXSIZE=1024;
    public static final  String TAG="Process";
    public boolean flag=true;

    //OpenCV库加载并初始化成功后的回调函数
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

    private void requirePermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ImageProcessActivity.this, new String[]{permission},123);
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_image);
        requirePermission();
        initUI();
        grayBtn.setOnClickListener(this);
        selectBtn.setOnClickListener(this);
    }

    private void initUI() {
        grayBtn=findViewById(R.id.gray_btn);
        selectBtn=findViewById(R.id.select_btn);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.gray_btn:
                processImage();
                break;
            case R.id.select_btn:
                selectImage();
                break;
        }
    }

    private void processImage() {
        processGray();
        if(flag)
        {
            img.setImageBitmap(dstBitmap);
            grayBtn.setText("还原");
            flag=false;
        }
        else
        {   img.setImageBitmap(selectBitmap);
            grayBtn.setText("灰度化");
            flag=true;
        }

    }

    private void selectImage() {
        Intent albumIntent = new Intent(Intent.ACTION_GET_CONTENT);
        albumIntent.addCategory(Intent.CATEGORY_OPENABLE);           //调用相册
        albumIntent.setType("image/*");
        startActivityForResult(albumIntent, 2);
    }
    public void processGray()
    {
        Mat src=new Mat();
        Mat dst=new Mat();
        Bitmap tmp=selectBitmap.copy(selectBitmap.getConfig(),true);
        dstBitmap= Bitmap.createBitmap(tmp.getWidth(),tmp.getHeight(), Bitmap.Config.RGB_565);
        Utils.bitmapToMat(selectBitmap,src);
        Imgproc.cvtColor(src,dst,Imgproc.COLOR_BGRA2GRAY);
        Utils.matToBitmap(dst,dstBitmap);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK) {
            Uri mPath = data.getData();
            if(processImageUtils == null){
                processImageUtils = new ProcessImageUtils(this);
            }
            String file = processImageUtils.getPath(mPath);
            selectBitmap = processImageUtils.decodeImage(file);
            if (selectBitmap == null) {

                return;
            }
            Log.i(TAG, "width=" + selectBitmap.getWidth() + ",height=" + selectBitmap.getHeight());

            img=(ImageView)findViewById(R.id.img);
            img.setImageBitmap(selectBitmap);
        }

    }

}
