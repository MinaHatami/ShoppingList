package com.minahatami.shoppinglist1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyArrayAdapter extends ArrayAdapter<Receipt> {
	private static final String TAG = "MyArrayAdapter";
	private Context mContext;
	private List<Receipt> receipts;
	private final int THUMBSIZE = 120;
	private final SimpleDateFormat form = new SimpleDateFormat("MM-dd-yyyy");

	public MyArrayAdapter(Context context, List<Receipt> receipts) {
		super(context, R.layout.activity_shopping_list_item, receipts);

		this.mContext = context;
		this.receipts = receipts;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		View view = convertView;

		if (view == null) {

			// inflate the layout
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			view = inflater.inflate(R.layout.activity_shopping_list_item,
					parent, false);

			// well set up the ViewHolder
			viewHolder = new ViewHolder();
			viewHolder.storeName = (TextView) view.findViewById(R.id.tvStoreName);
			viewHolder.purchaseDate = (TextView) view
					.findViewById(R.id.tvPurchaseDate);
			viewHolder.receiptAmount = (TextView) view.findViewById(R.id.tvReceiptAmount);
			viewHolder.imgViewReceipt = (ImageView) view.findViewById(R.id.imgViewReceipt);

			// store the holder with the view.
			view.setTag(viewHolder);

		} else {
			// we've just avoided calling findViewById() on resource every time
			// just use the viewHolder
			viewHolder = (ViewHolder) view.getTag();
		}

		Receipt receipt = receipts.get(position);

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
		
		viewHolder.purchaseDate.setText(newDateStr);
				
		viewHolder.storeName.setText(receipt.getStoreName());
		viewHolder.receiptAmount.setText("$" + ((double)receipt.getReceiptAmount() / 100));
		
		Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(
				getBitmap(receipt.getImage()), THUMBSIZE, THUMBSIZE);
		viewHolder.imgViewReceipt.setImageBitmap(ThumbImage);
		// setPic( , viewHolder.image);
		Log.v(TAG, "currentReceipts.getImage(): " + receipt.getImage());
		return view;
	}
	
	// Avoid running out of memory exception.
		public void setPic(String mCurrentPhotoPath, ImageView mImageView) {
			// Get the dimensions of the View
			int targetW = mImageView.getWidth();
			int targetH = mImageView.getHeight();

			// Get the dimensions of the bitmap
			BitmapFactory.Options bmOptions = new BitmapFactory.Options();
			bmOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
			int photoW = bmOptions.outWidth;
			int photoH = bmOptions.outHeight;

			// Determine how much to scale down the image
			int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

			// Decode the image file into a Bitmap sized to fill the View
			bmOptions.inJustDecodeBounds = false;
			bmOptions.inSampleSize = scaleFactor;
			bmOptions.inPurgeable = true;

			Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
			mImageView.setImageBitmap(bitmap);
		}

		

	private Bitmap getBitmap(String name) {
		// TODO Auto-generated method stub
		return BitmapFactory.decodeFile(name);
	}

	private static class ViewHolder {
		TextView storeName, purchaseDate, receiptAmount;
		ImageView imgViewReceipt;
	}
}
