package at.know_center.detectingactivities.logik;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;

import at.know_center.detectingactivities.contact.ContactGroup;
import at.know_center.detectingactivities.contact.Item;
import at.know_center.detectingactivities.sensors.ActivityDetectionManager;
import at.know_center.detectingactivities.sensors.ActivtiyUpdater;
import at.know_center.detectingactivities.sensors.ReceivedCallBlocker;

/**
 * Created by santokh on 14.05.15.
 */
public class BlockingCallManager extends BroadcastReceiver implements IncomingCalllStatus {

    private Context context;
    private DatabaseCallCheckMediator databaseCallCheckMediator;
    private ActivityDetectionManager activityDetectionManager;

    public BlockingCallManager(Context context, ActivtiyUpdater activtiyUpdater) {
        this.context = context;
        this.databaseCallCheckMediator = new DatabaseCallCheckMediator(context, activtiyUpdater);
        activityDetectionManager = new ActivityDetectionManager(context, activtiyUpdater);
;
    }

    public void startDetecting() {
        activityDetectionManager.startDetecting();
    }

    public void stopDetecting() {
        activityDetectionManager.stopDetecting();
    }

    public void startBlocking() {
        IntentFilter filter = new IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        context.registerReceiver(this, filter);

    }

    public void stopBlocking() {
        context.unregisterReceiver(this);
    }



    @Override
    public boolean isIncomingCallBlockable(String phoneNumber, Long timestamp) {
        return databaseCallCheckMediator.isIncomingCallBlockable(phoneNumber, timestamp);
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle b = intent.getExtras();
        if (b != null) {
            String state = b.getString(TelephonyManager.EXTRA_STATE);

            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {

            } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {

            } else if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                String incomingNumber = b.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                Log.d("Incoming call ", incomingNumber);
                ShowToast(context, " ringing");
                long timestamp = System.currentTimeMillis();
                if (isIncomingCallBlockable(incomingNumber, timestamp)) {
                    if (blockCall(context)) {
                        ShowToast(context, "Blocked");
                    } else {
                        ShowToast(context, "Not Blocked");
                    }
                }
            }

        }
    }

    public boolean blockCall(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Class classTelephony = Class.forName(telephonyManager.getClass().getName());
            Method methodGetITelephony = classTelephony.getDeclaredMethod("getITelephony");
            methodGetITelephony.setAccessible(true);
            Object telephonyInterface = methodGetITelephony.invoke(telephonyManager);
            Class telephonyInterfaceClass = Class.forName(telephonyInterface.getClass().getName());
            Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");
            methodEndCall.invoke(telephonyInterface);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void ShowToast(final Context mContext, final String mstr) {
        Toast.makeText(mContext, mstr, Toast.LENGTH_LONG).show();
    }
}
