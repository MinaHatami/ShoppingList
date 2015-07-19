package com.minahatami.shoppinglist1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{
	//Table Information
		public static final String TABLE_Receipts = "ReceiptTable";
		public static final String COLUMN_ENTRY_ID = "receiptId";
		public static final String COLUMN_STORENAME = "storeName";
		public static final String COLUMN_PURCHASEDATE = "purchaseDate";
		public static final String COLUMN_RECEIPTAMOUNT = "receiptAmount";
		public static final String COLUMN_IMAGE = "receiptimage";

		//Database Information
		 static final String DATABASE_NAME = "ReceiptList";
		 static final int DATABASE_VERSION = 3;

		public DBHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL("CREATE TABLE " + TABLE_Receipts + " (" + COLUMN_ENTRY_ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_STORENAME
					+ " TEXT NOT NULL, " + COLUMN_PURCHASEDATE + " TEXT, "
					+ COLUMN_RECEIPTAMOUNT + " INTEGER, " + COLUMN_IMAGE
					+ " TEXT)");

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_Receipts);
			onCreate(db);

		}

		public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			onUpgrade(db, oldVersion, newVersion);
		}
}
