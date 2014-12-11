package com.example.doit4me;


import java.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddReminder extends Activity 
{
	EditText msgTo,msgTime,msgDate,msgTitle,msgText;
	CheckBox msgSet;
	TextView errormsg;
	Boolean isOnce;
	
	static final int PICK_CONTACT_REQUEST = 1;
	private static final int CONTACT_PICKER_RESULT = 1001;
	private int year,month,day,hour,minute,myear,mmonth,mday,mhour,mminute;
	static final int DATE_PICKER_ID = 1111;
	static final int TIME_DIALOG_ID = 2222; 
    String contactNo,contactName;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_reminder);
		
		msgTitle=(EditText)findViewById(R.id.editText1);
		msgTo=(EditText)findViewById(R.id.editText2);
		msgDate=(EditText)findViewById(R.id.editText3);
		msgTime=(EditText)findViewById(R.id.editText4);
		msgText=(EditText)findViewById(R.id.editText5);
		msgSet=(CheckBox)findViewById(R.id.checkBox1);
		
		// Get current date by calender
		
				final Calendar c = Calendar.getInstance();
				year  = c.get(Calendar.YEAR);
				month = c.get(Calendar.MONTH);
				day   = c.get(Calendar.DAY_OF_MONTH);
				hour = c.get(Calendar.HOUR_OF_DAY);
			    minute = c.get(Calendar.MINUTE);
			         
			    myear=year; mmonth=month; mday=day; mhour=hour; mminute=minute;
			    // set current time into output textview
			    updateTime(hour, minute);
			         
				// Show current date
				
				msgDate.setText(new StringBuilder()
						// Month is 0 based, just add 1
						.append(month + 1).append("-").append(day).append("-")
						.append(year).append(" "));
		 
				// Button listener to show date picker dialog
				
				msgTime.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						showDialog(TIME_DIALOG_ID);
					}
				});
				msgTime.setOnFocusChangeListener(new OnFocusChangeListener() {
					
					@Override
					public void onFocusChange(View arg0, boolean arg1) {
						// TODO Auto-generated method stub
						if(arg1)
							showDialog(TIME_DIALOG_ID);
					}
				});
				
				msgDate.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
		                
						// On button click show datepicker dialog 
						showDialog(DATE_PICKER_ID);

					}

				});
				msgDate.setOnFocusChangeListener(new OnFocusChangeListener() {
					
					@Override
					public void onFocusChange(View arg0, boolean arg1) {
						// TODO Auto-generated method stub
						if(arg1)
							showDialog(DATE_PICKER_ID);
					}
				});

		msgTo.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if(arg1)
					seeContacts(findViewById(R.id.editText2));
			}
		});
	}
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_PICKER_ID:
			
			// open datepicker dialog. 
			// set date picker for current date 
			// add pickerListener listner to date picker
			return new DatePickerDialog(this, pickerListener, year, month,day);
			
		case TIME_DIALOG_ID:
            
            // set time picker as current time
            return new TimePickerDialog(this, timePickerListener, hour, minute,false);
 
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		@Override
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			
			year  = selectedYear;
			month = selectedMonth;
			day   = selectedDay;

			myear  = selectedYear;
			mmonth = selectedMonth;
			mday   = selectedDay;
			// Show selected date 
			msgDate.setText(new StringBuilder().append(month + 1)
					.append("-").append(day).append("-").append(year)
					.append(" "));
	
		   }
	    };
	    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
	         
	    	 
	        @Override
	        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
	            // TODO Auto-generated method stub
	            hour   = hourOfDay;
	            minute = minutes;
	 
	            mhour   = hourOfDay;
	            mminute = minutes;
	            
	            updateTime(hour,minute);
	             
	         }
	 
	    };
	    private static String utilTime(int value) {
	         
	        if (value < 10)
	            return "0" + String.valueOf(value);
	        else
	            return String.valueOf(value);
	    }
	    // Used to convert 24hr format to 12hr format with AM/PM values
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
	 
	          msgTime.setText(aTime);
	    }

	public void seeContacts(View v)
	{
		Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
	    pickContactIntent.setType(Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
	    startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
	}
	@Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {

		 if (reqCode == PICK_CONTACT_REQUEST) {
		        // Make sure the request was successful
		        if (resultCode == RESULT_OK) {
		            // Get the URI that points to the selected contact
		            Uri contactUri = data.getData();
		            // We only need the NUMBER column, because there will be only one row in the result
		            String[] projection = {Phone.NUMBER,Phone.DISPLAY_NAME};

		            // Perform the query on the contact to get the NUMBER column
		            // We don't need a selection or sort order (there's only one result for the given URI)
		            // CAUTION: The query() method should be called from a separate thread to avoid blocking
		            // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
		            // Consider using CursorLoader to perform the query.
		            Cursor cursor = getContentResolver()
		                    .query(contactUri, projection, null, null, null);
		            cursor.moveToFirst();

		            // Retrieve the phone number from the NUMBER column
		            int column = cursor.getColumnIndex(Phone.NUMBER);
		            int column1 = cursor.getColumnIndex(Phone.DISPLAY_NAME);
		            contactNo=cursor.getString(column);
		            contactName=cursor.getString(column1);
		            msgTo.setText(contactName);
		            // Do something with the phone number...
		        }
		    }
    }
	public void addReminder(View v)
	{	
		Calendar current=Calendar.getInstance();
		
		Calendar c=Calendar.getInstance();
		c.set(Calendar.MONTH,month);
		c.set(Calendar.YEAR, year);
		c.set(Calendar.DAY_OF_MONTH, day);
		c.set(Calendar.HOUR_OF_DAY,hour);
		c.set(Calendar.MINUTE,minute);
		c.set(Calendar.SECOND,0);
		
		String errMsg="";
		if(msgTitle.getText().toString().trim().length()==0 || msgTo.getText().toString().trim().length()==0 || msgTime.getText().toString().trim().length()==0 || msgDate.getText().toString().trim().length()==0 || msgText.getText().toString().trim().length()==0)
		{
			errMsg="All fields are required.\n";
		}
		else if(c.compareTo(current)<0){
			errMsg="Invalid date or time.\n";
		}
		if(errMsg.length()>0)
		{
			Toast.makeText(this,"ERROR : \n"+errMsg, Toast.LENGTH_LONG).show();
		}
		else
		{
			setAlarm(c);
		}
	}
	
	public void setAlarm(Calendar c)
	{
		int r=(msgSet.isChecked())?1:0;
		try{
			
			String rep="";
			final int alarmid=(int)System.currentTimeMillis();
			
			SQLiteDatabase db=null;
			db=this.openOrCreateDatabase("doitforme_db.db",MODE_PRIVATE, null);
			db.execSQL("DROP TABLE reminders_tb");
			db.execSQL("CREATE TABLE IF NOT EXISTS reminders_tb (alarmid int,msg_title varchar,toName varchar,toNumber varchar,msg_date varchar,msg_time varchar,msg_text varchar,isRepeat int(1),isOn int(1));");
			
			db.execSQL("INSERT INTO reminders_tb VALUES ("+alarmid+"'"+msgTitle.getText().toString()+"','"+contactName+"','"+contactNo+"','"+mday +"/"+mmonth +"/"+myear+"','"+mhour+":"+mminute+"','"+msgText.getText().toString()+"',"+ r+",1);");
			//Toast.makeText(this, "INSERT INTO reminders_tb VALUES ("+alarmid+"'"+msgTitle.getText().toString()+"','"+contactName+"','"+contactNo+"','"+mday +"/"+mmonth +"/"+myear+"','"+mhour+":"+mminute+"','"+msgText.getText().toString()+"',"+ r+",1)", Toast.LENGTH_LONG).show();			db.close();
		
		
		Intent timerIntent=new Intent(this,MyReceiver.class);
		PendingIntent pi=PendingIntent.getBroadcast(AddReminder.this,alarmid, timerIntent, 0);
		
		AlarmManager am=(AlarmManager)getSystemService(ALARM_SERVICE);
		
		if(msgSet.isChecked())
		{
			am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pi);
			rep="Daily";
		}
		else
		{
			am.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(), pi);
		}
		Toast.makeText(this,"Alarm set- "+mhour+":"+mminute+" "+mday+"/"+mmonth+"/"+myear+rep , Toast.LENGTH_SHORT).show();
		gotoMain();
		}catch(Exception e){ Toast.makeText(this,"Some error occurred!",Toast.LENGTH_LONG).show();}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_reminder, menu);
		return true;
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
}
