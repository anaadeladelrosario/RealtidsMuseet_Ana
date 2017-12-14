package com.realtidsmuseet.realtidsmuseet;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Locale;

public class Main3Activity extends AppCompatActivity {

    ArrayList<String> placeList = new ArrayList<>();

    TextView textView;
    Button b1;
    TextToSpeech t1;

    public void backToStart(View view){

        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);

    }
    public void show(View view){
        textView.setVisibility(View.VISIBLE);
    }

    public void hide(View view){
        textView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        textView = (TextView) findViewById(R.id.recap);
        b1 = (Button) findViewById(R.id.button2);

        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = textView.getText().toString();
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        ///////////////////////////////////////////////////////////////////////////////////////////
        textView = (TextView)findViewById(R.id.message);

        ListView listView= (ListView)findViewById((R.id.beaconList));

        placeList.add("Beacon1");
        placeList.add("Beacon2");
        placeList.add("Beacon3");
        placeList.add("Beacon4");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, placeList);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent (getApplicationContext(),Main4Activity.class);
                intent.putExtra("name", placeList.get(i));
                startActivity(intent);
            }
        });
    }
    public void onPause(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }
}
