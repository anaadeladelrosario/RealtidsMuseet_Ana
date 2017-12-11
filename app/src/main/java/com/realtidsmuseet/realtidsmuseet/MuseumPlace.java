package com.realtidsmuseet.realtidsmuseet;

/**
 * Created by Cecilia To on 12/3/17.
 * Modified by Hampus heavily 12/5/17.
 * Modified by Cecilia to have visited setup in the constructor as false 12/10/17.
 * Added the function get and set Belongs to the exhibition for tracking by Hampus 2017-12-10
 */

public class MuseumPlace {
    private String beaconMessage;
    private boolean visited = false;
    private String beaconBluetoothAdress;
    private Exhibition belongsToTheExhibition = null;

    MuseumPlace(String beaconBluetoothAdress,String beaconMessage){
        setBeaconBluetoothAdress(beaconBluetoothAdress);
        setVisitedStatus(false);
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
    public boolean getVisited(){
        return visited;
    }
    public void setVisitedStatus(boolean visited){
        this.visited = visited;
    }

    /* Exhibition */
    public Exhibition getBelongsToTheExhibition(){return belongsToTheExhibition;}
    public void setBelongsToTheExhibition(Exhibition belongsToTheExhibition) {this.belongsToTheExhibition = belongsToTheExhibition;}

    public String toString() { return "Address: " + getBeaconBluetoothAdress() + " visited: " + visited + "\n"; }
}
