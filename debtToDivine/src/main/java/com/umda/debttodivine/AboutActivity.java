package com.umda.debttodivine;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AboutActivity extends Activity  {
	private WebView webview;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutpage);
	}
	@Override
	public void onResume(){
		super.onResume();
		webview = (WebView)findViewById(R.id.webview);
		webview.setWebViewClient(new WebViewClient(){
		    public boolean shouldOverrideUrlLoading(WebView view, String url) {
		        if (url != null && url.startsWith("http://")) {
		            view.getContext().startActivity(
		                new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
		            return true;
		        } else {
		            return false;
		        }
		    }
		});
		webview.loadUrl("file:///android_asset/About.html");
	}
	
	public void buttonClick(View xview){
		if (xview.getId() == R.id.butrate){
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.umda.debttodivine")));
		}
		if (xview.getId() == R.id.butweb){
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.umda.net")));
		}
		if (xview.getId() == R.id.butmail){
			Intent gmail = new Intent(Intent.ACTION_SEND);
	            //gmail.setClassName("com.google.android.gm","com.google.android.gm.ComposeActivityGmail");
	            
				gmail.putExtra(Intent.EXTRA_EMAIL, new String[] { "info@umda.net" });
	            gmail.setData(Uri.parse("info@umda.net"));
	            gmail.putExtra(Intent.EXTRA_SUBJECT, "[Debt to the Divine: Android]");
	            gmail.setType("text/html");
	            startActivity(gmail);
		}
	}
	
	@Override
	public void onPause(){
		super.onPause();
	}
}