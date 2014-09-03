package com.umda.debttodivine;

import static com.umda.debttodivine.Constants.ASR;
import static com.umda.debttodivine.Constants.DAYS_IN_MONTH;
import static com.umda.debttodivine.Constants.DAYS_IN_YEAR;
import static com.umda.debttodivine.Constants.DHUHR;
import static com.umda.debttodivine.Constants.FAJR;
import static com.umda.debttodivine.Constants.FEMALE;
import static com.umda.debttodivine.Constants.HANAFI;
import static com.umda.debttodivine.Constants.ISHA;
import static com.umda.debttodivine.Constants.MAGHRIB;
import static com.umda.debttodivine.Constants.MALE;
import static com.umda.debttodivine.Constants.MILLISECONDS_PER_DAY;
import static com.umda.debttodivine.Constants.SHAFII;
import static com.umda.debttodivine.Constants.TYPE_FASTING;
import static com.umda.debttodivine.Constants.TYPE_PRAYER;
import static com.umda.debttodivine.Constants.WITR;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

public class Profile implements Parcelable{
	public String name;
	public String type;
	public String gender;
	public String madhhab;
	public Integer debtTotal;
	public Integer debtComplete;
	public Integer fajr;
	public Integer dhuhr;
	public Integer asr;
	public Integer maghrib;
	public Integer isha;
	public Integer witr;
	public Date deadline;
	public Boolean hasDeadline;
	public Boolean menstAvg;
	public Integer menstTotal;
	public Integer increment;
	
	public Profile(){
		name = "Default";
		type = TYPE_PRAYER;
		gender = MALE;
		madhhab = SHAFII;
		increment = 1;
		deadline = null;
		clearTotals();
	}
	
	public Integer getHighest(){
	    int xmax = Math.max(Math.max(Math.max(Math.max(fajr, dhuhr),asr), maghrib), isha);
	    if (madhhab.equalsIgnoreCase(HANAFI)){
	        xmax = Math.max(xmax, witr);
	    }
	    return xmax;
	}

	public Integer getLowest(){
	    int xmin = Math.min(Math.min(Math.min(Math.min(fajr, dhuhr),asr), maghrib), isha);
	    if (madhhab.equalsIgnoreCase(HANAFI)){
	        xmin = Math.min(xmin, witr);
	    }
	    return xmin;
	}

	
	public Integer getYears(){
		return debtTotal/DAYS_IN_YEAR;
	}
	
	public Integer getMonths(){
		return (debtTotal%DAYS_IN_YEAR)/DAYS_IN_MONTH;
	}
	
	public Integer getDays(){
		return (debtTotal%DAYS_IN_YEAR)%DAYS_IN_MONTH;
	}
	
	public Integer getMenst(){
	    if (menstAvg == false){
	        return  menstTotal;
	    }else{
	        return (menstTotal * 12 * getYears()) + (menstTotal * getMonths());
	    }
	}

	public Integer getMaxMenst(){
	    int xmax;
	    int xlochia;
	    
	    //add lochia
	    if (madhhab.equalsIgnoreCase(SHAFII)){
	        xlochia = (getYears()*60);
	    }else if (madhhab.equalsIgnoreCase(HANAFI)){
	        xlochia = (getYears()*40);            
	    }else{
	        xlochia = (getYears()*40);
	    }
	    
	    if (menstAvg == false){
	        xmax = (getYears()*12*15) + (getMonths()*15); //menses
	        xmax += xlochia;
	        
	        if (xmax > debtTotal - debtComplete){
	            xmax = debtTotal - debtComplete;
	        }
	        
	    }else{
	        xmax = 15;
	        int xmenst = (xmax * 12 * getYears())+(xmax * getMonths());
	        xmenst += xlochia;
	        while ((xmenst > debtTotal - debtComplete) && xmax >= 0) {
	            xmax -= 1;
	            xmenst = (xmax * 12 * getYears())+(xmax * getMonths());
	            xmenst += xlochia;
	        }
	    }
	    
	    return xmax;
	}
	
	public void clearTotals(){
		    debtTotal = 0;
		    debtComplete = 0;
		    fajr = 0;
		    dhuhr = 0;
		    asr = 0;
		    maghrib = 0;
		    isha = 0;
		    witr = 0;
		    menstTotal = 0;
		    menstAvg = false;
		    hasDeadline = false;

	}
	
