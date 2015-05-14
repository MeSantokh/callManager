package at.know_center.detectingactivities.sensors;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Method;

/**
 * Created by santokh on 12.05.15.
 */
public class ReceivedCallBlocker extends BroadcastReceiver {

    ActivtiyUpdater updater;

    private ITelephony telephonyService;

    public ReceivedCallBlocker()  {}

    public ReceivedCallBlocker(ActivtiyUpdater updater) {
        this.updater = updater;

    }

    public void onReceive(Context context, Intent intent) {
        Bundle b=intent.getExtras();
        if(b!=null){
            String state=b.getString(TelephonyManager.EXTRA_STATE);

            if(state.equals(TelephonyManager.EXTRA_STATE_IDLE)){

            }else if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){

            }else if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                String incomingNumber=b.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                Log.d("Incoming call " , incomingNumber);
                ShowToast(context," ringing");
                if (blockCall(context)) {
                    ShowToast(context, "Blocked");
                } else {
                    ShowToast(context, "Not Blocked");
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
    public void ShowToast(final Context mContext,final String mstr){
        Toast.makeText(mContext, mstr, Toast.LENGTH_LONG).show();
    }

}
