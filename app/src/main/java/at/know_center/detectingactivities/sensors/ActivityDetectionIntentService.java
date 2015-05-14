package at.know_center.detectingactivities.sensors;

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;


/**
 * Created by santokh on 12.05.15.
 */
public class ActivityDetectionIntentService extends IntentService {

    public ActivityDetectionIntentService() {
        super(ActivityDetectionIntentService.class.getSimpleName());

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            DetectedActivity mostProbableActivity = result.getMostProbableActivity();
            int confidence = mostProbableActivity.getConfidence();
            long timestamp = result.getTime();
            int activity = mostProbableActivity.getType();
            String activityName = getActivityName(mostProbableActivity.getType());

            Intent broadcastIntent = new Intent(ActivityDetectionConstParms.ACTITVY_DETCTION_BROADCAST);
            broadcastIntent.putExtra(ActivityDetectionConstParms.ACTIVITY_NAME, activityName);
            broadcastIntent.putExtra(ActivityDetectionConstParms.CONFIDENCE, confidence);
            broadcastIntent.putExtra(ActivityDetectionConstParms.TIMESTAMP, timestamp);
            sendBroadcast(broadcastIntent);

        }

    }

    public String getActivityName(final int activityType) {
        if(activityType == DetectedActivity.RUNNING) {
            return ActivityDetectionConstParms.RUNNING;
        }
        else if(activityType == DetectedActivity.WALKING) {
            return ActivityDetectionConstParms.WALKING;
        }
        else if(activityType == DetectedActivity.IN_VEHICLE) {
            return ActivityDetectionConstParms.IN_VEHICLE;
        }
        else if(activityType == DetectedActivity.ON_BICYCLE) {
            return ActivityDetectionConstParms.ON_BICYCLE;
        }
        else if(activityType == DetectedActivity.STILL) {
            return ActivityDetectionConstParms.STILL;
        }
        else if(activityType == DetectedActivity.ON_FOOT) {
            return ActivityDetectionConstParms.ON_FOOT;
        }
        else if(activityType == DetectedActivity.TILTING) {
            return ActivityDetectionConstParms.TILTING;
        }
        else {
            return ActivityDetectionConstParms.UNKNOWN;
        }
    }
}
