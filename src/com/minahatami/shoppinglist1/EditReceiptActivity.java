package com.minahatami.shoppinglist1;

import java.io.File;
import java.util.Calendar;

import com.minahatami.shoppinglist1.R;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EditReceiptActivity extends Activity {

	private final int SELECT_PHOTO = 1;

	EditText etNewStoreName, etNewReceiptAmount;
	TextView tvNewPurchaseDate;
	ImageView newImageView;
	private String path;

	static final int DATE_DIALOG_ID = 999;

	private int year;
	private int month;
	private int day;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_receipt);

		etNewStoreName = (EditText) findViewById(R.id.etNewStoreName);
		etNewReceiptAmount = (EditText) findViewById(R.id.etNewReceiptAmount);
		tvNewPurchaseDate = (TextView) findViewById(R.id.tvNewPurchaseDate);
		newImageView = (ImageView) findViewById(R.id.newReceiptImage);

		Receipt receipt = (Receipt) this.getIntent().getSerializableExtra(MainActivity.RECEIPT_KEY);
		
		etNewStoreName.setText(receipt.getStoreName());
		etNewReceiptAmount.setText("" + ((double)receipt.getReceiptAmount() / 100));
		tvNewPurchaseDate.setText(receipt.getPurchaseDate());
		path = receipt.getImage();
				
		if (path != null) {
			File imgFile = new File(path);

			if (imgFile.exists()) {

				Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
						.getAbsolutePath());

				newImageView.setImageBitmap(myBitmap);
			}
		}
		
		final Calendar c = Calendar.getInstance();
	    year = c.get(Calendar.YEAR);
	    month = c.get(Calendar.MONTH);
	    day = c.get(Calendar.DAY_OF_MONTH);

	}

	public void newDatePickerClick(View view) {
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
			tvNewPurchaseDate.setText(new StringBuilder().append(month + 1)
					.append("-").append(day).append("-").append(year)
					.append(" "));
		}
	};

	public void newImagePickerClick(View view) {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, SELECT_PHOTO);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK
				&& data != null) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			path = cursor.getString(columnIndex);
			cursor.close();

			// here you can call createImageThumbnail method passing (picturePath,480,800)
			// and set the received bitmap imageview directly instead of storing in bitmap.
			// eg. imageView.setImageBitmap(createImageThumbnail( picturePath, 480, 800));
			// imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath));

			Bitmap bitmap = BitmapFactory.decodeFile(path,
					new BitmapFactory.Options());
			newImageView.setImageBitmap(bitmap);
		}
	}

	public void editReceiptClick(View view) {
		String newStoreName = etNewStoreName.getEditableText().toString()
				.toUpperCase();
		int newReceiptAmount = (int) (Double.parseDouble(etNewReceiptAmount.getEditableText().toString()) * 100);
		String newPurchaseDate = tvNewPurchaseDate.getText().toString();

		int memberID = getIntent().getExtras().getInt("memberID");
		Log.d("MainActivity", "memberID is : " + memberID);

		SQLController db = new SQLController(this);
		db.open();
		if (db.updateData(memberID, newStoreName, newReceiptAmount,
				newPurchaseDate, path) > 0) {
			Toast.makeText(this, "Successfully Edited!", Toast.LENGTH_SHORT)
					.show();
		} else {
			Toast.makeText(this, "Unsuccessful!", Toast.LENGTH_SHORT).show();
		}
		db.updateData(memberID, newStoreName, newReceiptAmount,
				newPurchaseDate, path);
		db.close();
	}

}
