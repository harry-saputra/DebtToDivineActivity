package com.umda.debttodivine;

import static com.umda.debttodivine.Constants.TYPE_FASTING;
import static com.umda.debttodivine.Constants.TYPE_PRAYER;

import com.umda.debttodivine.util.IabHelper;
import com.umda.debttodivine.util.IabResult;
import com.umda.debttodivine.util.Inventory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DebtToDivineActivity extends Activity {
    /** Called when the activity is first created. */
	static final int TEXT_DIALOG_ID = 0;
	public static final String KEY_PROFILE = "com.umda.debttodivine.ProfileName";
	public Profile myProfile;
	public EditText dialogText = null;
	private View deadline_view;
	private TextView lblDaysTill;
	private TextView lblDaysPer;
	private TextView lblProfile;
	private TextView lblType;
    private TextView txtDaysComplete;
    private TextView txtDaysRemain;
    private Button butPlus;
	private TextProgressBar prgProgress;
	
	private IabHelper mHelper;
	private boolean mIsUnlocked = false;
	private String mSku = "com.umda.debttodivine.unlockpro";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        String base64EncodedPublicKey = Settings.getKey();
		mHelper = new IabHelper(this, base64EncodedPublicKey);
		
		//Turn off for production
		mHelper.enableDebugLogging(false);
		
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
		   public void onIabSetupFinished(IabResult result) {
		      if (!result.isSuccess()) {
		         return;
		      }
		      mHelper.queryInventoryAsync(mGotInventoryListener);
		   }
		});
    }
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            if (result.isFailure()) {
                return;
            }
            mIsUnlocked = inventory.hasPurchase(mSku);            
            if (mIsUnlocked){
            	// Update the shared preferences so that we don't perform
                // a RestoreTransactions again.
                Settings.setPro(DebtToDivineActivity.this, true);
            }else{
            	Settings.setPro(DebtToDivineActivity.this, false);
            }
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onResume(){
    	super.onResume();
    	
		
    	this.myProfile = Settings.loadProfile(this,Settings.getProfile(this));
    	toast("Welcome "+this.myProfile.name+"!");
    	
    	findViews();
    	setListeners();
        
        txtDaysComplete.setText(this.myProfile.debtComplete.toString());
    	txtDaysRemain.setText(this.myProfile.getRemaining().toString()); 
    	lblProfile.setText(this.myProfile.name);
    	lblType.setText(this.myProfile.type);
    	
    	if (this.myProfile.hasDeadline){
    		deadline_view.setVisibility(View.VISIBLE);
    		lblDaysTill.setText(this.myProfile.getDaysTill().toString());
        	lblDaysPer.setText(this.myProfile.getDaysPer().toString());
        }else{
    		deadline_view.setVisibility(View.GONE);
    	}

    }
    @Override
    public void onPause(){
    	super.onPause();
    	Settings.storeProfile(this, this.myProfile);
    }
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	if (mHelper != null) mHelper.dispose();
        mHelper = null;
    	
    }
    private void findViews() {
    	butPlus = (Button) findViewById(R.id.but_plus);
    	txtDaysComplete = (TextView) findViewById(R.id.txt_days_complete);
    	txtDaysRemain = (TextView) findViewById(R.id.txt_days_remain);
    	prgProgress = (TextProgressBar) findViewById(R.id.prog_remain);
    	lblProfile = (TextView) findViewById(R.id.lbl_profile_progress);
    	lblType = (TextView) findViewById(R.id.lbl_profile_type);
    	lblDaysTill = (TextView) findViewById(R.id.days_till);
    	lblDaysPer = (TextView) findViewById(R.id.days_per);
    	deadline_view = (View) findViewById(R.id.layout_deadline);
    }
    @Override
    protected Dialog onCreateDialog(int id) {
    	switch (id) {
	        case TEXT_DIALOG_ID:
	        	EditText dialogText = new EditText(this);
	        	dialogText.setId(999);
	        	dialogText.setInputType(InputType.TYPE_CLASS_NUMBER);
	        	AlertDialog alert = new AlertDialog.Builder(this)
	            .setTitle(R.string.alert_dialog_days_complete)
	            .setView(dialogText)
	            .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	String xstr = ((EditText)((Dialog)dialog).findViewById(999)).getText().toString();
	                	if (xstr.trim().equals("")) {
	                		txtDaysComplete.setText("0");
	                		myProfile.updProgress(getBaseContext(),0,true);
	                		return;
	                	}
	                	int value = Integer.parseInt(xstr);
	                	if (myProfile.getTotalDebt() - value < 0){
	    	    			toast("Error: Cannot pay back more than you owe");
	    	    			return;
	    	    		}else{
	    	    			myProfile.updProgress(getBaseContext(),value,true);
	    	    			txtDaysRemain.setText(myProfile.getRemaining().toString());
	    	    		}
	                	txtDaysComplete.setText(myProfile.debtComplete.toString());
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
    	findViews();
    	switch (id) {
        case TEXT_DIALOG_ID:
        	((EditText)dialog.findViewById(999)).setText(this.myProfile.debtComplete.toString());
        }
    }
    
    private void setListeners() {
    	//butPlus.setOnClickListener(this);
    	if (txtDaysComplete != null) {
    		txtDaysComplete.setOnClickListener(new EditText.OnClickListener() {
				//@Override
				public void onClick(View v) {
					showDialog(TEXT_DIALOG_ID);
				}
			});
		}
    	txtDaysComplete.addTextChangedListener(new TextWatcher() {
    		public void onTextChanged(CharSequence s, int start, int before, int count) {
    			if  (myProfile.getRemaining() == 0 && myProfile.getTotalDebt() > 0) {
    	    		toast("Congratulations on paying your debt to the Divine!");
    	    		butPlus.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue_tick));
    	        }else{
    	           	butPlus.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue_plus));
    	        }
    		}
			//@Override
			public void afterTextChanged(Editable s) {}
			//@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    	});
    	
    	txtDaysRemain.addTextChangedListener(new TextWatcher() {
    		public void onTextChanged(CharSequence s, int start, int before, int count) {
	    		if  (myProfile.hasDeadline) lblDaysPer.setText(myProfile.getDaysPer().toString());
	    		updateProgress();
    		}
			//@Override
			public void afterTextChanged(Editable s) {}
			//@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    	});
    }
    /*
    @Override
    protected void onSaveInstanceState(Bundle bundle){
    	super.onSaveInstanceState(bundle);
    }
    @Override
    protected void onRestoreInstanceState(Bundle bundle){
    	//findViews();
    	//updateProgress();
    	super.onRestoreInstanceState(bundle);
    	
    }
    */
    public void butPlusClick(View view){
    	int value = this.myProfile.debtComplete + this.myProfile.increment;
    	this.myProfile.updProgress(this, value,true);
		txtDaysRemain.setText(this.myProfile.getRemaining().toString());
    	txtDaysComplete.setText(this.myProfile.debtComplete.toString());
    	if (this.myProfile.getRemaining() == 0 && myProfile.getTotalDebt() > 0){
    		toast("Congratulations on paying your debt to the Divine!");
		}
    }
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
    	Intent intent;
		switch (item.getItemId()) {
		    case R.id.settings:
		    	intent = new Intent(this, Settings.class);
		    	startActivity(intent);
		    	return true;
		    case R.id.indiv:
		    	if (Settings.isPro(this)){
		    		intent = new Intent(this, IndividualPrayerActivity.class);
			    	startActivity(intent);
		    	}else{
		    		toast("Please purchase the PRO version to unlock this feature.");
		    	}
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
	    }
	    return false;
    }
    /*
    public int getProgPercent(){
    	double xpercent = 0;
    	int xtotal = this.myProfile.getTotalDebt();
    	int xcount = this.myProfile.debtComplete;
    	if  (xtotal!=0)	xpercent = Math.floor((((double)xcount/(double)xtotal)*100));
    	return (int)xpercent;
    }
    */
    public void updateProgress(){
    	int xprog = this.myProfile.getTotalPercent();//getProgPercent();
    	int xtotal = this.myProfile.getTotalDebt();
    	int xcount = this.myProfile.debtComplete;
    	prgProgress.setMax(xtotal);
    	prgProgress.setProgress(xcount);
    	prgProgress.setText(String.valueOf(xprog)+"%");
    	prgProgress.setTextSize(20f);
    	if (xprog < 40){
    		Rect bounds = prgProgress.getProgressDrawable().getBounds();
        	prgProgress.setProgressDrawable(getResources().getDrawable(R.drawable.red_progress));
        	prgProgress.getProgressDrawable().setBounds(bounds);
        	prgProgress.getProgressDrawable().setLevel(xprog*100);
    	}else if(xprog < 60){
    		Rect bounds = prgProgress.getProgressDrawable().getBounds();
        	prgProgress.setProgressDrawable(getResources().getDrawable(R.drawable.orange_progress));
        	prgProgress.getProgressDrawable().setBounds(bounds);
        	prgProgress.getProgressDrawable().setLevel(xprog*100);
    	}else if(xprog < 90){
    		Rect bounds = prgProgress.getProgressDrawable().getBounds();
        	prgProgress.setProgressDrawable(getResources().getDrawable(R.drawable.yellow_progress));
        	prgProgress.getProgressDrawable().setBounds(bounds);
        	prgProgress.getProgressDrawable().setLevel(xprog*100);
    	}else{
    		Rect bounds = prgProgress.getProgressDrawable().getBounds();
        	prgProgress.setProgressDrawable(getResources().getDrawable(R.drawable.green_progress));
        	prgProgress.getProgressDrawable().setBounds(bounds);
        	prgProgress.getProgressDrawable().setLevel(xprog*100);
    	}
    }
    private void toast(String message){
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	}
}