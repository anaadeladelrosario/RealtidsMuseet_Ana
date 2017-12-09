package com.realtidsmuseet.realtidsmuseet;

/**
 * Created by Cecilia To on 12/3/17.
 * Modified by Hampus heavily 12/5/17.
 */

public class MuseumPlace {
    private String beaconMessage;
    private boolean visited = false;
    private String beaconBluetoothAdress;

    MuseumPlace(String beaconBluetoothAdress, String beaconMessage){
        setBeaconBluetoothAdress(beaconBluetoothAdress);
        setBeaconMessage(beaconMessage);
    }

    /* BEACON MESSAGE*/
    public String getBeaconMessage() {
        return beaconMessage;
    }
    public void setBeaconMessage(String beaconMessage) {
        this.beaconMessage = beaconMessage;
    }


    /* BLUETOOTH ADRESS*/
    public String getBeaconBluetoothAdress() {
        return beaconBluetoothAdress;
    }
    public void setBeaconBluetoothAdress(String beaconBluetoothAdress) {
        this.beaconBluetoothAdress = beaconBluetoothAdress;
    }

    /* Visited */
    public boolean isVisited(){
        return visited;
    }
    public void setVisitedStatus(boolean visited){
        this.visited = visited;
    }

    public String toString() {
        return "Address: " + getBeaconBluetoothAdress() + " visited: " + visited + "\n";
    }
}
