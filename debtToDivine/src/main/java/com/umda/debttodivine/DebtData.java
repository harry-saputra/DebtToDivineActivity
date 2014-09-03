package com.umda.debttodivine;

import static android.provider.BaseColumns._ID;
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
import static com.umda.debttodivine.Constants.LOG_TABLE;
import static com.umda.debttodivine.Constants.LOG_PROFILE_COL;
import static com.umda.debttodivine.Constants.LOG_ACTION_COL;
import static com.umda.debttodivine.Constants.LOG_AMOUNT_COL;
import static com.umda.debttodivine.Constants.LOG_TYPE_COL;
import static com.umda.debttodivine.Constants.LOG_DATE_COL;
import static com.umda.debttodivine.Constants.SHAFII;
import static com.umda.debttodivine.Constants.TYPE_COL;
import static com.umda.debttodivine.Constants.TYPE_PRAYER;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DebtData extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "divinedebt.db" ;
	private static final int DATABASE_VERSION = 16;
	
	/** Create a helper object for the Events database */
	public DebtData(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + PROFILE_TABLE + 
				" (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
				PROFILE_ID_COL + " TEXT NOT NULL," + 
				TYPE_COL + " TEXT NOT NULL DEFAULT ('"+TYPE_PRAYER+"')," +
				GENDER_COL + " TEXT DEFAULT ('"+MALE+"')," +
				MADHHAB_COL + " TEXT NOT NULL," + 
				MENST_TOTAL_COL + " INTEGER," +
				MENST_AVG_COL + " INTEGER," +
				INCREMENT_COL + " INTEGER," +
				DEBT_TOTAL_COL + " INTEGER," + 
				DEBT_COMPLETE_COL + " INTEGER," + 
				HAS_DEADLINE_COL + " INTEGER," + 
				DEADLINE_COL + " TEXT);"
				);
		db.execSQL("CREATE TABLE " + PRAYER_TABLE + 
				" (" +  _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
				PROFILE_COL + " TEXT," +
				PRAYER_ID_COL + " TEXT," + 				
				PRAYER_COMPLETE_COL + " INTEGER," + 
				"FOREIGN KEY ("+PROFILE_COL+") REFERENCES "+PROFILE_TABLE+" ("+_ID+"));");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion == 1 && newVersion == 13){
			db.execSQL("ALTER TABLE " + PROFILE_TABLE + " ADD COLUMN " + TYPE_COL + " TEXT NOT NULL DEFAULT ('"+TYPE_PRAYER+"');");
			db.execSQL("ALTER TABLE " + PROFILE_TABLE + " ADD COLUMN " + GENDER_COL + " TEXT DEFAULT ('"+MALE+"');");
			db.execSQL("ALTER TABLE " + PROFILE_TABLE + " ADD COLUMN " + MENST_TOTAL_COL + " INTEGER;");
			db.execSQL("ALTER TABLE " + PROFILE_TABLE + " ADD COLUMN " + MENST_AVG_COL + " INTEGER;");
			db.execSQL("ALTER TABLE " + PROFILE_TABLE + " ADD COLUMN " + INCREMENT_COL + " INTEGER;");
			ContentValues values = new ContentValues();
			
			values.put(TYPE_COL, TYPE_PRAYER);
			values.put(GENDER_COL, MALE);
			values.put(MENST_TOTAL_COL, 0);
			values.put(MENST_AVG_COL, 0);
			values.put(INCREMENT_COL, 1);
			
			db.update(PROFILE_TABLE, values, null, null);
			
			values.clear();
			values.put(MADHHAB_COL, SHAFII);
			db.update(PROFILE_TABLE, values, MADHHAB_COL+ "<>?", new String[]{HANAFI});
			
		}
		if (newVersion == 16){
			db.execSQL("CREATE TABLE " + LOG_TABLE + 
					" (" +  _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
					LOG_PROFILE_COL + " TEXT," +
					LOG_ACTION_COL + " TEXT," +
					LOG_TYPE_COL + " TEXT," + 				
					LOG_AMOUNT_COL + " INTEGER," + 
					LOG_DATE_COL + " TEXT," + 
					"FOREIGN KEY ("+LOG_PROFILE_COL+") REFERENCES "+PROFILE_TABLE+" ("+_ID+"));");
		}
		//else{
			//db.execSQL("DROP TABLE " + PROFILE_TABLE +";");
			//db.execSQL("DROP TABLE " + PRAYER_TABLE +";");
			//onCreate(db);
		//}
		
		
	}
}