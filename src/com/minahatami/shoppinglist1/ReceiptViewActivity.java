package com.minahatami.shoppinglist1;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.minahatami.shoppinglist1.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

		// "path" is a key from putExtra() method
		String storeName = this.getIntent().getStringExtra("storeName");
		String purchaseDate = this.getIntent().getStringExtra("purchaseDate");
		int receiptAmount = this.getIntent().getIntExtra("receiptAmount", 0);
		String path = this.getIntent().getStringExtra("path");

		java.util.Date date = null;
		try 
		{
		    date = form.parse(purchaseDate);
		}
		catch (ParseException e) 
		{

		    e.printStackTrace();
		}
		SimpleDateFormat postFormater = new SimpleDateFormat("dd MMM, yyyy");
		String newDateStr = postFormater.format(date);
		viewPurchaseDate.setText(newDateStr);
		
		viewStoreName.setText(storeName);
		viewReceiptAmount.setText("$" + ((double)receiptAmount / 100));

		if (path != null) {
			File imgFile = new File(path);

			if (imgFile.exists()) {

				Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
						.getAbsolutePath());
				Bitmap newBitmap = Bitmap.createScaledBitmap(myBitmap, 500, 500, false);

				viewReceiptImage.setImageBitmap(newBitmap);
			}
		}

	}
}
