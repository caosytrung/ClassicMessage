package com.example.mammam.classicmessage.controllers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.example.mammam.classicmessage.models.MySms;
import com.example.mammam.classicmessage.models.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mam  Mam on 10/28/2016.
 */

public class ManagerSms {
    public static final String DB_NAME= "SmsDb.sqlite";
    public static final String DB_PATH_SUFF = "/databases/";

    private Context mContext;
    private SimpleDateFormat format;
    private SQLiteDatabase mSqLite;

    public ManagerSms(Context context) {
        mContext = context;
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    }
    private void openDatabase(){
        mSqLite = mContext.openOrCreateDatabase(DB_NAME,Context.MODE_PRIVATE,null);
    }
    //Chi lay lan dau tien
    private List<MySms> getListMsmFromSystem() {
        List<MySms> mySmses = new ArrayList<>();
        Uri uriSms = Uri.parse("content://sms");
        String[] querySms = new String[]{
                "read",
                "address",
                "person",
                "date",
                "body",
                "type"
        };

        Cursor c = mContext.getContentResolver().
                query(uriSms, querySms, null, null, null);
        if (c.getCount() == 0) {
            return mySmses;
        }

        int indexRead = c.getColumnIndex(querySms[0]);
        int indexAddress = c.getColumnIndex(querySms[1]);
        int indexPerson = c.getColumnIndex(querySms[2]);
        int indexDate = c.getColumnIndex(querySms[3]);
        int indexBody = c.getColumnIndex(querySms[4]);
        int indexType = c.getColumnIndex(querySms[5]);

        while (c.moveToNext()) {
            String read = c.getString(indexRead);
            String address = c.getString(indexAddress);
            String name = getNameFromContact(address,mContext);
            Date date = new Date(c.getLong(indexDate));
            String body = c.getString(indexBody);
            String type = c.getString(indexType);
            String dateFormat= format.format(date);
            if(address.startsWith("+84")){
                address = address.replace("+84","0");
            }
            mySmses.add(new MySms(body,address,name,read,type,dateFormat));

        }
//        for(int i = 0 ; i < mySmses.size(); i++){
//            Log.d(getClass().getSimpleName(),mySmses.get(i).getBody() + " --" +
//                    mySmses.get(i).getPhoneNumber() + " --" +
//                    mySmses.get(i).getReadState() + " --" +
//                    mySmses.get(i).getType() + " --" +
//                    mySmses.get(i).getName() + " --" +
//                    mySmses.get(i).getDate()
//            );
//        }
        return mySmses;
    }