	public Integer  getTotalDebt(){
	    int xdebtTotal = debtTotal;
	    xdebtTotal -= getMenst();
	    return xdebtTotal;
	}

	public Integer getRemaining(){
	    int x = getTotalDebt() - debtComplete;
	    
	    return x < 0 ? 0 : x;
	}
	
	public Integer getTotalPercent(){
		double xpercent = 0;
		int xtotal = this.getTotalDebt();
		int xcount = this.debtComplete;
		if  (xtotal!=0)	xpercent = Math.floor((((double)xcount/(double)xtotal)*100));
		return (int)xpercent;
	}
	
	public Integer getPrayerPercent(String xprayer){
		double xpercent = 0;
		int xtotal = this.getTotalDebt();
		int xcount = this.getPrayerComplete(xprayer);
		if  (xtotal!=0)	xpercent = Math.floor((((double)xcount/(double)xtotal)*100));
		return (int)xpercent;
	}
	
	public Integer getRemainingPrayer(String xprayer){
	    int x = 0;
	    int xdebtTotal = getTotalDebt();
	    
	    if (xprayer.equalsIgnoreCase(FAJR)){
	        x = (xdebtTotal - fajr);
	    }else if (xprayer.equalsIgnoreCase(DHUHR)){
	        x = (xdebtTotal - dhuhr);
	    }else if (xprayer.equalsIgnoreCase(ASR)){
	        x = (xdebtTotal - asr);
	    }else if (xprayer.equalsIgnoreCase(MAGHRIB)){
	        x = (xdebtTotal - maghrib);
	    }else if (xprayer.equalsIgnoreCase(ISHA)){
	        x = (xdebtTotal - isha);
	    }else if (xprayer.equalsIgnoreCase(WITR)){
	        x = (xdebtTotal - witr);
	    }
	    return x < 0 ? 0 : x;
	}

	public Integer getPrayerComplete(String xprayer){
	    int x = 0;
	    if (xprayer.equalsIgnoreCase(FAJR)){
	        x = fajr;
	    }else if (xprayer.equalsIgnoreCase(DHUHR)){
	        x = dhuhr;
	    }else if (xprayer.equalsIgnoreCase(ASR)){
	        x = asr;
	    }else if (xprayer.equalsIgnoreCase(MAGHRIB)){
	        x = maghrib;
	    }else if (xprayer.equalsIgnoreCase(ISHA)){
	        x = isha;
	    }else if (xprayer.equalsIgnoreCase(WITR)){
	        x = witr;
	    }
	    return x;
	}

	public String getDeadline(){
	    if (deadline == null) return null;
		String xformat = "dd-MM-yyyy";
	    SimpleDateFormat sdf = new SimpleDateFormat(xformat);

	    String xstring = sdf.format(deadline);  
	    return xstring;
	}
	
	public Date parseDate(String xstring){
		if (xstring == null) return null;
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date xdate;
    	try{
    		xdate = (Date)formatter.parse(xstring);
    	}catch (ParseException e){
    		xdate = Settings.today(); 
    	}
    	return xdate;
	}
	
	/*
	 * public Date getDeadlinePer(int xdaysper){
	 
	    Date xdeadline;
	    int xdaystill = getRemaining() / xdaysper;
	    
	    if (gender.equalsIgnoreCase(FEMALE)){
	        int xmenst = (xdaystill/30)*5;
	        xdaystill += xmenst;
	    }
	    Calendar c = Calendar.getInstance();
    	Date today = c.getTime();
    	double days = Math.ceil((xdeadline.getTime() - today.getTime())/MILLISECONDS_PER_DAY);
    	xdeadline
	    return xdeadline;
	}
	*/

	public Integer getDaysTill(){
		Date xtoday = Settings.today();
		long xdiff = (deadline.getTime() - xtoday.getTime());
		xdiff = (xdiff/MILLISECONDS_PER_DAY);
	    int xdays = (int)xdiff;
	    return xdays > 0 ? xdays : 0;
	}

