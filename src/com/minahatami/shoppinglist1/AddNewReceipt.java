package com.minahatami.shoppinglist1;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AddNewReceipt extends Activity{
	private final int SELECT_PHOTO = 1;
	private String storeName, receiptAmount, purchaseDate, imagePath;
	private ImageView imageViewAddReceipt;
	private EditText etStoreName, etReceiptAmount;
	private TextView tvPurchaseDate;
	final String pathName = "My Receipts";
	private final String TAG = "onActivityResult";

	static final int DATE_DIALOG_ID = 999;

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
	}

	public void imagePickerClick(View view) {
			/*Intent intent = new Intent(Intent.ACTION_PICK);
			intent.setType("image/*");*/
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent, SELECT_PHOTO);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			Bitmap imageBitmap = (Bitmap) extras.get("data");
			imageViewAddReceipt.setImageBitmap(imageBitmap);
			// save it in your external storage.
			try {

				imagePath = saveImage(imageBitmap);
				Bitmap bitmap = BitmapFactory.decodeFile(imagePath, new BitmapFactory.Options());
				//imageViewAddReceipt.setImageBitmap(bitmap);
				// create new picitem
				// myPics.add(new Pic(imgPath, imgPath));

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			Toast.makeText(getApplicationContext(),
					"Something went wrong! Please try again.",
					Toast.LENGTH_LONG).show();
		}
		
		
		
		
		/*	if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null) {
			super.onActivityResult(requestCode, resultCode, data);
			
			Uri selectedImage = data.getData();  
	            String[] filePathColumn = { MediaStore.Images.Media.DATA };  

	            Cursor cursor = getContentResolver().query(selectedImage,  
	                    filePathColumn, null, null, null);  
	            cursor.moveToFirst();  

	            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);  
	            imagePath = cursor.getString(columnIndex);  
	            cursor.close();

	            //here you can call createImageThumbnail method passing (picturePath,480,800) 
	            //and set the received bitmap imageview directly instead of storing in bitmap.
	            // eg. imageView.setImageBitmap(createImageThumbnail( picturePath, 480, 800));
	            // imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath));
	            
Bitmap bitmap = BitmapFactory.decodeFile(imagePath, new BitmapFactory.Options());
imageViewAddReceipt.setImageBitmap(bitmap); 
		}*/ 
		
	}
	
	private String saveImage(Bitmap imageBitmap) throws Exception {
		String imageFileName = generateNewImageFileName() + ".jpg";
		Log.v(TAG, "imageFileName: " + imageFileName);
		File file = new File(getRootDirectoy(), pathName + "/" + imageFileName);
		file.createNewFile();
		FileOutputStream fo = new FileOutputStream(file);
		imageBitmap.compress(Bitmap.CompressFormat.PNG, 85, fo);
		fo.flush();
		fo.close();

		return file.getAbsolutePath();
	}

	/*Bitmap createImageThumbnail(String imagePath, int width, int height) {
		Bitmap bitmap = null;
		  BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();  
		  bmpFactoryOptions.inJustDecodeBounds = true;  
		  int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight  
		    / (float) height);  
		  int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth  
		    / (float) width);  

		  if (heightRatio > 1 || widthRatio > 1) {  
		   if (heightRatio > widthRatio) {  
		    bmpFactoryOptions.inSampleSize = heightRatio;  
		   } else {  
		    bmpFactoryOptions.inSampleSize = widthRatio;  
		   }  
		  }  
		  bmpFactoryOptions.inJustDecodeBounds = false;    

		  bitmap = BitmapFactory.decodeFile(imagePath, bmpFactoryOptions);  
		  return bitmap;  
		 } 
 */

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

			// set selected date into datepicker also
			// dpResult.init(year, month, day, null);

		}
	};

	//TODO: 
	//when this button is clicked, data will be saved and a new receipt will
	// be created.
	public void addReceiptToListClick(View view) throws SQLException {
		storeName = etStoreName.getEditableText().toString().toUpperCase();
		purchaseDate = tvPurchaseDate.getText().toString();
		receiptAmount = "$" + etReceiptAmount.getEditableText().toString();

		SQLController receiptsDB = new SQLController(this);
		receiptsDB.open();
		
		receiptsDB.insertData(storeName, purchaseDate, receiptAmount, imagePath);
		Toast.makeText(this, "Succesfully Added To The List!", Toast.LENGTH_LONG).show();
		receiptsDB.close();
		
	}
}
