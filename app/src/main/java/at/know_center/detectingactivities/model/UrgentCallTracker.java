package at.know_center.detectingactivities.model;

/**
 * Created by santokh on 14.05.15.
 */
public class UrgentCallTracker {

    private String phoneNumber;
    private int callCounter;
    private String firstTimestamp;
    private String lastTimestamp;


    public UrgentCallTracker() {};
    public UrgentCallTracker(String phoneNumber, int callCounter, String firstTimestamp, String lastTimestamp) {
        this.phoneNumber = phoneNumber;
        this.callCounter = callCounter;
        this.firstTimestamp = firstTimestamp;
        this.lastTimestamp = lastTimestamp;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getCallCounter() {
        return callCounter;
    }

    public void setCallCounter(int callCounter) {
        this.callCounter = callCounter;
    }

    public String getLastTimestamp() {
        return lastTimestamp;
    }

    public void setLastTimestamp(String lastTimestamp) {
        this.lastTimestamp = lastTimestamp;
    }

    public String getFirstTimestamp() {
        return firstTimestamp;
    }

    public void setFirstTimestamp(String firstTimestamp) {
        this.firstTimestamp = firstTimestamp;
    }
}
