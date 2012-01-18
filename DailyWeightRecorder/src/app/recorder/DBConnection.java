package app.recorder;

import app.recorder.DailyWeightRecorderActivity.UserSchema;
import app.recorder.DailyWeightRecorderActivity.WeightSchema;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Config;
import android.util.Log;

public class DBConnection extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "DailyWeightDB";
	private static final int DATABASE_VERSION = 1;
	private static final boolean DEBUG = false;
	private static final boolean LOCAL_LOGV = DEBUG ? Config.LOGD : Config.LOGV;
	public DBConnection(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql;
		sql = "CREATE TABLE IF NOT EXISTS " + UserSchema.TABLE_NAME + " ("
				+ UserSchema.ID + " INTEGER primary key autoincrement, "
				+ UserSchema.USER_NAME + " text not null "
				+ ");";
		if (LOCAL_LOGV) {
			Log.v("SQL command =",sql);
		}
		db.execSQL(sql);
		sql = "CREATE TABLE IF NOT EXISTS " + WeightSchema.TABLE_NAME + " ("
				+ WeightSchema.WEIGHT_ID + " INTEGER primary key autoincrement, "
				+ WeightSchema.UID + " INTEGER not null, "
				+ WeightSchema.DATE + " text not null, " 
				+ WeightSchema.WEIGHT + " decimal not null"
				+ ");";
		if (LOCAL_LOGV) {
			Log.v("SQL command =",sql);
		}
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
