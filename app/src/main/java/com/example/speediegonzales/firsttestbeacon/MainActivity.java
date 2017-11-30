package com.example.speediegonzales.firsttestbeacon;

        import android.Manifest;
        import android.os.Handler;
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
        import java.util.Collection;


public class MainActivity extends AppCompatActivity implements BeaconConsumer{

    private BeaconManager beaconManager = null;
    private static final String IBEACON_LAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";
    private static final String TAG = "RangingActivity";
    private TextView logWindow;

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
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    String beaconsFound = (Integer.toString(beacons.size()) + " beacons found");
                    String BeaconText = "";
                    //Beacon b = beacons.iterator().next();
                    for(Beacon b : beacons){
                        // BeaconText += "Beacon with the BluetoothAddress " + b.getBluetoothAddress() + " is " +b.getDistance()+" meters away \n\n";
                        if(b.getBluetoothAddress().equals("E6:1C:EB:07:A0:BD")) {
                            BeaconText = beaconOne(BeaconText, b);
                        } else
                            BeaconText = beaconTwo(BeaconText, b);
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

    private String beaconOne(String BeaconText, Beacon beacon){
        BeaconText += "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ \n\n" +
                " Beacon with the BluetoothAddress " + beacon.getBluetoothAddress() + " is " +beacon.getDistance()+" meters away \n\n" +
                " Message from ROGER RABBIT \n\n @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ \n\n\n";
        return BeaconText;
    }

    private String beaconTwo(String BeaconText, Beacon beacon){
        BeaconText += "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% \n\n" +
                " Beacon with the BluetoothAddress " + beacon.getBluetoothAddress() + " is " +beacon.getDistance()+" meters away \n\n" +
                " Message to find NEMO \n\n %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n\n\n";
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
}
