package com.spring.login;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private Button button;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		button		=(Button) findViewById(R.id.loign);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			Intent intent		=new Intent();
				intent.setClass(getApplicationContext(), Login.class);
				startActivity(intent);
				
//				 Dialog dialog = new Dialog(MainActivity.this);
//	              dialog.setContentView(R.layout.login);
//	              
//	               dialog.show();
				
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
