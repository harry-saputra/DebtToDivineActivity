package com.umda.debttodivine;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class HelpSettings extends PreferenceActivity {
	
	// Option names and default values
	private static final String INFO_TYPE = "infotype";
	private static final String INFO_MADHHAB = "infomadhhab";
	private static final String INFO_GENDER = "infogender";
	private static final String INFO_MENST = "infomenst";
	private static final String INFO_DEBTTOTAL ="infodebttotal";
	private static final String INFO_DEADLINE ="infodeadline";
	
	private Preference pref_type;
	private Preference pref_madhhab;
	private Preference pref_gender;
	private Preference pref_debttotal;
	private Preference pref_menst;
	private Preference pref_deadline;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.helpsettings);
	}
	@Override
	protected void onResume(){
		super.onResume();
		findPrefs();
		setListeners();
	}
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	public void findPrefs(){
		pref_type = findPreference(INFO_TYPE);
		pref_gender = findPreference(INFO_GENDER);
		pref_madhhab = findPreference(INFO_MADHHAB);
		pref_debttotal = findPreference(INFO_DEBTTOTAL);
		pref_menst = findPreference(INFO_MENST);
		pref_deadline = findPreference(INFO_DEADLINE);
		
	}
	
	public void setListeners(){
		if (pref_type != null) {
			pref_type.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				public boolean onPreferenceClick(Preference preference) {
					Intent xintent = new Intent(HelpSettings.this, InfoActivity.class);
			    	xintent.putExtra("infopages", "file:///android_asset/DebtType.html");
			    	startActivity(xintent);
					return false;
				}
			});
		}
		if (pref_madhhab != null) {
			pref_madhhab.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				public boolean onPreferenceClick(Preference preference) {
					Intent xintent = new Intent(HelpSettings.this, InfoActivity.class);
			    	xintent.putExtra("infopages", "file:///android_asset/Madhhab.html");
			    	startActivity(xintent);
					return false;
				}
			});
		}
		if (pref_gender != null) {
			pref_gender.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				//@Override
				public boolean onPreferenceClick(Preference preference) {
					Intent xintent = new Intent(HelpSettings.this, InfoActivity.class);
			    	xintent.putExtra("infopages", "file:///android_asset/Gender.html");
			    	startActivity(xintent);
					return false;
				}
			});
		}
		if (pref_debttotal != null) {
			pref_debttotal.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				//@Override
				public boolean onPreferenceClick(Preference preference) {
					Intent xintent = new Intent(HelpSettings.this, InfoActivity.class);
			    	xintent.putExtra("infopages", "file:///android_asset/DebtTotal.html");
			    	startActivity(xintent);
					return false;
				}
			});
		}
		if (pref_menst != null) {
			pref_menst.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				//@Override
				public boolean onPreferenceClick(Preference preference) {
					Intent xintent = new Intent(HelpSettings.this, InfoActivity.class);
			    	xintent.putExtra("infopages", "file:///android_asset/Menst.html");
			    	startActivity(xintent);
					return false;
				}
			});
		}
		if (pref_deadline != null) {
			pref_deadline.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				//@Override
				public boolean onPreferenceClick(Preference preference) {
					Intent xintent = new Intent(HelpSettings.this, InfoActivity.class);
			    	xintent.putExtra("infopages", "file:///android_asset/Deadline.html");
			    	startActivity(xintent);
					return false;
				}
			});
		}
		
	}
}