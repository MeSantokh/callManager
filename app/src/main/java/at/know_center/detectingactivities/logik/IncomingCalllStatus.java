package at.know_center.detectingactivities.logik;

/**
 * Created by santokh on 14.05.15.
 */
public interface IncomingCalllStatus {

    public boolean isIncomingCallBlockable(String phoneNumber, Long timstamp);
}
