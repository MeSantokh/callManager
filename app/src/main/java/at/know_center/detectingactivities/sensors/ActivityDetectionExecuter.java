package at.know_center.detectingactivities.sensors;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;

/**
 * Created by santokh on 12.05.15.
 */
public class ActivityDetectionExecuter implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient client;
    private PendingIntent intent;

    public ActivityDetectionExecuter(Context context) {
        this.client = new GoogleApiClient.Builder(context)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        Intent i = new Intent(context, ActivityDetectionIntentService.class);
        intent = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void startDetecting() {
        Log.d("DETECTION", "activity detection started");
        client.connect();
    }

    public void stopDetecting() {
        Log.d("DETECTION", "activity detection stopped");
        client.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("DETECTION", "activity detection updates");
        PendingResult<Status> result = ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(client, 0, intent);
        result.setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                Log.d("DETECTION", " status: " + status + " " + status.isSuccess());
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {
        ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(client, intent);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
