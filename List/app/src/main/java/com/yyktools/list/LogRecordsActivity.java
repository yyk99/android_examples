package com.yyktools.list;

import android.graphics.Color;
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
                // TODO: read .CSV file
                for(int i = 0 ; i != 4 ; ++i) {
                    JSONObject o = new JSONObject();
                    o.put("column1", i * 10 + 1);
                    o.put("column2", i * 10 + 2);
                    o.put("column3", i * 10 + 3);

                    jArray.put(o);
                }
            }

            TableLayout tv = (TableLayout) findViewById(R.id.log_records_table);
            tv.removeAllViewsInLayout();
            int flag = 1;

            // when i=-1, loop will display heading of each column
            // then usually data will be display from i=0 to jArray.length()
            for (int i = -1; i < jArray.length(); ++i) {

                TableRow tr = new TableRow(LogRecordsActivity.this);

                tr.setLayoutParams(new LayoutParams(
                        LayoutParams.FILL_PARENT,
                        LayoutParams.WRAP_CONTENT));

                // this will be executed once
                if (flag == 1) {

                    TextView b3 = new TextView(LogRecordsActivity.this);
                    b3.setText("col1");
                    b3.setTextColor(android.graphics.Color.BLUE);
                    b3.setTextSize(15);
                    tr.addView(b3);

                    TextView b4 = new TextView(LogRecordsActivity.this);
                    b4.setPadding(10, 0, 0, 0);
                    b4.setTextSize(15);
                    b4.setText("col2");
                    b4.setTextColor(android.graphics.Color.BLUE);
                    tr.addView(b4);

                    TextView b5 = new TextView(LogRecordsActivity.this);
                    b5.setPadding(10, 0, 0, 0);
                    b5.setText("col3");
                    b5.setTextColor(android.graphics.Color.BLUE);
                    b5.setTextSize(15);
                    tr.addView(b5);
                    tv.addView(tr);

                    final View vline = new View(LogRecordsActivity.this);
                    vline.setLayoutParams(new
                            TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 2));
                    vline.setBackgroundColor(android.graphics.Color.BLUE);
                    tv.addView(vline); // add line below heading
                    flag = 0;
                } else {
                    JSONObject json_data = jArray.getJSONObject(i);

                    TextView b = new TextView(LogRecordsActivity.this);
                    String str = String.valueOf(json_data.getInt("column1"));
                    b.setText(str);
                    b.setTextColor(Color.RED);
                    b.setTextSize(15);
                    tr.addView(b);

                    TextView b1 = new TextView(LogRecordsActivity.this);
                    b1.setPadding(10, 0, 0, 0);
                    b1.setTextSize(15);
                    String str1 = json_data.getString("column2");
                    b1.setText(str1);
                    b1.setTextColor(Color.GREEN);
                    tr.addView(b1);

                    TextView b2 = new TextView(LogRecordsActivity.this);
                    b2.setPadding(10, 0, 0, 0);
                    String str2 = String.valueOf(json_data.getInt("column3"));
                    b2.setText(str2);
                    b2.setTextColor(Color.RED);
                    b2.setTextSize(15);
                    tr.addView(b2);
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
