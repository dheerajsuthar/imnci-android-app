package org.dheeraj.imnci;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SharedPrefsActivity extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference_screen);
	}
}
