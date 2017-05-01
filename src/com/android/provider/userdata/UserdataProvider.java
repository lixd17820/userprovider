package com.android.provider.userdata;

import java.io.IOException;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.util.Log;

public class UserdataProvider extends ContentProvider {

	public static final String TAG = "UserdataProvider";

	public UserdataDatabaseHelper dbAdapter;

	public static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(Userdata.AUTHORITY, "querysyscode",
				Userdata.QUERYSYSCODE);
		uriMatcher.addURI(Userdata.AUTHORITY, "updatesyscode",
				Userdata.UPDATESYSCODE);
		uriMatcher.addURI(Userdata.AUTHORITY, "updatehmb", Userdata.UPDATEHMB);
		uriMatcher.addURI(Userdata.AUTHORITY, "queryhmb", Userdata.QUERYHMB);
		uriMatcher.addURI(Userdata.AUTHORITY, "delhmb", Userdata.DEL_HMB);
		uriMatcher.addURI(Userdata.AUTHORITY, "queryVersion",
				Userdata.QUERY_VERSION);
		uriMatcher.addURI(Userdata.AUTHORITY, "delVersion",
				Userdata.DEL_VERSION);
		uriMatcher.addURI(Userdata.AUTHORITY, "addVersion",
				Userdata.ADD_VERSION);
	}

	// 创建数据库
	@Override
	public boolean onCreate() {
		int currentCode = 0;
		try {
			PackageManager pm = getContext().getPackageManager();
			PackageInfo pi = pm.getPackageInfo("com.android.provider.userdata",
					0);
			currentCode = pi.versionCode;
		} catch (NameNotFoundException e1) {
			e1.printStackTrace();
		}

		Log.e(TAG, "on create method");
		dbAdapter = UserdataDatabaseHelper.getDBAdapterInstance(getContext(),
				currentCode);
		try {
			dbAdapter.createDataBase();
		} catch (IOException e) {
			Log.i("*** select ", e.getMessage());
		}
		dbAdapter.openDataBase();
		return true;
	}

	@Override
	public String getType(Uri uri) {
		int code = uriMatcher.match(uri);
		switch (code) {
		case Userdata.QUERYSYSCODE:
		case Userdata.QUERYHMB:
			return Userdata.CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int rowId = 0;
		switch (uriMatcher.match(uri)) {
		case Userdata.DEL_HMB:
			rowId = dbAdapter.deleteRecordInDB(Userdata.Hmb.TABLE_NAME,
					selection, selectionArgs);
			break;
		case Userdata.DEL_VERSION:
			rowId = dbAdapter.deleteRecordInDB(Userdata.Version.TABLE_NAME,
					selection, selectionArgs);
			break;
		default:
			break;
		}
		return rowId;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// SQLiteDatabase db = databasehelper.getWritableDatabase();
		long rowId = 0;
		switch (uriMatcher.match(uri)) {
		case Userdata.ADD_VERSION:
			Log.e(TAG, "query ADD_VERSION");
			rowId = dbAdapter.insertRecordsInDB(Userdata.Version.TABLE_NAME,
					values);
			break;
		}
		// rowId = db.insert(Fixcode.FrmCode.CODE_TABLE_NAME,
		// Fixcode.FrmCode._ID,
		// values);
		if (rowId > 0) {
			Uri noteUri = ContentUris.withAppendedId(Userdata.CONTENT_URI,
					rowId);
			getContext().getContentResolver().notifyChange(noteUri, null);
			return noteUri;
		}
		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Log.e(TAG, "query userdatra");
		Cursor c = null;

		switch (uriMatcher.match(uri)) {
		// 查询字典表
		case Userdata.QUERYSYSCODE:
			c = dbAdapter
					.selectRecordsFromDB(Userdata.SysCode.TABLE_NAME,
							projection, selection, selectionArgs, null, null,
							sortOrder);
			break;
		// 查询文书号码表
		case Userdata.QUERYHMB:
			c = dbAdapter
					.selectRecordsFromDB(Userdata.Hmb.TABLE_NAME, projection,
							selection, selectionArgs, null, null, sortOrder);
			break;
		case Userdata.QUERY_VERSION:
			Log.e(TAG, "query QUERY_VERSION");
			c = dbAdapter
					.selectRecordsFromDB(Userdata.Version.TABLE_NAME,
							projection, selection, selectionArgs, null, null,
							sortOrder);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int row = 0;
		switch (uriMatcher.match(uri)) {
		// 更新字典表
		case Userdata.UPDATESYSCODE:
			row = (int) dbAdapter.replaceRecordsInDB(
					Userdata.SysCode.TABLE_NAME, values);
			break;
		case Userdata.UPDATEHMB:
			row = (int) dbAdapter.replaceRecordsInDB(Userdata.Hmb.TABLE_NAME,
					values);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		return row;
	}

}
