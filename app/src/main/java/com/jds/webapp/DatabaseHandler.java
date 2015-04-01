package com.jds.webapp;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 2;
	// Database Name
	private static final String DATABASE_NAME = "DataSaved";
	// Contacts table name
	private static final String T_ARTICLE = "t_article";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_KEY = "key";
	private static final String KEY_TITLE = "title";
	private static final String KEY_DATE = "date";
	private static final String KEY_AUTHOR = "author";
	private static final String KEY_PV = "pv";
	private static final String KEY_THUMBNAIL = "thumbnail";


	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v("DBMatome", "onCreate");
		String CREATE_TABLE = "CREATE TABLE " + T_ARTICLE + "("
				+ KEY_ID + " TEXT PRIMARY KEY,"
				+ KEY_KEY + " TEXT,"
				+ KEY_TITLE + " TEXT,"
				+ KEY_DATE + " TEXT,"
				+ KEY_AUTHOR + " TEXT,"
				+ KEY_PV + " TEXT,"
				+ KEY_THUMBNAIL + " TEXT"
				+ ")";
		db.execSQL(CREATE_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.v("DBMatome", "OnUpgrade");
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + T_ARTICLE);

		// Create tables again
		onCreate(db);
	}


	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new contact
	public void addArticle(DataListSavedArticle dataListSavedArticle) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, dataListSavedArticle.getId());
		values.put(KEY_KEY, dataListSavedArticle.getKey());
		values.put(KEY_TITLE, dataListSavedArticle.getTitle());
		values.put(KEY_DATE, dataListSavedArticle.getDate());
		values.put(KEY_AUTHOR, dataListSavedArticle.getAuthor());
		values.put(KEY_PV, dataListSavedArticle.getPv());
		values.put(KEY_THUMBNAIL, dataListSavedArticle.getThumbnail());

		// Inserting Row
		db.insert(T_ARTICLE, null, values);
		db.close(); // Closing database connection
	}

	public DataListSavedArticle getArticle(String id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(T_ARTICLE, new String[] { KEY_ID,
				KEY_KEY, KEY_TITLE, KEY_DATE, KEY_AUTHOR, KEY_PV, KEY_THUMBNAIL }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		DataListSavedArticle dataListSavedArticle = new DataListSavedArticle(cursor.getString(0),
				cursor.getString(1), cursor.getString(2), cursor.getString(3),
				cursor.getString(4), cursor.getString(5), cursor.getString(6));
		return dataListSavedArticle;
	}
	
	// Getting All Contacts
	public List<DataListSavedArticle> getAllSavedArticles() {
		List<DataListSavedArticle> articleList = new ArrayList<DataListSavedArticle>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + T_ARTICLE;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				DataListSavedArticle dataListSavedArticle = new DataListSavedArticle();
				dataListSavedArticle.setId(cursor.getString(0));
				dataListSavedArticle.setKey(cursor.getString(1));
				dataListSavedArticle.setTitle(cursor.getString(2));
				dataListSavedArticle.setDate(cursor.getString(3));
				dataListSavedArticle.setAuthor(cursor.getString(4));
				dataListSavedArticle.setPv(cursor.getString(5));
				dataListSavedArticle.setThumbnail(cursor.getString(6));
				// Adding contact to list
				articleList.add(dataListSavedArticle);
			} while (cursor.moveToNext());
		}

		// return contact list
		return articleList;
	}


	/*
 * Deleting a todo
 */
	public void deleteSaved(String article_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(T_ARTICLE, KEY_ID + " = ?",
				new String[]{String.valueOf(article_id) });
	}


	// Getting contacts Count
	public int getArticlesCount() {
		String countQuery = "SELECT  * FROM " + T_ARTICLE;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}
	// Getting contacts Count
	public long getArticleCount(String id) {
		String countQuery = "SELECT  * FROM " + T_ARTICLE +
				" WHERE " + KEY_ID + " = '" + id + "'";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		long cursorCount = cursor.getCount();
		cursor.close();

		// return count
		return cursorCount ;
	}

}
