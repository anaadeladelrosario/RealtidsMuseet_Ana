/**
 * 1) Hampus 2017-11-30: 1st checkin with initial setup where Hampus able to get beacon signals and display them
 * 2) Cecilia 2017-11-30: beaconOne(), beaconTwo(): simple print messages
 * 3) Cecilia 2017-12-03: create MuseumPlace class in order to track beacons in an arraylist
 * 4) Hampus: Modified both files so that we can pre-store beacons and later track them
 * 5) Cecilia 2017-12-10: added a check for visited state
 * 6) Change the tracking method
 */

package com.realtidsmuseet.realtidsmuseet;

import android.Manifest;
import android.content.Intent;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.altbeacon.beacon.*;
import org.altbeacon.beacon.service.RunningAverageRssiFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class MainActivity extends AppCompatActivity implements BeaconConsumer{

    private BeaconManager beaconManager = null;
    private static final String IBEACON_LAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";
    private static final String TAG = "BeaconTracker";
    private TextView logWindow;
    private TextView exhibitionHeader;
    private TextView progressBar;
    private TextView beaconTrackerText;
    private ArrayList<Beacon> allBeaconsListPresent = new ArrayList<>();
    private ArrayList<Beacon> registeredBeaconsListPresent = new ArrayList<>();
    private ArrayList<Exhibition> exhibitionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Realtidsmuseet");

        requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, 1234);

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(IBEACON_LAYOUT));
        BeaconManager.setRssiFilterImplClass(RunningAverageRssiFilter.class);
        RunningAverageRssiFilter.setSampleExpirationMilliseconds(1000);

        beaconManager.bind(this);
        logWindow = (TextView) findViewById(R.id.logWindow);
        exhibitionHeader = (TextView) findViewById(R.id.exhibitionHeader);
        progressBar = (TextView) findViewById((R.id.progressBar));
        beaconTrackerText = (TextView) findViewById((R.id.beaconTrackerText));
        logWindow.setMovementMethod(new ScrollingMovementMethod());
        exhibitionSetUp();

    }

    private void exhibitionSetUp(){
        MuseumPlace place1 = new MuseumPlace("EA:C9:B7:DD:8A:A0", "Hej från EA (Första platsen) \n");
        MuseumPlace place2 = new MuseumPlace("D8:4E:93:A8:E4:83", "Hej från D8 (Andra platsen)\n");
        MuseumPlace place3 = new MuseumPlace("F2:E1:A3:7E:CF:BC", "Hej från F2 (Tredje platsen)\n");

        MuseumPlace place4 = new MuseumPlace("E6:1C:EB:07:A0:BD", "Hej från Kermit the Frog (BD)\n");
        MuseumPlace place5 = new MuseumPlace("ED:98:F0:B5:DF:1E", "Hej från NEMO (IE) \n");
        MuseumPlace place6 = new MuseumPlace("E6:96:82:C4:71:3E", "Hej från Roger Rabbit (3E) \n");


        MuseumPlace place7 = new MuseumPlace("D7:F6:A5:22:10:03", "Ana blå \n");
        MuseumPlace place8 = new MuseumPlace("CD:44:8A:AA:5E:D8", "Ana röd \n");


        Exhibition exhibition1 = new Exhibition("Hampus historia", "Linear");
        Exhibition exhibition2 = new Exhibition("Cecilias historia", "Circular");
        Exhibition exhibition3 = new Exhibition("Anas historia", "Circular");
        exhibitionList.add(exhibition1);
        exhibitionList.add(exhibition2);
        exhibitionList.add(exhibition3);

        exhibition1.addBeacon(place1);
        exhibition1.addBeacon(place2);
        exhibition1.addBeacon(place3);

        exhibition2.addBeacon(place4);
        exhibition2.addBeacon(place5);
        exhibition2.addBeacon(place6);

        exhibition3.addBeacon(place7);
        exhibition3.addBeacon(place8);
    }


    private void beaconTracker(){
        if(allBeaconsListPresent.size() > 0){
            //Here we can run code for all beacons found.
            printToLogI("BEACON TRACKER:\nWE HAVE BEACONS IN THE ALL LIST");

            if(registeredBeaconsListPresent.size() > 0){
                //Here we can run code if we have known beacons found
                printToLogI("WE HAVE BEACONS IN THE REGISTERED LIST");

                if(isThereAnyKnownBeaconWithinMeters(5)){
                    printToLogI("WE HAVE BEACONS IN THE REGISTERED LIST WITHIN 5 METERS");
                    //Here we can run code if we have known beacons within the given meters
                    Beacon closestBeacon = getTheClosestRelevantBeacon();
                    MuseumPlace closestMuseumPlace = getMuseumPlaceFromBeacon(closestBeacon);
                    Exhibition inTheExhibition = closestMuseumPlace.getBelongsToTheExhibition();
                    String exhibitionName = inTheExhibition.getExhibitionName();

                    //Set the place as visited
                    //closestMuseumPlace.setVisitedStatus(true);

                    //If Exhibitiontype for the place is linear
                    if(inTheExhibition.getExhibitionType().equals("Linear")){
                        inTheExhibition.setPlaceAsVisitedAndAllBeforeAsVisited(closestMuseumPlace);
                    }else if(inTheExhibition.getExhibitionType().equals("Circular")){
                        closestMuseumPlace.setVisitedStatus(true);
                    }
                    //Show Exhibition name in header
                    clearAndPrintToScreenHeaderArea(exhibitionName);
                    //Show place message
                    clearAndPrintToScreenTextArea(closestMuseumPlace.getBeaconMessage());

                    //Show visited status of the exhibition
                    int[] visitedArray = inTheExhibition.getVisitedStatusArray();
                    clearAndPrintToScreenVisitedArea(Arrays.toString(visitedArray));
                }
                String beaconsFound = (Integer.toString(allBeaconsListPresent.size()) + " beacons found");
                String BeaconText = "";
                for(Beacon b : registeredBeaconsListPresent){
                    BeaconText += "Beacon with the BluetoothAddress " + b.getBluetoothAddress() + " is " +b.getDistance()+" meters away \n\n";
                }
                clearAndPrintToScreenBeaconTrackerArea(beaconsFound + "\n\n" + BeaconText);

            }
        }
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
                /*
                This function scans all the bacons and stores them into the ArrayList.
                 */
                if (beacons.size() > 0) { // The phone find at least one beacon
                    ArrayList<Beacon> allBeaconsListTemp = new ArrayList<>();
                    ArrayList<Beacon> registeredBeaconsListTemp = new ArrayList<>();
                    for(Beacon b : beacons){
                        allBeaconsListTemp.add(b);
                        if(getMuseumPlaceFromBeacon(b) != null){
                            registeredBeaconsListTemp.add(b);
                        }
                        printToLogI("The beacon " + b.getBluetoothAddress() + " is " + b.getDistance() + " meters away and is added to the temp list");
                    }
                    allBeaconsListPresent.clear();
                    allBeaconsListPresent.addAll(allBeaconsListTemp); //In this way, we always have allBeaconsListPresent filled with ALL the beacons currently seen by the mobile.
                    registeredBeaconsListPresent.clear();
                    registeredBeaconsListPresent.addAll(registeredBeaconsListTemp);//In this way, we always have registeredBeaconsListPresent filled with only the relevant beacons currently seen by the mobile.
                    printToLogI("The allBeaconsListPresent has " + allBeaconsListPresent.size() + " beacons in it");
                    printToLogI("The registeredBeaconsListPresent has " + registeredBeaconsListPresent.size() + " beacons in it");
                }
                beaconTracker();
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {    }

    }

    public boolean isThereAnyKnownBeaconWithinMeters(int meters){
        for(Beacon b : registeredBeaconsListPresent){
            if(b.getDistance() < meters){
                return true;
            }
        }
        return false;
    }

    public Beacon getTheClosestRelevantBeacon(){
        Beacon closestBeacon = null;
        for(Beacon b : registeredBeaconsListPresent){
            if(closestBeacon == null){
                closestBeacon = b;
            }else{
                if(b.getDistance() < closestBeacon.getDistance()){
                    closestBeacon = b;
                }
            }
        }
        return closestBeacon;
    }

    private MuseumPlace getMuseumPlaceFromBeacon(Beacon beacon){
        /*
        This function match a physical beacon to a MuseumPlace
         */
        MuseumPlace museumPlace = null;
        for(Exhibition exhibition : exhibitionList){
            if(exhibition.beaconExistInExhibtion(beacon) != null){
                return exhibition.beaconExistInExhibtion(beacon);
            }
        }
        return null;
    }

    private void printToLogI(String text){
        /*
        This function print a string message to the log
         */
        Log.i(TAG, text);
    }


    private void clearAndPrintToScreenBeaconTrackerArea(String text){
        final String textToPrint = text;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                beaconTrackerText.setText(textToPrint);
            }
        });
    }
    private void clearAndPrintToScreenVisitedArea(String text){
        final String textToPrint = text;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setText(textToPrint);
            }
        });
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
    private void clearAndPrintToScreenHeaderArea(String text){
        final String textToPrint = text;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                exhibitionHeader.setText(textToPrint);
            }
        });
    }
    public void showAll(View view){
        Intent intent = new Intent(getApplicationContext(),Main3Activity.class);
        startActivity(intent);
    }
    public void backToStart1(View view){

        Intent intent = new Intent(getApplicationContext(),Main2Activity.class);
        startActivity(intent);
    }

}