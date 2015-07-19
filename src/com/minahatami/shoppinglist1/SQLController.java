package com.minahatami.shoppinglist1;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public final class SQLController {
	private final String TAG = "SQLController";
	private DBHelper dbHelper;
	private final Context context;
	private SQLiteDatabase database;

	public SQLController(Context c) {
		context = c;
	}

	public SQLiteDatabase getDatabase() {
		return database;
	}

	public SQLController open() {
		dbHelper = new DBHelper(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	public void insertData(String storeName, String purchaseDate,
			int receiptAmount, String imagePath) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();

		cv.put(DBHelper.COLUMN_STORENAME, storeName);
		cv.put(DBHelper.COLUMN_PURCHASEDATE, purchaseDate);
		cv.put(DBHelper.COLUMN_RECEIPTAMOUNT, receiptAmount);
		cv.put(DBHelper.COLUMN_IMAGE, imagePath);

		database.insert(DBHelper.TABLE_Receipts, null, cv);

	}

	public List<Receipt> getReceipts() {
		List<Receipt> receipts = new ArrayList<Receipt>();

		Cursor cursor = readData();
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Receipt c = new Receipt(cursor.getInt(0), cursor.getString(1),
						cursor.getString(2), cursor.getInt(3),
						cursor.getString(4));

				receipts.add(c);
			}

			cursor.close();
		}

		return receipts;
	}

	public Cursor readData() {
		Log.v(TAG, "database is null: " + (database == null));

		String[] allColumns = new String[] { DBHelper.COLUMN_ENTRY_ID,
				DBHelper.COLUMN_STORENAME, DBHelper.COLUMN_PURCHASEDATE,
				DBHelper.COLUMN_RECEIPTAMOUNT, DBHelper.COLUMN_IMAGE };

		this.open();

		Log.v(TAG, "database is null: " + (database == null));

		Cursor c = database.query(DBHelper.TABLE_Receipts, allColumns, null,
				null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}

	public int updateData(long memberID, String storeName,
			int receiptAmount, String purchaseDate, String imagePath) {
		ContentValues cvUpdate = new ContentValues();

		// TODO:
		cvUpdate.put(DBHelper.COLUMN_STORENAME, storeName);
		cvUpdate.put(DBHelper.COLUMN_RECEIPTAMOUNT, receiptAmount);
		cvUpdate.put(DBHelper.COLUMN_PURCHASEDATE, purchaseDate);
		cvUpdate.put(DBHelper.COLUMN_IMAGE, imagePath);
		int i = database.update(DBHelper.TABLE_Receipts, cvUpdate,
				DBHelper.COLUMN_ENTRY_ID + " = " + memberID, null);
		return i;
	}

	public void deleteData(long memberID) {
		database.delete(DBHelper.TABLE_Receipts, DBHelper.COLUMN_ENTRY_ID + "="
				+ memberID, null);
	}

	public boolean verification(String name, String dateOfBirth)
			throws SQLException {
		int count = -1;
		Cursor c = null;
		try {
			String query = "SELECT COUNT(*) FROM " + DBHelper.TABLE_Receipts
					+ " WHERE " + DBHelper.COLUMN_STORENAME + " = ? AND "
					+ DBHelper.COLUMN_PURCHASEDATE + " = ?";
			c = database.rawQuery(query, new String[] { name, dateOfBirth });
			if (c.moveToFirst()) {
				count = c.getInt(0);
			}
			return count > 0;
		} finally {
			if (c != null) {
				c.close();
			}
		}
	}

	public double calculate(String storeName, int month) {
		Cursor c = null;
		double amount = 0;
		try {

			String query = "SELECT SUM(" + DBHelper.COLUMN_RECEIPTAMOUNT
					+ ") FROM " + DBHelper.TABLE_Receipts + " WHERE "
					+ DBHelper.COLUMN_STORENAME + " like '%" + storeName + "%' AND "
					+ DBHelper.COLUMN_PURCHASEDATE + " like '%" + month + "%'";
			
			/*
			 * + "AND strftime('%m', " + DBHelper.COLUMN_PURCHASEDATE + ") = ?";
			 */

			/*c = database.query(DBHelper.TABLE_Receipts, new String[] { "SUM("
					+ DBHelper.COLUMN_RECEIPTAMOUNT + ")" }, " like %'"
					+ storeName + "'%", new String[] {}, null, null, null);*/
			
//			c = database.query(DBHelper.TABLE_Receipts, new String[] { "SUM("
//					+ DBHelper.COLUMN_RECEIPTAMOUNT + ")" }, DBHelper.COLUMN_STORENAME + " LIKE '?'",
//					new String[] { storeName + "%" }, null, null, null);
			 c = database.rawQuery(query, null);
			// .rawQuery(query, new String[] { storeName, "" + month });

			if (c != null && c.moveToFirst()) {
				amount = c.getDouble(0);
			}
			Log.v(TAG, "amount: " + amount);
			Log.v(TAG, "storeName: " + storeName);

			return amount;
		} finally {
			if (c != null) {
				c.close();
			}
		}
	}
}
