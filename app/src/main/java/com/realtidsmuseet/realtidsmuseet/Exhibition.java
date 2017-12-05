package com.realtidsmuseet.realtidsmuseet;

import java.util.ArrayList;

/**
 * Created by Hampus on 2017-12-05.
 */

public class Exhibition {
    private String exhibitionName;
    private String exhibitionType;
    private ArrayList<MuseumBeacon> beaconList = new ArrayList<>();
    private MuseumBeacon startBeacon;
    private MuseumBeacon endBeacon;

    Exhibition(String exhibitionName, String exhibitionType){
        this.exhibitionName = exhibitionName;
        this.exhibitionType = exhibitionType;
    }

    public String getExhibitionName() {
        return exhibitionName;
    }
    public void setExhibitionName(String exhibitionName) {
        this.exhibitionName = exhibitionName;
    }

    public String getExhibitionType() {
        return exhibitionType;
    }
    public void setExhibitionType(String exhibitionType) {
        this.exhibitionType = exhibitionType;
    }

    public MuseumBeacon getStartBeacon() {
        return startBeacon;
    }
    public void setStartBeacon(MuseumBeacon startBeacon) {
        this.startBeacon = startBeacon;
    }

    public MuseumBeacon getEndBeacon() {
        return endBeacon;
    }
    public void setEndBeacon(MuseumBeacon endBeacon) {
        this.endBeacon = endBeacon;
    }

    public void addBeacon(MuseumBeacon beacon){
        String beaconType = beacon.getBeaconType();
        if(beaconType.equals("start")){
            if(getStartBeacon() == null){
                setStartBeacon(beacon);
            }else{
                // Maybe some error handling that is already exist a start beacon, you cant add 2
            }
        }else if(beaconType.equals("end")){
            if(getEndBeacon() == null){
                setEndBeacon(beacon);
            }else {
                // Maybe some error handling that is already exist a end beacon, you cant add 2
            }
        }
        beaconList.add(beacon); // Add the beacon to the exhibition list
    }
}
