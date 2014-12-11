package com.example.doit4me;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import android.R.bool;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast; 
import android.widget.ToggleButton;
import android.view.View.OnClickListener;


public class MainActivity extends Activity{


	ArrayList<String> lst;
	ArrayAdapter<String> aa;
	ArrayList<String> tlst;
	
	ExpandableListAdapterDemo listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    
    int itemselected;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        
        tlst=new ArrayList<String>();
        // preparing list data
        prepareListData();
 
        listAdapter = new ExpandableListAdapterDemo(this, listDataHeader, listDataChild);
 
        // setting list adapter
        expListView.setAdapter(listAdapter);
       
        //setListAdapter(listAdapter);
        registerForContextMenu(expListView);

	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	    ContextMenuInfo menuInfo) {
	  if (v.getId()==R.id.lvExp) {
		  ExpandableListView.ExpandableListContextMenuInfo info=
		            (ExpandableListView.ExpandableListContextMenuInfo)menuInfo;
		   // int type=ExpandableListView.getPackedPositionType(info.packedPosition);
		  int type=ExpandableListView.getPackedPositionGroup(info.packedPosition);
		    Log.e("type",""+type);
		    itemselected=type;
	  //  AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
	    menu.setHeaderTitle("Action");
	    String[] menuItems =new String[]{"Edit","Cancel"};
	    for (int i = 0; i<menuItems.length; i++) {
	      menu.add(Menu.NONE, i, i, menuItems[i]);
	    }
	  }
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	  //AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
	  int menuItemIndex = item.getItemId();
	  String[] menuItems =new String[]{"Edit","Cancel"};
	  String menuItemName = menuItems[menuItemIndex];
	  //String listItemName = Countries[info.position];

	  switch(menuItemIndex)
	  {
	  case 0:
		  Intent i=new Intent(this,EditActivity.class);
		  i.putExtra("msgtitle",tlst.get(itemselected));
		  this.startActivity(i);
		  break;
		  
	  case 1:
		  break;
		  
      default:
    	  break;
	  }
	 
	 
	  return true;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void addNew(View v)
	{
		Intent i =new Intent(this,AddReminder.class);
		startActivity(i);
	}

	private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
 
      try{
		SQLiteDatabase db=null;
		db=this.openOrCreateDatabase("doitforme_db.db",MODE_PRIVATE, null);
		//db.execSQL("CREATE TABLE IF NOT EXISTS reminders_tb (msg_title varchar,toName varchar,toNumber varchar,msg_date varchar,msg_time varchar,msg_text varchar,isRepeat int(1));");
		
		Cursor c=db.rawQuery("SELECT * FROM reminders_tb",null);
	
		if(c.getCount()>0)
		{
			int i=0;
			c.moveToFirst();
			do{
				int mtitle=c.getColumnIndex("msg_title");
				String stitle=c.getString(mtitle);
				listDataHeader.add(stitle);
				
				tlst.add(stitle);
				
				int mdate=c.getColumnIndex("msg_date");
				String sdate=c.getString(mdate);
				
				int mtime=c.getColumnIndex("msg_time");
				String stime=c.getString(mtime);
				String[] s=stime.split(":");
				stime=updateTime(Integer.parseInt(s[0]), Integer.parseInt(s[1]));
				
				String datetime="DATE TIME - "+sdate+" "+stime;
				
				int mto=c.getColumnIndex("toName");
				String sname="TO : "+c.getString(mto);
				
				int mtext=c.getColumnIndex("msg_text");
				String stext="TEXT : "+c.getString(mtext);
				
				
				int misrepeat=c.getColumnIndex("isRepeat");
				String isrepeat=(c.getInt(misrepeat)==1)?"Repeat - ":"No repeat";
				
				int mison=c.getColumnIndex("isOn");
				String ison=(c.getInt(mison)==1)?"ON":"OFF";
				
				String comb="REMINDER : "+isrepeat+" - "+ison;
				
				List<String> subitems = new ArrayList<String>();
				subitems.add(sname);
				subitems.add(datetime);
				subitems.add(stext);
				subitems.add(comb);
				
				
				listDataChild.put(listDataHeader.get(i), subitems); // Header, Child data
				 
				i++;
			}while(c.moveToNext());
			db.close();
		}
		else
			Toast.makeText(this,"There are no reminders", Toast.LENGTH_SHORT).show();
		
		
	}catch(Exception e){ Toast.makeText(this,"Some error occurred!",Toast.LENGTH_LONG).show();}
	
    }
	  private String updateTime(int hours, int mins) {
	         
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
	 
	          return aTime;
	    }

}


