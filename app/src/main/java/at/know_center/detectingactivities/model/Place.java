package at.know_center.detectingactivities.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by santokh on 14.05.15.
 */
public class Place implements Serializable {

    private long id;
    private String name;
    private String streetName;
    private int streetNumber;
    private String cityName;
    private ArrayList<Integer> groupPermissions;

    public Place() {}

    public Place(long id, String name, String streetName, int streetNumber, String cityName) {
        this.id = id;
        this.name = name;
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.cityName = cityName;
        this.groupPermissions = new ArrayList<Integer>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public ArrayList<Integer> getGroupPermissions() {
        return groupPermissions;
    }

    public void setGroupPermissions(ArrayList<Integer> groupPermissions) {
        this.groupPermissions = groupPermissions;
    }

}
