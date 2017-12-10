/**
 * 1) Hampus 2017-11-30: 1st checkin with initial setup where Hampus able to get beacon signals and display them
 * 2) Cecilia 2017-11-30: beaconOne(), beaconTwo(): simple print messages
 * 3) Cecilia 2017-12-03: create MuseumPlace class in order to track beacons in an arraylist
 * 4) Hampus: Modified both files so that we can pre-store beacons and later track them
 * 5) Cecilia 2017-12-10: added a check for visited state
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
    private TextView exhibitionHeader;
    private ArrayList<Beacon> beaconsListPresent = new ArrayList<>();
    private ArrayList<Exhibition> exhibitionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Realtidsmuseet");

        requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, 1234);

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(IBEACON_LAYOUT));
        beaconManager.bind(this);
        logWindow = (TextView) findViewById(R.id.logWindow);
        exhibitionHeader = (TextView) findViewById(R.id.exhibitionHeader);
        logWindow.setMovementMethod(new ScrollingMovementMethod());
        exhibitionSetUp();

    }

    private void exhibitionSetUp(){
        MuseumPlace place1 = new MuseumPlace("D8:4E:93:A8:E4:83", "Hej från D8 \n");
        MuseumPlace place2 = new MuseumPlace("EA:C9:B7:DD:8A:A0", "Hej från EA \n");
        MuseumPlace place3 = new MuseumPlace("F2:E1:A3:7E:CF:BC", "Hej från F2 \n");
        MuseumPlace place4 = new MuseumPlace("E6:1C:EB:07:A0:BD", "Hej från Kermit the Frog (BD)\n");
        MuseumPlace place5 = new MuseumPlace("ED:98:F0:B5:DF:1E", "Hej från NEMO (IE) \n");
        MuseumPlace place6 = new MuseumPlace("E6:96:82:C4:71:3E", "Hej från Roger Rabbit (3E) \n");
        Exhibition exhibition1 = new Exhibition("Hampus historia", "Linear");
        Exhibition exhibition2 = new Exhibition("Cecilias historia", "Linear");
        exhibitionList.add(exhibition1);
        exhibitionList.add(exhibition2);
        exhibition1.addBeacon(place1);
        exhibition1.addBeacon(place2);
        exhibition1.addBeacon(place3);
        exhibition2.addBeacon(place4);
        exhibition2.addBeacon(place5);
        exhibition2.addBeacon(place6);
    }


    private void beaconTracker(){
        if(beaconsListPresent.size() > 0){
            Beacon closest = findClosestBeacon();
            String beaconsFound = (Integer.toString(beaconsListPresent.size()) + " beacons found");
            String BeaconText = "The closest Beacon with the BluetoothAddress " + closest.getBluetoothAddress() + " is " + closest.getDistance()+" meters away \n";
            MuseumPlace place = findMuseumPlaceFromBeacon(closest);
            String message;
            if(place == null){
                message = "This beacon is not registred";
            }else{
                message = place.getBeaconMessage() + "\n\nhas visited: "+place.isVisited();
            }
            clearAndPrintToScreenHeaderArea(place.getBelongsToTheExhibition().getExhibitionName());
            clearAndPrintToScreenTextArea(beaconsFound + "\n\n" + BeaconText + "\n\n" + message);
            printToLogI(beaconsFound + "\n\n" + BeaconText + "\n\n" + message+"\n\n");
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
                beaconTracker();
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {    }

    }

    private Beacon findClosestBeacon(){
        /*
        This function find the closest beacon in the arraylist
        */
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

}