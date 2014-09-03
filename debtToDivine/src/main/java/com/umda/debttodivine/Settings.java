package com.umda.debttodivine;

import static android.provider.BaseColumns._ID;
import static com.umda.debttodivine.Constants.DAYS_IN_MONTH;
import static com.umda.debttodivine.Constants.DAYS_IN_YEAR;
import static com.umda.debttodivine.Constants.DEADLINE_COL;
import static com.umda.debttodivine.Constants.DEBT_COMPLETE_COL;
import static com.umda.debttodivine.Constants.DEBT_TOTAL_COL;
import static com.umda.debttodivine.Constants.GENDER_COL;
import static com.umda.debttodivine.Constants.HANAFI;
import static com.umda.debttodivine.Constants.HAS_DEADLINE_COL;
import static com.umda.debttodivine.Constants.INCREMENT_COL;
import static com.umda.debttodivine.Constants.MADHHAB_COL;
import static com.umda.debttodivine.Constants.MALE;
import static com.umda.debttodivine.Constants.MENST_AVG_COL;
import static com.umda.debttodivine.Constants.MENST_TOTAL_COL;
import static com.umda.debttodivine.Constants.PRAYER_COMPLETE_COL;
import static com.umda.debttodivine.Constants.PRAYER_ID_COL;
import static com.umda.debttodivine.Constants.PRAYER_TABLE;
import static com.umda.debttodivine.Constants.PROFILE_COL;
import static com.umda.debttodivine.Constants.PROFILE_ID_COL;
import static com.umda.debttodivine.Constants.PROFILE_TABLE;
import static com.umda.debttodivine.Constants.TYPE_COL;
import static com.umda.debttodivine.Constants.LOG_TABLE;
import static com.umda.debttodivine.Constants.LOG_ACTION_COL;
import static com.umda.debttodivine.Constants.LOG_AMOUNT_COL;
import static com.umda.debttodivine.Constants.LOG_PROFILE_COL;
import static com.umda.debttodivine.Constants.LOG_TYPE_COL;
import static com.umda.debttodivine.Constants.LOG_DATE_COL;

import static com.umda.debttodivine.Constants.TYPE_FASTING;
import static com.umda.debttodivine.Constants.TYPE_PRAYER;
import static com.umda.debttodivine.Constants.FAJR;
import static com.umda.debttodivine.Constants.DHUHR;
import static com.umda.debttodivine.Constants.ASR;
import static com.umda.debttodivine.Constants.MAGHRIB;
import static com.umda.debttodivine.Constants.ISHA;
import static com.umda.debttodivine.Constants.WITR;
import static com.umda.debttodivine.Constants.FEMALE;
/*
import harmony.java.awt.Color;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Font;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.net.Uri;
*/

import java.util.Calendar;
import java.util.Date;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
//import android.preference.PreferenceCategory;

public class Settings extends PreferenceActivity {
	
	// Option names and default values
	private static final String PREF_PROFILE = "profile_id";
	private static final String PREF_REM_PROFILE = "rem_profile";
	private static final String PREF_EDIT_PROFILE = "edit_profile";
	private static final String PREF_MADHHAB = "madhhab";
	private static final String PREF_GENDER = "gender";
	private static final String PREF_TYPE = "type";
	private static final String PREF_INCREMENT = "increment";
	private static final String PREF_MENST = "menst_total";
	public static final String PREF_DEBTTOTAL ="debt_total";
	public static final String PREF_DEBTCOMPLETE ="debt_complete";
	private static final String PREF_DEADLINE_DATE ="deadline_date";
	private static final String PREF_DEADLINE_CHK ="deadline_chk";
	public static final String PREF_UNLOCK = "unlock_pro";
	private static final String PREF_HELP_SETTINGS = "helpsettings";
	private static final String PREF_ABOUT = "about";
	
	private ListPreference pref_profile;
	private Preference pref_rem_profile;
	private Preference pref_edit_profile;
	private ListPreference pref_type;
	private ListPreference pref_madhhab;
	private ListPreference pref_gender;
	private Preference pref_debttotal;
	private Preference pref_menst;
	private Preference pref_increment;
	private Preference pref_deadline_date;
	private CheckBoxPreference pref_deadline_chk;
	
	private Preference pref_about;
	private Preference pref_helpsettings;
	private static Preference pref_unlock;
	
	private static DebtData debtdata;
	public EditText dialogText = null;
	private String xalerttitle = null;
	
	public Profile myProfile;
	/*
	private static Font titleFont = new Font(Font.HELVETICA, 28, Font.BOLD);
	private static Font redFont = new Font(Font.HELVETICA, 28, Font.BOLD, Color.RED);
	private static Font subFont = new Font(Font.HELVETICA, 16, Font.BOLD);
	private static Font smallBold = new Font(Font.HELVETICA, 12, Font.BOLD);
	private static Font smallNormal = new Font(Font.HELVETICA, 12, Font.NORMAL);
	*/
	
    static final int DATE_DIALOG_ID = 0;
    static final int TEXT_DIALOG_ID = 1;
    static final int REM_PROFILE_DIALOG_ID = 2;
    static final int RESET_DIALOG_ID = 3;
    static final int INCREMENT_DIALOG_ID = 4;
    static final int DEBTTOTAL_DIALOG_ID = 5;
    static final int EDIT_DIALOG_ID = 6;
    
    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		//this.myProfile = getIntent().getExtras().getParcelable(Constants.PROFILE_OBJECT);
		this.myProfile = loadProfile(this,getProfile(this));
		
		if (debtdata == null) debtdata = new DebtData(this);

