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
import android.widget.TextView;
import android.widget.Toast;

public class PrayerArrayAdapter extends ArrayAdapter<String> {
    String[] values;
    IndividualPrayerActivity activity;
    static final int TEXT_DIALOG_ID = 0;
	
    public PrayerArrayAdapter(Context context, String[] values) {
		super(context, R.layout.indiv_prayer, values);
	    this.activity=(IndividualPrayerActivity) context;
        this.values = values;
	}
    
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null)
            convertView = View.inflate(activity, R.layout.indiv_prayer, null);
    
		Button button = (Button)convertView.findViewById(R.id.but_pray_plus);
        TextView prayer = (TextView)convertView.findViewById(R.id.prayer_title);
        TextView amt_remain = (TextView)convertView.findViewById(R.id.amt_remain);
        TextView amt_complete = (TextView)convertView.findViewById(R.id.amt_complete);
        TextProgressBar progress = (TextProgressBar)convertView.findViewById(R.id.prog_remain);
        ImageView image = (ImageView)convertView.findViewById(R.id.prayer_image);
        String xprayer = this.values[position];//c.getString(c.getColumnIndex(PRAYER_ID_KEY));

        if (xprayer.equals(FAJR)){
        	image.setImageDrawable(activity.getResources().getDrawable(R.drawable.prayer_fajr_small));	
        }else if (xprayer.equals(DHUHR)){
        	image.setImageDrawable(activity.getResources().getDrawable(R.drawable.prayer_dhuhr_small));
        }else if (xprayer.equals(ASR)){
        	image.setImageDrawable(activity.getResources().getDrawable(R.drawable.prayer_asr_small));
        }else if (xprayer.equals(MAGHRIB)){
        	image.setImageDrawable(activity.getResources().getDrawable(R.drawable.prayer_maghrib_small));
        }else if (xprayer.equals(ISHA)){
        	image.setImageDrawable(activity.getResources().getDrawable(R.drawable.prayer_isha_small));
        }else if (xprayer.equals(WITR)){
        	image.setImageDrawable(activity.getResources().getDrawable(R.drawable.prayer_witr_small));
        }
        if (this.activity.myProfile.getRemainingPrayer(xprayer) == 0 && this.activity.myProfile.getTotalDebt() > 0){
        	button.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.blue_tick));
        }else{
        	button.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.blue_plus));
        }
        prayer.setText(xprayer);
        Integer xcount = this.activity.myProfile.getPrayerComplete(xprayer);
        Integer xtotal = this.activity.myProfile.getTotalDebt();
        
        amt_remain.setText(this.activity.myProfile.getRemainingPrayer(xprayer).toString());
        amt_complete.setText(xcount.toString());
        amt_complete.setTag(xprayer+"complete");
        amt_remain.setTag(xprayer+"remain");
        progress.setTag(xprayer+"progress");
        
        button.setTag(xprayer);
		updateProgress(activity, progress, xtotal, xcount);
		
        button.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v) {
				String xprayer = v.getTag().toString();
				View xview = (View) v.getParent();
				View xroot = (View) v.getRootView();
				TextView amt_remain = (TextView)xview.findViewWithTag(xprayer+"remain");
				TextView amt_complete = (TextView)xview.findViewWithTag(xprayer+"complete");
				TextProgressBar progress = (TextProgressBar)xroot.findViewWithTag(xprayer+"progress");
		        
				//int value = activity.myProfile.getPrayerComplete(xprayer) + activity.myProfile.increment;
		    	//activity.myProfile.updPrayer(getContext(), xprayer, value, true);
				activity.myProfile.updPrayerDiff(getContext(), xprayer, activity.myProfile.increment, true);
				amt_remain.setText(activity.myProfile.getRemainingPrayer(xprayer).toString());
				amt_complete.setText(activity.myProfile.getPrayerComplete(xprayer).toString());
		    	if  (activity.myProfile.getRemainingPrayer(xprayer) == 0 && activity.myProfile.getTotalDebt() > 0) {
		    		toast("Congratulations on paying your "+xprayer+" debt!");
		        	v.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.blue_tick));
		    	}
		    	updateProgress(activity, progress, activity.myProfile.getTotalDebt(), activity.myProfile.getPrayerComplete(xprayer));
			}
        	
        });
        if (amt_complete != null) {
        	amt_complete.setOnClickListener(new EditText.OnClickListener() {
				public void onClick(View v) {
					String xprayer = v.getTag().toString();
					fieldClick(xprayer, "complete");
				}
			});
		}
        if (amt_remain != null) {
			amt_remain.setOnClickListener(new EditText.OnClickListener() {
				public void onClick(View v) {
					String xprayer = v.getTag().toString();
					fieldClick(xprayer, "remain");
				}
			});
		}
        return convertView;
    }
	private void fieldClick(String xvalue, String xsubstr){
		xvalue = xvalue.substring(0, xvalue.indexOf(xsubstr));
		activity.prayer = xvalue;
		activity.removeDialog(TEXT_DIALOG_ID);
		activity.showDialog(TEXT_DIALOG_ID);
	}
	public int getProgPercent(int xtotal, int xcount){
    	double xpercent = 0;
    	
    	if  (xtotal!=0)	xpercent = Math.floor((((double)xcount/(double)xtotal)*100));
    	return (int)xpercent;
    }
    public void updateProgress(Context context, TextProgressBar progress, int xtotal, int xcount){
    	int xprog = getProgPercent(xtotal, xcount);
    	progress.setMax(xtotal);
    	progress.setProgress(xcount);
    	progress.setText(String.valueOf(xprog)+"%");
    	progress.setTextSize(15f);
    	if (xprog < 40){
    		Rect bounds = progress.getProgressDrawable().getBounds();
    		progress.setProgressDrawable(context.getResources().getDrawable(R.drawable.red_progress));
    		progress.getProgressDrawable().setBounds(bounds);
    		progress.getProgressDrawable().setLevel(xprog*100);
    	}else if(xprog < 60){
    		Rect bounds = progress.getProgressDrawable().getBounds();
    		progress.setProgressDrawable(context.getResources().getDrawable(R.drawable.orange_progress));
    		progress.getProgressDrawable().setBounds(bounds);
    		progress.getProgressDrawable().setLevel(xprog*100);
    	}else if(xprog < 90){
    		Rect bounds = progress.getProgressDrawable().getBounds();
    		progress.setProgressDrawable(context.getResources().getDrawable(R.drawable.yellow_progress));
        	progress.getProgressDrawable().setBounds(bounds);
        	progress.getProgressDrawable().setLevel(xprog*100);
    	}else{
    		Rect bounds = progress.getProgressDrawable().getBounds();
    		progress.setProgressDrawable(context.getResources().getDrawable(R.drawable.green_progress));
    		progress.getProgressDrawable().setBounds(bounds);
    		progress.getProgressDrawable().setLevel(xprog*100);
    	}
    }
    private void toast(String message){
		Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
	}
}
