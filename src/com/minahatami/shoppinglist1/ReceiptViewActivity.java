package com.minahatami.shoppinglist1;

import java.io.File;

import com.minahatami.shoppinglist1.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ReceiptViewActivity extends Activity{
	TextView viewStoreName, viewPurchaseDate, viewReceiptAmount;
	ImageView viewReceiptImage;

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
		String receiptAmount = this.getIntent().getStringExtra("receiptAmount");
		String path = this.getIntent().getStringExtra("path");

		viewStoreName.setText(storeName);
		viewPurchaseDate.setText(purchaseDate);
		viewReceiptAmount.setText(receiptAmount);

		File imgFile = new File(path);

		if (imgFile.exists()) {

			Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
					.getAbsolutePath());

			viewReceiptImage.setImageBitmap(myBitmap);

		}

	}
}
