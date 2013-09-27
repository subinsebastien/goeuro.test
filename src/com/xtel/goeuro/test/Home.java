package com.xtel.goeuro.test;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

public class Home extends Activity implements TextWatcher, OnClickListener	{

	private PlacesAdapter pA;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		pA = new PlacesAdapter(this, android.R.layout.simple_list_item_1);
		AutoCompleteTextView placeOne = (AutoCompleteTextView) findViewById(R.id.actv_place_one);
		AutoCompleteTextView placeTwo = (AutoCompleteTextView) findViewById(R.id.actv_place_two);
		placeOne.setAdapter(pA);
		placeOne.addTextChangedListener(this);
		
		placeTwo.setAdapter(pA);
		placeTwo.addTextChangedListener(this);
		
		findViewById(R.id.btn_search).setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public void afterTextChanged(Editable s) {}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		pA.setText(s.toString());
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btn_search)	{
			Toast.makeText(this, "Search is not yet implemented", Toast.LENGTH_SHORT).show();
		}
	}
}
