package com.example.administrator.opencvlsndemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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

import java.util.PropertyResourceBundle;

import list.CommandConstants;
import list.ProcessImageUtils;
@RequiresApi(api = Build.VERSION_CODES.KITKAT)

public class ProcessImageActivity extends AppCompatActivity implements View.OnClickListener {

   public static final String TAG="OBCLIB";
   private int CHOOSE_PHOTO=1;
   private String command;
   Bitmap selectedBitmap;
   private ProcessImageUtils processImageUtils;
   ImageView imageView;
   //权限申请
    private void requirePermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ProcessImageActivity.this, new String[]{permission},123);
            }
        }
    }
    //加载OpenCV库
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
        setContentView(R.layout.activity_process_image);
        requirePermission();
        imageView=findViewById(R.id.imageView);
        command=this.getIntent().getStringExtra("command");
        Button processBtn=(Button)findViewById(R.id.processButton);
        processBtn.setTag("PROCESS");
        processBtn.setOnClickListener(this);
        processBtn.setText(command);
        Button selectBtn=(Button)findViewById(R.id.select_imgButton);
      /*  selectedBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.tayler);*/
        selectBtn.setTag("SELECT");
        selectBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Object obj=v.getTag();
        if(obj instanceof String)
        {
            if("SELECT".equals(obj.toString())){
                selectImage();
                return;
            }
            else if("PROCESS".equals(obj.toString()))
            {
                processCommand();
            }
        }

    }


    private void selectImage() {
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT)
                .addCategory(Intent.CATEGORY_OPENABLE)
                .setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_PHOTO && resultCode == RESULT_OK) {
            Uri mPath = data.getData();
            if(processImageUtils == null){
                processImageUtils = new ProcessImageUtils(this);
            }
            String file = processImageUtils.getPath(mPath);
            selectedBitmap = processImageUtils.decodeImage(file);
            if (selectedBitmap == null) {

                return;
            }
            Log.i(TAG, "width=" + selectedBitmap.getWidth() + ",height=" + selectedBitmap.getHeight());

            imageView=findViewById(R.id.imageView);
            imageView.setImageBitmap(selectedBitmap);
        }
    }
    private void processCommand() {

        Bitmap temp=selectedBitmap.copy(selectedBitmap.getConfig(),true);
        if(CommandConstants.TEST_ENV_COMMAND.equals(command))
        {
            temp= ProcessImageUtils.convert2Gray(temp);
        }else if(CommandConstants.MAT_PIXEL_INVERT_COMMAND.equals(command))
        {
            temp=ProcessImageUtils.invert(temp);
        }
        else if(CommandConstants.BITMAP_PIXEL_INVERT_COMMAND.equals(command)) {
           ProcessImageUtils.local_invert(temp);
        }
        else if(CommandConstants.PIXEL_SUBSTRACT_COMMAND.equals(command))
        {
            temp=ProcessImageUtils.substract(temp);
        }
        else if(CommandConstants.ADJUST_CONTRAST_COMMAND.equals(command))
        {
            ProcessImageUtils.adjustContrast(temp);
        }
        else if(CommandConstants.IMAGE_CONTAINER_COMMAND.equals(command))
        {
            temp=ProcessImageUtils.demoMatUsage();
        }
        else if(CommandConstants.SUB_IMAGE_COMMAND.equals(command))
        {
            temp=ProcessImageUtils.getROIArea(temp);
        }
        else if(CommandConstants.BLUR_IMAGE_COMMAND.equals(command))
        {
            ProcessImageUtils.meanBlur(temp);
        }
        else if(CommandConstants.GAUSSIAN_BLUR_COMMAND.equals(command))
        {
            ProcessImageUtils.gaussianBlur(temp);
        }
        else if(CommandConstants.BI_BLUR_COMMAND.equals(command))
        {
            ProcessImageUtils.biBlur(temp);
        }
        else if(CommandConstants.CUSTOM_BLUR_COMMAND.equals(command)||
                (CommandConstants.CUSTOM_EDGE_COMMAND.equals(command))||
                (CommandConstants.CUSTOM_SHARPEN_COMMAND.equals(command)))
        {
            ProcessImageUtils.customFilter(command,temp);
        }
        else if(CommandConstants.ERODE_COMMAND.equals(command)||
                (CommandConstants.DILATE_COMMAND.equals(command)))
        {
            ProcessImageUtils.erodeOrdilate(command,temp);
        }
        else if(CommandConstants.MORPH_LINE_COMMAND.equals(command))
        {
            ProcessImageUtils.morphLineDetection(command,temp);
        }
        ImageView imgView = findViewById(R.id.imageView);
        imgView.setImageBitmap(temp);
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


}
