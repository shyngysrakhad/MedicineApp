package com.a4devspirit.a1.medicineapp.Reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompletedReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        // just create AlarmListAdapter and it will load alarms and start them
        new AlarmListAdapter(context);
    }
}