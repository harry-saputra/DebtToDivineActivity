package com.umda.debttodivine;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
//import android.content.Intent;

public class InfoActivity extends Activity  {
	private String xinfopage;
	private WebView webview;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.infopages);
	}
	@Override
	public void onResume(){
		super.onResume();
		webview = (WebView)findViewById(R.id.webview);
		xinfopage = getIntent().getStringExtra("infopages");
		if  (xinfopage != null){
			webview.loadUrl(xinfopage);
		}
	}
	@Override
	public void onPause(){
		super.onPause();
	}
}