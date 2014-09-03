package com.umda.debttodivine;

import static com.umda.debttodivine.Constants.ASR;
import static com.umda.debttodivine.Constants.DHUHR;
import static com.umda.debttodivine.Constants.FAJR;
import static com.umda.debttodivine.Constants.ISHA;
import static com.umda.debttodivine.Constants.MAGHRIB;
import static com.umda.debttodivine.Constants.WITR;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class LogsAdapter extends SimpleCursorAdapter {
    String[] values;
    LogsActivity activity;
    static final int TEXT_DIALOG_ID = 0;
	
    public LogsAdapter(Context context, String[] values) {
		super(context, R.layout.logs_entry, null, values, null);
	    this.activity=(LogsActivity) context;
        this.values = values;
	}
    
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null)
            convertView = View.inflate(activity, R.layout.indiv_prayer, null);
    
		Button button = (Button)convertView.findViewById(R.id.but_undo);
        TextView prayer = (TextView)convertView.findViewById(R.id.prayer_title);
        TextView amt_remain = (TextView)convertView.findViewById(R.id.amt_remain);
        TextView amt_complete = (TextView)convertView.findViewById(R.id.amt_complete);
        
        
		
        button.setOnClickListener(new Button.OnClickListener(){
        	@Override
			public void onClick(View v) {
				
			}
        	
        });
        
        return convertView;
    }
	
    private void toast(String message){
		Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
	}
}
