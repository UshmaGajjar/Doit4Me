package com.example.doit4me;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends Activity {

	EditText etitle,etime,edate,eto,etext;
	CheckBox cbRepeat,cbOn;
	String eno;
	TextView errormsg;
	Boolean isRepeat,isOn;
	
	static final int PICK_CONTACT_REQUEST = 1;
	private static final int CONTACT_PICKER_RESULT = 1001;
	private int year,month,day,hour,minute,myear,mmonth,mday,mhour,mminute;
	static final int DATE_PICKER_ID = 1111;
	static final int TIME_DIALOG_ID = 2222; 
    String contactNo,contactName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);
		
		etitle=(EditText)findViewById(R.id.editText1);
		eto=(EditText)findViewById(R.id.editText2);
		edate=(EditText)findViewById(R.id.editText3);
		etime=(EditText)findViewById(R.id.editText4);
		etext=(EditText)findViewById(R.id.editText5);
		
		cbRepeat=(CheckBox)findViewById(R.id.checkBox1);
		cbOn=(CheckBox)findViewById(R.id.checkBox2);
		
		final Calendar cal = Calendar.getInstance();
		year  = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH);
		day   = cal.get(Calendar.DAY_OF_MONTH);
		hour = cal.get(Calendar.HOUR_OF_DAY);
	    minute = cal.get(Calendar.MINUTE);
	         
	    myear=year; mmonth=month; mday=day; mhour=hour; mminute=minute;
		
	    updateTime(hour, minute);
	    
		Intent i=getIntent();
		
		String msgtitle=i.getStringExtra("msgtitle");
		
		try{
			SQLiteDatabase db=null;
			db=this.openOrCreateDatabase("doitforme_db.db",MODE_PRIVATE, null);
			//db.execSQL("CREATE TABLE IF NOT EXISTS reminders_tb (msg_title varchar,toName varchar,toNumber varchar,msg_date varchar,msg_time varchar,msg_text varchar,isRepeat int(1));");
			
			Cursor c=db.rawQuery("SELECT * FROM reminders_tb WHERE msg_title=?",new String[]{msgtitle});
		
			if(c.getCount()==1)
			{
				c.moveToFirst();
				do{
					int mtitle=c.getColumnIndex("msg_title");
					etitle.setText(c.getString(mtitle));
					etitle.setEnabled(false);		
					
					int mdate=c.getColumnIndex("msg_date");
					edate.setText(c.getString(mdate));
					
					int mtime=c.getColumnIndex("msg_time");
					String sstime=c.getString(mtime);
					String[] s=sstime.split(":");
					updateTime(Integer.parseInt(s[0]), Integer.parseInt(s[1]));
					
					int mto=c.getColumnIndex("toName");
					eto.setText(c.getString(mto));
					
					int mno=c.getColumnIndex("toNumber");
					eno=c.getString(mto);
					
					int mtext=c.getColumnIndex("msg_text");
					etext.setText(c.getString(mtext));
					
					int misrepeat=c.getColumnIndex("isRepeat");
					if(c.getInt(misrepeat)==1)
						cbRepeat.setChecked(true);
					
					int mison=c.getColumnIndex("isOn");
					if(c.getInt(mison)==1)
						cbOn.setChecked(true);
					
					 
				}while(c.moveToNext());
				db.close();
			}
			else
				Toast.makeText(this,"hello", Toast.LENGTH_SHORT).show();
		}catch(Exception e){ Toast.makeText(this,"Some error occurred!",Toast.LENGTH_LONG).show();}
			
	}
	public void cancelIt(View v)
	{
		gotoMain();
	}
	public void gotoMain()
	{
		Intent i=new Intent(this,MainActivity.class);
		this.startActivity(i);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit, menu);
		return true;
	}
	private void updateTime(int hours, int mins) {
        
        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";
 
         
        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);
 
        // Append in a StringBuilder
         String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();
 
          etime.setText(aTime);
    }
}
