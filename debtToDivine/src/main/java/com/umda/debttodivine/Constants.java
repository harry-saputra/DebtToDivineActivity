package com.umda.debttodivine;
import android.provider.BaseColumns;

public interface Constants extends BaseColumns {
	
	public static final String PROFILE_OBJECT = "profile_object";
	
	public static final String PROFILE_TABLE = "profiles";
	public static final String PRAYER_TABLE = "prayers";
	public static final String LOG_TABLE = "logs";
	

	public static final String PROFILE_ID_COL = "profile_id";
	public static final String MADHHAB_COL = "madhhab";
	public static final String TYPE_COL = "type";
	public static final String GENDER_COL = "gender";
	public static final String DEBT_TOTAL_COL = "total_debt";
	public static final String DEBT_COMPLETE_COL = "total_complete";
	public static final String HAS_DEADLINE_COL = "has_deadline";
	public static final String DEADLINE_COL = "deadline_date";
	public static final String MENST_TOTAL_COL = "menst_total";
	public static final String MENST_AVG_COL = "menst_avg";
	public static final String INCREMENT_COL = "increment";
	
	public static final String PROFILE_COL = "profile";
	public static final String PRAYER_ID_COL = "prayer_id";
	public static final String PRAYER_COMPLETE_COL = "prayer_complete";
	
	public static final String LOG_ACTION_COL = "log_action";
	public static final String LOG_TYPE_COL = "log_type";
	public static final String LOG_AMOUNT_COL = "log_amount";
	public static final String LOG_PROFILE_COL = "log_profile";
	public static final String LOG_DATE_COL = "log_date";
	
	public static final Integer DAYS_IN_YEAR = 365;
	public static final Integer DAYS_IN_MONTH = 30;
	
	public static final String FAJR = "Fajr";
	public static final String DHUHR = "Dhuhr";
	public static final String ASR = "Asr";
	public static final String MAGHRIB = "Maghrib";
	public static final String ISHA = "Isha";
	public static final String WITR = "Witr";
	
	public static final String HANAFI = "Hanafi";
	public static final String SHAFII = "Shafii";
	public static final String MALIKI = "Maliki";
	public static final String HANBALI = "Hanbali";
	
	public static final String TYPE_PRAYER = "Prayer";
	public static final String TYPE_FASTING = "Fasting";
	public static final String TYPE_MONETARY = "Monetary";
	public static final String TYPE_EXPIATION = "Expiation";
	
	public static final String  MALE = "Male";
	public static final String  FEMALE = "Female";
	
	public static final long MILLISECONDS_PER_DAY = 86400000;
	
}