package at.know_center.detectingactivities.logik;

import android.util.Log;

import at.know_center.detectingactivities.sensors.ActivityDetectionConstParms;
import at.know_center.detectingactivities.sensors.ActivtiyUpdater;

/**
 * Created by santokh on 15.05.15.
 */
public class DetectedActivityHolder implements ActivtiyUpdater {
    private static final String NAME = DetectedActivityHolder.class.getSimpleName();
    private String currentActivity = "NO STATUTS";
    private int inVehicleThresholdConfidence = 60;
    private int confidence = 0;
    private Long timestamp;


    public DetectedActivityHolder() { }

    public boolean isCurrentActivityInVehicle() {
        if(this.currentActivity.equals(ActivityDetectionConstParms.STILL)) {
            return true;
        }
        return false;
    }

    public boolean  isInVehicleConfidenceSatisfied() {
        return (this.confidence >= this.inVehicleThresholdConfidence);
    }

    @Override
    public void updateActivity(String activityName, int confidence, long timestamp) {
        Log.d(NAME, "activityName: " + activityName + ", confidence: " + confidence + ", timestamp: " + timestamp);
        this.currentActivity = activityName;
        this.confidence = confidence;
        this.timestamp = timestamp;
    }

    @Override
    public void blockCalls(String phonenumber) {

    }
}