	public String getDaysPer(){
		int xdaystill = getDaysTill();
		int xremain = getRemaining();
	    if (xdaystill == 0) return "0";
	    if (xremain == 0) return "0";
	    
	    if (type.equalsIgnoreCase(TYPE_FASTING)) return "1";
	    
	    if (gender.equalsIgnoreCase(FEMALE)){
	        int xmenst = (xdaystill/DAYS_IN_MONTH)*5;
	        xdaystill -= xmenst;
	    }

	    Integer xdaysper = (xremain/xdaystill);
	    xdaysper = xdaysper > 0 ? xdaysper : 1;

	    
	    return xdaysper.toString();
	}

	public Boolean updProgress(Context context, int xcomplete, Boolean xlog){
	    
	    if (xcomplete > getTotalDebt()) xcomplete = getTotalDebt();
	    
	    int xdiff;
	    xdiff = xcomplete - this.debtComplete;
	    this.debtComplete = xcomplete;
	    
	    if  (xlog){
	    	if  (xdiff != 0) {
	    		Settings.addLogEntry(context, this, "add", this.type, xdiff);
	    	}
    	}
	    
    	updPrayerDiff(context, FAJR,xdiff,false);
	    updPrayerDiff(context, DHUHR,xdiff,false);
	    updPrayerDiff(context, ASR,xdiff,false);
	    updPrayerDiff(context, MAGHRIB,xdiff,false);
	    updPrayerDiff(context, ISHA,xdiff,false);
	    updPrayerDiff(context, WITR,xdiff,false);
	
	    return true;
	}

	public void updPrayerDiff(Context context, String xprayer, int xdiff, Boolean xlog){
	    
	    int xcomplete = getPrayerComplete(xprayer) + xdiff;
	    
	    if  (xcomplete > getTotalDebt()) xcomplete = getTotalDebt();
	    
	    if  (xcomplete < 0) xcomplete = 0;
	    
	    xdiff = xcomplete - getPrayerComplete(xprayer);
	    
	    if  (xlog){
	    	if  (xdiff != 0) {
	    		Settings.addLogEntry(context, this, "add", xprayer, xdiff);	
	    	}
    	}
	    
	    updPrayer(context,xprayer,xcomplete,xlog);
	    
	}

	public Boolean updPrayer(Context context, String xprayer, int xvalue, Boolean xlog){
	    
	    if  ((xvalue) > getTotalDebt()) xvalue = getTotalDebt();
	    
	    if (xprayer.equalsIgnoreCase(FAJR)){
	        fajr = xvalue;
	    }else if (xprayer.equalsIgnoreCase(DHUHR)){
	        dhuhr = xvalue;
	    }else if (xprayer.equalsIgnoreCase(ASR)){
	        asr = xvalue;
	    }else if (xprayer.equalsIgnoreCase(MAGHRIB)){
	        maghrib = xvalue;
	    }else if (xprayer.equalsIgnoreCase(ISHA)){
	        isha = xvalue;
	    }else if (xprayer.equalsIgnoreCase(WITR)){
	        witr = xvalue;
	    }
	    debtComplete = getLowest();

	    return true;
	    
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(type);
        out.writeString(gender);
        out.writeString(madhhab);
        out.writeString(hasDeadline.toString());
        out.writeString(getDeadline());
        out.writeString(menstAvg.toString());
        out.writeInt(menstTotal);
        out.writeInt(debtTotal);
        out.writeInt(debtComplete);
        out.writeInt(fajr);
        out.writeInt(dhuhr);
        out.writeInt(asr);
        out.writeInt(maghrib);
        out.writeInt(isha);
        out.writeInt(witr);
        out.writeInt(increment);
        
        
    }

    public static final Parcelable.Creator<Profile> CREATOR
            = new Parcelable.Creator<Profile>() {
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };
    
    private Profile(Parcel in) {
        name = in.readString();
        type = in.readString();
        gender = in.readString();
        madhhab = in.readString();
        hasDeadline = Boolean.parseBoolean(in.readString());
        deadline = parseDate(in.readString());
        menstAvg = Boolean.parseBoolean(in.readString());
        menstTotal = in.readInt();
        debtTotal = in.readInt();
        debtComplete = in.readInt();
        fajr = in.readInt();
        dhuhr = in.readInt();
        asr = in.readInt();
        maghrib = in.readInt();
        isha = in.readInt();
        witr = in.readInt();
        increment = in.readInt();
        
        
    }

}//Profile Class