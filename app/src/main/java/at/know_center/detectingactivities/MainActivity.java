package at.know_center.detectingactivities;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import at.know_center.detectingactivities.sensors.ActivityDetectionConstParms;
import at.know_center.detectingactivities.sensors.ActivityDetectionManager;
import at.know_center.detectingactivities.sensors.ActivtiyUpdater;
import at.know_center.detectingactivities.sensors.ReceivedCallBlocker;


public class MainActivity extends Activity implements ActivtiyUpdater {
    TextView txtActivityName;
    ActivityDetectionManager activityDetectionManager;
    ReceivedCallBlocker callBlockerReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtActivityName = (TextView)findViewById(R.id.txtActivityName);
    }



    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        callBlockerReceiver = new ReceivedCallBlocker(this);
        registerReceiver(callBlockerReceiver, filter);
        activityDetectionManager = new ActivityDetectionManager(this, this);
        activityDetectionManager.startDetecting();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(callBlockerReceiver);
        activityDetectionManager.stopDetecting();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateActivity(String activityName, int confidence, long timestamp) {
        txtActivityName.setText("Name " + activityName + ", conf " + String.valueOf(confidence));
            int inVehicleThreshold = 80;

            if(activityName.equals(ActivityDetectionConstParms.RUNNING)) {

            }
            else if(activityName.equals(ActivityDetectionConstParms.WALKING)) {

            }
            else if(activityName.equals(ActivityDetectionConstParms.IN_VEHICLE)) {
                if(confidence > inVehicleThreshold) {

                }

            }
            else if(activityName.equals( ActivityDetectionConstParms.ON_BICYCLE)) {

            }
            else if(activityName.equals(ActivityDetectionConstParms.STILL)) {

            }
            else if(activityName.equals(ActivityDetectionConstParms.ON_FOOT)) {

            }
            else if(activityName.equals(ActivityDetectionConstParms.TILTING)) {

            }
            else {

            }



    }

    @Override
    public void blockCalls(String phonenumber) {
        String  text = txtActivityName.getText().toString();
        txtActivityName.setText(text + ", blockedPhoneNr " + phonenumber);
    }
}
