package at.know_center.detectingactivities.sensors;

/**
 * Created by santokh on 12.05.15.
 */
public interface ITelephony {
    boolean endCall();
    void answerRingingCall();
    void silenceRinger();
}
