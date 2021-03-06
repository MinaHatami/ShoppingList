package com.minahatami.shoppinglist1;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.MonthDisplayHelper;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AddNewReceipt extends Activity {
	private final int SELECT_PHOTO = 1;
	
	String imagePath;
	private ImageView imageViewAddReceipt;
	private EditText etStoreName, etReceiptAmount;
	private TextView tvPurchaseDate;
	final String pathName = "My Receipts";
	private final String TAG = "onActivityResult";

	static final int DATE_DIALOG_ID = 999;

	public static final String[] MONTHS = { "Jan", "Feb", "Mar", "Apr", "May",
			"Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
	
	private int year;
	private int month;
	private int day;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_receipt);

		etStoreName = ((EditText) findViewById(R.id.etStoreName));
		tvPurchaseDate = ((TextView) findViewById(R.id.tvPurchaseDate));
		etReceiptAmount = ((EditText) findViewById(R.id.etReceiptAmount));
		imageViewAddReceipt = (ImageView) findViewById(R.id.imageViewAddReceipt);

		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		// display the current date (this method is below)
		// updateDisplay();
	}

	public void imagePickerClick(View view) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, SELECT_PHOTO);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();

			Bitmap imageBitmap = (Bitmap) extras.get("data");
			Bitmap resizedBitmap = imageBitmap.createScaledBitmap(imageBitmap,
					50, 50, false);
			imageViewAddReceipt.setImageBitmap(resizedBitmap);

			// save it in your external storage.
			try {

				imagePath = saveImage(imageBitmap);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			Toast.makeText(getApplicationContext(),
					"Something went wrong! Please try again.",
					Toast.LENGTH_LONG).show();
		}

	}

	private String saveImage(Bitmap imageBitmap) throws Exception {
		String imageFileName = generateNewImageFileName() + ".jpg";

		Log.v(TAG, "imageFileName: " + imageFileName);

		File file = new File(getRootDirectoy(), pathName);
		if (!file.exists()) {
			file.mkdir();
		}
		file = new File(file.getAbsolutePath(), imageFileName);

		file.createNewFile();
		FileOutputStream fo = new FileOutputStream(file);
		imageBitmap.compress(Bitmap.CompressFormat.PNG, 85, fo);
		fo.flush();
		fo.close();

		Log.v(TAG, "file.getAbsolutePath(): " + file.getAbsolutePath());

		return file.getAbsolutePath();
	}

	private File getRootDirectoy() {
		// TODO Auto-generated method stub
		return Environment.getExternalStorageDirectory();
	}

	private String generateNewImageFileName() {
		return new SimpleDateFormat("yyMMdd_HHmmss").format(new Date());
	}

	public void datePickerClick(View v) {
		showDialog(DATE_DIALOG_ID);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			// set date picker as current date
			return new DatePickerDialog(this, datePickerListener, year, month,
					day);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;

			// set selected date into textview
			tvPurchaseDate.setText(new StringBuilder().append(month + 1)
					.append("-").append(day).append("-").append(year)
					.append(" "));

		}
	};

	// when this button is clicked, data will be saved and a new receipt will
	// be created.
	public void addReceiptToListClick(View view) throws SQLException {
		String storeName = etStoreName.getEditableText().toString().toUpperCase();
		String purchaseDate = tvPurchaseDate.getText().toString();
		int receiptAmount = (int) (Double.parseDouble(etReceiptAmount
				.getEditableText().toString()) * 100);
		Log.v(TAG, "receiptAmount: " + receiptAmount);

		SQLController receiptsDB = new SQLController(this);
		receiptsDB.open();

		Log.v(TAG, "imagePath: " + imagePath);

		receiptsDB
				.insertData(storeName, purchaseDate, receiptAmount, imagePath);
		Toast.makeText(this, "Succesfully Added To The List!",
				Toast.LENGTH_LONG).show();
		receiptsDB.close();

	}
}
