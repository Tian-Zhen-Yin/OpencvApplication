package com.example.administrator.opencvlsndemo;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import list.CommandData;
import list.MyListViewAdapter;
@RequiresApi(api = Build.VERSION_CODES.M)
public class MainActivity extends AppCompatActivity {
    private MyListViewAdapter myListViewAdapter;
    private String command;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadViewData();
    }


    private void loadViewData() {
        myListViewAdapter=new MyListViewAdapter(this.getApplicationContext());
        ListView listView=this.findViewById(R.id.command_ListView);
        listView.setAdapter(myListViewAdapter);

        myListViewAdapter.getModel().addAll(CommandData.getCommandList());
        myListViewAdapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object obj=view.getTag();
                if(obj instanceof CommandData)
                {
                    CommandData cmdata=(CommandData)obj;
                    command=cmdata.getCommand();
                }
                Intent intent=new Intent(MainActivity.this,ProcessImageActivity.class);
                intent.putExtra("command",command);
                startActivity(intent);
            }
        });
    }

}
