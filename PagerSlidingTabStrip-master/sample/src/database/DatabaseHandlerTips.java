package database;


import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandlerTips extends SQLiteOpenHelper {

	// All static variables
	// Database version
	private static final int DATABASE_VERSION = 1;

	// Database name
	private static final String DATABASE_NAME_TIP = "TipsManager";

	// Images table name
	private static final String TABLE_TIPS = "table_tips";

	// Images table columns names
	private static final String TITLE = "title";
	private static final String CONTENT = "content";

	public DatabaseHandlerTips(Context context) {
		super(context, DATABASE_NAME_TIP, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i("create db", "createdb");
		String CREATE_IMAGES_TABLE = "CREATE TABLE " + TABLE_TIPS + "(" + TITLE
				+ " VARCHAR PRIMARY KEY" + "," + CONTENT + " VARCHAR)";
		db.execSQL(CREATE_IMAGES_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIPS);
		// Create tables again
		onCreate(db);
	}

	public long addTip(String title, String content) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TITLE, title); // Image Name
		values.put(CONTENT, content);
		return db.insert(TABLE_TIPS, null, values);

	}

	public String getTip(String title) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(TABLE_TIPS, new String[] { CONTENT }, TITLE
				+ "=?", new String[] { title }, null, null, null, null);
		cursor.moveToFirst();
		if (cursor != null && cursor.getCount() > 0) {
			return cursor.getString(0);
		} else {
			return null;
		}

	}

	// Getting All Images
	public ArrayList<String> getAllTips() {
		ArrayList<String> imageList = new ArrayList<String>();
		// Select All Query
		String selectQuery = "";
		selectQuery = "SELECT  * FROM " + TABLE_TIPS;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				imageList.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}

		// return contact list
		return imageList;
	}

	// Getting images Count
	public int getTipsCount() {
		String countQuery = "";
		countQuery = "SELECT  * FROM " + TABLE_TIPS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		// return count
		return cursor.getCount();
	}

	// Deleting single image
	public void deleteTip(String title) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_TIPS, TITLE + " = ?", new String[] { title });
		db.close();
	}

}
