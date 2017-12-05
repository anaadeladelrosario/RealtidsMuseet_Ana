/**
 * Team members: Hampus Idstam, Ana Adela del Rosario and Cecilia To
 * Project: IoT RealTime Museum
 *
 * 1) Hampus Idstam 2017-11-30: 1st checkin with initial setup where Hampus able to get beacon signals and display them
 * 2) Cecilia To 2017-11-30: beaconOne(), beaconTwo(): simple print messages
 * 3) Cecilia To 2017-12-03:
 *         a) create MuseumBeacon class in order to track beacons in an arraylist
 *         b) setting up to only take the nearest beacon by comparing the distance
 * 4) Cecilia To 2017-12-04: add addBeaconToArrayList() to redo repeating code
 */


package com.example.speediegonzales.firsttestbeacon;

import android.Manifest;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;



public class MainActivity extends AppCompatActivity implements BeaconConsumer{

    private BeaconManager beaconManager = null;
    private static final String IBEACON_LAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";
    private static final String TAG = "RangingActivity";
    private TextView logWindow;
    private ArrayList<MuseumBeacon> beaconsList = new ArrayList<>();

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

                if (beacons.size() > 0) {
                    String beaconsFound = (Integer.toString(beacons.size()) + " beacons found");
                    String BeaconText = "";

                    for(Beacon b : beacons){
                        if(b.getDistance() < beacons.iterator().next().getDistance()){
                         BeaconText += "==> This beacon distance " + b.getDistance() + " compare to next beacon " + beacons.iterator().next().getDistance()+" meters away \n\n";
                         BeaconText = beaconOne(BeaconText, b);

                        } else if(beacons.iterator().next().getDistance() < b.getDistance()){
                            BeaconText += "==> Next beacon distance " + beacons.iterator().next().getDistance() + " compare to this beacon " + b.getDistance()+" meters away \n\n";
                            BeaconText = beaconTwo(BeaconText, beacons.iterator().next());
                        } else
                            BeaconText = beaconThree(BeaconText, b);
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

    private void printToLogI(String text){
        Log.i(TAG, text);
    }

    private MuseumBeacon addBeaconToArrayList(Beacon beacon){
        MuseumBeacon museumBeacon = null;
        if(!beaconsList.contains(beacon.getBluetoothAddress())){
            //check if beacon is already in beaconslist, if not, add beacon
            museumBeacon = new MuseumBeacon(beacon);
            beaconsList.add(museumBeacon);
        }
        return museumBeacon;
    }
    private String beaconOne(String BeaconText, Beacon beacon){
        MuseumBeacon museumBeacon = addBeaconToArrayList(beacon);

        BeaconText += "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ \n\n" +
                " Beacon with the BluetoothAddress " + beacon.getBluetoothAddress() + " is " + beacon.getDistance()+" meters away \n\n" +
                " Message from ROGER RABBIT \n\n" + museumBeacon.toString() + "\n @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ \n\n\n";
        return BeaconText;
    }

    private String beaconTwo(String BeaconText, Beacon beacon){
        MuseumBeacon museumBeacon = addBeaconToArrayList(beacon);

        BeaconText += "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% \n\n" +
                " Beacon with the BluetoothAddress " + beacon.getBluetoothAddress() + " is " +beacon.getDistance()+" meters away \n\n" +
                " Message to find NEMO \n\n " + museumBeacon.toString() + "\n %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n\n\n";
        return BeaconText;
    }

    private String beaconThree(String BeaconText, Beacon beacon){
        MuseumBeacon museumBeacon = addBeaconToArrayList(beacon);

        BeaconText += "$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ \n\n" +
                " Beacon with the BluetoothAddress " + beacon.getBluetoothAddress() + " is " +beacon.getDistance()+" meters away \n\n" +
                " Message to KERMIT THE FROG \n\n " + museumBeacon.toString() + "\n $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ \n\n\n";
        return BeaconText;
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

    private void gitTestByHampus(){
        /* Test-function */
        /* test2 */
    }
}
