package com.minahatami.shoppinglist1;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class SearchReceiptActivity extends Activity implements
		OnItemSelectedListener {
	private static final String TAG = "SearchReceiptActivity";
	
	EditText etSearchStore;
	TextView tvResult, tvResultDollar;
	Spinner spinner;
	//String storename, spentAmount;
	int monthSelected;
	//double totalAmount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_receipt);

		etSearchStore = (EditText) findViewById(R.id.etSearchStore);
		tvResult = (TextView) findViewById(R.id.tvResult);
		tvResultDollar = (TextView) findViewById(R.id.tvResultDollar);
		spinner = (Spinner) findViewById(R.id.spinner);
		spinner.setOnItemSelectedListener(this);

		

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.months_array,
				android.R.layout.simple_spinner_item);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		monthSelected = (int) parent.getItemIdAtPosition(position);
		Log.v(TAG, "monthSelected: " + monthSelected);

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

	public void btCalculateClick(View view) {
		String storename = etSearchStore.getEditableText().toString().toUpperCase();
		SQLController db = new SQLController(this);
		db.open();
		double totalAmount = db.calculate(storename,monthSelected) / 100;
		tvResultDollar.setText("" + totalAmount);
		db.close();
	}

}
