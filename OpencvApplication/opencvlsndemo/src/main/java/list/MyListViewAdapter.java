package list;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.opencvlsndemo.R;

import java.util.ArrayList;
import java.util.List;

public class MyListViewAdapter extends BaseAdapter {
    private List<CommandData> commandDataList;
    private Context context;
    public MyListViewAdapter(Context context)
    {
        this.context=context;
        this.commandDataList=new ArrayList<>();
    }
    public List<CommandData> getModel() {
        return this.commandDataList;
    }
    @Override
    public int getCount() {
        return commandDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return commandDataList.get(position);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public long getItemId(int position) {
        return commandDataList.get(position).getId();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.rowitem,parent,false);
        TextView textView=view.findViewById(R.id.row_textView);
        view.setTag(commandDataList.get(position));
        textView.setText(commandDataList.get(position).getCommand());
        return view;
    }
}
