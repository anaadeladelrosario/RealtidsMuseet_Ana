package com.realtidsmuseet.realtidsmuseet;

/**
 * Created by Cecilia To on 12/3/17.
 */
import org.altbeacon.beacon.Beacon;

public class MuseumBeacon {
    private Beacon museumBeacon;
    private String blueToothAdress;
    private boolean visited;

    MuseumBeacon(Beacon museumBeacon){
        this.blueToothAdress = museumBeacon.getBluetoothAddress();
        this.visited = true;
    }

    public boolean getVisited(){
        return this.visited;
    }

    public boolean setVisited(boolean visited){
        return visited = visited;
    }

    public String getMuseumBeaconBlueToothAddress(){
        return this.blueToothAdress;
    }

    public String toString() {
        return "Address: " + blueToothAdress + " visited: " + visited +"\n";
    }
}
