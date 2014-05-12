package database;


import java.util.ArrayList;

import com.google.android.gms.internal.cu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandlerNums extends SQLiteOpenHelper {

	// All static variables
	// Database version
	private static final int DATABASE_VERSION = 1;

	// Database name
	private static final String DATABASE_NAME_NUM = "NumsManager";

	// Images table name
	private static final String TABLE_NUMS = "table_nums";

	// Images table columns names
	private static final String NUMBER = "number";
	private static final String CONTENT = "content";

	public DatabaseHandlerNums(Context context) {
		super(context, DATABASE_NAME_NUM, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_IMAGES_TABLE = "CREATE TABLE " + TABLE_NUMS + " ("
				+ NUMBER + " VARCHAR PRIMARY KEY " + "," + CONTENT
				+ " VARCHAR)";
		db.execSQL(CREATE_IMAGES_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NUMS);
		// Create tables again
		onCreate(db);
	}

	public long addNum(String number, String content) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(NUMBER, number);
		values.put(CONTENT, content);
		return db.insert(TABLE_NUMS, null, values);

	}

	public String getNumber(String number) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(TABLE_NUMS, new String[] { NUMBER, CONTENT },
				NUMBER + "=?", new String[] { number }, null, null, null, null);
		cursor.moveToFirst();
		if (cursor != null && cursor.getCount() > 0) {
			return cursor.getString(0) + "-" + cursor.getString(1);
		} else {
			return null;
		}

	}

	// Getting All Images
	public ArrayList<String> getAllNumbers() {
		ArrayList<String> imageList = new ArrayList<String>();
		// Select All Query
		String selectQuery = "";
		selectQuery = "SELECT  * FROM " + TABLE_NUMS;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				imageList
						.add(cursor.getString(0) + " - " + cursor.getString(1));
			} while (cursor.moveToNext());
		}

		// return contact list
		return imageList;
	}

	// Getting images Count
	public int getNumbersCount() {
		String countQuery = "";
		countQuery = "SELECT  * FROM " + TABLE_NUMS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		// return count
		return cursor.getCount();
	}

	// Deleting single image
	public void deleteImage(String number) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NUMS, NUMBER + " = ?", new String[] { number });
		db.close();
	}

}
