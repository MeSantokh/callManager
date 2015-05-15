package at.know_center.detectingactivities.sensors;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by santokh on 12.05.15.
 */
public class ActivityDetectionManager extends BroadcastReceiver {

    public ActivityDetectionExecuter executer;
    public Context context;
    public ActivtiyUpdater updater;


    public ActivityDetectionManager(Context context, ActivtiyUpdater updater) {
        executer = new ActivityDetectionExecuter(context);
        this.context = context;
        this.updater = updater;
    }

    public void startDetecting() {
        IntentFilter filter = new IntentFilter(ActivityDetectionConstParms.ACTITVY_DETCTION_BROADCAST);
        context.registerReceiver(this, filter);
        executer.startDetecting();
    }

    public void stopDetecting() {
        executer.stopDetecting();
        context.unregisterReceiver(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String[] state = intent.getStringArrayExtra(TelephonyManager.EXTRA_STATE);
        if (intent.getAction().equals(ActivityDetectionConstParms.ACTITVY_DETCTION_BROADCAST)) {
            String activityName = intent.getExtras().getString(ActivityDetectionConstParms.ACTIVITY_NAME);
            int confidence = intent.getExtras().getInt(ActivityDetectionConstParms.CONFIDENCE);
            long timestamp = intent.getExtras().getLong(ActivityDetectionConstParms.TIMESTAMP);
            updater.updateActivity(activityName, confidence, timestamp);

        }
    }

}
