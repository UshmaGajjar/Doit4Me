package com.example.doit4me;

import java.util.Calendar;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.SmsManager;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
	
	SQLiteDatabase db;
	private int year,month,day,hour,minute;
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		
		Calendar c=Calendar.getInstance();
		year  = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day   = c.get(Calendar.DAY_OF_MONTH);
		hour = c.get(Calendar.HOUR_OF_DAY);
	    minute = c.get(Calendar.MINUTE);
	    
	  try{
	 
		db=arg0.openOrCreateDatabase("doitforme_db.db",0,null);
		Cursor cursor=db.rawQuery("SELECT * FROM reminders_tb WHERE msg_time and msg_date;",null);
		
		if(cursor.getCount()>0)
		{
			cursor.moveToFirst();
		
			do{
				
				int mto=cursor.getColumnIndex("toName");
				String emto =cursor.getString(mto);
				
				int mno=cursor.getColumnIndex("toNumber");
				String eno=cursor.getString(mto);
				
				int mtext=cursor.getColumnIndex("msg_text");
				String etext=cursor.getString(mtext);
				
				PendingIntent pendingIntent=PendingIntent.getActivity(arg0,0,new Intent(arg0,MyReceiver.class),0);
				
				SmsManager sms=SmsManager.getDefault();
				
				sms.sendTextMessage(eno,null,etext, pendingIntent, null);
				
				Toast.makeText(arg0,"Message sent to : "+emto ,Toast.LENGTH_SHORT).show();
			}while(cursor.moveToNext());
		}
	 }catch(Exception e){ Toast.makeText(arg0,"Some error occurred!",Toast.LENGTH_LONG).show();}
	}

}
