package com.minahatami.shoppinglist1;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.minahatami.shoppinglist1.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class ReceiptViewActivity extends Activity {
	TextView viewStoreName, viewPurchaseDate, viewReceiptAmount;
	ImageView viewReceiptImage;
	private final SimpleDateFormat form = new SimpleDateFormat("MM-dd-yyyy");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receipt_view);

		viewStoreName = (TextView) findViewById(R.id.viewStoreName);
		viewPurchaseDate = (TextView) findViewById(R.id.viewPurchaseDate);
		viewReceiptAmount = (TextView) findViewById(R.id.viewReceiptAmount);
		viewReceiptImage = (ImageView) findViewById(R.id.viewReceiptImage);

		// "Receipt" is a key from putExtra() method	
		Receipt receipt = (Receipt) this.getIntent().getSerializableExtra(MainActivity.RECEIPT_KEY);

		String newDateStr = receipt.getPurchaseDate();
		try 
		{
			//Log.v(TAG, "getPurchaseDate: " + receipt.getPurchaseDate());
			
			if(newDateStr != null && !newDateStr.isEmpty()){
				java.util.Date date = form.parse(newDateStr);
				SimpleDateFormat postFormater = new SimpleDateFormat("dd MMM, yyyy");
				newDateStr = postFormater.format(date);
			}
		}
		catch (ParseException e){
		    e.printStackTrace();
		}
		viewPurchaseDate.setText(newDateStr);
		
		viewStoreName.setText(receipt.getStoreName());
		viewReceiptAmount.setText("$" + ((double)receipt.getReceiptAmount() / 100));

		if (receipt.getImage() != null) {
			File imgFile = new File(receipt.getImage());

			if (imgFile.exists()) {

				Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
						.getAbsolutePath());
				Bitmap newBitmap = Bitmap.createScaledBitmap(myBitmap, 500, 500, false);

				viewReceiptImage.setImageBitmap(newBitmap);
			}
		}

	}
}
