package com.example.demo1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.demo1.Adapter.MyListAdapter;
import com.example.demo1.Adapter.consatnts;
import com.example.demo1.Adapter.dataBean;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity  {

    private static final String TAG = "MainActivity";
    private MyListAdapter listAdapter;
    private String command;
    private Context mcontext;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewData();

    }

    private void initViewData() {
        mcontext=MainActivity.this;
        listView=findViewById(R.id.btn_list);
        listAdapter=new MyListAdapter(mcontext);
        listView.setAdapter(listAdapter);
        listAdapter.getModel().add(new dataBean(consatnts.TEST_ENV_COMMAND,1));
        listAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MainActivity.this,ProcessImageActivity.class);
                startActivity(intent);
            }
        });
    }




}