package com.yyktools.list;

import android.graphics.Color;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

public class LogRecordsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_records);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    void showCsvData() {
        try {
            JSONArray jArray = new JSONArray();
            {
                if(false) {
                    // TODO: read .CSV file
                    for (int i = 0; i != 4; ++i) {
                        JSONObject o = new JSONObject();
                        o.put("column1", i * 10 + 1);
                        o.put("column2", i * 10 + 2);
                        o.put("column3", i * 10 + 3);

                        jArray.put(o);
                    }
                } else {
                    File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "thrust_hp.csv");
                    BufferedReader br = new BufferedReader(new FileReader(f));

                    String line;
                    try {
                        while ((line = br.readLine()) != null) {
                            String[] words = line.split(",");
                            jArray.put(new JSONArray(words));
                        }
                    } catch (java.io.IOException ex) {
                        ; // TODO: react
                        Toast.makeText(getApplicationContext(), "File read error", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            TableLayout tv = (TableLayout) findViewById(R.id.log_records_table);
            tv.removeAllViewsInLayout();

            // when i=-1, loop will display heading of each column
            // then usually data will be display from i=0 to jArray.length()
            for (int i = 0; i < jArray.length(); ++i) {

                TableRow tr = new TableRow(LogRecordsActivity.this);
                tr.setLayoutParams(new LayoutParams(
                        LayoutParams.FILL_PARENT,
                        LayoutParams.WRAP_CONTENT));

                // this will be executed once
                if (i == 0) {
                    for (int k = 0; k < jArray.getJSONArray(i).length(); ++k) {
                        TextView b = new TextView(LogRecordsActivity.this);
                        b.setText(jArray.getJSONArray(i).getString(k) + " ");
                        b.setTextColor(android.graphics.Color.BLUE);
                        b.setTextSize(15);
                        tr.addView(b);
                    }
                    tv.addView(tr);

                    final View vline = new View(LogRecordsActivity.this);
                    vline.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 2));
                    vline.setBackgroundColor(android.graphics.Color.BLUE);
                    tv.addView(vline); // add line below heading
                } else {
                    for (int k = 0; k < jArray.getJSONArray(i).length(); ++k) {
                        TextView b = new TextView(LogRecordsActivity.this);
                        b.setText(jArray.getJSONArray(i).getString(k) + " ");
                        b.setTextColor(android.graphics.Color.BLUE);
                        b.setTextSize(15);
                        tr.addView(b);
                    }
                    tv.addView(tr);
                    final View vline1 = new View(LogRecordsActivity.this);
                    vline1.setLayoutParams(new
                            TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 1));
                    vline1.setBackgroundColor(Color.WHITE);
                    tv.addView(vline1);  // add line below each row
                }
            }
        } catch (org.json.JSONException e) {
            android.util.Log.e("log_tag", "Error parsing data " + e.toString());
            Toast.makeText(getApplicationContext(), "JsonArray fail", Toast.LENGTH_SHORT).show();
        } catch (java.io.FileNotFoundException e) {
            Toast.makeText(getApplicationContext(), "File not found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();

        showCsvData();
    }

//    @Override
//    public boolean onOptionsItemSelected(android.view.MenuItem item) {
//        switch (item.getItemId()) {
//            // Respond to the action bar's Up/Home button
//            case android.R.id.home:
//                android.content.Intent upIntent = NavUtils.getParentActivityIntent(this);
//                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
//                    // This activity is NOT part of this app's task, so create a new task
//                    // when navigating up, with a synthesized back stack.
//                    TaskStackBuilder.create(this)
//                            // Add all of this activity's parents to the back stack
//                            .addNextIntentWithParentStack(upIntent)
//                            // Navigate up to the closest parent
//                            .startActivities();
//                } else {
//                    // This activity is part of this app's task, so simply
//                    // navigate up to the logical parent activity.
//                    NavUtils.navigateUpTo(this, upIntent);
//                }
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
