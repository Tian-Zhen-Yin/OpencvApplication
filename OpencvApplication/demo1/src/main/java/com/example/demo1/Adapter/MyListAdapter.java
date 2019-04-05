package com.example.demo1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.demo1.R;

import java.util.ArrayList;
import java.util.List;

public class MyListAdapter extends BaseAdapter {

    private List<dataBean> dataList;
    private Context context;

    public MyListAdapter(Context mcontext) {
        this.context=mcontext;
        this.dataList=new ArrayList<>();
    }

    public List<dataBean> getModel()
    {
        return this.dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return dataList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        view=LayoutInflater.from(context).inflate(R.layout.rawitem,parent,false);

        TextView itemText=(TextView)view.findViewById(R.id.raw_text);
        //
        view.setTag(dataList.get(position));
        itemText.setText(dataList.get(position).getCommand());
        return view;
    }
}
