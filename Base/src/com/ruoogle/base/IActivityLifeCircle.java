/**
 * 
 */
package com.ruoogle.base;

import android.content.Intent;
import android.os.Bundle;

/**
 * @author shicong
 *
 */
public interface IActivityLifeCircle {
	
	void emuOnCreate(Bundle savedInstanceState);
	void emuOnDestroy();
	void emuOnPause();
	void emuOnRestart();
	void emuOnResume();
	void emuOnStart();
	void emuOnStop();
	void emuonActivityResult(int requestCode, int resultCode, Intent data);
}
