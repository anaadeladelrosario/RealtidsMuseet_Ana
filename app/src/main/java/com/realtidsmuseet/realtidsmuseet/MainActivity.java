/**
 * 1) Hampus 2017-11-30: 1st checkin with initial setup where Hampus able to get beacon signals and display them
 * 2) Cecilia 2017-11-30: beaconOne(), beaconTwo(): simple print messages
 * 3) Cecilia 2017-12-03: create MuseumPlace class in order to track beacons in an arraylist
 * 4) Hampus: Modified both files so that we can pre-store beacons and later track them
 */

package com.realtidsmuseet.realtidsmuseet;

import android.Manifest;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import org.altbeacon.beacon.*;

import java.util.ArrayList;
import java.util.Collection;

public class MainActivity extends AppCompatActivity implements BeaconConsumer{

    private BeaconManager beaconManager = null;
    private static final String IBEACON_LAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";
    private static final String TAG = "BeaconTracker";
    private TextView logWindow;
    private ArrayList<Beacon> beaconsListPresent = new ArrayList<>();
    private ArrayList<Exhibition> exhibitionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, 1234);

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(IBEACON_LAYOUT));
        beaconManager.bind(this);
        logWindow = (TextView) findViewById(R.id.logWindow);
        logWindow.setMovementMethod(new ScrollingMovementMethod());
        exhibitionSetUp();

    }

    private void exhibitionSetUp(){
        MuseumPlace place1 = new MuseumPlace("D8:4E:93:A8:E4:83", "Hej från D8 \n");
        MuseumPlace place2 = new MuseumPlace("EA:C9:B7:DD:8A:A0", "Hej från EA \n");
        MuseumPlace place3 = new MuseumPlace("F2:E1:A3:7E:CF:BC", "Hej från F2 \n");
        Exhibition exhibition1 = new Exhibition("Sveriges historia", "Linear");
        exhibitionList.add(exhibition1);
        exhibition1.addBeacon(place1);
        exhibition1.addBeacon(place2);
        exhibition1.addBeacon(place3);
    }


    private void beaconTracker(){
        // TEMP CODE TO PRINT TO SCREEN
        Beacon closest = findClosestBeacon();

        if(beaconsListPresent.size() > 0){
            String beaconsFound = (Integer.toString(beaconsListPresent.size()) + " beacons found");
            String BeaconText = "The closest Beacon with the BluetoothAddress " + closest.getBluetoothAddress() + " is " + closest.getDistance()+" meters away \n";

            MuseumPlace place = findMuseumPlaceFromBeacon(closest);
            String message;
            if(place == null){
                message = "null";
            }else{
                message = place.getBeaconMessage();
            }


            clearAndPrintToScreenTextArea(beaconsFound + "\n\n" + BeaconText + "\n\n" + message);
            printToLogI(beaconsFound + "\n\n" + BeaconText + "\n\n" + message);
        }

        /*
        printToLogI("!beaconTracker! : BeaconsListPresent:" + beaconsListPresent.size() + " beacons in it");
        if(beaconsListPresent.size() > 0){
            String beaconsFound = (Integer.toString(beaconsListPresent.size()) + " beacons found");
            String BeaconText = "";
            for(Beacon b : beaconsListPresent){
                BeaconText += "Beacon with the BluetoothAddress " + b.getBluetoothAddress() + " is " +b.getDistance()+" meters away \n\n";
            }
            clearAndPrintToScreenTextArea(beaconsFound + "\n\n" + BeaconText);
            printToLogI(beaconsFound + "\n\n" + BeaconText);
        }
        */
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(final Collection<Beacon> beacons, Region region) {
                ArrayList<Beacon> beaconsListTemp = new ArrayList<>();
                if (beacons.size() > 0) { // The phone find at least one beacon
                    beaconsListTemp = new ArrayList<>();
                    for(Beacon b : beacons){
                        beaconsListTemp.add(b);
                        printToLogI("The beacon " + b.getBluetoothAddress() + " is " + b.getDistance() + " meters away and is added to the temp list");
                    }
                    beaconsListPresent.clear();
                    beaconsListPresent.addAll(beaconsListTemp); //In this way, we always have beaconsListPresent filled with the beacons currently seen by the mobile.
                    printToLogI("The temp list is now stored in the beaconsListPresent");
                    printToLogI("The beaconsListTemp has " + beaconsListTemp.size() + " beacons in it");
                    printToLogI("The beaconsListPresent has " + beaconsListPresent.size() + " beacons in it");
                }
                beaconTracker(); // Maybe remove this one

            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {    }

    }


    private Beacon findClosestBeacon(){
        Beacon closest = null;
        for(Beacon b : beaconsListPresent){
            if(closest == null){
                closest = b;
            }else{
                if(b.getDistance() < closest.getDistance()){
                    closest = b;
                }
            }
        }
        return closest;
    }

    private MuseumPlace findMuseumPlaceFromBeacon(Beacon beacon){
        MuseumPlace museumPlace = null;
        for(Exhibition exhibition : exhibitionList){
            if(exhibition.beaconExistInExhibtion(beacon) != null){
                return exhibition.beaconExistInExhibtion(beacon);
            }
        }
        return null;
    }

    /* TEMPORARILY COMMENT OUT - CECILIAS CODE!
    @Override
    public void onBeaconServiceConnect() {
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(final Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) { // The phone find at least one beacon
                    String beaconsFound = (Integer.toString(beacons.size()) + " beacons found");
                    String BeaconText = "";

                    for(Beacon b : beacons){
                        if(b.getDistance() < beacons.iterator().next().getDistance()){
                            BeaconText += "B distance " + b.getDistance() + " where next" + beacons.iterator().next().getDistance()+" meters away \n\n";
                            //BeaconText = beaconOne(BeaconText, b);

                        } else if(beacons.iterator().next().getDistance() < b.getDistance()){
                            BeaconText += "B next distance " + beacons.iterator().next().getDistance() + " where original" + b.getDistance()+" meters away \n\n";
                            //BeaconText = beaconTwo(BeaconText, beacons.iterator().next());
                        } else{
                            //BeaconText = beaconThree(BeaconText, b);
                        }

                    }
                    clearAndPrintToScreenTextArea(beaconsFound + "\n\n" + BeaconText);
                    printToLogI(beaconsFound + "\n\n" + BeaconText);
                }

            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {    }
    }


    private String beaconOne(String BeaconText, Beacon beacon){
        MuseumPlace museumBeacon = null;
        if(!beaconsList.contains(beacon.getBluetoothAddress())){
            //check if beacon is already in beaconslist, if not, add beacon
            museumBeacon = new MuseumPlace(beacon);
            beaconsList.add(museumBeacon);
        }

        BeaconText += "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ \n\n" +
                " Beacon with the BluetoothAddress " + beacon.getBluetoothAddress() + " is " +beacon.getDistance()+" meters away \n\n" +
                " Message from ROGER RABBIT \n\n" + museumBeacon.toString() + "\n @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ \n\n\n";
        return BeaconText;
    }

    private String beaconTwo(String BeaconText, Beacon beacon){
        MuseumPlace museumBeacon = null;
        if(!beaconsList.contains(beacon.getBluetoothAddress())){
            //check if beacon is already in beaconslist, if not, add beacon
            museumBeacon = new MuseumPlace(beacon);
            beaconsList.add(museumBeacon);
        }

        BeaconText += "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% \n\n" +
                " Beacon with the BluetoothAddress " + beacon.getBluetoothAddress() + " is " +beacon.getDistance()+" meters away \n\n" +
                " Message to find NEMO \n\n " + museumBeacon.toString() + "\n %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n\n\n";
        return BeaconText;
    }

    private String beaconThree(String BeaconText, Beacon beacon){
        MuseumPlace museumBeacon = null;
        if(!beaconsList.contains(beacon.getBluetoothAddress())){
            //check if beacon is already in beaconslist, if not, add beacon
            museumBeacon = new MuseumPlace(beacon);
            beaconsList.add(museumBeacon);
        }

        BeaconText += "$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ \n\n" +
                " Beacon with the BluetoothAddress " + beacon.getBluetoothAddress() + " is " +beacon.getDistance()+" meters away \n\n" +
                " Message to KERMIT THE FROG \n\n " + museumBeacon.toString() + "\n $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ \n\n\n";
        return BeaconText;
    }
    */

    private void printToLogI(String text){
        Log.i(TAG, text);
    }

    private void clearAndPrintToScreenTextArea(String text){
        final String textToPrint = text;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                logWindow.setText(textToPrint);
            }
        });
    }
}