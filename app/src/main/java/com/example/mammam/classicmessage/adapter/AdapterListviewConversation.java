package com.example.mammam.classicmessage.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mammam.classicmessage.R;
import com.example.mammam.classicmessage.models.MySms;

import java.util.List;

/**
 * Created by Mam  Mam on 10/31/2016.
 */

public class AdapterListviewConversation extends BaseAdapter {
    public static final int TYPE_1 = 0;
    public static final int TYPE_2 = 1;

    private Context mContext;
    private List<MySms> mySmses;

    public AdapterListviewConversation(Context context,List<MySms> mySmses){
        mContext = context;
        this.mySmses = mySmses;
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        int type = Integer.parseInt(mySmses.get(position).getType()) -1;
        return type;
    }

    @Override
    public int getCount() {
        Log.d("cccc",mySmses.size() + "");
        return mySmses.size();
    }

    @Override
    public MySms getItem(int position) {
        return mySmses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder viewHolder = null;
        if(null == convertView){
            viewHolder = new MyViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            if(Integer.parseInt(mySmses.get(position).getType()) -1 == TYPE_1){
                convertView = inflater.inflate(R.layout.item_listview_conversation_type_1,parent,false);
            }
            else {
                convertView = inflater.inflate(R.layout.item_listview_conversation_type_2,parent,false);
            }
            viewHolder.tvInbox = (TextView) convertView.findViewById(R.id.tv_inbox);
            convertView.setTag(viewHolder);

        }else {
            viewHolder = (MyViewHolder) convertView.getTag();
        }
        viewHolder.tvInbox.setText(mySmses.get(position).body);



        return convertView;
    }
    public static class MyViewHolder{
        public TextView tvInbox;
    }

}
