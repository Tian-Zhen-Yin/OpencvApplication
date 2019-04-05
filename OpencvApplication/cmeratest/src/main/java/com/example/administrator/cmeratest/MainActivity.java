package com.example.administrator.cmeratest;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private ImageView imageView;
    private ImageUtil imageUtil;
    public static final String TAG="MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requirePermission();
        button = (Button) findViewById(R.id.choose);
        imageView = (ImageView) findViewById(R.id.imageview);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent albumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                albumIntent.addCategory(Intent.CATEGORY_OPENABLE);           //调用相册
                albumIntent.setType("image/*");
                startActivityForResult(albumIntent, 2);

            }
        });
    }
    private void requirePermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
            if (ActivityCompat.checkSelfPermission(MainActivity.this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission},123);
            }
        }
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 2 && resultCode == RESULT_OK) {
            Uri mPath = data.getData();
            if(imageUtil == null){
                imageUtil = new ImageUtil(this);
            }
            String file = imageUtil.getPath(mPath);
            Bitmap bitmap = imageUtil.decodeImage(file);
            if (bitmap == null) {

                return;
            }
            Log.i(TAG, "width=" + bitmap.getWidth() + ",height=" + bitmap.getHeight());

            imageView.setImageBitmap(bitmap);
    }
    }



}
