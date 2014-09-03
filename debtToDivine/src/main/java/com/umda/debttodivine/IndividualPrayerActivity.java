package com.umda.debttodivine;

import static com.umda.debttodivine.Constants.TYPE_FASTING;
import static com.umda.debttodivine.Constants.TYPE_PRAYER;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
//import static com.umda.debttodivine.Constants.*;

public class IndividualPrayerActivity extends ListActivity  {
	static final int TEXT_DIALOG_ID = 0;
	public String prayer;
	public Profile myProfile;
	public PrayerArrayAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.indiv_main);
	}
	@Override
	public void onResume(){
		super.onResume();
		//this.myProfile = getIntent().getExtras().getParcelable(Constants.PROFILE_OBJECT);
		this.myProfile = Settings.loadProfile(this,Settings.getProfile(this));
		if  (this.myProfile != null) {
			String[] values = Settings.getPrayerArray(this.myProfile.madhhab, this);
			adapter = new PrayerArrayAdapter(this, values);
			setListAdapter(adapter);
		}
	}
	@Override
	public void onPause(){
		super.onPause();
		Settings.storeProfile(this, this.myProfile);
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
		    case R.id.settings:
		    	intent = new Intent(this, Settings.class);
		    	//intent.putExtras(b);
		    	startActivity(intent);
		    	return true;
		    case R.id.logs:
		    	if (Settings.isPro(this)){
		    		intent = new Intent(this, LogsActivity.class);
			    	startActivity(intent);
		    	}else{
		    		toast("Please purchase the PRO version to unlock this feature.");
		    	}
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
    
    @Override
    protected Dialog onCreateDialog(int id) {
    	switch (id) {
	        case TEXT_DIALOG_ID:
	        	EditText dialogText = new EditText(this);
	        	dialogText.setId(998);
	        	dialogText.setInputType(InputType.TYPE_CLASS_NUMBER);
	        	String xtitle = getResources().getString(R.string.alert_dialog_days_complete);
	        	AlertDialog alert = new AlertDialog.Builder(this)
	            .setTitle(this.prayer+" "+xtitle)
	            .setView(dialogText)
	            .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	String str = ((EditText)((Dialog)dialog).findViewById(998)).getText().toString();
	                	if (str.trim().equals("")) {
	                		return;
	                	}
	                	int xvalue = Integer.parseInt(str);
	                	int xdiff = xvalue - myProfile.getPrayerComplete(prayer);
	                	myProfile.updPrayerDiff(getBaseContext(), prayer, xdiff, true);
	                	if  (myProfile.getRemainingPrayer(prayer) == 0) {
	    		    		toast("Congratulations on paying your "+prayer+" debt!");
	                	}
	                	adapter.notifyDataSetChanged();
	                }
	            })
	            .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	dialog.dismiss();
	                }
	            })
	            .create();
	            return alert;
        }
        return null;
    }
    @Override
    protected void onPrepareDialog(int id, Dialog dialog){
    	super.onPrepareDialog(id, dialog);
    	switch (id) {
        case TEXT_DIALOG_ID:
        	((EditText)dialog.findViewById(998)).setText("");
        }
    }
    private void toast(String message){
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
    
}