    public static String getNameFromContact(String phoneNumber,Context context) {

        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.
                        PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(phoneNumber)
        );
        Cursor cContact = resolver.query(uri,
                new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME},
                null, null, null);
        String name = null;
        if (cContact.moveToFirst()){
            name = cContact.getString(cContact.
                    getColumnIndex(ContactsContract
                            .PhoneLookup.DISPLAY_NAME));

        }
        cContact.close();
        return name;

    }
    private void closeDatabase(){
        mSqLite.close();
    }

    public void insertSms(MySms mySms){
        ContentValues values = new ContentValues();

        values.put("body",mySms.getBody());
        values.put("phoneNumber",mySms.getPhoneNumber());
        values.put("name",mySms.getName());
        values.put("readState",mySms.getReadState());
        values.put("type",mySms.getType());
        values.put("date",mySms.getDate());

        mSqLite.insert("Sms1",null,values);
    }
    public List<MySms> getListSmsFromDatabase(){
        openDatabase();
        List<MySms> mySmses = new ArrayList<>();
        String query = "SELECT * FROM Sms1";
        Cursor cSms = mSqLite.rawQuery(query,null);

        int indexBody = cSms.getColumnIndex("body");
        int indexPhonenumber = cSms.getColumnIndex("phoneNumber");
        int indexName = cSms.getColumnIndex("name");
        int indexReadState = cSms.getColumnIndex("readState");
        int indexType = cSms.getColumnIndex("type");
        int indexDate = cSms.getColumnIndex("date");
        cSms.moveToFirst();
        while (!cSms.isAfterLast()){
            String body = cSms.getString(indexBody);
            String phoneNumber = cSms.getString(indexPhonenumber);
            String name = cSms.getString(indexName);
            String readState = cSms.getString(indexReadState);
            String type = cSms.getString(indexType);
            String date = cSms.getString(indexDate);
            Log.d(getClass().getSimpleName(),body + " " + phoneNumber + " " + name + " " + readState + " " + type + " " + date + " " );
            mySmses.add(new MySms(body,phoneNumber,name,readState,type,date));
            cSms.moveToNext();

        }
        return mySmses;

    }
    public List<MySms> getListSmsFromPhoneNumber(String phoneN){
        openDatabase();
        List<MySms> mySmses = new ArrayList<>();
        String query = "SELECT * FROM Sms1 WHERE phoneNumber=" + "'"+phoneN+"'";
        Cursor cSms = mSqLite.rawQuery(query,null);

        int indexBody = cSms.getColumnIndex("body");
        int indexPhonenumber = cSms.getColumnIndex("phoneNumber");
        int indexName = cSms.getColumnIndex("name");
        int indexReadState = cSms.getColumnIndex("readState");
        int indexType = cSms.getColumnIndex("type");
        int indexDate = cSms.getColumnIndex("date");
        cSms.moveToFirst();
        while (!cSms.isAfterLast()){
            String body = cSms.getString(indexBody);
            String phoneNumber = cSms.getString(indexPhonenumber);
            String name = cSms.getString(indexName);
            String readState = cSms.getString(indexReadState);
            String type = cSms.getString(indexType);
            String date = cSms.getString(indexDate);
            // Log.d(getClass().getSimpleName(),body + " " + phoneNumber + " " + name + type + "  " + readState  );
            mySmses.add(new MySms(body,phoneNumber,name,readState,type,date));
            cSms.moveToNext();

        }
        return mySmses;
    }

    public void upDateSms(MySms mySms){
        openDatabase();
        ContentValues values = new ContentValues();
        values.put("body",mySms.getDate());
        values.put("phoneNumber",mySms.getPhoneNumber());
        values.put("name",mySms.getName());
        values.put("readState","1");
        values.put("type",mySms.getType());
        values.put("date",mySms.getDate());
        mSqLite.update("Sms1",values,"date=" + mySms.getDate(),null );
    }

    private void insertSmsFromSystem(){
        openDatabase();
        List<MySms> mySmses = getListMsmFromSystem();
        for(int i = 0 ; i < mySmses.size(); i++){
            String name = mySmses.get(i).getName();
            String body = mySmses.get(i).getBody();
            String phoneNumber = mySmses.get(i).getPhoneNumber();
            String readState = mySmses.get(i).getReadState();
            String date = mySmses.get(i).getDate();
            String type = mySmses.get(i).getType();

            ContentValues values = new ContentValues();

            values.put("body",body);
            values.put("phoneNumber",phoneNumber);
            values.put("name",name);
            values.put("readState",readState);
            values.put("type",type);
            values.put("date",date);

            mSqLite.insert("Sms1",null,values);
        }


    }
    public void copyDateBaseToSystem()  {
       // mContext.deleteDatabase(DB_NAME);
        File dbFile = mContext.getDatabasePath(DB_NAME);

        Toast.makeText(mContext,"CC",Toast.LENGTH_LONG).show();


        if(!dbFile.exists()){
            InputStream in = null;
            OutputStream out = null;



            try {
                in = mContext.getAssets().open(DB_NAME);
                String outFile = getPathSystem();


                File file = new File(mContext.getApplicationInfo().dataDir + DB_PATH_SUFF );
                if(!file.exists()){
                    file.mkdir();
                }
                out = new FileOutputStream(outFile);
                byte[] bytes = new byte[1024];
                int length ;
                while ((length = in.read(bytes)) > 0){
                    out.write(bytes,0,length);
                }
                out.flush();
                out.close();
                in.close();
                Toast.makeText(mContext,"ThanhCOng",Toast.LENGTH_LONG).show();
                Log.d(getClass().getSimpleName(),"cho du lieu");
                insertSmsFromSystem();


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public List<User> getUser(){
        openDatabase();
        List<User> users = new ArrayList<>();
        String query = "SELECT DISTINCT phoneNumber FROM Sms1 ORDER BY date DESC";
        Cursor cSms = mSqLite.rawQuery(query,null);
//        Cursor cSms = mSqLite.query(true,"Sms1",
//                new String[]{"name","phoneNumber"},
//                null,new String[]{"name","phoneNumber"},
//                null,null,null,null);
        int indexPhonenumber = cSms.getColumnIndex("phoneNumber");
       // int indexName = cSms.getColumnIndex("name");
        //   int indexDate = cSms.getColumnIndex("date");
        cSms.moveToFirst();
       // HashMap<String,String> map = new HashMap<>();
        List<String> strings = new ArrayList<>();
        while (!cSms.isAfterLast()){
            String phoneNumber = cSms.getString(indexPhonenumber);
        //    String name = cSms.getString(indexName);
        //    String date = cSms.getString(indexDate);

          //  Log.d(getClass().getSimpleName(), " " + phoneNumber + " " + name   + " " + date );
            strings.add(phoneNumber);
           // users.add(new User(name,phoneNumber, " "));
            cSms.moveToNext();

        }
        cSms.close();
        closeDatabase();
        openDatabase();


        for(int i = 0 ; i <strings.size() ; i++ ){
            String query1 = "SELECT name,body,date FROM Sms1 WHERE phoneNumber='" +
                    strings.get(i)  + "' ORDER BY date DESC LIMIT 1  ";
            Cursor c = mSqLite.rawQuery(query1,null);
            c.moveToFirst();
            String nName = c.getString(c.getColumnIndex("name"));
            String nBody = c.getString(c.getColumnIndex("body"));
            String nDate = c.getString(c.getColumnIndex("date"));
           // Log.d(getClass().getSimpleName(),a + b + cc + "  " +  strings.get(i));
            users.add(new User(nName,strings.get(i),nBody,nDate));
            c.close();
        }
        return users;
    }

    private String getPathSystem(){
        return mContext.getApplicationInfo().dataDir  +
                DB_PATH_SUFF + DB_NAME;
    }


}
