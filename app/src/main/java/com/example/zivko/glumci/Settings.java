package com.example.zivko.glumci;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Živko on 2016-11-19.
 */

public class Settings extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
