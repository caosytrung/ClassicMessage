package com.example.mammam.classicmessage.ui;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mammam.classicmessage.R;
import com.example.mammam.classicmessage.adapter.AdapterListviewConversation;
import com.example.mammam.classicmessage.controllers.ManagerSms;
import com.example.mammam.classicmessage.models.MySms;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Mam  Mam on 10/31/2016.
 */

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String KEY_PHONE = "PHONE_NUMBER";
    private static final String KEY_NAME = "NAME";
    private static final String TAG = "SecondActivity";
    private TextView tvShowName;
    private EditText edtSendSms;
    private Button btnSendSms;
    private ListView lvConversation;
    private AdapterListviewConversation mAdapterListviewConversation;
    private List<MySms> smsList;
    private ManagerSms managerSms;
    String phoneNumber = null;
    String name = null;
    private static final String ERROR = "ERROR";
    private static final String DELIVERED = "DELIVERED";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        initCommons();
        initViews();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ERROR);
        filter.addAction(DELIVERED);

    }
    private void initCommons(){
        Intent intent = getIntent();
        phoneNumber = intent.getStringExtra(KEY_PHONE);
        name = intent.getStringExtra(KEY_NAME);
        managerSms = new ManagerSms(this);
        smsList = managerSms.getListSmsFromPhoneNumber(phoneNumber);
        mAdapterListviewConversation = new AdapterListviewConversation(this,smsList);
    }
    private void initViews(){
        tvShowName = (TextView) findViewById(R.id.tv_show_name_user);
        tvShowName.setText(name);
        btnSendSms = (Button) findViewById(R.id.btn_send_sms);
        edtSendSms = (EditText) findViewById(R.id.edt_send_sms);
        btnSendSms.setOnClickListener(this);
        lvConversation = (ListView) findViewById(R.id.lv_conversation);
        lvConversation.setAdapter(mAdapterListviewConversation);
        lvConversation.setSelection(smsList.size() - 1);
    }
    private void sendSms(String phoneNumber){
        SmsManager smsManager = SmsManager.getDefault();
        Intent intentDelivered = new Intent(DELIVERED);
        Intent intentError = new Intent(ERROR);
        IntentFilter filter = new IntentFilter();
        PendingIntent pendingIntentDel = PendingIntent.
                getBroadcast(this,1,intentDelivered,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntentErr = PendingIntent.
                getBroadcast(this,1,intentError,PendingIntent.FLAG_UPDATE_CURRENT);
        String body = edtSendSms.getText().toString();
        smsManager.sendTextMessage(phoneNumber,null,body,pendingIntentErr,pendingIntentDel);
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
        managerSms.insertSms(new MySms(body,phoneNumber,name,"1","2",format.format(date)));
        smsList.add(new MySms(body,phoneNumber,name,"1","2",format.format(date)));
        mAdapterListviewConversation.notifyDataSetChanged();
        lvConversation.setSelection(smsList.size() -1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_send_sms:
                sendSms(phoneNumber);
                Toast.makeText(this,"da gui rrrrrrrr",Toast.LENGTH_LONG).show();

                break;
        }

    }

    public static class BroadCastSentSms extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case ERROR:
                        switch (getResultCode()){
                            case Activity.RESULT_OK:
                                Toast.makeText(context, "SMS sent",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                                Toast.makeText(context, "Generic failure",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case SmsManager.RESULT_ERROR_NO_SERVICE:
                                Toast.makeText(context, "No service",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case SmsManager.RESULT_ERROR_NULL_PDU:
                                Toast.makeText(context, "Null PDU",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case SmsManager.RESULT_ERROR_RADIO_OFF:
                                Toast.makeText(context, "Radio off",
                                        Toast.LENGTH_SHORT).show();
                                break;
                        }
                    break;
                case DELIVERED:
                    switch (getResultCode()){
                        case Activity.RESULT_OK:
                            Toast.makeText(context, "Success SMS",
                                    Toast.LENGTH_LONG).show();
                            break;
                        case Activity.RESULT_CANCELED:
                            Toast.makeText(context, "SMS not delivered",
                                    Toast.LENGTH_LONG).show();
                            break;
                    }
                     break;
            }
        }
    }

}
