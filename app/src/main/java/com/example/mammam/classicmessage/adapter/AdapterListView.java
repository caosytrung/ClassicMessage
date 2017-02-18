package com.example.mammam.classicmessage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mammam.classicmessage.R;
import com.example.mammam.classicmessage.models.MySms;
import com.example.mammam.classicmessage.models.User;

import java.util.List;

/**
 * Created by Mam  Mam on 10/29/2016.
 */

public class AdapterListView extends BaseAdapter {
    private List<User> mUsers;
    private Context mContext;

    public AdapterListView(Context context,List<User> users){
        mContext = context;
        mUsers = users;

    }
    @Override
    public int getCount() {
        return mUsers.size();

    }

    @Override
    public User getItem(int position) {

        return mUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder v;
        if(null == convertView){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.item_listview_main,parent,false);
            v = new ViewHolder();
            v.tvBody = (TextView) convertView.findViewById(R.id.tv_body);
            v.tvDate = (TextView)convertView.findViewById(R.id.date);
            v.tvName = (TextView) convertView.findViewById(R.id.tv_nameUser);
            convertView.setTag(v);
        }
        else {
            v = (ViewHolder) convertView.getTag();
        }
        User user = mUsers.get(position);
        if(null == user.getName()){
            v.tvName.setText(user.getPhoneNumber());
        }
        else {
            v.tvName.setText(user.getName());
        }

        v.tvDate.setText(user.getDate());
        v.tvBody.setText(user.getLastSMs());



        return convertView;
    }
    public static class ViewHolder{
        private TextView tvName;
        private TextView tvDate;
        private TextView tvBody;
    }



}