		findPrefs();
		setListeners();
		loadPrefProfile();
		loadPrefs(Settings.this,this.myProfile);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		storeProfile(Settings.this, this.myProfile);
	}
	
	public void findPrefs(){
		pref_profile = (ListPreference) findPreference(PREF_PROFILE);
		pref_rem_profile = findPreference(PREF_REM_PROFILE);
		pref_edit_profile = findPreference(PREF_EDIT_PROFILE);
		pref_type = (ListPreference)findPreference(PREF_TYPE);
		pref_gender = (ListPreference)findPreference(PREF_GENDER);
		pref_madhhab = (ListPreference)findPreference(PREF_MADHHAB);
		pref_increment = findPreference(PREF_INCREMENT);
		pref_debttotal = findPreference(PREF_DEBTTOTAL);
		pref_menst = findPreference(PREF_MENST);
		pref_deadline_date = findPreference(PREF_DEADLINE_DATE);
		pref_deadline_chk = (CheckBoxPreference)findPreference(PREF_DEADLINE_CHK);
		
		pref_helpsettings = findPreference(PREF_HELP_SETTINGS);
		pref_about = findPreference(PREF_ABOUT);
		pref_unlock = findPreference(PREF_UNLOCK);
	}
	
	public void enablePrefs(boolean enabled){
		pref_edit_profile.setEnabled(enabled);
		pref_type.setEnabled(enabled);
		pref_madhhab.setEnabled(enabled);
		pref_gender.setEnabled(enabled);
		pref_increment.setEnabled(enabled);
		pref_debttotal.setEnabled(enabled);
		if  (myProfile != null && (myProfile.gender.equalsIgnoreCase(MALE) || myProfile.type.equalsIgnoreCase(TYPE_PRAYER) == false)){
			pref_menst.setEnabled(false);
		}else{
			pref_menst.setEnabled(enabled);
		}
		pref_deadline_chk.setEnabled(enabled);
		pref_deadline_date.setEnabled(enabled);
	}
	
	public void clearPrefs(Context context){
		setPreferenceString(context, pref_profile, null, null, getResources().getString(R.string.profile_summary));
		setPreferenceString(context, pref_type, null, null, getResources().getString(R.string.type_summary));
		setPreferenceString(context, pref_madhhab, null, null, getResources().getString(R.string.madhhab_summary));
		setPreferenceString(context, pref_gender, null, null, getResources().getString(R.string.gender_summary));
		setPreferenceString(context, pref_increment, null, null, getResources().getString(R.string.increment_summary));
		setPreferenceString(context, pref_debttotal, null, null, "");
		setPreferenceString(context, pref_menst, null, null, getResources().getString(R.string.menst_total_summary));
		updateDeadline(context, false, null);
	}
	
	public void loadPrefs(Context context, Profile myProfile){
		if (myProfile != null){
		
			setPreferenceString(context, pref_profile, myProfile.name,myProfile.name, getResources().getString(R.string.profile_summary));
			pref_profile.setValue(myProfile.name);
			setPreferenceString(context, pref_type, myProfile.type,myProfile.type, getResources().getString(R.string.type_summary));
			pref_type.setValue(myProfile.type);
			setPreferenceString(context, pref_madhhab, myProfile.madhhab,myProfile.madhhab, getResources().getString(R.string.madhhab_summary));
			pref_madhhab.setValue(myProfile.madhhab);
			setPreferenceString(context, pref_gender, myProfile.gender,myProfile.gender, getResources().getString(R.string.gender_summary));
			pref_gender.setValue(myProfile.gender);
			
			String xdebtsummary;
			xdebtsummary = myProfile.getYears().toString() + "Yr "+ myProfile.getMonths().toString() + "Mth "+ myProfile.getDays().toString() + "Day";
			setPreferenceString(context, pref_increment, myProfile.increment.toString(), myProfile.increment.toString(), getResources().getString(R.string.increment_summary));
			setPreferenceString(context, pref_debttotal, myProfile.debtTotal.toString(), xdebtsummary, "");
			setPreferenceString(context, pref_menst, myProfile.getMenst().toString(), myProfile.getMenst().toString(), getResources().getString(R.string.menst_total_summary));
			updateDeadline(context, myProfile.hasDeadline, myProfile.getDeadline());
			
			enablePrefs(true);

		}else{
			clearPrefs(context);
			enablePrefs(false);
		}
	}
	
	public void loadPrefProfile(){
		String[] FROM = { _ID, PROFILE_ID_COL};
		
		SQLiteDatabase db = debtdata.getReadableDatabase();
		Cursor cursor = db.query(PROFILE_TABLE, FROM, null, null, null, null, null);
		Integer xcount = cursor.getCount();
		if (isPro(this)){
			if  (xcount >= 0) xcount += 1;
		}
		if (xcount == 0) xcount = 1;
		
		CharSequence[] entries = new String[xcount];
		CharSequence[] entryValues = new String[xcount];
		if  (cursor.getCount() == 0) {
			entries[0] = "Add New Profile";
			entryValues[0] = "AddProfile";
		}else{
			int i = 0;
			while (cursor.moveToNext()){
				entries[i] = cursor.getString(cursor.getColumnIndex(PROFILE_ID_COL));
				entryValues[i] = cursor.getString(cursor.getColumnIndex(PROFILE_ID_COL));
				i++;
			}
			if (isPro(this)){
				entries[i] = "Add New Profile";
				entryValues[i] = "AddProfile";
			}
		}
		loadListPreference(pref_profile, entries, entryValues);
		cursor.close();
		debtdata.close();
	}
	
	public static void storeProfile(Context context, Profile xprofile){
		try {
			updateProfile(context,xprofile);
		}finally{
			debtdata.close();
		}
	}
	
	private void loadListPreference(ListPreference preference, CharSequence[] entries, CharSequence[] entryValues){
		preference.setEntries(entries);
		preference.setEntryValues(entryValues);
	}
	
	private DatePickerDialog.OnDateSetListener mDateSetListener =
			new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        	String xdate = String.format("%02d-%02d-%04d",dayOfMonth,monthOfYear+1,year);
        	Date test = new Date(year-1900,monthOfYear,dayOfMonth);
			Date test2 = today();
			
			if (test.compareTo(test2) >= 0){
				updateDeadline(Settings.this, true, xdate.toString());
			}else{
				Settings.toast(Settings.this, "Deadline must be later than today");
				updateDeadline(Settings.this, false, null);
			}
            
        }
    };
    
    @Override
    protected Dialog onCreateDialog(int id){
    	AlertDialog alert;
    	LayoutInflater inflater;
    	EditText dialogText;
    	
    	View xview;
        switch (id) {
	        case DATE_DIALOG_ID:
	        	Date date = this.myProfile.deadline;
	        	if (date == null){
	        		date = today();
		        }
	        	
	        	Integer xyear = date.getYear() + 1900;
	        	Integer xmonth = date.getMonth();
	        	Integer xday = date.getDate();
	        	
	        	DatePickerDialog dialog = new DatePickerDialog(this,mDateSetListener,xyear, xmonth, xday);
				dialog.setButton(DialogInterface.BUTTON_NEGATIVE, 
					getString(R.string.cancel), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							if  (which == DialogInterface.BUTTON_NEGATIVE){
								updateDeadline(Settings.this, false, null);
							}
						}
				});
	            return dialog;
	            
	        case TEXT_DIALOG_ID:
	        	dialogText = new EditText(this);
	        	dialogText.setId(999);
	        	dialogText.setInputType(InputType.TYPE_CLASS_TEXT);
	        	alert = new AlertDialog.Builder(this)
	            .setTitle(R.string.alert_dialog_new_profile)
	            .setView(dialogText)
	            .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	EditText dialogText = ((EditText)((Dialog)dialog).findViewById(999));
	                	String newprofile = dialogText.getText().toString().trim();
	                	newprofile = newprofile == "" ? null : newprofile;
	                	myProfile = loadProfile(Settings.this, newprofile);
						if (checkNewProfile(Settings.this, newprofile)){
							toast(Settings.this, "Profile ID already exists, loading Profile");
						}else{
							myProfile.name = newprofile;
							storeProfile(Settings.this, myProfile);	
						}
						loadPrefs(Settings.this, myProfile);
						loadPrefProfile();
	                }
	            })
	            .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	dialog.dismiss();
	                }
	            })
	            .create();
	            return alert;
	            
	    	case REM_PROFILE_DIALOG_ID:
	        	alert = new AlertDialog.Builder(this)
	            .setTitle(xalerttitle)
	            .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
					//@Override
					public void onClick(DialogInterface dialog, int which) {
						deleteProfile(myProfile);
					}
	            })
	            .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	dialog.dismiss();
	                }
	            })
	            .create();
	            return alert;
	            
	    	case EDIT_DIALOG_ID:
	        	dialogText = new EditText(this);
	        	dialogText.setId(999);
	        	dialogText.setInputType(InputType.TYPE_CLASS_TEXT);
	        	alert = new AlertDialog.Builder(this)
	            .setTitle(R.string.alert_dialog_edit_profile)
	            .setView(dialogText)
	            .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	EditText dialogText = ((EditText)((Dialog)dialog).findViewById(999));
	                	String newprofile = dialogText.getText().toString().trim();
	                	newprofile = newprofile == "" ? null : newprofile;
						if (checkNewProfile(Settings.this, newprofile)){
							toast(Settings.this, "Profile ID already exists");
						}else{
							editProfile(Settings.this, myProfile, newprofile);
							setPreferenceString(Settings.this, pref_profile, newprofile, newprofile, getResources().getString(R.string.profile_summary));
							myProfile = loadProfile(Settings.this, newprofile);
							loadPrefs(Settings.this, myProfile);
							loadPrefProfile();
						}
	                }
	            })
	            .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	dialog.dismiss();
	                }
	            })
	            .create();
	            return alert;
	            
	    	case RESET_DIALOG_ID:
	        	alert = new AlertDialog.Builder(this)
	            .setTitle(xalerttitle)
	            .setPositiveButton(R.string.alert_dialog_yes, new DialogInterface.OnClickListener() {
					//@Override
					public void onClick(DialogInterface dialog, int which) {
						if (myProfile != null){
							myProfile.clearTotals();
							loadPrefs(Settings.this, myProfile);
						}
					}
	            })
	            .setNegativeButton(R.string.alert_dialog_no, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	dialog.dismiss();
	                }
	            })
	            .create();
	            return alert;
	            
	    	case INCREMENT_DIALOG_ID:
	    		inflater = getLayoutInflater();
	    		xview = inflater.inflate(R.layout.increment, null);
	    		SeekBar xseek = (SeekBar)xview.findViewById(R.id.seekbar);
	    		final TextView xtext = (TextView)xview.findViewById(R.id.seektext);
	    		xseek.setHapticFeedbackEnabled(true);
	    		xseek.setKeyProgressIncrement(5);
	    		xseek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					
					public void onStopTrackingTouch(SeekBar seekBar) {}
					
					public void onStartTrackingTouch(SeekBar seekBar) {}
					
					public void onProgressChanged(SeekBar seekBar, int progress,
							boolean fromUser) {
						if (progress == 0) progress = 1;
						if (fromUser){
							myProfile.increment = progress;
						}
						xtext.setText(String.valueOf(progress));
						
					}
				});
	    		xseek.setProgress(myProfile.increment);
	    		
	    		alert = new AlertDialog.Builder(this)
	            .setTitle("Set Increment")
	            .setView(xview)
	            .setNegativeButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	loadPrefs(Settings.this, myProfile);
	                	dialog.dismiss();
	                }
	            })
	            .create();
	            return alert;
	    	case DEBTTOTAL_DIALOG_ID:
	    		inflater = getLayoutInflater();
	    		xview = inflater.inflate(R.layout.debttotal, null);
	    		final SeekBar xseekyear = (SeekBar)xview.findViewById(R.id.yearbar);
	    		final SeekBar xseekmonth = (SeekBar)xview.findViewById(R.id.monthbar);
	    		final SeekBar xseekday = (SeekBar)xview.findViewById(R.id.daybar);
	    		final TextView xtextyear = (TextView)xview.findViewById(R.id.yeartext);
	    		final TextView xtextmonth = (TextView)xview.findViewById(R.id.monthtext);
	    		final TextView xtextday = (TextView)xview.findViewById(R.id.daytext);
	    		
	    		xseekyear.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					public void onStopTrackingTouch(SeekBar seekBar) {}
					public void onStartTrackingTouch(SeekBar seekBar) {}
					public void onProgressChanged(SeekBar seekBar, int progress,
							boolean fromUser) {
						if (fromUser){
							//myProfile.increment = progress;
						}
						String xstring = progress == 1 ? "1 Year" : String.valueOf(progress)+" Years";
						xtextyear.setText(xstring);
						
					}
				});
	    		xseekyear.setProgress(1);	
	    		xseekyear.setProgress(myProfile.getYears());
	    		
	    		xseekmonth.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					public void onStopTrackingTouch(SeekBar seekBar) {}
					public void onStartTrackingTouch(SeekBar seekBar) {}
					public void onProgressChanged(SeekBar seekBar, int progress,
							boolean fromUser) {
						if (fromUser){
							//myProfile.increment = progress;
						}
						String xstring = progress == 1 ? "1 Month" : String.valueOf(progress)+" Months";
						xtextmonth.setText(xstring);
						
					}
				});
	    		xseekmonth.setProgress(1);
	    		xseekmonth.setProgress(myProfile.getMonths());
	    		
	    		xseekday.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					public void onStopTrackingTouch(SeekBar seekBar) {}
					public void onStartTrackingTouch(SeekBar seekBar) {}
					public void onProgressChanged(SeekBar seekBar, int progress,
							boolean fromUser) {
						if (fromUser){
							//myProfile.increment = progress;
						}
						String xstring = progress == 1 ? "1 Day" : String.valueOf(progress)+" Days";
						xtextday.setText(xstring);
						
					}
				});
	    		xseekday.setProgress(1);
	    		xseekday.setProgress(myProfile.getDays());
	    		
	    		alert = new AlertDialog.Builder(this)
	            .setTitle("Set Debt Total")
	            .setView(xview)
	            .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	if (myProfile != null){
	                		Integer xyears = xseekyear.getProgress()*DAYS_IN_YEAR;
	                		Integer xmonths = xseekmonth.getProgress()*DAYS_IN_MONTH;
	                		Integer xdays = xseekday.getProgress();
	                		Integer xtotal = xyears + xmonths + xdays;
	                		myProfile.debtTotal = 
	                				xtotal - myProfile.getMenst() < myProfile.debtComplete ? 
	                						myProfile.debtComplete : xtotal;
	                	}
	                	loadPrefs(Settings.this, myProfile);
	                	dialog.dismiss();
	                }
	            })
	            .create();
	            return alert;
        }
        return null;
    }
    
    public static Date today(){
    	Calendar c = Calendar.getInstance();
    	Date xdate = new Date(c.getTime().getYear(),c.getTime().getMonth(),c.getTime().getDate());
    	return xdate;
    }
	
    public void setListeners(){
		
		if (pref_profile != null) {
			pref_profile.setOnPreferenceChangeListener(new OnPreferenceChangeListener(){
				//@Override
		        public boolean onPreferenceChange(Preference preference,Object newValue) {
					if (newValue.toString().trim() == "AddProfile"){
						removeDialog(TEXT_DIALOG_ID);
						showDialog(TEXT_DIALOG_ID);
						return false;
					}else{
						String newprofile = newValue.toString().trim();
						newprofile = newprofile == "" ? null : newprofile;
						myProfile = loadProfile(Settings.this, newprofile);
						myProfile.name = newprofile;
						loadPrefs(Settings.this, myProfile);
						loadPrefProfile();
					}
					
					return true;
		        }
			});
		};
		
		if (pref_rem_profile != null) {
			pref_rem_profile.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				//@Override
				public boolean onPreferenceClick(Preference preference) {
					if (myProfile != null){
						removeDialog(REM_PROFILE_DIALOG_ID);
						xalerttitle = "Are you sure you want to remove this Profile?";
						showDialog(REM_PROFILE_DIALOG_ID);
					}
					return true;
				}
			});
		}
		if (pref_edit_profile != null) {
			pref_edit_profile.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				//@Override
				public boolean onPreferenceClick(Preference preference) {
					if (myProfile != null){
						removeDialog(EDIT_DIALOG_ID);
						showDialog(EDIT_DIALOG_ID);
					}
					return true;
				}
			});
		}
		if (pref_increment != null) {
			pref_increment.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				//@Override
				public boolean onPreferenceClick(Preference preference) {
					if (myProfile != null){
						removeDialog(INCREMENT_DIALOG_ID);
						showDialog(INCREMENT_DIALOG_ID);
					}
					return true;
				}
			});
		}
		if (pref_debttotal != null) {
			pref_debttotal.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				//@Override
				public boolean onPreferenceClick(Preference preference) {
					if (myProfile != null){
						removeDialog(DEBTTOTAL_DIALOG_ID);
						showDialog(DEBTTOTAL_DIALOG_ID);
					}
					return false;
				}
			});
		}
		if (pref_debttotal != null) {
			pref_debttotal.setOnPreferenceChangeListener(new OnPreferenceChangeListener(){
				//@Override
		        public boolean onPreferenceChange(Preference preference,Object newValue) {
		        	newValue = newValue.toString().equals("") ? "0" : newValue;
					if (myProfile != null){
						myProfile.debtTotal = Integer.parseInt(newValue.toString());
						
						if (myProfile.menstTotal > 0) {
							if (myProfile.menstTotal > myProfile.getMaxMenst()){
								myProfile.menstTotal = myProfile.getMaxMenst();
							}
						}
					}
					loadPrefs(Settings.this,myProfile);
		        	return true;
				}
			});
		}
		if (pref_menst != null) {
			pref_menst.setOnPreferenceChangeListener(new OnPreferenceChangeListener(){
				//@Override
		        public boolean onPreferenceChange(Preference preference,Object newValue) {
		        	newValue = newValue.toString().equals("") ? "0" : newValue;
					if (myProfile != null){
						Integer xmenst = Integer.parseInt(newValue.toString());
						xmenst = xmenst > myProfile.getMaxMenst() ? myProfile.getMaxMenst() : xmenst;
						myProfile.menstTotal = xmenst;
					}
					loadPrefs(Settings.this,myProfile);
		        	return true;
				}
			});
		}
		if (pref_deadline_chk != null) {
			pref_deadline_chk.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				//@Override
				public boolean onPreferenceClick(Preference preference) {
					if (((CheckBoxPreference)preference).isChecked()){
						removeDialog(DATE_DIALOG_ID);
						showDialog(DATE_DIALOG_ID);
					}else{
						updateDeadline(Settings.this, false, null);
					}
					return true;
				}
			});
		}
		
		if (pref_deadline_date != null) {
			pref_deadline_date.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				//@Override
				public boolean onPreferenceClick(Preference preference) {
					/*
					Intent xintent = new Intent(Settings.this, DateActivity.class);
			    	startActivity(xintent);
					return false;
					*/
					if (pref_deadline_chk.isChecked()){
						removeDialog(DATE_DIALOG_ID);
						showDialog(DATE_DIALOG_ID);
					}else{
						updateDeadline(Settings.this, false, null);
					}
					return true;
					
				}
				
			});
		}
		
		if (pref_madhhab != null) {
			pref_madhhab.setOnPreferenceChangeListener(new OnPreferenceChangeListener(){
				//@Override
		        public boolean onPreferenceChange(Preference preference,Object newValue) {
		        	if (myProfile != null){
		        		myProfile.madhhab = newValue.toString();
		        	}
					loadPrefs(Settings.this, myProfile);
		        	return true;
		        }
			});
		}
		if (pref_gender != null) {
			pref_gender.setOnPreferenceChangeListener(new OnPreferenceChangeListener(){
				//@Override
		        public boolean onPreferenceChange(Preference preference,Object newValue) {
		        	if (myProfile != null){
		        		myProfile.gender = newValue.toString();
		        		if (myProfile.gender.equalsIgnoreCase(MALE)){
		        			myProfile.menstTotal = 0;
		        		}
		        	}
					loadPrefs(Settings.this, myProfile);
		        	return true;
		        }
			});
		}
		if (pref_type != null) {
			pref_type.setOnPreferenceChangeListener(new OnPreferenceChangeListener(){
				//@Override
		        public boolean onPreferenceChange(Preference preference,Object newValue) {
		        	if (myProfile != null){
		        		if  (myProfile.type.equalsIgnoreCase(newValue.toString()) == false){
	        				removeDialog(RESET_DIALOG_ID);
	        				xalerttitle = "Profile Type changed. Reset all counts?";
							showDialog(RESET_DIALOG_ID);
		        		}
		        		myProfile.type = newValue.toString();
		        		if  (myProfile.type.equalsIgnoreCase(TYPE_FASTING)){
		        			myProfile.menstTotal = 0;
		        		}
		        	}
		        	loadPrefs(Settings.this, myProfile);
		        	return true;
		        }
			});
		}
		if (pref_helpsettings != null) {
			pref_helpsettings.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				public boolean onPreferenceClick(Preference preference) {
					Intent xintent = new Intent(Settings.this, HelpSettings.class);
			    	startActivity(xintent);
					return false;
				}
			});
		}
		if (pref_about != null) {
			pref_about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				//@Override
				public boolean onPreferenceClick(Preference preference) {
					Intent xintent = new Intent(Settings.this, AboutActivity.class);
			    	startActivity(xintent);
					return false;
				}
			});
		}
		if (pref_unlock != null) {
			pref_unlock.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				//@Override
				public boolean onPreferenceClick(Preference preference) {
					Intent xintent = new Intent(Settings.this, PurchaseActivity.class);
			    	startActivity(xintent);
					return false;
				}
			});
		}
	}
	
	private void updateDeadline(Context context, Boolean update, String xdate) {
    	if (update == false) {
    		setPreferenceString(context, pref_deadline_date, null, "", "");
    		setPreferenceBoolean(context, PREF_DEADLINE_CHK,update);
            pref_deadline_chk.setChecked(update);
    	}else{
    		pref_deadline_chk.setChecked(update);
    		setPreferenceBoolean(context,PREF_DEADLINE_CHK,update);
    		setPreferenceString(context,pref_deadline_date,xdate,xdate,"");
    	}
    	if  (this.myProfile != null){
    		this.myProfile.hasDeadline = update;
    		this.myProfile.deadline = myProfile.parseDate(xdate);
    	}
    	
    }
	
	public static String getProfile(Context context) {
		String value = getPreferenceString(context, PREF_PROFILE, null);
		return value;
	}
	
	public static Boolean checkNewProfile(Context context,String xprofile){
		if  (xprofile == null) return false;
		if (debtdata == null) {
			debtdata = new DebtData(context);
		}
		//myProfile = new Profile();
		String[] FROM = {_ID,PROFILE_ID_COL};
		String[] WHERE = {xprofile};
		SQLiteDatabase db = debtdata.getReadableDatabase();
		Cursor cursor = db.query(PROFILE_TABLE, FROM, PROFILE_ID_COL+"=?", WHERE, null, null, null);
		if (cursor.getCount() > 0){
			return true;
		}
		return false;
	}
	
	public static Profile loadProfile(Context context,String xprofile){
		Profile myProfile = new Profile();
		Cursor cursor;
		if (debtdata == null) {
			debtdata = new DebtData(context);
		}
		SQLiteDatabase db = debtdata.getReadableDatabase();
		
		String[] FROM = {_ID,PROFILE_ID_COL,TYPE_COL,GENDER_COL,MADHHAB_COL,INCREMENT_COL,
				DEBT_TOTAL_COL,DEBT_COMPLETE_COL,MENST_AVG_COL,MENST_TOTAL_COL,HAS_DEADLINE_COL,DEADLINE_COL};
		String[] WHERE = {xprofile};
		
		if (xprofile != null){
			cursor = db.query(PROFILE_TABLE, FROM, PROFILE_ID_COL+"=?", WHERE, null, null, null);
			cursor.moveToFirst();
		}else{
			cursor = db.query(PROFILE_TABLE, FROM, null, null, null, null, null);
			cursor.moveToFirst();
		}
		
		if (cursor.getCount() > 0){
			myProfile.name = cursor.getString(cursor.getColumnIndex(PROFILE_ID_COL));
			
			myProfile.madhhab = cursor.getString(cursor.getColumnIndex(MADHHAB_COL));
			myProfile.debtTotal = cursor.getInt(cursor.getColumnIndex(DEBT_TOTAL_COL));
			myProfile.debtComplete = cursor.getInt(cursor.getColumnIndex(DEBT_COMPLETE_COL));
			myProfile.hasDeadline = cursor.getInt(cursor.getColumnIndex(HAS_DEADLINE_COL)) == 1 ? true:false;
			
			//version 1.3
			if  (db.getVersion() >= 13){
				myProfile.type = cursor.getString(cursor.getColumnIndex(TYPE_COL));
				myProfile.gender = cursor.getString(cursor.getColumnIndex(GENDER_COL));
				myProfile.menstTotal = cursor.getInt(cursor.getColumnIndex(MENST_TOTAL_COL));
				myProfile.menstAvg = cursor.getInt(cursor.getColumnIndex(MENST_AVG_COL)) == 1 ? true:false;
				myProfile.increment = cursor.getInt(cursor.getColumnIndex(INCREMENT_COL));
			}
			
			if  (myProfile.hasDeadline == false) {
				myProfile.deadline = null;
			}else{
				myProfile.deadline = myProfile.parseDate(cursor.getString(cursor.getColumnIndex(DEADLINE_COL)));
			}
			cursor.close();
			
			FROM = new String [] {_ID,PROFILE_COL,PRAYER_ID_COL,PRAYER_COMPLETE_COL};
			if (xprofile == null){
				WHERE = new String[] {myProfile.name};
			}
			cursor = db.query(PRAYER_TABLE, FROM, PROFILE_COL+"=?", WHERE, null, null, null);
			String xprayer;
			Integer xvalue;
			while (cursor.moveToNext()){
				xprayer = cursor.getString(cursor.getColumnIndex(PRAYER_ID_COL));
				xvalue = cursor.getInt(cursor.getColumnIndex(PRAYER_COMPLETE_COL));
				myProfile.updPrayer(context,xprayer,xvalue,false);
			}
		}
		
		cursor.close();
		debtdata.close();
		return myProfile;
		
	}
	
	public static void updateProfile(Context context, Profile myProfile){
		
		if (myProfile == null) return;
		if (debtdata == null) debtdata = new DebtData(context);
		SQLiteDatabase db = debtdata.getWritableDatabase();
		
		String[] WHERE = {myProfile.name};
		ContentValues values = new ContentValues();
		ContentValues prayervalues = new ContentValues();
		
		values.put(PROFILE_ID_COL, myProfile.name);
		values.put(MADHHAB_COL, myProfile.madhhab);
		values.put(DEBT_TOTAL_COL, myProfile.debtTotal);
		values.put(DEBT_COMPLETE_COL, myProfile.debtComplete);
		values.put(HAS_DEADLINE_COL, (myProfile.hasDeadline == true ? 1 : 0));
		values.put(DEADLINE_COL, myProfile.getDeadline());
		
		//Version 1.3
		if  (db.getVersion() >= 13){
			values.put(MENST_TOTAL_COL, myProfile.menstTotal);
			values.put(MENST_AVG_COL, (myProfile.menstAvg == true ? 1 : 0));
			values.put(INCREMENT_COL, myProfile.increment);
			values.put(TYPE_COL, myProfile.type);
			values.put(GENDER_COL, myProfile.gender);
		}
		
		String[] prayers = getPrayerArray(myProfile.madhhab, context);
		
		if  (db.update(PROFILE_TABLE, values, PROFILE_ID_COL+"=?", WHERE) > 0) {
			if  (myProfile.madhhab.equalsIgnoreCase(HANAFI) == false){
				db.delete(PRAYER_TABLE, PROFILE_COL+"=? and "+PRAYER_ID_COL+"=?", new String[]{myProfile.name,WITR});
			}
		}else{
			db.insertOrThrow(PROFILE_TABLE, null, values);
		}
		for (int i = 0; i < prayers.length; i++){
			prayervalues.clear();
			prayervalues.put(PROFILE_COL, myProfile.name);
			prayervalues.put(PRAYER_ID_COL, prayers[i]);
			prayervalues.put(PRAYER_COMPLETE_COL, myProfile.getPrayerComplete(prayers[i]));
			if  (db.update(PRAYER_TABLE, prayervalues, PROFILE_COL+"=? and "+PRAYER_ID_COL+"=?", new String[]{myProfile.name,prayers[i]}) <= 0) {
				db.insertOrThrow(PRAYER_TABLE, null, prayervalues);
			}
		}
		debtdata.close();
		
	}
	
	public static void editProfile(Context context, Profile myProfile, String newprofile){

		if (myProfile == null) return;
		if (newprofile == null) return;
		if (debtdata == null) debtdata = new DebtData(context);
		SQLiteDatabase db = debtdata.getWritableDatabase();
		
		String[] WHERE = {myProfile.name};
		ContentValues values = new ContentValues();
		
		
		values.put(PROFILE_ID_COL, newprofile);
		db.update(PROFILE_TABLE, values, PROFILE_ID_COL+"=?", WHERE);
		
		values.clear();
		values.put(PROFILE_COL, newprofile);
		db.update(PRAYER_TABLE, values, PROFILE_COL+"=?", WHERE);
		
		debtdata.close();
		toast(context, "Profile ID updated");
	}
	
	private static void toast(Context context, String message){
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
	
	public void deleteProfile(Profile myProfile){
		String[] WHERE = {myProfile.name};
		SQLiteDatabase db = debtdata.getWritableDatabase();
		db.delete(PROFILE_TABLE, PROFILE_ID_COL+"=?", WHERE);
		db.delete(PRAYER_TABLE, PROFILE_COL+"=?", WHERE);
		debtdata.close();
		
		this.myProfile = null;//loadProfile(null, this);
		loadPrefs(this, this.myProfile);
		loadPrefProfile();
		
	}
	
	public static void addLogEntry(Context context, Profile myProfile, String action, String type, Integer amount){
		
		if (myProfile == null) return;
		if (debtdata == null) debtdata = new DebtData(context);
		SQLiteDatabase db = debtdata.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		
		values.put(LOG_PROFILE_COL, myProfile.name);
		values.put(LOG_ACTION_COL, action);
		values.put(LOG_TYPE_COL, type);
		values.put(LOG_AMOUNT_COL, amount);
		
		db.insertOrThrow(LOG_TABLE, null, values);
		debtdata.close();
	}
	
	public static void undoLogEntry(Context context, Profile myProfile){
		if  (myProfile == null) return;
		Cursor cursor;
		if (debtdata == null) debtdata = new DebtData(context);
		SQLiteDatabase db = debtdata.getReadableDatabase();
		
		String[] FROM = {_ID,LOG_PROFILE_COL,LOG_ACTION_COL,LOG_TYPE_COL,LOG_AMOUNT_COL,LOG_DATE_COL};
		String[] WHERE = {myProfile.name};
		
		cursor = db.query(LOG_TABLE, FROM, LOG_PROFILE_COL+"=?", WHERE, null, null, null);
		cursor.moveToLast();
		
		if (cursor.getCount() > 0){
			String log_action = cursor.getString(cursor.getColumnIndex(LOG_ACTION_COL));
			String log_type = cursor.getString(cursor.getColumnIndex(LOG_TYPE_COL));
			Integer log_amount = cursor.getInt(cursor.getColumnIndex(LOG_AMOUNT_COL));
			//String log_date = cursor.getString(cursor.getColumnIndex(LOG_DATE_COL));
			
			if  (log_type.equals(TYPE_FASTING) || log_type.equals(TYPE_PRAYER)){
				if  (log_action.equalsIgnoreCase("add")){
					myProfile.updProgress(context, myProfile.debtComplete-log_amount,false); }
				else{
					myProfile.updProgress(context, myProfile.debtComplete+log_amount,false);
				}
			}else if  (log_type.equals(FAJR) || log_type.equals(DHUHR) || log_type.equals(ASR) || 
					   log_type.equals(MAGHRIB) || log_type.equals(ISHA) || log_type.equals(WITR)){
				if  (log_action.equalsIgnoreCase("add")){
					myProfile.updPrayerDiff(context, log_type, -log_amount,false); }
				else{
					myProfile.updPrayerDiff(context, log_type, log_amount,false);
				}
			}
			//myProfile.updPrayer(xprayer,xvalue);
			
		}
		cursor.close();
		debtdata.close();
	}
	
	public void deleteLogEntry(Context context, Profile myProfile, String id){
		String[] WHERE = {myProfile.name, id};
		SQLiteDatabase db = debtdata.getWritableDatabase();
		db.delete(LOG_TABLE, LOG_PROFILE_COL+"=? and "+_ID+"=?", WHERE);
		debtdata.close();
	}
	
	public static void setPreferenceString(Context context, Preference preference, String value, String summary, String deftsummary) {
		PreferenceManager.getDefaultSharedPreferences(context).edit().putString(preference.getKey(), value).commit();
		if (value == null){
			preference.setSummary(deftsummary);
		}else if (value.equals("")){
			preference.setSummary(deftsummary);
		}else
			preference.setSummary(summary);
	}
	
	public static void setPreferenceBoolean(Context context, String preference, boolean value) {
		PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(preference, value).commit();
	}
	
	public static String getPreferenceString(Context context, String key, String deft){
		String value = PreferenceManager.getDefaultSharedPreferences(context).getString(key, deft);
		return value;
	}
	
	public static boolean getPreferenceBoolean(Context context, String key, boolean deft){
		boolean value = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, deft);
		return value;
	}
	
	public static String[] getPrayerArray(String xmadhhab, Context context){
		String[] prayers = null;
		if  (xmadhhab.equalsIgnoreCase(HANAFI)){
			prayers = context.getResources().getStringArray(R.array.hanafi_prayers);  
		}else{
			prayers = context.getResources().getStringArray(R.array.other_prayers);
	 	}
	 	return prayers;
	}
	
	public static Boolean isPro(Context context){
		Boolean xvalue = getPreferenceBoolean(context, PREF_UNLOCK, false);
		return true;
	}
	
	public static void setPro(Context context, Boolean ispro){
		setPreferenceBoolean(context, PREF_UNLOCK, ispro);
	}
	public static String getKey(){
		String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQE";
		base64EncodedPublicKey = base64EncodedPublicKey + "Ane24xbAvxSGpPuCAgMfsVrUYI86jdUwnw4oWoMODVNWUG";
		base64EncodedPublicKey = base64EncodedPublicKey + "wqRd9zBwkEca+UYKfm6fkJqIp9q0pSk0tmR+now7t4/cai";
		base64EncodedPublicKey = base64EncodedPublicKey + "FVjXGzm569P0MYRoXEniiek6rFbapzQ0359/QBZ7gwZXP1";
		base64EncodedPublicKey = base64EncodedPublicKey + "AVqiwxQXGKGTkR18ouxZEAmWTwycl1ngMEHqXbKgMOKLnP";
		base64EncodedPublicKey = base64EncodedPublicKey + "cSZyWdL4RQ1VUmO19mwNuYV2ivMsMcV6leBr0fe4FVTa6c/";
		base64EncodedPublicKey = base64EncodedPublicKey + "3Lb0uWGk9khcBRnrYnmNiS5OZLjbT0mtxLu3AMtWTq0Wu6";
		base64EncodedPublicKey = base64EncodedPublicKey + "AcMK/TojKTt5n0m+ojxKvAm0llIxeEioWtwMSwMZm+Lf8k";
		base64EncodedPublicKey = base64EncodedPublicKey + "nyX83i5Smf/3UXNW6rawIDAQAB";
		return base64EncodedPublicKey;
	}
	public static void shareTalliesText(Context context, Profile myProfile, Intent sharingIntent){
		String sharebody = "Profile: "+myProfile.name+"\n";
		sharebody = sharebody.concat("Profile Type: "+myProfile.type+"\n");
		sharebody = sharebody.concat("Total Debt: "+myProfile.getTotalDebt().toString()+" days\n");
		sharebody = sharebody.concat("Total Completed: "+myProfile.debtComplete.toString()+" days\n");
		sharebody = sharebody.concat("Total Remaining: "+myProfile.getRemaining().toString()+" days\n");
		sharebody = sharebody.concat("Percentage Completed: "+myProfile.getTotalPercent().toString()+"%\n");
		if  (myProfile.hasDeadline){
			sharebody = sharebody.concat("Deadline Date: "+myProfile.getDeadline()+"\n");
			sharebody = sharebody.concat("Days till Deadline: "+myProfile.getDaysTill().toString()+" days\n");
		}
		if  (myProfile.gender.equals(FEMALE)){
			sharebody = sharebody.concat("Menstrual Days: "+myProfile.getMenst().toString()+"\n");
		}
		sharebody = sharebody.concat("Percentage Completed: "+myProfile.getTotalPercent().toString()+"%\n");
		if  (myProfile.type.equals(TYPE_PRAYER)){
			if  (Settings.isPro(context)){
				sharebody = sharebody.concat("\nIndividual Prayer Tallies: \n");
				sharebody = sharebody.concat(FAJR+" "+myProfile.getPrayerPercent(FAJR)+"%\n Completed: "+myProfile.getPrayerComplete(FAJR)+" - Remaining: "+myProfile.getRemainingPrayer(FAJR)+"\n\n");
				sharebody = sharebody.concat(DHUHR+" "+myProfile.getPrayerPercent(DHUHR)+"%\n Completed: "+myProfile.getPrayerComplete(DHUHR)+" - Remaining: "+myProfile.getRemainingPrayer(DHUHR)+"\n\n");
				sharebody = sharebody.concat(ASR+" "+myProfile.getPrayerPercent(ASR)+"%\n Completed: "+myProfile.getPrayerComplete(ASR)+" - Remaining: "+myProfile.getRemainingPrayer(ASR)+"\n\n");
				sharebody = sharebody.concat(MAGHRIB+" "+myProfile.getPrayerPercent(MAGHRIB)+"%\n Completed: "+myProfile.getPrayerComplete(MAGHRIB)+" - Remaining: "+myProfile.getRemainingPrayer(MAGHRIB)+"\n\n");
				sharebody = sharebody.concat(ISHA+" "+myProfile.getPrayerPercent(ISHA)+"%\n Completed: "+myProfile.getPrayerComplete(ISHA)+" - Remaining: "+myProfile.getRemainingPrayer(ISHA)+"\n\n");
				if  (myProfile.madhhab.equals(HANAFI)){
					sharebody = sharebody.concat(WITR+" "+myProfile.getPrayerPercent(WITR)+"%\n Completed: "+myProfile.getPrayerComplete(WITR)+" - Remaining: "+myProfile.getRemainingPrayer(WITR)+"\n");	
				}
			}
			
		}else if(myProfile.type.equals(TYPE_FASTING)){
			
		}
    	sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, sharebody);
	}
	/*
	public static void shareTalliesPdf(Profile myProfile, Intent sharingintent){
		Document document = new Document();
    	String xpath = android.os.Environment.getExternalStorageDirectory() + java.io.File.separator + "DebtToDivine";
    	File logDir = new File(android.os.Environment.getExternalStorageDirectory().toString(), "DebtToDivine"); 
    	logDir.mkdirs(); 
    	String xfile = "DebtTally.pdf";
        
        try {
                // step 2:
                // we create a writer that listens to the document
                // and directs a PDF-stream to a file
        		PdfWriter.getInstance(document, new FileOutputStream(xpath + java.io.File.separator + xfile));
                // step 3: we open the document
                document.open();
                // step 4: we add a paragraph to the document
                document.add(new Paragraph("Debt to Divine Tallies",titleFont));
                Paragraph paragraph2 =  new Paragraph("Prayer Tallies",smallBold);
                
                PdfPTable table = new PdfPTable(4);
                PdfPCell c1 = new PdfPCell(new Phrase("Prayers"));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);

                c1 = new PdfPCell(new Phrase("Completed"));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);

                c1 = new PdfPCell(new Phrase("Remaining"));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);
                
                c1 = new PdfPCell(new Phrase("Percentage (%)"));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);
                table.setHeaderRows(1);

                table.addCell("Fajr");
                table.addCell("1.1");
                table.addCell("1.2");
                table.addCell("2.1");
                table.addCell("2.2");
                table.addCell("2.3");
                table.addCell("2.3");
                table.addCell("2.3");
                
                paragraph2.add(new Paragraph(" "));
                paragraph2.add(new Paragraph(" "));
                paragraph2.add(new Paragraph(" "));
                paragraph2.add(table);
                document.add(paragraph2);
        } catch (DocumentException de) {
                System.err.println(de.getMessage());
        } catch (IOException ioe) {
                System.err.println(ioe.getMessage());
        }
    	
        // step 5: we close the document
        document.close();
        sharingintent.putExtra(Intent.EXTRA_STREAM, Uri.parse(xpath + java.io.File.separator + xfile));
    	
	}
	*/
}