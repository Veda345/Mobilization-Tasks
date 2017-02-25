package com.example.vorona.infocaller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by vorona on 26.07.16.
 */

public class CallReceiver extends BroadcastReceiver{

    private void showWindow(Context context, String phone) {
        Log.w("show", "window");
        SearchAsyncTask search = new SearchAsyncTask(context);
        search.execute(phone);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.w("Reciver", "OnReceive");
        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
            String phoneState = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if (phoneState.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                showWindow(context, phoneNumber);
            }
        }
    }
}
