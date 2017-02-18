package com.example.mammam.classicmessage;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mammam.classicmessage.controllers.ManagerSms;
import com.example.mammam.classicmessage.custom.MyViewGroup;
import com.example.mammam.classicmessage.models.MySms;
import com.example.mammam.classicmessage.ui.SecondActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mam  Mam on 10/31/2016.
 */

public class MyService extends Service implements View.OnClickListener {

    private static final String KEY_PHONE = "PHONE_NUMBER";
    private static final String KEY_NAME = "NAME";
    private WindowManager mWindowManager;
    private ManagerSms mManagerSms;
    private WindowManager.LayoutParams mParams;
    private MyViewGroup mViewgroup1;
    private MyViewGroup myViewGroup2;
    private Button btnCloseSms;
    private Button btnViewApp;
    private Button btnReplyFast;
    private TextView tvSender;
    private TextView tvReceivedDate;
    private TextView tvReceivedBody;
    private Button btnSend;
    private EditText edtSend;
    private TextView tvSender1;
    private static final String PHONE = "PHONE";
    private static final String NAME = "NAME";
    private static final String BODY = "BODY";
    private String name;
    private String body;
    private String phoneNumber;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        intiViews(intent);
       // Intent intent1 = new Intent("aaa");
        return START_NOT_STICKY;

    }

    private void intiViews(Intent intent) {
        mManagerSms = new ManagerSms(this);
        mWindowManager= (WindowManager) getSystemService(WINDOW_SERVICE);
        mViewgroup1 = new MyViewGroup(this);
        View viewSMS= View.inflate(this,R.layout.sms_layout,mViewgroup1);
        btnCloseSms = (Button) viewSMS.findViewById(R.id.btnClosePopup);
        btnReplyFast = (Button) viewSMS.findViewById(R.id.btnReplyFast);
        btnViewApp = (Button) viewSMS.findViewById(R.id.btn_viewApp);
        tvReceivedDate = (TextView) viewSMS.findViewById(R.id.tvSmsReceivedDate);
        tvSender = (TextView) viewSMS.findViewById(R.id.tvSender);
        tvReceivedBody = (TextView) viewSMS.findViewById(R.id.tvSmsReceivedBody);

        btnCloseSms.setOnClickListener(this);
        btnReplyFast.setOnClickListener(this);
        btnViewApp.setOnClickListener(this);

        WindowManager.LayoutParams params = mParams =
                new WindowManager.LayoutParams();
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.height =WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        mParams.flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        mParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        mWindowManager.addView(mViewgroup1,mParams);
        String body = intent.getStringExtra(BODY);
        String name = intent.getStringExtra(NAME);
        phoneNumber = intent.getStringExtra(PHONE);
        tvSender.setText(name);
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tvReceivedDate.setText(format.format(date));
        tvReceivedBody.setText(body);

        myViewGroup2 = new MyViewGroup(this);
        View viewRep = View.inflate(this,R.layout.sms_send_layput,myViewGroup2);
        btnSend = (Button) viewRep.findViewById(R.id.btn_send1);
        edtSend = (EditText) viewRep.findViewById(R.id.edt_send1);
        tvSender1 = (TextView) viewRep.findViewById(R.id.tvSender1);
        btnSend.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnClosePopup:
                mWindowManager.removeView(mViewgroup1);
                onDestroy();
                break;
            case R.id.btn_send1:
                String body = edtSend.getText().toString();
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber,null,body,null,null);
                mWindowManager.removeView(myViewGroup2);
                break;

            case R.id.btnReplyFast:
                mWindowManager.removeView(mViewgroup1);
                mWindowManager.addView(myViewGroup2,mParams);
                break;
            case R.id.btn_viewApp:
                mWindowManager.removeView(mViewgroup1);
             //   mManagerSms.insertSms(new MySms());
                Intent intent = new Intent(this,SecondActivity.class);
                Bundle bundle  = new Bundle();
                //bundle.putString();
                break;
        }
    }

    public static class SmsBroadCast extends BroadcastReceiver{

        private static final String PHONE = "PHONE";
        private static final String NAME = "NAME";
        private static final String BODY = "BODY";
        private ManagerSms mManagerSms;

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            Object[] pdus = (Object[]) bundle.get("pdus");
            SmsMessage[] msgs = new SmsMessage[pdus.length];
            String str = "";
            String phoneNumber = "";

            // For every SMS message received
            for (int i=0; i < msgs.length; i++) {
                // Convert Object array
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                // Sender's phone number
                phoneNumber +=  msgs[i].getOriginatingAddress();
                // Fetch the text message
                str += msgs[i].getMessageBody().toString();
                str += "\n";
            }
            if(phoneNumber.startsWith("+84")){
                phoneNumber = phoneNumber.replace("+84","0");
            }

            String name = ManagerSms.getNameFromContact(phoneNumber,context);
            if(null == name){
                name = phoneNumber;
            }
            Intent intent1 = new Intent(context,MyService.class);
            Bundle bundle1 = new Bundle();
            bundle1.putString(PHONE,phoneNumber);
            bundle1.putString(BODY,str);
            bundle1.putString(NAME,name);
            intent1.putExtras(bundle1);
            mManagerSms = new ManagerSms(context);
            mManagerSms.insertSms(new
                    MySms(str,phoneNumber,name,"1","1",new SimpleDateFormat().
                    format(new Date())));

            context.startService(intent1);

        }
    }
}
