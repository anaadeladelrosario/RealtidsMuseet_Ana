package com.realtidsmuseet.realtidsmuseet;

import android.util.Log;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;

/**
 * Created by Hampus on 2017-12-05.
 * Modified by Cecilia add beaconCheckVisited() then call this function in beaconExistInExhibtion() on 12/10/17.
 * Changed the visited tracker to only check as visited for the closest one 2017-12-11 // Hampus
 */

public class Exhibition {
    private String exhibitionName;
    private String exhibitionType;
    private ArrayList<MuseumPlace> placeList = new ArrayList<>();

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

    public void addBeacon(MuseumPlace beacon){
        placeList.add(beacon); // Add the beacon to the exhibition list
        beacon.setBelongsToTheExhibition(this);
    }

    public MuseumPlace beaconExistInExhibtion(Beacon beacon){
        String blutoothadress = beacon.getBluetoothAddress();
        for(MuseumPlace p : placeList){
            if(p.getBeaconBluetoothAdress().equals(blutoothadress)) {
                //beaconCheckVisited(blutoothadress);
                return p;
            }
        }
        return null;
    }

    public void setPlaceAsVisitedAndAllBeforeAsVisited(MuseumPlace place){
        for(MuseumPlace p : placeList){
            if (!p.getVisited()) {
                p.setVisitedStatus(true);
                printToLogI("The beacon with the message: " + p.getBeaconMessage() + " is set as visited!!! ");
            }
            if(p == place){
                return;
            }
        }
    }

    public int[] getVisitedStatusArray(){
        int size = placeList.size();
        int[] visitedStatusArray = new int[size];
        for(int i=0; i<size; i++){
            boolean visited = placeList.get(i).getVisited();
            if(visited){
                visitedStatusArray[i] = 1;
            }else{
                visitedStatusArray[i] = 0;
            }

        }
        return visitedStatusArray;
    }

    /*
    private void beaconCheckVisited(String beaconAddress){
        String beaconAddressTemp = beaconAddress;
        for(MuseumPlace a : placeList){
            if(a.getBeaconBluetoothAdress().equals(beaconAddressTemp)){
                if(a.getVisited() == false){
                    a.setVisitedStatus(true);
                }
                printToLogI("Check Visited State: "  + a.toString());
            }else{
                printToLogI(a.getBeaconBluetoothAdress() +" = not visited.");
            }
        }
    }
    */

    private void printToLogI(String text){
        String TAG = "BeaconTracker";
        Log.i(TAG, text);
    }
}
