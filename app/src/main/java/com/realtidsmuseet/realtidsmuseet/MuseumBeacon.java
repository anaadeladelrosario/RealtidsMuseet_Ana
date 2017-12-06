package com.realtidsmuseet.realtidsmuseet;

/**
 * Created by Cecilia To on 12/3/17.
 * Modified by Hampus heavily 12/5/17.
 */

public class MuseumBeacon {
    private String beaconType;
    private String beaconMessage;
    private boolean visited = false;
    private String bluetoothAdress;

    MuseumBeacon(String bluetoothAdress, String beaconType, String beaconMessage){
        setBluetoothAdress(bluetoothAdress);
        setBeaconType(beaconType);
        setBeaconMessage(beaconMessage);
    }

    /* BEACON MESSAGE*/
    public String getBeaconMessage() {
        return beaconMessage;
    }
    public void setBeaconMessage(String beaconMessage) {
        this.beaconMessage = beaconMessage;
    }

    /* BEACON TYPE*/
    public String getBeaconType() {
        return beaconType;
    }
    public void setBeaconType(String beaconType) {
        String beaconTypeLowerCase = beaconType.toLowerCase();
        if(beaconTypeLowerCase.equals("start") || beaconTypeLowerCase.equals("normal") || beaconTypeLowerCase.equals("end")){
            this.beaconType = beaconTypeLowerCase;
        }else{
            this.beaconType = "unknown";
        }
    }

    /* BLUETOOTH ADRESS*/
    public String getBluetoothAdress() {
        return bluetoothAdress;
    }
    public void setBluetoothAdress(String bluetoothAdress) {
        this.bluetoothAdress = bluetoothAdress;
    }

    public String toString() {
        return "Address: " + getBluetoothAdress() + " visited: " + visited + " type:" + getBeaconType() + "\n";
    }
}
