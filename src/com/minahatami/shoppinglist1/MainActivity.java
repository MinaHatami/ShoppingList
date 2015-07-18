package com.minahatami.shoppinglist1;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";
	
	ListView receiptList;
	private List<Receipt> receipts;
	ArrayAdapter<Receipt> adapter;
	SQLController sqlController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping_list);

		sqlController = new SQLController(this);
		//receipts = sqlController.getReceipts();
		receiptList = (ListView) findViewById(R.id.listView);
		//adapter = new MyArrayAdapter(this, receipts);

		// When clicked on an item view
		receiptList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(parent.getContext(),
						ReceiptViewActivity.class);
				// putExtra adds extended data to theintent.
				// "path" is a key which will be used by PictureViewActivity
				// class to get the path of the file
				// intent.putExtra("path",receipts.get(position).getImage());
				startActivity(intent);

			}
		});

		// When long-clicked on an item view
		receiptList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("Long Click", "USED!");
				return false;
			}
		});

		//receiptList.setAdapter(adapter);
		registerForContextMenu(receiptList);
	}

	@Override
	protected void onResume() {
		super.onResume();
		receipts = sqlController.getReceipts();
		adapter = new MyArrayAdapter(this, receipts);
		receiptList.setAdapter(adapter);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		
		Log.d("Menu", "Menu");
		menu.setHeaderTitle("Choose Action!");
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		switch (item.getItemId()) {
		case R.id.view:
			Log.d("getItemID", "view");

			Intent i = new Intent(this, ReceiptViewActivity.class);
			// ???? Revise this
			transferData(i, info);
			
			startActivity(i);

			return true;

		case R.id.edit:
			Log.d("getItemID", "edit");

			Intent intent = new Intent(this, EditReceiptActivity.class);
			transferData(intent, info);
			startActivity(intent);

			// ?????? Update adapter after update SQLite----->
			// onContentChanged()
			//adapter.notifyDataSetChanged();

			return true;

		case R.id.delete:
			Log.d("getItemID", "delete");

			int customerId = adapter.getItem(info.position).getId();
			
			
			sqlController.deleteData(customerId);
			// sqlController.close();

			adapter.remove(adapter.getItem(info.position));
			adapter.notifyDataSetChanged();

			return true;

		default:
			return super.onContextItemSelected(item);
		}

	}

	private void transferData(Intent i, AdapterContextMenuInfo info) {
		i.putExtra("memberID", adapter.getItem(info.position).getId());
		i.putExtra("storeName", adapter.getItem(info.position)
				.getStoreName());
		i.putExtra("purchaseDate", adapter.getItem(info.position)
				.getPurchaseDate());
		i.putExtra("path", adapter.getItem(info.position).getImage());
		i.putExtra("receiptAmount", adapter.getItem(info.position)
				.getReceiptAmount());
		
		Log.v(TAG, "path: "+ adapter.getItem(info.position).getImage());
		
	}

	public void addReceiptClick(View view) {
		Intent i = new Intent(this, AddNewReceipt.class);
		startActivity(i);
	}
	public void searchReceiptClick(View view) {
		Intent i = new Intent(this, SearchReceiptActivity.class);
		startActivity(i);
	}
	

	
}
