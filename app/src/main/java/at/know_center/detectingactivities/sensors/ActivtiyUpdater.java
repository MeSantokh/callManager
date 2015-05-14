package at.know_center.detectingactivities.sensors;

/**
 * Created by santokh on 12.05.15.
 */
public interface ActivtiyUpdater {

    public void updateActivity(final String activityName, final int confidence, final long timestamp);
    public void blockCalls(String phonenumber);
}
