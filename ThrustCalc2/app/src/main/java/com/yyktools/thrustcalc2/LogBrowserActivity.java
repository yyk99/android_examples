package com.yyktools.thrustcalc2;

import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class LogBrowserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_browser);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();

        showLogRecords();
    }

    void showLogRecords()
    {
        ListView listView = (ListView) findViewById(R.id.logList);

//        if (!isExternalStorageReadable()) {
//            /* permission will be requested */
//            return;
//        }

        try {
            File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "thrust_hp.csv");
            BufferedReader br = new BufferedReader(new FileReader(f));

            ArrayList<String> _lines = new ArrayList<String>();
            String line;
            try {
                while ((line = br.readLine()) != null) {
                    String[] words = line.split(",");
                    _lines.add(words[0]); // date & time
                    double diam = Double.parseDouble(words[1]);
                    double pitch = Double.parseDouble(words[2]);
                    double rpm = Double.parseDouble(words[3]);
                    int blades = Integer.parseInt(words[4]);
                    _lines.add(String.format("Prop: %.1fx%.1f, %d at %.1f", diam, pitch, blades, rpm));
                    _lines.add("Power(hp): " + words[5] + ", Thrust(lbs): " + words[6]);
                    _lines.add("Speed(mph): " + words[7]);
                    _lines.add(" ");
                }
            } catch (java.io.IOException ex) {
                ; // TODO: react
                Toast.makeText(getApplicationContext(), "File read error", Toast.LENGTH_SHORT).show();
            }

            ArrayAdapter<String> adapter;
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, _lines);
            listView.setAdapter(adapter);
        }catch(java.io.FileNotFoundException ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
