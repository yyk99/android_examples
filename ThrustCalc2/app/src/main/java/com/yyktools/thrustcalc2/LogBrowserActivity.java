package com.yyktools.thrustcalc2;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

class SmartAdapter extends ArrayAdapter<String> {
    public  SmartAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<String> objects) {
        super(context, resource, objects);
    }

    @Override
    public @NonNull
    View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView textview = (TextView)super.getView(position, convertView, parent);

        if (position % 4 == 0) {
            textview.setTypeface(textview.getTypeface(), Typeface.BOLD);
            Drawable d = getContext().getResources().getDrawable(R.drawable.rounded_corners, null);
            textview.setBackground(d);
        } else {
            textview.setTypeface(Typeface.DEFAULT);
            textview.setBackgroundResource(android.R.color.white);
        }

        return  textview;
    }
}

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

        showLogRecords();
        if(savedInstanceState != null) {
            int pos = savedInstanceState.getInt("firstVisiblePosition");
            ListView listView = (ListView) findViewById(R.id.logList);
            listView.setSelection(pos);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle out)
    {
        super.onSaveInstanceState(out);
        ListView listView = (ListView) findViewById(R.id.logList);
        int pos = listView.getFirstVisiblePosition();
        out.putInt("firstVisiblePosition", pos);
    }

    static String G(double f)
    {
        int h = (int)f;
        return (h != f ? String.format("%.1f", f) : String.format("%d", h));
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
                    _lines.add(String.format("Prop: %sx%s, %d at %d", G(diam), G(pitch), blades, (int)rpm));
                    double power = Double.parseDouble(words[5]);
                    double thrust = Double.parseDouble(words[6]);
                    double speed = Double.parseDouble(words[7]);
                    _lines.add(String.format("Power: %.2fhp, Thrust: %.2f(lbs)", power, thrust));
                    _lines.add(String.format("Speed: %.1fmph", speed));
                }
            } catch (java.io.IOException ex) {
                ; // TODO: react
                Toast.makeText(getApplicationContext(), "File read error", Toast.LENGTH_SHORT).show();
            }

            ArrayAdapter<String> adapter;
//            adapter = new SmartAdapter(this, android.R.layout.simple_list_item_1, _lines);
            adapter = new SmartAdapter(this, R.layout.textview_yyk, _lines);
            listView.setAdapter(adapter);
        }catch(java.io.FileNotFoundException ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
