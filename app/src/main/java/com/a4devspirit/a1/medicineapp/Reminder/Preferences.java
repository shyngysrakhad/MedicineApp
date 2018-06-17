package com.a4devspirit.a1.medicineapp.Reminder;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.a4devspirit.a1.medicineapp.R;

public class Preferences extends PreferenceActivity
{
    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        addPreferencesFromResource(R.xml.preferences);
    }
}
