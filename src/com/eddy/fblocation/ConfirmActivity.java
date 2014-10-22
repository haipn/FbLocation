package com.eddy.fblocation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TextView;

public class ConfirmActivity extends Activity {
	
	TextView mTvName;
	TextView mTvAge;
	Button mBtnAccept;
	Button mBtnDecline;
	DatePicker mPickerBirthday;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirm_layout);
		mTvName = (TextView) findViewById(R.id.tvName);
		mTvAge = (TextView) findViewById(R.id.tvAge);
		String bd = getIntent().getStringExtra(MainActivity.BIRTHDAY);
		mTvName.setText(getIntent().getStringExtra(MainActivity.NAME));
		
		mPickerBirthday = (DatePicker) findViewById(R.id.pickerBirthday);
		
		mBtnAccept = (Button) findViewById(R.id.btnAccept);
		mBtnDecline = (Button) findViewById(R.id.btnDecline);
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Date date;
		try {
			date = sdf.parse(bd);
			Calendar cld = Calendar.getInstance();
			cld.setTime(date);
			mTvAge.setText(String.format("Your age: %d", getAge(cld)));
			mPickerBirthday.init(cld.get(Calendar.YEAR), cld.get(Calendar.MONTH), cld.get(Calendar.DAY_OF_MONTH), new OnDateChangedListener() {
				
				@Override
				public void onDateChanged(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					Calendar change = Calendar.getInstance();
					change.set(year, monthOfYear, dayOfMonth);
					mTvAge.setText(String.format("Your age: %d", getAge(change)));
				}
			});
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mBtnAccept.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(ConfirmActivity.this, MapActivity.class));
			}
		});
	}
	
	public int getAge(Calendar birthday)  {
		Calendar today = Calendar.getInstance();  
		int age = today.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);  
		if (today.get(Calendar.MONTH) < birthday.get(Calendar.MONTH)) {
		  age--;  
		} else if (today.get(Calendar.MONTH) == birthday.get(Calendar.MONTH)
		    && today.get(Calendar.DAY_OF_MONTH) < birthday.get(Calendar.DAY_OF_MONTH)) {
		  age--;  
		}
		if (age >= 18) {
			mBtnAccept.setEnabled(true);
		} else {
			mBtnAccept.setEnabled(false);
		}
		return age;
	}
}
