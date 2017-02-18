package com.example.mammam.classicmessage.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.mammam.classicmessage.R;
import com.example.mammam.classicmessage.adapter.AdapterListView;
import com.example.mammam.classicmessage.controllers.ManagerSms;
import com.example.mammam.classicmessage.models.User;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String KEY_PHONE = "PHONE_NUMBER";
    private static final String KEY_NAME = "NAME";
    private ManagerSms managerSms;
    private ListView mListView;
    private List<User> users;
    private AdapterListView mAdapterListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1234);
            }
        }
        initCommons();
        initViews();
        setUpFlabbyView();
    }

    private void initCommons(){
        managerSms = new ManagerSms(this);
        managerSms.copyDateBaseToSystem();

    }

    private void initViews(){
        mListView = (ListView) findViewById(R.id.list_view_main);
    }
    private void setUpFlabbyView(){
        users = new ArrayList<>();
        users = managerSms.getUser();
        mAdapterListView = new AdapterListView(this,users);
        mListView.setAdapter(mAdapterListView);
        mListView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,SecondActivity.class);
        Bundle b = new Bundle();
        b.putString(KEY_PHONE,users.get(position).getPhoneNumber());
        b.putString(KEY_NAME,users.get(position).getName());
        intent.putExtras(b);
        startActivity(intent);
    }
}
