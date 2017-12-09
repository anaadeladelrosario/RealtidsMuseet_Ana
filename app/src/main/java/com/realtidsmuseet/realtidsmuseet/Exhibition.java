package com.realtidsmuseet.realtidsmuseet;

import android.util.Log;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;

/**
 * Created by Hampus on 2017-12-05.
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
    }
    public MuseumPlace beaconExistInExhibtion(Beacon beacon){
        String blutoothadress = beacon.getBluetoothAddress();

        for(MuseumPlace p : placeList){
            printToLogI(blutoothadress + " compared to " + p.getBeaconBluetoothAdress());
            if(p.getBeaconBluetoothAdress().equals(blutoothadress)){
                printToLogI(" =  the same");
                return p;
            }else{
                printToLogI(" = not the same");
            }
        }
        return null;
    }
    private void printToLogI(String text){
        String TAG = "BeaconTracker";
        Log.i(TAG, text);
    }
}
