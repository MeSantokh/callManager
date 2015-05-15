package at.know_center.detectingactivities.logik;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import at.know_center.detectingactivities.contact.ContactGroup;
import at.know_center.detectingactivities.contact.Item;
import at.know_center.detectingactivities.database.GroupGenerlCallDrivingPermDAO;
import at.know_center.detectingactivities.database.GroupUrgentCallDrivingPermDAO;
import at.know_center.detectingactivities.database.PlacesDAO;
import at.know_center.detectingactivities.database.UrgentCallTrackerDAO;
import at.know_center.detectingactivities.model.GroupWithPermission;
import at.know_center.detectingactivities.model.Place;
import at.know_center.detectingactivities.model.UrgentCallTracker;
import at.know_center.detectingactivities.sensors.ActivtiyUpdater;

/**
 * Created by santokh on 14.05.15.
 */
public class DatabaseCallCheckMediator implements IncomingCalllStatus{

    int urgentCallThreshold = 5; // mintues

    // currently supporting threshold of three, TODO make dynamically
    int urgentCallAttemptsThreshold = 3;

    private GroupUrgentCallDrivingPermDAO groupUrgentCallDrivingPermDAO;
    private GroupGenerlCallDrivingPermDAO groupGenerlCallDrivingPermDAO;
    private UrgentCallTrackerDAO urgentCallTrackerDAO;
    private PlacesDAO placesDAO;
    private ContactGroup contactGroup;
    private Context context;
    private  DetectedActivityHolder detectedActivityHolder;

    public DatabaseCallCheckMediator(Context context, ActivtiyUpdater detectedActivityHolder) {
        this.context = context;
        groupUrgentCallDrivingPermDAO = new GroupUrgentCallDrivingPermDAO(context);
        groupGenerlCallDrivingPermDAO = new GroupGenerlCallDrivingPermDAO(context);
        urgentCallTrackerDAO = new UrgentCallTrackerDAO(context);
        placesDAO = new PlacesDAO(context);
        this.detectedActivityHolder = (DetectedActivityHolder) detectedActivityHolder;
        this.contactGroup = new ContactGroup(context);
        contactGroup.initContactList();
    }

    @Override
    public boolean isIncomingCallBlockable(String phoneNumber, Long timestamp) {
        ArrayList<Item> groups = contactGroup.getContactGroupsByPhoneNumber(phoneNumber);
        if (groups == null) {
            return false;
        }
        if(detectedActivityHolder.isCurrentActivityInVehicle() &&
             detectedActivityHolder.isInVehicleConfidenceSatisfied() ) {
            if (hasGeneralPermWhileDriving(phoneNumber, groups)) {
                return true;
            }

            if (hasUrgentPermWhileDriving(phoneNumber, groups)) {
                if (isUrgentCallAccordingToTracker(phoneNumber, timestamp)) {
                    return true;
                }
            }
        }
        if(hasPlaceBasedPermission(groups)) {
            return true;
        }
        return false;
    }

    public Boolean hasGeneralPermWhileDriving(String phoneNumber,  ArrayList<Item> groups ) {
        for(Item groupItem : groups) {
            if(groupItem.id != null) {
                GroupWithPermission groupWithPermObj = groupGenerlCallDrivingPermDAO.getGroupWithPermObj(Integer.parseInt(groupItem.id));
                if(groupWithPermObj != null && groupWithPermObj.hasPermission()) {
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean hasUrgentPermWhileDriving(String phoneNumber,  ArrayList<Item> groups ) {
        for(Item groupItem : groups) {
            if(groupItem.id != null) {
                GroupWithPermission groupWithPermObj = groupGenerlCallDrivingPermDAO.getGroupWithPermObj(Integer.parseInt(groupItem.id));
                if(groupWithPermObj != null && groupWithPermObj.hasPermission()) {
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean isUrgentCallAccordingToTracker(String phoneNumber, Long timestamp) {
        UrgentCallTracker trackerObj = urgentCallTrackerDAO.getUrgentCallTrackerByPhoneNumber(phoneNumber);
        if(trackerObj != null) {
            if(trackerObj.getCallCounter() ==  urgentCallAttemptsThreshold-1) {
                Long firstTimestamp = trackerObj.getFirstTimestamp();
                Long timestampDiff = (timestamp - firstTimestamp);
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(timestampDiff);
                int urgentCallDiff = c.get(Calendar.MINUTE);
                urgentCallTrackerDAO.updateCallCounter(trackerObj, timestamp);
                if (urgentCallDiff < urgentCallThreshold) {
                    return true;
                }
            }else {
                urgentCallTrackerDAO.updateCallCounter(trackerObj, timestamp);
            }
        }
        return false;
    }

    public Boolean hasPlaceBasedPermission(ArrayList<Item> groups ) {
        Place currentPlace = getCurrentPlace();
        List<Place> allPlaces = placesDAO.getAllPlaces();
        Place matchedPlace = null;
        for(Place placeItem: allPlaces) {
            if(placeItem.getCityName().equals(currentPlace.getCityName()) &&
               placeItem.getStreetName().equals(currentPlace.getStreetName()) &&
               placeItem.getStreetNumber() == currentPlace.getStreetNumber()) {
                matchedPlace = placeItem;
                break;
            }
        }
        if(matchedPlace == null) {
            return false;
        }
        for(Item group : groups) {
            if(matchedPlace.getGroupPermissions().contains(group.id)) {
                return true;
            }
        }
        return false;
    }


    public Place getCurrentPlace() {
        //TODO find a way to get current Position
        Place currentPlace = new Place();
        return currentPlace;
    }
}
