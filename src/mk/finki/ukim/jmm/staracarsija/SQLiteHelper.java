package mk.finki.ukim.jmm.staracarsija;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_VENUES = "venues";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_IME = "ime";
	public static final String COLUMN_TIP = "tip";
	public static final String COLUMN_RATING = "rating";
	public static final String COLUMN_COORDS = "coords";
	public static final String COLUMN_VID = "venueId";
	public static final String COLUMN_CATEGORY = "kategorija";
	
	private static final String DATABASE_NAME = "asd.db";
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_CREATE = 
		"create table " + TABLE_VENUES + "(" +
		COLUMN_ID + " integer primary key autoincrement, " +
		COLUMN_IME + " text not null, " +
		COLUMN_TIP + " integer not null, " +
		COLUMN_RATING + " real not null, " +
		COLUMN_COORDS + " text not null, " +
		COLUMN_VID + " text not null, " +
		COLUMN_CATEGORY + " text not null);";
	
	public SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(SQLiteHelper.class.getName(), "Upgrading database from" +
				oldVersion + " to " + newVersion + "which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_VENUES);
		onCreate(db);
	}


}
