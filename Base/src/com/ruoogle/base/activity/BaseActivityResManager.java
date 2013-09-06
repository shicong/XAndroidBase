/**
 * 
 */
package com.ruoogle.base.activity;

import com.ruoogle.base.IActivityResManager;
import com.ruoogle.base.Util.Debug;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author shicong
 *
 */
public abstract class BaseActivityResManager extends Activity implements IActivityResManager{

	private final String TAG = "ActivityResManager";
	private final boolean DEBUG = true;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Debug.d(TAG, "onCreate and ready to resInit()", DEBUG);
		resInit();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Debug.d(TAG, "onDestroy and ready to resRelease()", DEBUG);
		resRelease();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Debug.d(TAG, "onPause", DEBUG);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Debug.d(TAG, "onRestart and ready to resRestore", DEBUG);
		resRestore();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Debug.d(TAG, "onResume", DEBUG);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Debug.d(TAG, "onStart", DEBUG);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Debug.d(TAG, "onStop and ready to resRelease...", DEBUG);
		resRelease();
	}
	
}
