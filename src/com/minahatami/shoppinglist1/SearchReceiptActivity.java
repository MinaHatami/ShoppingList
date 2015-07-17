package com.minahatami.shoppinglist1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class SearchReceiptActivity extends Activity {
	EditText etSearchStore;
	TextView tvResult, tvResultDollar;
	Spinner spinner;
	String storename, spentAmount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_receipt);

		etSearchStore = (EditText) findViewById(R.id.etSearchStore);
		tvResult = (TextView) findViewById(R.id.tvResult);
		tvResultDollar = (TextView) findViewById(R.id.tvResultDollar);
		spinner = (Spinner) findViewById(R.id.spinner);
		
		storename = etSearchStore.getEditableText().toString();

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.months_array,
				android.R.layout.simple_spinner_item);
		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item );
		spinner.setAdapter(adapter);
	}
	
	public void btCalculateClick(View view){
		
	}
}
