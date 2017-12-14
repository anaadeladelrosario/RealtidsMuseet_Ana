package com.realtidsmuseet.realtidsmuseet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {

    public void clickFunction(View view){

        //Hello message when click to start searching.
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);

        Toast.makeText(Main2Activity.this,"Hello there" + "!", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

}
