package at.know_center.detectingactivities;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

import at.know_center.detectingactivities.contact.ContactGroup;
import at.know_center.detectingactivities.contact.Item;
import at.know_center.detectingactivities.database.DBHelper;
import at.know_center.detectingactivities.database.PlacesDAO;
import at.know_center.detectingactivities.database.UrgentCallTrackerDAO;
import at.know_center.detectingactivities.logik.BlockingCallManager;
import at.know_center.detectingactivities.logik.DetectedActivityHolder;
import at.know_center.detectingactivities.model.Place;
import at.know_center.detectingactivities.model.UrgentCallTracker;
import at.know_center.detectingactivities.sensors.ActivityDetectionConstParms;
import at.know_center.detectingactivities.sensors.ActivityDetectionManager;
import at.know_center.detectingactivities.sensors.ActivtiyUpdater;
import at.know_center.detectingactivities.sensors.ReceivedCallBlocker;


public class MainActivity extends Activity implements ActivtiyUpdater {
    TextView txtActivityName;
    BlockingCallManager blockingCallManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtActivityName = (TextView)findViewById(R.id.txtActivityName);
        blockingCallManager = new BlockingCallManager(this, new DetectedActivityHolder());
    }



    @Override
    protected void onResume() {
        super.onResume();
        blockingCallManager.startDetecting();
        blockingCallManager.startBlocking();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // unregisterReceiver(callBlockerReceiver);
        // activityDetectionManager.stopDetecting();
        blockingCallManager.startDetecting();
        blockingCallManager.startBlocking();

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
    }

    @Override
    public void blockCalls(String phonenumber) {
        String  text = txtActivityName.getText().toString();
        txtActivityName.setText(text + ", blockedPhoneNr " + phonenumber);
    }
}
