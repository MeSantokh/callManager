package at.know_center.detectingactivities.model;

/**
 * Created by santokh on 14.05.15.
 */
public class UrgentCallTracker {

    private String phoneNumber;
    private int callCounter;
    private Long firstTimestamp;
    private Long lastTimestamp;


    public UrgentCallTracker() {};
    public UrgentCallTracker(String phoneNumber, int callCounter, Long firstTimestamp, Long lastTimestamp) {
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

    public Long getLastTimestamp() {
        return lastTimestamp;
    }

    public void setLastTimestamp(Long lastTimestamp) {
        this.lastTimestamp = lastTimestamp;
    }

    public Long getFirstTimestamp() {
        return firstTimestamp;
    }

    public void setFirstTimestamp(Long firstTimestamp) {
        this.firstTimestamp = firstTimestamp;
    }
}
