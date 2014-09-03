package com.umda.debttodivine;

import static android.provider.BaseColumns._ID;
import static com.umda.debttodivine.Constants.LOG_TABLE;
import static com.umda.debttodivine.Constants.LOG_AMOUNT_COL;
import static com.umda.debttodivine.Constants.LOG_PROFILE_COL;
import static com.umda.debttodivine.Constants.LOG_TYPE_COL;
import static com.umda.debttodivine.Constants.LOG_DATE_COL;

import static com.umda.debttodivine.Constants.TYPE_FASTING;
import static com.umda.debttodivine.Constants.TYPE_PRAYER;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
//import static com.umda.debttodivine.Constants.*;

public class LogsActivity extends ListActivity  {
	static final int TEXT_DIALOG_ID = 0;
	public Profile myProfile;
	private Cursor cursor;
	private DebtData debtdata;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logs_main);
		this.myProfile = Settings.loadProfile(this,Settings.getProfile(this));
		if  (this.myProfile != null) {
			if (debtdata == null) debtdata = new DebtData(this);
			
			SQLiteDatabase db = debtdata.getReadableDatabase();
			String[] FROM = {_ID,LOG_DATE_COL,LOG_TYPE_COL,LOG_AMOUNT_COL};
			String[] WHERE = {this.myProfile.name};
			
			cursor = db.query(LOG_TABLE, FROM, LOG_PROFILE_COL+"=?", WHERE, null, null, null);
			
			startManagingCursor(cursor);
			// THE DESIRED COLUMNS TO BE BOUND
			//String[] columns = new String[] { People.NAME, People.NUMBER };
			// THE XML DEFINED VIEWS WHICH THE DATA WILL BE BOUND TO
			int[] TO = new int[] { R.id.log_date, R.id.log_date, R.id.log_type, R.id.log_amount};
			// CREATE THE ADAPTER USING THE CURSOR POINTING TO THE DESIRED DATA AS WELL AS THE LAYOUT INFORMATION
			SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(this, R.layout.logs_entry, cursor, FROM, TO);
			// SET THIS ADAPTER AS YOUR LISTACTIVITY'S ADAPTER
			this.setListAdapter(mAdapter);
			//ListView listView = (ListView) findViewById(R.id.list);
			//listView.setAdapter(mAdapter);
		}
	}
	@Override
	public void onResume(){
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    if (debtdata != null) {
	    	debtdata.close();
	    }
	    if (cursor != null) {
	    	cursor.close();
	    }
	}
    /*
    public boolean onCreateOptionsMenu(Menu menu) {
	    super.onCreateOptionsMenu(menu);
	    MenuInflater inflater = getMenuInflater();
	    if (myProfile != null){
	    	if (myProfile.type.equalsIgnoreCase(TYPE_PRAYER)){
	    		inflater.inflate(R.menu.menu_prayer, menu);
	    	}else if (myProfile.type.equalsIgnoreCase(TYPE_FASTING)){
	    		inflater.inflate(R.menu.menu_fasting, menu);
	    	}else{
	    		inflater.inflate(R.menu.menu_fasting, menu);	
	    	}
	    	
	    }else{
	    	inflater.inflate(R.menu.menu_fasting, menu);
	    }
	    return true;
    }
    */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	super.onPrepareOptionsMenu(menu);
    	menu.clear();
	    MenuInflater inflater = getMenuInflater();
	    if (myProfile != null){
	    	if (myProfile.type.equalsIgnoreCase(TYPE_PRAYER)){
	    		inflater.inflate(R.menu.menu_prayer, menu);
	    	}else if (myProfile.type.equalsIgnoreCase(TYPE_FASTING)){
	    		inflater.inflate(R.menu.menu_fasting, menu);
	    	}else{
	    		inflater.inflate(R.menu.menu_fasting, menu);	
	    	}
	    	
	    }else{
	    	inflater.inflate(R.menu.menu_fasting, menu);
	    }
	    return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	//Bundle b = new Bundle();
		//b.putParcelable(Constants.PROFILE_OBJECT, this.myProfile);
    	Intent intent;
    	
		switch (item.getItemId()) {
			case R.id.indiv:
		    	intent = new Intent(this, IndividualPrayerActivity.class);
		    	startActivity(intent);
		    	return true;
	    	case R.id.settings:
		    	intent = new Intent(this, Settings.class);
		    	startActivity(intent);
		    	return true;
		    case R.id.share:
		    	intent = new Intent(android.content.Intent.ACTION_SEND);
		    	//sharingIntent.setType("application/pdf");
		    	intent.setType("text/plain");
		    	intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "[Debt to the Divine: Android] Tally for Profile: "+myProfile.name+" - "+myProfile.type);
		    	
		    	//Settings.shareTalliesPdf(myProfile, sharingIntent);
		    	Settings.shareTalliesText(this, myProfile, intent);
		    	startActivity(Intent.createChooser(intent, "Share via"));
		    	return true;
		    // More items go here (if any) ...
	    }
	    return false;
    }
    
    private void toast(String message){
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
    
}